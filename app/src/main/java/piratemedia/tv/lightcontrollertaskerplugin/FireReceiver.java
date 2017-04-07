/*
 * Copyright 2013 two forty four a.m. LLC <http://www.twofortyfouram.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package piratemedia.tv.lightcontrollertaskerplugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import piratemedia.tv.lightcontrollertaskerplugin.bundle.BundleScrubber;
import piratemedia.tv.lightcontrollertaskerplugin.bundle.PluginBundleManager;
import tv.piratemedia.lightcontroler.api.LightControllerAPI;
import tv.piratemedia.lightcontroler.api.LightControllerException;
import tv.piratemedia.lightcontroler.api.LightZone;

import java.util.Locale;

/**
 * This is the "fire" BroadcastReceiver for a Locale Plug-in setting.
 *
 * @see com.twofortyfouram.locale.Intent#ACTION_FIRE_SETTING
 * @see com.twofortyfouram.locale.Intent#EXTRA_BUNDLE
 */
public final class FireReceiver extends BroadcastReceiver
{
    private LightControllerAPI api;

    /**
     * @param context {@inheritDoc}.
     * @param intent the incoming {@link com.twofortyfouram.locale.Intent#ACTION_FIRE_SETTING} Intent. This
     *            should contain the {@link com.twofortyfouram.locale.Intent#EXTRA_BUNDLE} that was saved by
     *            {@link EditActivity} and later broadcast by Locale.
     */
    @Override
    public void onReceive(final Context context, final Intent intent)
    {
        /*
         * Always be strict on input parameters! A malicious third-party app could send a malformed Intent.
         */

        /*if (!com.twofortyfouram.locale.Intent.ACTION_FIRE_SETTING.equals(intent.getAction()))
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG,
                        String.format(Locale.US, "Received unexpected Intent action %s", intent.getAction())); //$NON-NLS-1$
            }
            return;
        }*/

        BundleScrubber.scrub(intent);

        final Bundle bundle = intent.getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
        BundleScrubber.scrub(bundle);

        try {
            api = new LightControllerAPI(context);
        } catch(LightControllerException e) {
            Toast.makeText(context, "Cant Access Light Controller API", Toast.LENGTH_LONG).show();
            return;
        }

        if (PluginBundleManager.isBundleValid(bundle))
        {
            LightZone zone = api.getZone(bundle.getInt(PluginBundleManager.BUNDLE_EXTRA_INT_ZONE_ID), bundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_ZONE_TYPE));
            /*String message = Character.toUpperCase(zone.Type.charAt(0)) + zone.Type.substring(1) + ": " + zone.Name + " turn " + (bundle.getBoolean(PluginBundleManager.BUNDLE_EXTRA_BOOELAN_LIGHT_STATE) ? "on" : "off");
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();*/

            if(bundle.getBoolean(PluginBundleManager.BUNDLE_EXTRA_BOOELAN_LIGHT_STATE)) {
                api.lightsOn(zone);
            } else {
                api.lightsOff(zone);
            }
        }
    }
}