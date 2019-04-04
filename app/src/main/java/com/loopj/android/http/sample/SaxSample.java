/*
    Android Asynchronous Http Client Sample
    Copyright (c) 2014 Marek Sebera <marek.sebera@gmail.com>
    https://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.loopj.android.http.sample;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.SaxAsyncHttpResponseHandler;
import com.nfp.update.R;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

public class SaxSample extends SampleParentActivity {

    private static final String LOG_TAG = "SaxSample";
    private final com.loopj.android.http.SaxAsyncHttpResponseHandler saxAsyncHttpResponseHandler = new com.loopj.android.http.SaxAsyncHttpResponseHandler<com.loopj.android.http.sample.SaxSample.SAXTreeStructure>(new com.loopj.android.http.sample.SaxSample.SAXTreeStructure()) {
        @Override
        public void onStart() {
            clearOutputs();
        }

        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, com.loopj.android.http.sample.SaxSample.SAXTreeStructure saxTreeStructure) {
            debugStatusCode(LOG_TAG, statusCode);
            debugHeaders(LOG_TAG, headers);
            debugHandler(saxTreeStructure);
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, com.loopj.android.http.sample.SaxSample.SAXTreeStructure saxTreeStructure) {
            debugStatusCode(LOG_TAG, statusCode);
            debugHeaders(LOG_TAG, headers);
            debugHandler(saxTreeStructure);
        }

        private void debugHandler(com.loopj.android.http.sample.SaxSample.SAXTreeStructure handler) {
            for (com.loopj.android.http.sample.SaxSample.Tuple t : handler.responseViews) {
                addView(getColoredView(t.color, t.text));
            }
        }
    };

    @Override
    public com.loopj.android.http.ResponseHandlerInterface getResponseHandler() {
        return saxAsyncHttpResponseHandler;
    }

    @Override
    public String getDefaultURL() {
        return "http://bin-iin.com/sitemap.xml";
    }

    @Override
    public boolean isRequestHeadersAllowed() {
        return false;
    }

    @Override
    public boolean isRequestBodyAllowed() {
        return false;
    }

    @Override
    public int getSampleTitle() {
        return R.string.title_sax_example;
    }

    @Override
    public com.loopj.android.http.RequestHandle executeSample(com.loopj.android.http.AsyncHttpClient client, String URL, cz.msebera.android.httpclient.Header[] headers, cz.msebera.android.httpclient.HttpEntity entity, com.loopj.android.http.ResponseHandlerInterface responseHandler) {
        return client.get(this, URL, headers, null, responseHandler);
    }

    private class Tuple {
        public final Integer color;
        public final String text;

        public Tuple(int _color, String _text) {
            this.color = _color;
            this.text = _text;
        }
    }

    private class SAXTreeStructure extends org.xml.sax.helpers.DefaultHandler {

        public final java.util.List<com.loopj.android.http.sample.SaxSample.Tuple> responseViews = new java.util.ArrayList<com.loopj.android.http.sample.SaxSample.Tuple>();

        public void startElement(String namespaceURI, String localName,
                                 String rawName, org.xml.sax.Attributes atts) {
            responseViews.add(new com.loopj.android.http.sample.SaxSample.Tuple(LIGHTBLUE, "Start Element: " + rawName));
        }

        public void endElement(String namespaceURI, String localName,
                               String rawName) {
            responseViews.add(new com.loopj.android.http.sample.SaxSample.Tuple(LIGHTBLUE, "End Element  : " + rawName));
        }

        public void characters(char[] data, int off, int length) {
            if (length > 0 && data[0] != '\n') {
                responseViews.add(new com.loopj.android.http.sample.SaxSample.Tuple(LIGHTGREEN, "Characters  :  " + new String(data,
                        off, length)));
            }
        }
    }
}
