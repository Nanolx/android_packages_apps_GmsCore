/*
 * Copyright (C) 2013-2017 microG Project Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.microg.gms.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.R;

import org.microg.gms.gcm.GcmDatabase;
import org.microg.gms.gcm.GcmPrefs;
import org.microg.gms.snet.SafetyNetPrefs;
import org.microg.nlp.Preferences;
import org.microg.tools.ui.AbstractDashboardActivity;
import org.microg.tools.ui.ResourceSettingsFragment;

import java.io.File;

import static org.microg.gms.checkin.TriggerReceiver.PREF_ENABLE_CHECKIN;
import static org.microg.gms.ui.SettingsActivityBurger.LastKlocation;
import static org.microg.gms.ui.SettingsActivityBurger.SearchLocationApp;
import static org.microg.gms.ui.SettingsActivityBurger.showTmp;

public class SettingsActivity extends AbstractDashboardActivity {
    public SettingsActivity() {
        preferencesResource = R.xml.preferences_start;
        addCondition(Conditions.GCM_BATTERY_OPTIMIZATIONS);
        addCondition(Conditions.PERMISSIONS);
    }

    @Override
    protected Fragment getFragment() {
        return new FragmentImpl();
    }

    public static class FragmentImpl extends ResourceSettingsFragment {

        public static final String PREF_ABOUT = "pref_about";
        public static final String PREF_GCM = "pref_gcm";
        public static final String PREF_SNET = "pref_snet";
        public static final String PREF_UNIFIEDNLP = "pref_unifiednlp";
        public static final String PREF_CHECKIN = "pref_checkin";

        public FragmentImpl() {
            preferencesResource = R.xml.preferences_start;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            menu.add(0, 0, 0, "Last known location");
            menu.add(0, 1, 0, "/system/etc/gps.conf");
            menu.add(0, 2, 0, "Geolocation Map");
            menu.add(0, 3, 0, "Google Account");
            menu.add(0, 4, 0, "System specifics");
            menu.add(0, 9, 0, R.string.prefcat_about);

            super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            Context mContext = getContext();
            String tmp ="";
            switch (item.getItemId()) {
                case 0:
                    LastKlocation(mContext);
                    return true;
                case 1:
                    if (new File("/system/etc/gps.conf").exists()) {
                        SettingsActivityBurger.showTmp(mContext,"@&$", "cat /system/etc/gps.conf", true);
                    }
                    else SettingsActivityBurger.showTmp(mContext,"@&$", "cat /vendor/etc/gps.conf", true);
                    return true;
                case 2:
                    startActivity(new Intent(mContext,PlacePickerActivity.class));
                    return true;
                case 3:
                    startActivity(new Intent(mContext,AccountSettingsActivity.class));
                    return true;
                case 4:
                    SearchLocationApp(mContext);
                    return true;
                case 9:
                    showTmp(mContext, getResources().getString(R.string.prefcat_about),
                            "\n* microG Wiki: https://github.com/microg/android_packages_apps_GmsCore/wiki>microG Wiki"+
                            "\n* microG support: https://forum.xda-developers.com/android/apps-games/app-microg-gmscore-floss-play-services-t3217616"+
                            "\n* donate to MaR-V-iN: https://www.paypal.me/larma"+
                            "\n* NanoDroid docs: https://gitlab.com/Nanolx/NanoDroid/blob/master/README.md"+
                            "\n* NanoDroid support: https://forum.xda-developers.com/apps/magisk/module-nanomod-5-0-20170405-microg-t3584928",
                            false);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        @Override
        public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
            super.onCreatePreferencesFix(savedInstanceState, rootKey);
            updateDetails();
        }

        @Override
        public void onResume() {
            super.onResume();
            updateDetails();
        }

        private void updateDetails() {
            findPreference(PREF_ABOUT).setSummary(getString(R.string.about_version_str, AboutFragment.getSelfVersion(getContext())) + " (NanoDroid)" );

            if (GcmPrefs.get(getContext()).isEnabled()) {
                GcmDatabase database = new GcmDatabase(getContext());
                int regCount = database.getRegistrationList().size();
                database.close();
                findPreference(PREF_GCM).setSummary(getString(R.string.abc_capital_on) + " / " + getResources().getQuantityString(R.plurals.gcm_registered_apps_counter, regCount, regCount));
            } else {
                findPreference(PREF_GCM).setSummary(R.string.abc_capital_off);
            }

            if (SafetyNetPrefs.get(getContext()).isEnabled()) {
                String snet_info = "";

                if (SafetyNetPrefs.get(getContext()).isOfficial()) {
                    snet_info = getString(R.string.pref_snet_status_official_info);
                } else if (SafetyNetPrefs.get(getContext()).isSelfSigned()) {
                    snet_info = getString(R.string.pref_snet_status_self_signed_info);
                } else if (SafetyNetPrefs.get(getContext()).isThirdParty()) {
                    snet_info = getString(R.string.pref_snet_status_third_party_info);
                }

                findPreference(PREF_SNET).setSummary(getString(R.string.service_status_enabled) + " / " + snet_info);
            } else {
                findPreference(PREF_SNET).setSummary(R.string.service_status_disabled);
            }

            Preferences unifiedNlPrefs = new Preferences(getContext());
            int backendCount = TextUtils.isEmpty(unifiedNlPrefs.getLocationBackends()) ? 0 :
                    Preferences.splitBackendString(unifiedNlPrefs.getLocationBackends()).length;
            backendCount += TextUtils.isEmpty(unifiedNlPrefs.getGeocoderBackends()) ? 0 :
                    Preferences.splitBackendString(unifiedNlPrefs.getGeocoderBackends()).length;
            findPreference(PREF_UNIFIEDNLP).setSummary(getResources().getQuantityString(R.plurals.pref_unifiednlp_summary, backendCount, backendCount));

            boolean checkinEnabled = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(PREF_ENABLE_CHECKIN, false);
            findPreference(PREF_CHECKIN).setSummary(checkinEnabled ? R.string.service_status_enabled : R.string.service_status_disabled);
        }
    }
}
