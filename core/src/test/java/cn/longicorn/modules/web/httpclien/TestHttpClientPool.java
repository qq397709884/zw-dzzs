package cn.longicorn.modules.web.httpclien;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import cn.longicorn.modules.web.httpclient.SharedHttpClientHolder;

public class TestHttpClientPool {

    public static void main(String[] args) throws Exception {
    	SharedHttpClientHolder holder = new SharedHttpClientHolder();
    	holder.init();
    	CloseableHttpClient httpclient = holder.get();
    	
        try {
            // create an array of URIs to perform GETs on
            String[] urisToGet = {
                "http://c.wits.net.cn"
            };
            
            // create a thread for each URI
            GetThread[] threads = new GetThread[urisToGet.length];
            long start = System.currentTimeMillis();
            for(int x = 0; x < 10; x++) {
	            for (int i = 0; i < threads.length; i++) {
	                HttpGet httpget = new HttpGet(urisToGet[i]);
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
            }
            long end = System.currentTimeMillis();
            
            System.out.println("耗时:" + String.valueOf((end - start)));
        } finally {
        	holder.destroy();
        }
    }

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
                CloseableHttpResponse response = httpClient.execute(httpget, context);
                try {
                   // System.out.println(id + " - get executed");
                    // get the response body as an array of bytes
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                       // byte[] bytes = EntityUtils.toByteArray(entity);
                        System.out.println(EntityUtils.toString(entity));
                       // System.out.println(id + " - " + bytes.length + " bytes read");
                    }
                } finally {
                   // response.close();
                }
            } catch (Exception e) {
            	httpget.abort();
                System.out.println(id + " - error: " + e);
            }
        }

    }

}
