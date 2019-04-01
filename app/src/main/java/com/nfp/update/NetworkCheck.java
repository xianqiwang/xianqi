package com.nfp.update;

import android.content.Context;
import android.net.ConnectivityManager;
import java.lang.reflect.Method;
import android.telephony.TelephonyManager;
import android.net.NetworkInfo;
import android.content.Intent;
import android.content.ComponentName;
import android.app.Activity;
import android.provider.Settings;
public class NetworkCheck {

    private static Context context ;
    /** 网络不可用 */
    public static final int NO_NET_WORK = 0;
    /** 是wifi连接 */
    public static final int WIFI = 1;
    /** 不是wifi连接 */
    public static final int NO_WIFI = 2;

    NetworkCheck(android.content.Context context){
     this.context=context;
    }

    public static boolean isNetWorkAvailable(){
        boolean isAvailable = false ;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isAvailable()){
            isAvailable = true;
        }
        return isAvailable;
    }

    public static int getNetWorkType() {

        if (!isNetWorkAvailable()) {
            return NO_NET_WORK;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting())
            return WIFI;
        else
            return NO_WIFI;
    }

    @SuppressWarnings("static-access")
    public static boolean isWiFiConnected(){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo.getType() == manager.TYPE_WIFI ? true : false;
    }

    public static boolean isMobileDataEnable(){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMobileDataEnable = false;
        isMobileDataEnable = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        return isMobileDataEnable;
    }

    public static boolean isWifiDataEnable(){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiDataEnable = false;
        isWifiDataEnable = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        return isWifiDataEnable;
    }


    public static void GoSetting(Activity activity){
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        activity.startActivity(intent);
    }


    public static void openSetting(android.app.Activity activity) {
        Intent intent = new Intent ("/");
        ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cn);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }


    public static boolean getMobileDataState ( Object[] arg) {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getSystemService (Context.CONNECTIVITY_SERVICE);

            Class ownerClass = mConnectivityManager.getClass ();
            Class[] argsClass = null;
            if (arg != null) {
                argsClass = new Class[ 1 ];
                argsClass[ 0 ] = arg.getClass ();
            }
            Method method = ownerClass.getMethod ("getMobileDataEnabled", argsClass);
            Boolean isOpen = (Boolean) method.invoke (mConnectivityManager, arg);
            return isOpen;

        } catch (Exception e) {
            return false;
        }
    }


    public boolean isNetWork () {
        ConnectivityManager mConnectivity = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        android.telephony.TelephonyManager mTelephony = (TelephonyManager) context.getSystemService (context.TELEPHONY_SERVICE);
        //检查网络连接
        NetworkInfo info = mConnectivity.getActiveNetworkInfo ();

        if (info == null || ! mConnectivity.getBackgroundDataSetting ()) {
            return false;
        }
        return info.isConnected ();
    }

    public boolean isWifiorMobilledata () {
        ConnectivityManager mConnectivity = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        android.telephony.TelephonyManager mTelephony = (TelephonyManager) context.getSystemService (context.TELEPHONY_SERVICE);
        //检查网络连接
        NetworkInfo info = mConnectivity.getActiveNetworkInfo ();
        int netType = info.getType ();
        int netSubtype = info.getSubtype ();

        if (netType == ConnectivityManager.TYPE_WIFI) {  //WIFI
            return info.isConnected ();
        } else if (netType == ConnectivityManager.TYPE_MOBILE &&
                netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
                && ! mTelephony.isNetworkRoaming ()) {   //MOBILE
            return info.isConnected ();
        } else {
            return false;
        }
    }

    public static boolean isWifi () {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo ();
        if (activeNetInfo != null && activeNetInfo.getType () == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public static boolean checkNetworkConnection () {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo (ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo (ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable () || mobile.isAvailable ())  //getState()方法是查询是否连接了数据网络
            return true;
        else
            return false;
    }

    public boolean isNormalData () {

        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo (ConnectivityManager.TYPE_MOBILE);   //获取移动网络信息
        if (mMobileNetworkInfo != null) {
            return mMobileNetworkInfo.isAvailable ();    //getState()方法是查询是否连接了数据网络
        }

        return false;
    }

    public boolean checkSimCard () {
        TelephonyManager tm = (TelephonyManager) context.getSystemService (context.TELEPHONY_SERVICE);//取得相关系统服务
        StringBuffer sb = new StringBuffer ();
        Boolean state = null;
        switch (tm.getSimState ()) { //getSimState()取得sim的状态  有下面6中状态
            case TelephonyManager.SIM_STATE_ABSENT:
                state= false;
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                state= false;
                break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                state= true;

                break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                state= true;

                break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                state= true;

                break;
            case TelephonyManager.SIM_STATE_READY:
                state= true;
                break;
        }

/*        if (tm.getSimSerialNumber () != null) {
            sb.append ("@" + tm.getSimSerialNumber ().toString ());
        } else {
            sb.append ("@无法取得SIM卡号");
        }*/

        if(tm.getSimOperator().equals("")){
            sb.append("@无法取得供货商代码");
        }else{
            sb.append("@" + tm.getSimOperator().toString());
        }

        if(tm.getSimOperatorName().equals("")){
            sb.append("@无法取得供货商");
        }else{
            sb.append("@" + tm.getSimOperatorName().toString());
        }

        if(tm.getSimCountryIso().equals("")){
            sb.append("@无法取得国籍");
        }else{
            sb.append("@" + tm.getSimCountryIso().toString());
        }

        if (tm.getNetworkOperator().equals("")) {
            sb.append("@无法取得网络运营商");
        } else {
            sb.append("@" + tm.getNetworkOperator());
        }
        if (tm.getNetworkOperatorName().equals("")) {
            sb.append("@无法取得网络运营商名称");
        } else {
            sb.append("@" + tm.getNetworkOperatorName());
        }
        if (tm.getNetworkType() == 0) {
            sb.append("@无法取得网络类型");
        } else {
            sb.append("@" + tm.getNetworkType());
        }
        return state;
    }


}
