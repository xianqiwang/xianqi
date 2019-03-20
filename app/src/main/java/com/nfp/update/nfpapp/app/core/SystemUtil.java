/*
 * Copyright (C) SIMCOM
 *
 * Added by chenqian
 */

package com.nfp.update.nfpapp.app.core;

import android.content.Context;
import android.content.ContentResolver;
import android.provider.Settings;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import android.view.Window;
/**
 * Class that provides access to some system property manage.
 */

public class SystemUtil {
    private static final String TAG = "SystemUtil";
    private static String PROP_FILE = "/persist/sim.properties";
    private static final String delemeter = "=";

    /*static {
        try {
            if(!(new File(PROP_FILE).exists())){
                Log.e("SystemUtil","/persist/sim.properties doesn't exist,try to create it,some methods of this class will throw exception if create the file failed");
                new File(PROP_FILE).createNewFile();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }*/

    public static boolean getSmsAutoRegSwitch() {
        String ret = getProperty("yulong.sms_auto_reg_switch");
        return ret != null ? ret.equals("1") : false;
    }

    public static void setSmsAutoRegSwitch(boolean on_off){
        setProperty("yulong.sms_auto_reg_switch",on_off ? "1" : "0");
    }

    public static String getSmsAutoRegText(){
        String ret = getProperty("yulong.sms_auto_reg_text");
        return ret;
    }

    public static void setSmsAutoRegText(String regStr){
        setProperty("yulong.sms_auto_reg_text",regStr);
    }

    public static int getDMValue() {
        int  ftm_mode = 0;
        String ftmValue = com.nfp.update.PropertyUtils.get("persist.sys.ftmmode","unknow");
        android.util.Log.w(TAG, "getDMValue : ftmValue = " + ftmValue);

        if (null != ftmValue) {
            ftm_mode = Integer.parseInt(ftmValue);
            android.util.Log.w(TAG, "getDMValue : ftm_mode = " + ftm_mode);
        }

        return ftm_mode;
    }

    public static String getSN() {
        String snStr = com.nfp.update.PropertyUtils.get("persist.sys.snnumber","unknow");
        android.util.Log.w(TAG, "getSN : snStr = " + snStr);
        return snStr;
    }

    public static void setProperty(String name, String value) {
        String valueSaved = getProperty(name);
        boolean exist = false;
        if (valueSaved != null) {
            exist = true;
        }
        java.io.RandomAccessFile raf = null;
        try {
            //If exist property
            if (exist) {
                changeExistName(name, value);
                return;
            }

            String property = name + "=" + value + "\n";
            raf = new java.io.RandomAccessFile(PROP_FILE, "rw");
            long length = raf.length();
            raf.seek(length);
            raf.write(property.getBytes());
            android.util.Log.w(TAG, "setProperty : property = " + property);
       } catch (java.io.FileNotFoundException e) {
           e.printStackTrace();
       } catch (java.io.IOException e) {
           e.printStackTrace();
       } finally {
           if (exist)
               return;

           try {
               raf.close();
           } catch (java.io.IOException e) {
               e.printStackTrace();
           }
        }
    }

