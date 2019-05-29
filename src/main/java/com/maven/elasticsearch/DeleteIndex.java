package com.maven.elasticsearch;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;

public class DeleteIndex {

    private Client client;

    @Before
    public void initClient() throws Exception{
        client = TransportClient.builder().build().addTransportAddress( new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9300));
    }


    @Test
    // 索引/删除操作
    public void createIndex() throws IOException {
        // 创建索引
        /*client.admin().indices().prepareCreate("blog01").get();*/

        // 删除索引
        client.admin().indices().prepareDelete("blog03").get();

        // 关闭连接
        client.close();
    }

    @Test
    // 映射操作
    public void createMapping() throws Exception {
        // 创建索引
         client.admin().indices().prepareCreate("blog03").execute().actionGet();
        // 添加映射
        /**
         * 格式： "mappings" : { "article" : { "dynamic" : "false", "properties" :
         * { "id" : { "type" : "string" }, "content" : { "type" : "string" },
         * "author" : { "type" : "string" } } } }
         */
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("article")
                .startObject("properties")
                .startObject("id").field("type", "integer").field("store", "yes").endObject()
                .startObject("title").field("type", "string").field("store", "yes").field("analyzer", "ik").endObject()
                .startObject("content").field("type", "string").field("store", "yes").field("analyzer", "ik").endObject()
                .endObject()
                .endObject()
                .endObject();

        PutMappingRequest mapping = Requests.putMappingRequest("blog03").type("article").source(builder);
        client.admin().indices().putMapping(mapping).get();

        // 关闭连接
        client.close();
    }
}
