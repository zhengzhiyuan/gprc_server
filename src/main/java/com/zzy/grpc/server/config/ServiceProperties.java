package com.zzy.grpc.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 服务静态配置，配置内容参考config/application*.properties文件。
 */
@ConfigurationProperties(prefix = "service", ignoreUnknownFields = false)
public class ServiceProperties {

    private String roomcode;
    private final Rest rest = new Rest();
    private final Grpc grpc = new Grpc();

    /**
     * grpc配置
     */
    public static class Grpc {
        private Integer port;
        private String host;

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }
    }

    /**
     * restTemplate配置
     */
    public static class Rest {
        private Integer maxTotal;
        private Integer defaultMaxPerRoute;
        private Integer socketTimeout;
        private Integer connectTimeout;
        private Integer connectionRequestTimeout;

        public Integer getMaxTotal() {
            return maxTotal;
        }

        public void setMaxTotal(Integer maxTotal) {
            this.maxTotal = maxTotal;
        }

        public Integer getDefaultMaxPerRoute() {
            return defaultMaxPerRoute;
        }

        public void setDefaultMaxPerRoute(Integer defaultMaxPerRoute) {
            this.defaultMaxPerRoute = defaultMaxPerRoute;
        }

        public Integer getSocketTimeout() {
            return socketTimeout;
        }

        public void setSocketTimeout(Integer socketTimeout) {
            this.socketTimeout = socketTimeout;
        }

        public Integer getConnectTimeout() {
            return connectTimeout;
        }

        public void setConnectTimeout(Integer connectTimeout) {
            this.connectTimeout = connectTimeout;
        }

        public Integer getConnectionRequestTimeout() {
            return connectionRequestTimeout;
        }

        public void setConnectionRequestTimeout(Integer connectionRequestTimeout) {
            this.connectionRequestTimeout = connectionRequestTimeout;
        }

    }

    public String getRoomcode() {
        return roomcode;
    }

    public void setRoomcode(String roomcode) {
        this.roomcode = roomcode;
    }

    public Rest getRest() {
        return rest;
    }

    public Grpc getGrpc() {
        return grpc;
    }

}
