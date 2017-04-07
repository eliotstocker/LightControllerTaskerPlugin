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

package piratemedia.tv.lightcontrollertaskerplugin.bundle;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import piratemedia.tv.lightcontrollertaskerplugin.Constants;

/**
 * Class for managing the {@link com.twofortyfouram.locale.Intent#EXTRA_BUNDLE} for this plug-in.
 */
public final class PluginBundleManager
{
    public static final String BUNDLE_EXTRA_INT_VERSION_CODE = "tv.piratemedia.lightcontrollertaskerplugin.extra.INT_VERSION_CODE"; //$NON-NLS-1$
    public static final String BUNDLE_EXTRA_INT_ZONE_ID = "tv.piratemedia.lightcontrollertaskerplugin.extra.INT_ZONE_ID";
    public static final String BUNDLE_EXTRA_STRING_ZONE_TYPE = "tv.piratemedia.lightcontrollertaskerplugin.extra.STRING_ZONE_TYPE";
    public static final String BUNDLE_EXTRA_BOOELAN_LIGHT_STATE = "tv.piratemedia.lightcontrollertaskerplugin.extra.BOOLEAN_LIGHT_STATE";

    public static boolean isBundleValid(final Bundle bundle)
    {
        if (null == bundle)
        {
            return false;
        }

        //TODO: make this work

        return true;
    }

    /**
     * @param context Application context.
     * @param message The toast message to be displayed by the plug-in. Cannot be null.
     * @return A plug-in bundle.
     */
    public static Bundle generateBundle(final Context context, final int zoneId, final String zoneType, final boolean lightState)
    {
        final Bundle result = new Bundle();
        result.putInt(BUNDLE_EXTRA_INT_VERSION_CODE, Constants.getVersionCode(context));
        result.putInt(BUNDLE_EXTRA_INT_ZONE_ID, zoneId);
        result.putString(BUNDLE_EXTRA_STRING_ZONE_TYPE, zoneType);
        result.putBoolean(BUNDLE_EXTRA_BOOELAN_LIGHT_STATE, lightState);

        return result;
    }

    /**
     * Private constructor prevents instantiation
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private PluginBundleManager()
    {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}