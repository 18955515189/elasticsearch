package com.maven.elasticsearch;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

public class ElasticSearchTestDelete {

    private Client client;

    @Before
    public void getClient() throws  Exception{
        client = TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
    }

    /**
     * 测试 delete
     */
    @Test
    public void testDelete() {
        DeleteResponse response = client.prepareDelete("blog02", "article", "3")
                .get();
        String index = response.getIndex();
        String type = response.getType();
        String id = response.getId();
        long version = response.getVersion();
        System.out.println(index + " : " + type + ": " + id + ": " + version);
        client.close();
    }

}