    public static String getProperty(String name) {
        java.io.InputStream in = null;
        String value = "";
        try {
            in = new java.io.BufferedInputStream(new java.io.FileInputStream(PROP_FILE));
            java.util.Properties prop = new java.util.Properties();
            prop.load(in);
            value = prop.getProperty(name);
            android.util.Log.w(TAG, "Get property value is: " + value);
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    private static java.util.ArrayList<String> readProperties(java.io.BufferedReader br) throws java.io.IOException {
        java.util.ArrayList<String> propArray = new java.util.ArrayList<String>();
        String prop = "";
        while ((prop = br.readLine()) != null) {
            propArray.add(prop);
            android.util.Log.w(TAG, "read property : prop = " + prop);
        }
        br.close();
        return propArray;
    }

    private static void changeExistName(String name, String value) throws java.io.IOException{
        java.util.ArrayList<String> propArray = readProperties(new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(PROP_FILE))));
        StringBuffer propBuffer = new StringBuffer();
        String newProperty = "";
        for (String property : propArray) {
            newProperty = property;
            if (property.contains(name)) {
                String[] propMap = splitProperty(property, delemeter);
                newProperty = propMap[0] + delemeter + value;
            }
            propBuffer.append(newProperty).append("\n");
        }

        java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(PROP_FILE)));
        bw.write(propBuffer.toString());
        bw.flush();
        bw.close();
    }

    private static String[] splitProperty(String src, String splitter) {
        java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(src, splitter);
        String[] results = new String[tokenizer.countTokens()];
        int i = 0;
        while (tokenizer.hasMoreTokens()) {
            results[i] = tokenizer.nextToken();
            i++;
        }
        return results;
    }

    public static String getYLParam(String sParamName) {
        if (null == sParamName) {
            android.util.Log.w(TAG, "getYLParam : sParamName is null");
            return null;
        }
        android.util.Log.w(TAG, "getYLParam : sParamName = " + sParamName);

        String value = getProperty(sParamName);
        android.util.Log.w(TAG, "getYLParam : value = " + value);

        return value;
    }

    public static void setYLParam(String sParamName, String sValue) {
        if (null == sParamName) {
            android.util.Log.w(TAG, "setYLParam : sParamName is null");
            return;
        }

        if (null == sValue) {
            android.util.Log.w(TAG, "setYLParam : sValue is null");
            return;
        }
        setProperty(sParamName, sValue);
        android.util.Log.w(TAG, "setYLParam : sParamName = " + sParamName + ", sValue = " + sValue);

    }

    public static String getSoftWareVersion() {
        String strVer = com.nfp.update.PropertyUtils.get("ro.build.display.id", "unknown");
        android.util.Log.w(TAG, "getSoftWareVersion : strVer = " + strVer);
        return strVer;
    }

    public static boolean setTestMode(final boolean enable) {
        boolean ret = false;
        java.io.FileOutputStream outStream = null;

        com.nfp.update.PropertyUtils.set("sys.usb.testmode", enable?"1":"0");

        try {
            outStream = new java.io.FileOutputStream("//persist//usb_testmode");
            String buf = enable ? "1" : "0";
            buf += "\n";
            outStream.write(buf.getBytes("UTF-8"));
            outStream.flush();
            ret = true;
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outStream.close();
            } catch (java.io.IOException e) {
                //donothing
            }
        }

        return ret;
    }

    //set testMode Properties for fastmmi by zhaokun in 20151012 {
    public static void syncTestModeToProp() {
        String strTestMode = com.nfp.update.PropertyUtils.get("sys.usb.testmode", "unkown");
        boolean testModeEnable = isTestMode();
	 if("unkown".equals(strTestMode)){
            com.nfp.update.PropertyUtils.set("sys.usb.testmode", testModeEnable?"1":"0");
            return;
	 }

        boolean testModeProp = false;
        try{
            int a =Integer.parseInt(strTestMode);
            testModeProp = (a==1?true:false);
        }catch(Exception e){
            e.printStackTrace();
        }
        if(testModeProp != testModeEnable)
            com.nfp.update.PropertyUtils.set("sys.usb.testmode", testModeEnable?"1":"0");
    }
    //set testMode Properties for fastmmi by zhaokun in 20151012 }

    public static boolean isTestMode() {
        boolean ret = false;
        java.io.FileInputStream inputStream = null;
        try {
            inputStream = new java.io.FileInputStream("//persist//usb_testmode");
            byte[] buf = new byte[1];
            inputStream.read(buf, 0, 1);
            String enable = new String(buf, "UTF-8");
            if ("1".equals(enable)) {
                ret = true;
            }
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (java.io.IOException e) {
                //donothing
            }
        }
        return ret;
    }

	public static String ScannerCameraIdFile = "/storage/sdcard0/.2DScannerId";
	public static String ScannerStateFile = "storage/sdcard0/.2DScannerState";
	public static String CameraStateFile = "/storage/sdcard0/.2DScannerCameraState";

    public static void writeScannerCameraId(int id) {
        java.io.File scannerCameraIdFile = new java.io.File( ScannerCameraIdFile );
        java.io.FileOutputStream scannerCameraIdOps = null;
        if (!scannerCameraIdFile.exists()) {
            try {
                scannerCameraIdFile.createNewFile();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("chmod 0666 "+ScannerCameraIdFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            scannerCameraIdOps = new java.io.FileOutputStream(scannerCameraIdFile);
            String sid = id+"\n";
            scannerCameraIdOps.write(sid.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                scannerCameraIdOps.close();
            } catch (Exception e) {
                // donothing
            }
        }
    }

    public static int  readScannerCameraId() {
        java.io.File scannerCameraIdFile = new java.io.File( ScannerCameraIdFile );
        int result = -1;
        java.io.FileInputStream fis = null;
        java.io.InputStreamReader inputreader = null;
        java.io.BufferedReader buffreader = null;
        try {
            fis = new java.io.FileInputStream(scannerCameraIdFile);
            inputreader = new java.io.InputStreamReader(fis);
            buffreader = new java.io.BufferedReader(inputreader);
            String cameraId = buffreader.readLine();
            result = Integer.parseInt(cameraId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
                inputreader.close();
                buffreader.close();
            } catch (Exception e) {
                // donothing
            }
        }
        return result;
    }

	//public static int TwoScannerState = -1;

    public static void writeScannerState(int id) {
        java.io.File scannerStateFile = new java.io.File( ScannerStateFile );
        java.io.FileOutputStream ops = null;
        if (!scannerStateFile.exists()) {
            try {
                scannerStateFile.createNewFile();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("chmod 0666 "+ScannerStateFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ops = new java.io.FileOutputStream(scannerStateFile);
            String sid = id+"\n";
            ops.write(sid.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ops.close();
            } catch (Exception e) {
                // donothing
            }
        }
    }

    public static int  readScannerState() {
        final java.io.File scannerStateFile = new java.io.File( ScannerStateFile );
        int result = -1;
        java.io.FileInputStream fis = null;
        java.io.InputStreamReader inputreader = null;
        java.io.BufferedReader buffreader = null;
        try {
            fis = new java.io.FileInputStream(scannerStateFile);
            inputreader = new java.io.InputStreamReader(fis);
            buffreader = new java.io.BufferedReader(inputreader);
            String state = buffreader.readLine();
            result = Integer.parseInt(state);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
                inputreader.close();
                buffreader.close();
            } catch (Exception e) {
                // donothing
            }
        }
        return result;
    }

    public static void writeCameraState(int id) {
        java.io.File cameraStateFile = new java.io.File( CameraStateFile );
        java.io.FileOutputStream ops = null;
        if (!cameraStateFile.exists()) {
            try {
                cameraStateFile.createNewFile();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("chmod 0666 "+CameraStateFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ops = new java.io.FileOutputStream(cameraStateFile);
            String sid = id+"\n";
            ops.write(sid.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ops.close();
            } catch (Exception e) {
                // donothing
            }
        }
    }

    public static int  readCameraState() {
        java.io.File cameraStateFile = new java.io.File( CameraStateFile );
        int result = -1;
        java.io.FileInputStream fis = null;
        java.io.InputStreamReader inputreader = null;
        java.io.BufferedReader buffreader = null;
        try {
            fis = new java.io.FileInputStream(cameraStateFile);
            inputreader = new java.io.InputStreamReader(fis);
            buffreader = new java.io.BufferedReader(inputreader);
            String state = buffreader.readLine();
            result = Integer.parseInt(state);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
                inputreader.close();
                buffreader.close();
            } catch (Exception e) {
                // donothing
            }
        }
        return result;
    }

    public static int getTid() {
        return 1;
    }

    public static int getTid(android.content.Context context) {
        if (null == context) {
            android.util.Log.e(TAG, "getTid : context = " + context);
            return 1;
        }

        return 1;
    }

    public static void setLedColor(android.content.Context context, int light_id, int color) {
        if (null == context) {
            android.util.Log.e(TAG, "setLedColor : context = " + context);
            return;
        }

    }

    public static void setLedFlashing(android.content.Context context, int light_id, int color, int[] pattern, int patternIndex) {
        if (null == context) {
            android.util.Log.e(TAG, "setLedFlashing : context = " + context);
            return;
        }

    }

    public static void turnLedOff(android.content.Context context, int light_id) {
        if (null == context) {
            android.util.Log.e(TAG, "turnLedOff : context = " + context);
            return;
        }

    }

    public static void setButtonLightEnabled(android.content.Context context, boolean on) {
        if (null == context) {
            android.util.Log.e(TAG, "setButtonLightEnabled : context = " + context);
            return;
        }

    }

    public static boolean deletePersistEventLog(android.content.Context context) {
        if (null == context) {
            android.util.Log.e(TAG, "deletePersistEventLog : context = " + context);
            return false;
        }

        return false;


    }

    public static android.view.Window mCurrentVisibleWindow;
    public static void setCurrentVisibleWindow (android.view.Window window) {
        mCurrentVisibleWindow = window;
    }
}


