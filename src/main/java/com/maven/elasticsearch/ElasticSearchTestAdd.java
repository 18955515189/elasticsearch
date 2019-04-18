package com.maven.elasticsearch;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class ElasticSearchTestAdd {
    private Client client;

    @Before
    public void getClient() throws  Exception{
        client = TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
    }

    /**
     * 建立文档
     * 没有映射创建，自动创建索引 和 映射
     * 方式一：拼装json字符串
     */
    @Test
    public void createIndexNoMapping(){
        String json = "{" +
                "\"id\":\"1\"," +
                "\"title\":\"基于Lucene的搜索服务器\"," +
                "\"content\":\"它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口\"" +
                "}";
        IndexResponse indexResponse = this.client.prepareIndex("blog02", "article", "1").setSource(json).execute().actionGet();
        // 结果获取
        String index = indexResponse.getIndex();
        String type = indexResponse.getType();
        String id = indexResponse.getId();
        long version = indexResponse.getVersion();
        boolean created = indexResponse.isCreated();
        System.out.println(index + " : " + type + ": " + id + ": " + version + ": " + created);
        // 关闭连接
        client.close();
    }


    /**
     * 建立文档
     * 没有映射创建，自动创建索引 和 映射
     * 方式二：拼装json字符串
     */
    @Test
    public void createIndexNoMapping1(){
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("id", "2");
        json.put("title", "基于Lucene的搜索服务器");
        json.put("content", "它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口");
        IndexResponse indexResponse = this.client.prepareIndex("blog02", "article", "2").setSource(json).execute().actionGet();
        // 结果获取
        String index = indexResponse.getIndex();
        String type = indexResponse.getType();
        String id = indexResponse.getId();
        long version = indexResponse.getVersion();
        boolean created = indexResponse.isCreated();
        System.out.println(index + " : " + type + ": " + id + ": " + version + ": " + created);
        // 关闭连接
        client.close();
    }

    /**
     * 建立文档
     * 没有映射创建，自动创建索引 和 映射
     * 方式三：使用es的帮助类，创建json对象
     */
    @Test
    public void createIndexNoMapping2() throws IOException {
        // 使用es的帮助类，创建一个json方式的对象
        /**
         * 描述json 数据
         * {id:xxx, title:xxx, content:xxx}
         */
        XContentBuilder sourceBuilder = XContentFactory.jsonBuilder()
                .startObject()
                .field("id", 3)
                .field("title", "ElasticSearch是一个基于Lucene的搜索服务器")
                .field("content",
                        "它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。Elasticsearch是用Java开发的，并作为Apache许可条款下的开放源码发布，是当前流行的企业级搜索引擎。设计用于云计算中，能够达到实时搜索，稳定，可靠，快速，安装使用方便。")
                .endObject();
        // 创建索引
        IndexResponse indexResponse = client.prepareIndex("blog02", "article", "3").setSource(sourceBuilder).get();
        // 结果获取
        String index = indexResponse.getIndex();
        String type = indexResponse.getType();
        String id = indexResponse.getId();
        long version = indexResponse.getVersion();
        boolean created = indexResponse.isCreated();
        System.out.println(index + " : " + type + ": " + id + ": " + version + ": " + created);
        // 关闭连接
        client.close();
    }
}
