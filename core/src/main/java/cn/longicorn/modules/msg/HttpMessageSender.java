package cn.longicorn.modules.msg;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lkh on 2014/11/27
 */

public class HttpMessageSender {

    private String msgUrl;
    private String smsUrl;

    public HttpResponse send(String fromUser, String toUser, String content, String sign, String type) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(msgUrl);
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("fromUser", fromUser));
            params.add(new BasicNameValuePair("toUser", toUser));
            params.add(new BasicNameValuePair("content", content));
            params.add(new BasicNameValuePair("sign", sign));
            params.add(new BasicNameValuePair("type", type));
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            return httpClient.execute(httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpPost.releaseConnection();
        }
        return null;
    }

    public HttpResponse sendSms(String fromUser, String toUser, String content) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(smsUrl);
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("fromUser", fromUser));
            params.add(new BasicNameValuePair("toUser", toUser));
            params.add(new BasicNameValuePair("content", content));
            params.add(new BasicNameValuePair("type", "text/sms"));
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            return httpClient.execute(httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpPost.releaseConnection();
        }
        return null;
    }

    public void setMsgUrl(String msgUrl) {
        this.msgUrl = msgUrl;
    }

    public void setSmsUrl(String smsUrl) {
        this.smsUrl = smsUrl;
    }
}