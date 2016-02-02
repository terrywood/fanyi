package xibox.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 14-2-25.
 */
public class Translate {

    //private static String  url ="http://openapi.baidu.com/public/2.0/bmt/translate?client_id=yRwriFe7pVwYVPQhUlNukxD6&from=auto&to=auto";
    //private static String  url ="http://openapi.baidu.com/public/2.0/bmt/translate?client_id=yRwriFe7pVwYVPQhUlNukxD6&from=auto&to=auto";

    private static String  url ="http://api.fanyi.baidu.com/api/trans/vip/translate?from=auto&to=auto";
    //

    public  JSONObject  translate(String q) throws IOException, JSONException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        JSONObject jsonObject = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
  /*          nvps.add(new BasicNameValuePair("from", "en"));
            nvps.add(new BasicNameValuePair("to", "zh"));
            */
            //nvps.add(new BasicNameValuePair("q", "北京市 丰台区 \n芳菲路88号院13号楼4单元1202(100071)"));
            nvps.add(new BasicNameValuePair("q", q));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            CloseableHttpResponse response2 = httpclient.execute(httpPost);
            try {
                System.out.println(response2.getStatusLine());
                HttpEntity entity2 = response2.getEntity();
                String content = EntityUtils.toString(entity2);
                System.out.println(content);
                jsonObject = new JSONObject(content) ;
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity2);
            } finally {
                response2.close();
            }
        } finally {
            httpclient.close();
        }
        return jsonObject;
    }

    public static void main(String[] args) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {


            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
  /*          nvps.add(new BasicNameValuePair("from", "en"));
            nvps.add(new BasicNameValuePair("to", "zh"));
            nvps.add(new BasicNameValuePair("transtype", "trans"));*/

            String salt = String.valueOf(System.currentTimeMillis());
            String q ="北京市 丰台区 \n芳菲路88号院13号楼4单元1202(100071)";
            String appid ="20160201000010512";
            String scert ="udkyfspaX1h3iDrUMKzd";
            String sign  = MD5Util.md5Encode(appid+q+salt+scert);



            nvps.add(new BasicNameValuePair("appid", appid));
            nvps.add(new BasicNameValuePair("q", q));
            nvps.add(new BasicNameValuePair("salt", salt));
            nvps.add(new BasicNameValuePair("sign", sign));




            //httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            CloseableHttpResponse response2 = httpclient.execute(httpPost);

            try {
                System.out.println(response2.getStatusLine());
                HttpEntity entity2 = response2.getEntity();
                String content = EntityUtils.toString(entity2);
                System.out.println(content);
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity2);
            } finally {
                response2.close();
            }
        } finally {
            httpclient.close();
        }
    }
}
