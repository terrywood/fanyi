/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package org.apache.http.examples.me;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * An example that performs GETs from multiple threads.
 *
 */
public class ClientMultiThreadedExecution {
    static String hitUrl ="http://t.hd.xiaomi.com/s/";
    static String URL = "http://tc.hd.xiaomi.com/hdget?product=tv&fk=1297&_=13926954272531392696030691";
    static String cookie ="mstuid=1392091386877_2265; xmuuid=XMGUEST-699CF100-92D1-11E3-A72C-599A5C075AAE; lastsource=miwifi.com; userId=10127790; xm_user_www_num=0; XM_10127790_UN=%E5%90%B4%E5%BF%97%E6%98%8E; __utma=219621008.1740760687.1392091387.1392257587.1392694585.3; __utmb=219621008.4.9.1392694600780; __utmc=219621008; __utmz=219621008.1392257587.2.2.utmcsr=miwifi.com|utmccn=(referral)|utmcmd=referral|utmcct=/; mstz=CN-WW-HP-AD-A0|http://p.www.xiaomi.com/open/index.html|1788589092.3|0";


    public static void main(String[] args) throws Exception {
        // Create an HttpClient with the ThreadSafeClientConnManager.
        // This connection manager must be used if more than one thread will
        // be using the HttpClient.
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(10);

        CloseableHttpClient httpclient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();
        try {
            // create an array of URIs to perform GETs on
          /*  String[] urisToGet = {
                "http://hc.apache.org/",
                "http://hc.apache.org/httpcomponents-core-ga/",
                "http://hc.apache.org/httpcomponents-client-ga/",
            };*/
            int times = 100;
            String[] urisToGet = new  String[times];
            long ss =System.currentTimeMillis();
            for(int i =0;i<times;i++){
                ss++;
                urisToGet[i]=URL+"&_="+ss+System.currentTimeMillis();
            }


            // create a thread for each URI
            GetThread[] threads = new GetThread[urisToGet.length];
            for (int i = 0; i < threads.length; i++) {
                HttpGet httpget = new HttpGet(urisToGet[i]);
                httpget.setHeader("cookie",cookie);
                //httpget.setHeader("User-Agent","Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A5376e Safari/8536.25");
                threads[i] = new GetThread(httpclient, httpget, i + 1);
            }

            // start the threads
            for (int j = 0; j < threads.length; j++) {
                threads[j].start();
            }

            // join the threads
            for (int j = 0; j < threads.length; j++) {
                threads[j].join();
            }

        } finally {
            httpclient.close();
        }
    }



    /**
     * A thread that performs a GET.
     */
    static class GetThread extends Thread {

        private final CloseableHttpClient httpClient;
        private final HttpContext context;
        private final HttpGet httpget;
        private final int id;




        public GetThread(CloseableHttpClient httpClient, HttpGet httpget, int id) {
            this.httpClient = httpClient;
            this.context = new BasicHttpContext();
            this.httpget = httpget;
            this.id = id;
        }

        /**
         * Executes the GetMethod and prints some status information.
         */
        @Override
        public void run() {
            try {
                System.out.println(id + " - about to get something from " + httpget.getURI());
                CloseableHttpResponse response = httpClient.execute(httpget, context);
                try {
                    System.out.println(id + " - get executed");
                    // get the response body as an array of bytes
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        callback( EntityUtils.toString(entity),id);
                    }else{
                        System.out.println(id + " - is null");
                    }
                } finally {
                    response.close();
                }
            } catch (Exception e) {
                System.out.println(id + " - error: " + e.getMessage());
            }
        }

    }


    public static void callback(String _body,int id) throws JSONException {
        if(StringUtils.isBlank(_body)) return;
        String  body =  StringUtils.substringBetween( _body, "(", ")");
        System.out.println("body["+body+"]");
        JSONObject jsonObject = new JSONObject(body);
        if(jsonObject.getBoolean("login")){
            JSONObject miphone =  jsonObject.getJSONObject("status").getJSONObject("miphone");
            if(miphone.getBoolean("reg")){
                String hdurl= miphone.getString("hdurl");
                if(StringUtils.isNotBlank(hdurl)){
                    hdurl = hitUrl+ hdurl ;
                    System.out.println(hdurl);
                    runBroswer(hdurl);
                }else{
                    System.out.println("user is not hit!!!");
                }
            } else{
                System.out.println("user is not register!!!");
            }
        } else{
            System.out.println("user is not login!!!");
        }
    }
    public static void runBroswer(String webSite) {
        try {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
                URI uri = new URI(webSite);
                desktop.browse(uri);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
    }
}
