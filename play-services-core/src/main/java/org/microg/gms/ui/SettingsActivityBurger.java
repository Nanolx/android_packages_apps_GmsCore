package org.microg.gms.ui;

import android.annotation.SuppressLint;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import android.text.util.Linkify;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

final class SettingsActivityBurger {

    public static void SearchLocationApp(Context mContext){
        String[] bEnables = {//TT"config_enableUpdateableTimeZoneRules",
                "config_enableNetworkLocationOverlay",
                "config_enableFusedLocationOverlay",
                "config_enableHardwareFlpOverlay",
                "config_enableGeocoderOverlay",
                "config_enableGeofenceOverlay"
        };
        String[] sEnables = {//TT"config_timeZoneRulesUpdaterPackage",
                "config_networkLocationProviderPackageName",
                "config_fusedLocationProviderPackageName",
                "config_hardwareFlpPackageName",
                "config_geocoderProviderPackageName",
                "config_geofenceProviderPackageName",
                "config_defaultNetworkRecommendationProviderPackage"
        };
        String tmp2 = "";
        String tmp=getProp("ro.services.whitelist.packagelist");
        if (tmp.length()>0) tmp ="\n#> OMNIROM/system whitelist detected:"+"\n"+tmp+"\n";

        tmp += "\n#> PROVIDERS \n";
        int id = mContext.getResources().getIdentifier("android:array/config_locationProviderPackageNames",null,null);
        //List<PackageInfo> apks = mContext.getPackageManager().getInstalledPackages(PackageManager.MATCH_SYSTEM_ONLY);
        List<ApplicationInfo> apps = mContext.getPackageManager().getInstalledApplications(0);
        if (Build.VERSION.SDK_INT >= 23) {
            ArrayList<String> appsNames= new ArrayList<>();
            UsageStatsManager mUsageStats;
            mUsageStats = mContext.getSystemService(UsageStatsManager.class);
            for (ApplicationInfo app : apps) {
                appsNames.add(app.packageName);
                if (app.packageName.contains(".location")) {
                    tmp2 += (mUsageStats.isAppInactive(app.packageName) ? "0ff " : " ON ");
                    //tmp+="\n"+String.format("%-20s",app.packageName);
                    tmp2 += app.sourceDir + "\n";
                }
            }
            if (tmp2.length()>0) {
                tmp += "\u25A0*.location* apk detected\n"+tmp2+"\n";
                tmp2="";
            }

            if (id!=0){
                String[] lProviders = mContext.getResources().getStringArray(id);
                for (String s: lProviders){
                    if (appsNames.contains(s)) {
                        tmp2 += (mUsageStats.isAppInactive(s) ? "0ff " : " ON ");
                    } else tmp2+="000 ";
                    tmp2 += s+"\n";
                }
            }

            if (tmp2.length()>0) {
                tmp += "\u25A0?_locationProviderPackageNames\n"+tmp2;
                tmp2="";
            }


        } else {
            for(ApplicationInfo app : apps) {
                if (app.packageName.contains(".location")) {
                    tmp2 += app.sourceDir + "\n";
                }
            }

            if (tmp2.length()>0) {
                tmp += "\u25A0*.location* apk detected\n"+tmp2+"\n";
                tmp2="";
            }

            if (id!=0){
                String[] lProviders = mContext.getResources().getStringArray(id);
                for (String s: lProviders)tmp2 += s+"\n";
            }

            if (tmp2.length()>0) {
                tmp += "\u25A0?_locationProviderPackageNames\n"+tmp2;
                tmp2="";
            }


        }

        tmp += "\n\n#> FRAMEWORK-res _location config(s):";
        boolean b = false;
        for (int i=0; i< bEnables.length; i++) {
            id = mContext.getResources().getIdentifier("android:bool/"+bEnables[i],null,null);
            if (id!=0) {
                b = mContext.getResources().getBoolean(id);
                tmp += "\n"+( b ? "\u25C9": "\u25CE" );
                tmp += String.format("%-6s",mContext.getResources().getString(id))+bEnables[i].substring(6);//+mContext.getResources().getResourceTypeName(id);
            }
            id = mContext.getResources().getIdentifier("android:string/"+sEnables[i],null,null);
            if (id!=0) {
                tmp += "\n"+( b ? "\u25CEX": "\u25C9X" )+ sEnables[i].substring(6);
                tmp += "\n ="+mContext.getResources().getText(id);//+mContext.getResources().getResourceTypeName(id);
            }
            tmp += "\n";
        }
        id = mContext.getResources().getIdentifier("android:string/config_defaultNetworkRecommendationProviderPackage",null,null);
        if (id!=0) tmp+= "\n\n"+"?_defaultNetworkRecommendationProviderPackage\n ="+mContext.getResources().getString(id);//+mContext.getResources().getResourceTypeName(id);

        id = mContext.getResources().getIdentifier("android:string/config_defaultDndAccessPackages",null,null);
        if (id!=0) tmp+= "\n\n"+"?_defaultDndAccessPackages\n ="+mContext.getResources().getString(id);//+mContext.getResources().getResourceTypeName(id);

        tmp += "\n\n";


        showTmp(mContext,"ROM Specifics & Implementations", tmp, true);
    }

