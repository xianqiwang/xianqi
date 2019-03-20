/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nfp.update;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

import com.nfp.update.polling.PollingService;
import com.nfp.update.polling.PollReceiver;

public class DebugService extends Service {

    private final String COMMERCIAL_URL = "http://bcm.ms.seiko-sol.co.jp/cgi-bin/bcmdiff/";
    private String TEST_URL = "http://p9008-ipngnfx01funabasi.chiba.ocn.ne.jp/cgi-bin/bcmdiff/";
    private String BASE_URL =  COMMERCIAL_URL;
    private String INITIAL_URL =  COMMERCIAL_URL;
    public SharedPreferences spref;

    private static Context context;

    public static Context getInstance(){
        return context;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        spref = this.getSharedPreferences("debug_comm", 0);
    }

    public void fotaSet(int isOpen) {

        SharedPreferences.Editor editor = spref.edit();
        editor.putInt("IS_OPEN", isOpen);
        editor.commit();
        if(readFotaSet() == isOpen){
            notificationResult("fotaset", "OK");
        }else{
            notificationResult("fotaset", "ERROR");
        }
    }

    public int readFotaSet() {
        return spref.getInt("IS_OPEN", 0);
    }

    public void fotaServerSet(int flag) {
        SharedPreferences.Editor pEdit = spref.edit();
        pEdit.putInt("fota_url",flag);
        pEdit.putString("new_fota_url", "");
        pEdit.commit();

        if(flag== readFotaServer()){
            notificationResult("fotaserverN", "OK");
        }else{
            notificationResult("fotaserverN", "ERROR");
        }
    }

    public void fotaServerUrl(String url) {
        SharedPreferences.Editor pEdit = spref.edit();
        pEdit.putString("new_fota_url",url);
        pEdit.commit();

        if(!url.equals("")){
            INITIAL_URL = url;
            notificationResult("fotaserverL", "OK");
        }else{
            notificationResult("fotaserverL", "ERROR");
        }
    }

    public int readFotaServer() {
        int url_flag = spref.getInt("fota_url", 0);
        return url_flag;
    }

    public String readFotaServerUrl() {
        String URL = spref.getString("new_fota_url","");
        int flag = readFotaServer();
        if(!URL.equals("")){
            //INITIAL_URL = URL;
            flag = 3;
        }
        switch (flag){
            case 0:
                BASE_URL = COMMERCIAL_URL;
                break;
            case 1:
                BASE_URL = TEST_URL;
                break;
            case 2:
                BASE_URL = COMMERCIAL_URL;
                break;
            case 3:
                BASE_URL = URL;
                break;
        }
        return BASE_URL;
    }

    public void fotaPolset(int flag) {
        if(flag == 1){
            UpdateUtil.stopPollingService(this);
        }else if(flag == 0){
            UpdateUtil.startPollingService(this, UpdateUtil.getPollStartTime(this));
        }

        if(readPolsetValue() == flag){
            notificationResult("fotapolset", "OK");
        }else{
            notificationResult("fotapolset", "ERROR");
        }
    }

    public int readPolsetValue() {
        SharedPreferences mSpref = PreferenceManager.getDefaultSharedPreferences(this);
        return mSpref.getInt("pol_switch",1);
    }

    public void fotaPoltimer(int time) {
        UpdateUtil.setPollStartTime(time, this);
        if(readPolsetValue()==0){
            UpdateUtil.stopPollingService(this);
        }

        Calendar afterTime = Calendar.getInstance();
        afterTime.add(Calendar.MINUTE, time);
        UpdateUtil.startPollingService(this, afterTime.getTimeInMillis());

        if(readPoltimer() == time){
            notificationResult("fotapoltimer", "OK");
        }else{
            notificationResult("fotapoltimer", "ERROR");
        }
    }

    public int readPoltimer() {
        int polTime = spref.getInt("polling_start_time", 0);
        return polTime;
    }

    public String readPolDate() {
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(context);
        return spref.getString("pol_timer", "");
    }

    public void fotaRetry(int time) {
        UpdateUtil.setRetryTime(time, this);
        if(readRetryTime() == time){
            notificationResult("fotaretry", "OK");
        }else{
            notificationResult("fotaretry", "ERROR");
        }
    }

    public int readRetryTime() {
        int retryTime = UpdateUtil.getRetryTime(this);
        if(retryTime==-1){
             retryTime = 1440;
        }
        return retryTime;
    }

