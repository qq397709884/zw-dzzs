package cn.longicorn.modules.web.httpclient;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Apache HttpClient的简单封装，初始化连接池和销毁分别调用init和destroy.
 * 与Spring集成时，可以注册bena
 * <bean id="sharedHttpClientHolder" class="cn.longicorn.modules.web.httpclient.SharedHttpClientHolder" 
 *		init-method="init" destroy-method="destroy">
 *	</bean>
 */
public class SharedHttpClientHolder {

    private static final Logger logger = LoggerFactory.getLogger(SharedHttpClientHolder.class);
    private static final int DEFAULT_TIMEOUT_SECONDS = 60;
    private static final int DEFAULT_HTTP_POOL_SIZE = 100;
    private CloseableHttpClient httpClient;

    private int poolSize = DEFAULT_HTTP_POOL_SIZE;
    private int timeout = DEFAULT_TIMEOUT_SECONDS;

    public SharedHttpClientHolder() {

    }

    /**
     * Constructor
     * @param poolSize    连接池大小，简化为到每服务的池大小和总池大小都用这个值
     * @param timeout    超时时间，简化为读超时与连接超时都用这个时间，单位秒
     */
    public SharedHttpClientHolder(int poolSize, int timeout) {
        this.poolSize = poolSize;
        this.timeout = timeout;
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("java.net.preferIPv6Addresses", "false");
    }

    public CloseableHttpClient get() {
        if (httpClient == null) {
            throw new IllegalStateException("Httpclient has not been initialized.");
        }
        return httpClient;
    }

    /**
     * 使用HTTPClient之前，需要调用init方法初始化连接池
     */
    public SharedHttpClientHolder init() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(timeout * 1000)
                .setConnectTimeout(timeout * 1000)
                .build();

        httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(poolSize)
                .setMaxConnPerRoute(poolSize)
                .setDefaultRequestConfig(requestConfig)
                .build();
        return this;
    }

    /**
     * 退出系统前，必须调用destroy方法以释放资源
     */
    public void destroy() {
        try {
            httpClient.close();
        } catch (IOException e) {
            logger.error("Httpclient close fail", e);
        }
    }

    public void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

}
