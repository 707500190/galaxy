package com.sun.content.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * ES 连接配置；
 *
 * @author sunshilong
 * @version 1.0
 * @date 2022/3/7
 */
@Configuration
@Slf4j
public class ESConfig {

    //TODO: 后续改配置文件读取
//    @Value("${spring.elasticsearch.rest.uris}")
    public String host = "localhost";
//    @Value("${spring.elasticsearch.rest.username}")
    public String userName = "";
//    @Value("${spring.elasticsearch.rest.password}")
    public String password = "";
//    @Value("${spring.elasticsearch.rest.port}")
    public String port = "9200";

    /**
     * 创建es连接
     */
    @Bean
    public RestHighLevelClient buildClient() {
//        Assert.notNull(password, "es password can not be empty");

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));

        String[] ports = port.split(",");
            HttpHost[] httpHosts = new HttpHost[ports.length];
            for (int i = 0; i < ports.length; i++) {
                httpHosts[i] = new HttpHost(host, Integer.parseInt(ports[i]));
            }

        RestClientBuilder restClientBuilder = RestClient.builder(httpHosts);
        ConnectionKeepAliveStrategy myStrategy = (response, context) -> {
            HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (it.hasNext()) {
                HeaderElement he = it.nextElement();
                String param = he.getName();
                String value = he.getValue();
                if (value != null && param.equalsIgnoreCase("timeout")) {
                    return 1000;
                }
            }
            return 1000;//如果没有约定，则默认定义时长为1s
        };
        //配置身份验证
        restClientBuilder.setHttpClientConfigCallback(
                httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                        .setMaxConnTotal(200).setMaxConnPerRoute(100).setKeepAliveStrategy(myStrategy));
        restClientBuilder.setRequestConfigCallback(
                requestConfigBuilder -> requestConfigBuilder.setConnectTimeout(-1).setSocketTimeout(-1));
        return new RestHighLevelClient(restClientBuilder);
    }
}