    public void setFotaGoup(int gid) {
        UpdateUtil.setGoupID(gid, this);
        if(readFotaGoup() == gid){
            notificationResult("fotagidN", "OK");
        }else{
            notificationResult("fotagidN", "ERROR");
        }
    }

    public int readFotaGoup() {
        return UpdateUtil.getGoupID(this);
    }

    public void initialFotaGoup() {
        UpdateUtil.setGoupID(1, this);
        if(readFotaGoup() == 1){
            notificationResult("fotagidD", "OK");
        }
    }

    public void setFotaProxy(int num) {
        UpdateUtil.setFotaProxy(num, this);
        if(readFotaProxy() == num){
            notificationResult("fotaproxyN", "OK");
        }else{
            notificationResult("fotaproxyN", "ERROR");
        }
    }

    public int readFotaProxy() {
        return UpdateUtil.getFotaProxy(this);
    }

    public void initialFotaProxy() {
        UpdateUtil.setFotaProxy(0, this);
        if(readFotaProxy() == 0){
            notificationResult("fotaproxyD", "OK");
        }
    }

    public void fotaSystime(int flag) {
        SharedPreferences.Editor pEdit = spref.edit();
        pEdit.putInt("set_fota_systime",flag);
        pEdit.commit();

        if(readFotaSystime() == flag){
            notificationResult("fotasystime", "OK");
        }else{
            notificationResult("fotasystime", "ERROR");
        }
    }

    public int readFotaSystime() {
        return spref.getInt("set_fota_systime", 0);
    }

    public void notificationResult(String key, String result) {
        Intent intent = new Intent();
        if(result.equals("OK")||result.equals("ERROR")){
            intent.setAction("com.update.command.setResult");
        }else{
            intent.setAction("com.update.command.readResult");
        }
        intent.putExtra(key, result);
        sendBroadcast(intent);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        context = DebugService.this;
        int data = 0;
        String url = "";

        if(intent==null){
            return;
        }

        String fname = intent.getStringExtra("fName");
        String setRead = intent.getStringExtra("setread");
        if(setRead.equals("-l")){
            url = intent.getStringExtra("nValue");
        }else if(setRead.equals("-s")){
            data = intent.getIntExtra("nValue", 0);
        }

        if(fname.equals("fotaset")){
            if(setRead.equals("-s")){
                fotaSet(data);
             }else if(setRead.equals("-r")){
                notificationResult("fotaset", String.valueOf(readFotaSet()));
             }
        }else if(fname.equals("fotaserver")){
            if(setRead.equals("-s")){
                fotaServerSet(data);
             }else if(setRead.equals("-r")){
                notificationResult("fotaserver", readFotaServerUrl());
             }else if(setRead.equals("-l")){
                 fotaServerUrl(url);
             }
        }else if(fname.equals("fotapolset")){
            if(setRead.equals("-s")){
                fotaPolset(data);
             }else if(setRead.equals("-r")){
                notificationResult("fotapolset", String.valueOf(readPolsetValue()));
             }
        }else if(fname.equals("fotapoltimer")){
            if(setRead.equals("-s")){
                fotaPoltimer(data);
             }else if(setRead.equals("-r")){
                if(readPolsetValue()==0){
                    notificationResult("fotapoltimer", readPolDate());
                }else{
                    notificationResult("fotapoltimer", "Not running");
                }
             }
        }else if(fname.equals("fotaretry")){
            if(setRead.equals("-s")){
                fotaRetry(data);
             }else if(setRead.equals("-r")){
                notificationResult("fotaretry", String.valueOf(readRetryTime()));
             }
        }else if(fname.equals("fotagid")){
            if(setRead.equals("-s")){
                setFotaGoup(data);
             }else if(setRead.equals("-r")){
                notificationResult("fotagid", String.valueOf(readFotaGoup()));
             }else if(setRead.equals("-d")){
                initialFotaGoup();
             }
        }else if(fname.equals("fotaproxy")){
            if(setRead.equals("-s")){
                setFotaProxy(data);
             }else if(setRead.equals("-r")){
                notificationResult("fotaproxy", String.valueOf(readFotaProxy()));
             }else if(setRead.equals("-d")){
                initialFotaProxy();
             }
        }else if(fname.equals("fotasystime")){
            if(setRead.equals("-s")){
                fotaSystime(data);
             }else if(setRead.equals("-r")){
                notificationResult("fotasystime", String.valueOf(readFotaSystime()));
             }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, Service.START_REDELIVER_INTENT, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