    public static void LastKlocation(Context mContext) {
        String tmp="\n>";
        try {
            LocationManager locationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);
            if (Build.VERSION.SDK_INT >= 28) tmp +=("Gnss "+locationManager.getGnssYearOfHardware()+":\n"+locationManager.getGnssHardwareModelName())+"\n\n";
            for (String s:locationManager.getAllProviders()){
                tmp += "\n\n"+String.format("%-8s",s);
                if (locationManager.isProviderEnabled(s)) {
                    tmp+=" ON/"+locationManager.getProvider(s).getPowerRequirement();
                    try {
                        Location location = locationManager.getLastKnownLocation(s);
                        if (location != null) tmp +="\n"+location.toString();
                    } catch (SecurityException e) {
                        tmp = ("error"+ e.getMessage() + '\n' + Log.getStackTraceString(e));
                    }
                } else tmp += " 0ffÂ°";

            }
        } catch (Throwable e) {
            tmp =("error"+ e.getMessage() + '\n' + Log.getStackTraceString(e));
        }
        showTmp(mContext,"Last known location(s)", tmp, true);
    }

    public static void showTmp(Context mContext, String t, String s, boolean bold) {
        String title =t;
        TextView showText = new TextView(mContext);

        if (t.equals("@&$")) {
            title = s;
            showText.setText(getZinfo(s,"\n\u25A0",false));
        } else {
            showText.setText(s);

            showText.setAutoLinkMask(Linkify.ALL);
            Linkify.addLinks(showText, Linkify.WEB_URLS);
        }

        if (Build.VERSION.SDK_INT >= 11) showText.setTextIsSelectable(true);

        if (bold) showText.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/RobotoMono-Bold.ttf"));
 
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(showText)
               .setTitle(title)
               .setCancelable(true)
               .setNegativeButton(android.R.string.ok, null)
               .show();
    }

    private static String getZinfo(String s, String sPlus, boolean bool) {
        try {
            Process p = Runtime.getRuntime().exec(s);
            InputStream is = null;
            is = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String tmp;
            String tmp2 = "";

            if (bool) br.readLine();
            while ((tmp = br.readLine()) != null)
            {
                tmp2 +=sPlus+tmp;
            }
            is.close();
            br.close();
            if (tmp2.length() !=0) return tmp2;
            return "Unknown";
        } catch (Exception ex) {
            return "ERROR: " + ex.getMessage();
        }
    }

    public static String getProp(String s) {
        try {
            @SuppressLint("PrivateApi")
            Class<?> aClass = Class.forName("android.os.SystemProperties");
            Method method = aClass.getMethod("get", String.class);
            Object platform = method.invoke(null, s);

            return platform instanceof String ? (String) platform : "<" + platform + ">";

        } catch (Exception e) {
            return "";
        }
    }

}
