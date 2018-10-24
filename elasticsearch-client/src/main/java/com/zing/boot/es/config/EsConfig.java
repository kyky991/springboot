package com.zing.boot.es.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class EsConfig {

    @Bean
    public TransportClient transportClient() throws UnknownHostException {
        InetSocketTransportAddress node = new InetSocketTransportAddress(
                InetAddress.getByName("192.168.110.129"), 9300);

        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();

        TransportClient transportClient = new PreBuiltTransportClient(settings);
        transportClient.addTransportAddress(node);
        return transportClient;
    }
}
