package com.maven.elasticsearch;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

public class ElasticSearchTestSearch {

    private Client client;

    @Before
    public void initClient() throws Exception{
        client = TransportClient.builder().build().addTransportAddress( new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9300));
    }

    /**
     * 搜索文档数据（单个索引）
     */
    @Test
    public void testGetData(){
        GetResponse response = client.prepareGet("blog02", "article", "1")
                                     .setOperationThreaded(false)
                                     .get();
        System.out.println( response.getSourceAsString() );
        client.close();
    }


    /**
     * 搜索文档数据（多个索引）
     */
    @Test
    public void testMultiGet(){
        MultiGetResponse multiGetResponse = client.prepareMultiGet()
                                                  .add("blog02", "article", "1")
                                                  .add("blog02", "article", "2","3")
                                                  .get();
        for (MultiGetItemResponse itemResponse : multiGetResponse) {
            GetResponse response = itemResponse.getResponse();
            if (response.isExists()) {
                String sourceAsString = response.getSourceAsString();
                System.out.println(sourceAsString);
            }
        }
        client.close();
    }

}
