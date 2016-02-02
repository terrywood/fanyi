package org.apache.http.examples.me;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.List;

/**
 * Created by Administrator on 14-1-22.
 */
public class XiaoMi {

    public static void main(String[] args) throws Exception {
        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();
        try {


            HttpUriRequest login = RequestBuilder.post()
                    .setUri(new URI("https://account.xiaomi.com/pass/serviceLoginAuth2"))
                    .addParameter("user", "10127790")
                    .addParameter("pwd", "abcabc123123")
                    .addParameter("sid", "eshopm")
                    .addParameter("display", "mobile")
                    .addParameter("callback", "http://m.xiaomi.com/mshopapi/v1/authorize/sso_callback?followup=http%3A%2F%2Fm.xiaomi.com%2Findex.html%23ac%3Daccount%26op%3Dindex&sign=MjIzYzEwMzEzODg1NmI0ZGI2OGViZDljOGRlNjZmOTExYjE1NDBlNw,,")
                    .addParameter("qs", "%3Fcallback%3Dhttp%253A%252F%252Fm.xiaomi.com%252Fmshopapi%252Fv1%252Fauthorize%252Fsso_callback%253Ffollowup%253Dhttp%25253A%25252F%25252Fm.xiaomi.com%25252Findex.html%252523ac%25253Daccount%252526op%25253Dindex%2526sign%253DMjIzYzEwMzEzODg1NmI0ZGI2OGViZDljOGRlNjZmOTExYjE1NDBlNw%252C%252C%26sid%3Deshopm")
                    .addParameter("_sign", "6GgO3qUEcMlKu2xfo8aEAQg65aw=")
                    .build();
            CloseableHttpResponse response2 = httpclient.execute(login);
            try {
                HttpEntity entity = response2.getEntity();

                System.out.println("Login form get: " + response2.getStatusLine());
                System.out.println("haha-" + EntityUtils.toString(entity));
                EntityUtils.consume(entity);

               // System.out.println("haha"+EntityUtils.toString(entity));
                System.out.println("Post logon cookies:");
                List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.isEmpty()) {
                    System.out.println("None");
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        System.out.println("- " + cookies.get(i).toString());
                    }
                }
            } finally {
                response2.close();
            }

            HttpGet httpget = new HttpGet("https://someportal/");
            CloseableHttpResponse response1 = httpclient.execute(httpget);
            try {
                HttpEntity entity = response1.getEntity();

                System.out.println("Login form get: " + response1.getStatusLine());
                EntityUtils.consume(entity);

                System.out.println("Initial set of cookies:");
                List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.isEmpty()) {
                    System.out.println("None");
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        System.out.println("- " + cookies.get(i).toString());
                    }
                }
            } finally {
                response1.close();
            }

        } finally {
            httpclient.close();
        }
    }
}
