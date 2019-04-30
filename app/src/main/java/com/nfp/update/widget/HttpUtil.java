package com.nfp.update.widget;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import android.content.Context;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.Header;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONException;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import java.io.UnsupportedEncodingException;
import com.loopj.android.http.JsonHttpResponseHandler;
import java.io.File;
import com.loopj.android.http.BinaryHttpResponseHandler;
import java.io.FileNotFoundException;
public class HttpUtil {

    private static final String BASE_URL = "http://192.168.1.101:8890/type/jason/action/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public  static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){

        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(Context context, String url,HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        client.post(context,getAbsoluteUrl(url),entity, contentType, responseHandler);
    }

    private static String getAbsoluteUrl(String url) {
        return BASE_URL + url;
    }


class CallHttpUtil{

    public void callMethod(){
        RequestParams params=new RequestParams();
        params.put("params","{\"classify_id\":70,\"page\":1,\"page_count\":2}");
        HttpUtil.get("getConfig", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                }

        });

    }

public void callJosn(Context context){

    JSONObject jsonObject=new JSONObject();
    try {

        jsonObject.put("Blower", 1);
    } catch (JSONException e) {
        e.printStackTrace();
    }
    //封装方法中post的参数
    ByteArrayEntity entity = null;
    try {
        entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
        //用application/json向其传达这是json类型的接口数据
        entity.setContentType(new BasicHeader (HTTP.CONTENT_TYPE, "application/json"));
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    //封装类.post发送数据
    HttpUtil.post(context, "control", entity, "application/json", new JsonHttpResponseHandler () {


/*        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }


        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
        }*/

    });


   }

    private   AsyncHttpClient asyncHttpClient;



    public void call(int v) {
            asyncHttpClient=new AsyncHttpClient();


        switch (v){
            case 1:
                String url="http://apis.juhe.cn/mobile/get?phone=15711492842&key=4e12ebd27315d998b61d2606f463b50d";
                asyncHttpClient.get(url, null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
                break;
            case 2:
                String postUrl="http://v.juhe.cn/toutiao/index";
                RequestParams params=new RequestParams();
                params.put("type","社会");
                params.put("key","4867f81a3bcde50c94e6103a95cde181");
                asyncHttpClient.post(postUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
                break;
            case 3:
                String imgUrl="http://img.lanrentuku.com/img/allimg/1707/14988864745279.jpg";
                asyncHttpClient.get(imgUrl, null, new BinaryHttpResponseHandler () {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {

                    }
                });
                break;
            case 4:
                String uploadUrl="http://192.168.1.92:8080/webapps/ROOT";
                java.io.File file=new File("/storage/emulated/0/66666.png");
                RequestParams paramsFile=new RequestParams();
                try {
                    paramsFile.put("hhhh",file);
                    asyncHttpClient.post(uploadUrl, paramsFile, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

}}