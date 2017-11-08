package cn.longicorn.modules.web.httpclien;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import cn.longicorn.modules.utils.Encodes;
import cn.longicorn.modules.web.httpclient.SharedHttpClientHolder;

public class TestHttp {

	public static void main(String[] args) throws Exception {
		long x = System.currentTimeMillis();
		SharedHttpClientHolder pool = new SharedHttpClientHolder();
		pool.init();
		try {
			for (int i = 0; i < 100; i++) {
				CloseableHttpClient httpclient = pool.get();

				//HttpPost httpPost = new HttpPost("http://192.168.10.26/circles/share/get_news");
				HttpPost httpPost = new HttpPost("http://www.163.com");
				String username = "1234";
				String password = "1234";
				String auth = Encodes.encodeBase64((username + ":" + password).getBytes());
				httpPost.addHeader("authorization", "Basic " + auth);

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("arrows", "1"));
				params.add(new BasicNameValuePair("lastId", "0"));

				httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

				HttpResponse res = httpclient.execute(httpPost);
				//System.out.println(res.getStatusLine());
				String rtn = EntityUtils.toString(res.getEntity());
				System.out.println(rtn);
			}
		} finally {
			pool.destroy();
		}
		System.out.println((System.currentTimeMillis() - x) + "毫秒");
	}

}