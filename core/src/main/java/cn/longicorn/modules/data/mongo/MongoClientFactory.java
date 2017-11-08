package cn.longicorn.modules.data.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MongoClientFactory {

    private String host;
    private int port;

    /**
     * 连接串,优先级高于host/port参数.
     * <p/>
     * 语法: host1:port1,host2:port2...hostX:portX
     */
    private String connectStr;

    private String description;
    private int connectionsPerHost = 10;
    private int threadsAllowedToBlockForConnectionMultiplier = 5;
    private int maxWaitTime = 1000 * 60 * 2;
    private int connectTimeout = 1000 * 10;
    private int socketTimeout = 0;
    private boolean socketKeepAlive = false;
    private boolean autoConnectRetry = false;
    private long maxAutoConnectRetryTime = 0;
    private boolean cursorFinalizerEnabled = true;
    private boolean alwaysUseMBeans = false;
    private boolean salveOk = true;

    public MongoClientFactory() {
    }

    public MongoClientFactory(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public MongoClient create() throws UnknownHostException {
        MongoClientOptions.Builder builder = MongoClientOptions.builder();
        builder
                .description(description)
                .connectionsPerHost(connectionsPerHost)
                .threadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier)
                .maxWaitTime(maxWaitTime)
                .connectTimeout(connectTimeout)
                .socketTimeout(socketTimeout)
                .socketKeepAlive(socketKeepAlive)
                .cursorFinalizerEnabled(cursorFinalizerEnabled)
                // 如果设置了slaveOk为ture（默认值），优先读取Secondary服务器，否则Primary优先
                .readPreference(salveOk ? ReadPreference.secondaryPreferred() : ReadPreference.primaryPreferred())
                .alwaysUseMBeans(alwaysUseMBeans);

        List<ServerAddress> addressList = new ArrayList<>();
        if (connectStr == null) {
            addressList.add(new ServerAddress(host, port));
        } else {
            String[] servers = connectStr.split(",");
            for (String server : servers) {
                String[] params = server.split(":");

                int port = params.length == 1 ? 27017 : Integer.parseInt(params[1].trim());
                addressList.add(new ServerAddress(params[0], port));
            }
        }
        return new MongoClient(addressList, builder.build());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getConnectionsPerHost() {
        return connectionsPerHost;
    }

    public void setConnectionsPerHost(int connectionsPerHost) {
        this.connectionsPerHost = connectionsPerHost;
    }

    public int getThreadsAllowedToBlockForConnectionMultiplier() {
        return threadsAllowedToBlockForConnectionMultiplier;
    }

    public void setThreadsAllowedToBlockForConnectionMultiplier(int threadsAllowedToBlockForConnectionMultiplier) {
        this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
    }

    public int getMaxWaitTime() {
        return maxWaitTime;
    }

    public void setMaxWaitTime(int maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public boolean isSocketKeepAlive() {
        return socketKeepAlive;
    }

    public void setSocketKeepAlive(boolean socketKeepAlive) {
        this.socketKeepAlive = socketKeepAlive;
    }

    public boolean isAutoConnectRetry() {
        return autoConnectRetry;
    }

    public void setAutoConnectRetry(boolean autoConnectRetry) {
        this.autoConnectRetry = autoConnectRetry;
    }

    public long getMaxAutoConnectRetryTime() {
        return maxAutoConnectRetryTime;
    }

    public void setMaxAutoConnectRetryTime(long maxAutoConnectRetryTime) {
        this.maxAutoConnectRetryTime = maxAutoConnectRetryTime;
    }

    public boolean isCursorFinalizerEnabled() {
        return cursorFinalizerEnabled;
    }

    public void setCursorFinalizerEnabled(boolean cursorFinalizerEnabled) {
        this.cursorFinalizerEnabled = cursorFinalizerEnabled;
    }

    public boolean isAlwaysUseMBeans() {
        return alwaysUseMBeans;
    }

    public void setAlwaysUseMBeans(boolean alwaysUseMBeans) {
        this.alwaysUseMBeans = alwaysUseMBeans;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getConnectStr() {
        return connectStr;
    }

    public void setConnectStr(String connectStr) {
        this.connectStr = connectStr;
    }

    public boolean isSalveOk() {
        return salveOk;
    }

    public void setSalveOk(boolean salveOk) {
        this.salveOk = salveOk;
    }
}
