package piratemedia.tv.lightcontrollertaskerplugin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import piratemedia.tv.lightcontrollertaskerplugin.bundle.BundleScrubber;
import piratemedia.tv.lightcontrollertaskerplugin.bundle.PluginBundleManager;
import tv.piratemedia.lightcontroler.api.LightControllerAPI;
import tv.piratemedia.lightcontroler.api.LightControllerException;
import tv.piratemedia.lightcontroler.api.LightZone;
import tv.piratemedia.lightcontroler.api.OnPermissionChanged;

/**
 * This is the "Edit" activity for a Locale Plug-in.
 * <p>
 * This Activity can be started in one of two states:
 * <ul>
 * <li>New plug-in instance: The Activity's Intent will not contain
 * {@link com.twofortyfouram.locale.Intent#EXTRA_BUNDLE}.</li>
 * <li>Old plug-in instance: The Activity's Intent will contain
 * {@link com.twofortyfouram.locale.Intent#EXTRA_BUNDLE} from a previously saved plug-in instance that the
 * user is editing.</li>
 * </ul>
 *
 * @see com.twofortyfouram.locale.Intent#ACTION_EDIT_SETTING
 * @see com.twofortyfouram.locale.Intent#EXTRA_BUNDLE
 */
public final class EditActivity extends AbstractPluginActivity
{
    private LightControllerAPI api;
    private LightZone zone;
    private TextView zoneSelected;
    private Switch zoneState;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        BundleScrubber.scrub(getIntent());

        final Bundle localeBundle = getIntent().getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
        BundleScrubber.scrub(localeBundle);

        setContentView(R.layout.activityedit);

        try {
            api = new LightControllerAPI(this);
        } catch(LightControllerException e) {
            final Context _this = this;
            LinearLayout view = (LinearLayout)findViewById(R.id.install_app);
            Button install = (Button)findViewById(R.id.install_button);
            view.setVisibility(View.VISIBLE);
            if(e.getCode() == LightControllerException.TYPE_APPLICATION_OLD) {
                TextView desc = (TextView)findViewById(R.id.install_desc);
                desc.setText(R.string.app_update_description);
                install.setText(R.string.app_update_button);
            }
            install.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LightControllerAPI.getApplicationFromPlayStore(_this);
                    finish();
                }
            });
            return;
        }

        final LinearLayout perms = (LinearLayout) findViewById(R.id.perm_request);

        if(api.hasPermission()) {
            perms.setVisibility(View.GONE);
        } else {
            Button permBtn = (Button) findViewById(R.id.perm_button);
            api.setOnPermissionChanged(new OnPermissionChanged() {
                @Override
                public void onChange() {
                    if(api.hasPermission()) {
                        perms.setVisibility(View.GONE);
                    }
                }
            });
            permBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    api.RequiestPermission();
                }
            });
        }


        Button zoneSelect = (Button) findViewById(R.id.select_light_zone);
        zoneSelected = (TextView) findViewById(R.id.selected_light_zone);
        zoneState = (Switch) findViewById(R.id.zone_state);

        if (null == savedInstanceState)
        {
            if (PluginBundleManager.isBundleValid(localeBundle))
            {
                /*final String message =
                        localeBundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_MESSAGE);
                ((EditText) findViewById(android.R.id.text1)).setText(message);*/
                int zoneId = localeBundle.getInt(PluginBundleManager.BUNDLE_EXTRA_INT_ZONE_ID);
                String zoneType = localeBundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_ZONE_TYPE);
                Boolean state = localeBundle.getBoolean(PluginBundleManager.BUNDLE_EXTRA_BOOELAN_LIGHT_STATE);

                zone = api.getZone(zoneId, zoneType);
                zoneSelected.setText(Character.toUpperCase(zone.Type.charAt(0)) + zone.Type.substring(1) + ": " + zone.Name);
                zoneState.setChecked(state);
            }
        }

        zoneSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                api.pickZone();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LightControllerAPI.PickRequestCode) {
            if(resultCode == RESULT_OK) {
                zone = (LightZone)data.getSerializableExtra("LightZone");
                zoneSelected.setText(Character.toUpperCase(zone.Type.charAt(0)) + zone.Type.substring(1) + ": " + zone.Name);
            } else if(resultCode == RESULT_CANCELED) {

            }
        }
    }

    @Override
    public void finish()
    {
        if (!isCanceled())
        {
            if (zone != null)
            {
                final Intent resultIntent = new Intent();

                final Bundle resultBundle =
                        PluginBundleManager.generateBundle(getApplicationContext(), zone.ID, zone.Type, zoneState.isChecked());
                resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, resultBundle);

                String message = Character.toUpperCase(zone.Type.charAt(0)) + zone.Type.substring(1) + ": " + zone.Name + " turn " + (zoneState.isChecked() ? "on" : "off");
                final String blurb = generateBlurb(getApplicationContext(), message);
                resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, blurb);

                setResult(RESULT_OK, resultIntent);
            }
        }

        super.finish();
    }

    /**
     * @param context Application context.
     * @param message The toast message to be displayed by the plug-in. Cannot be null.
     * @return A blurb for the plug-in.
     */
    /* package */static String generateBlurb(final Context context, final String message)
    {
        final int maxBlurbLength =
                context.getResources().getInteger(R.integer.twofortyfouram_locale_maximum_blurb_length);

        if (message.length() > maxBlurbLength)
        {
            return message.substring(0, maxBlurbLength);
        }

        return message;
    }
}