package com.maven.elasticsearch;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.util.Iterator;

public class QueryBuilderDSL {

    private Client client;

    @Before
    public void getClient() throws  Exception{
        client = TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
    }


    /**
     * 搜索在elasticsearch中创建的文档对象
     * @throws Exception
     */
    @Test
    public void testSearch() throws Exception{
        // 搜索数据
        // get()方法 === execute().actionGet()
        // SearchResponse searchResponse = client.prepareSearch("blog02")
        // .setTypes("article").setQuery(QueryBuilders.matchAllQuery()).get();
        /**
         /**
         * 1、ElasticSearch提供QueryBuileders.queryStringQuery(搜索内容)
         *     查询方法，对所有字段进行分词查询
         */
		SearchResponse searchResponse = client.prepareSearch("blog02").setTypes("article")
				.setQuery(QueryBuilders.queryStringQuery("全面")) .get();
        /**
         * 2、 只想查询content里包含全文 ，使用QueryBuilders.wildcardQuery模糊查询 *任意字符串 ?任意单个字符
         */
		/*SearchResponse searchResponse = client.prepareSearch("blog02").setTypes("article")
				.setQuery(QueryBuilders.wildcardQuery("content", "*全文*")).get();*/
        /**
         * 3、 查询content词条为“搜索” 内容，使用TermQuery
         */
       /* SearchResponse searchResponse = client.prepareSearch("blog02")
                .setTypes("article")
                .setQuery(QueryBuilders.termQuery("content", "全文")).get();*/
        SearchHits hits = searchResponse.getHits(); // 获取命中次数，查询结果有多少对象
        System.out.println("查询结果有：" + hits.getTotalHits() + "条");
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            SearchHit searchHit = iterator.next(); // 每个查询对象
            System.out.println(searchHit.getSourceAsString()); // 获取字符串格式打印
            System.out.println("id:" + searchHit.getSource().get("id"));
            System.out.println("title:" + searchHit.getSource().get("title"));
            System.out.println("content:" + searchHit.getSource().get("content"));
            for (Iterator<SearchHitField> ite = searchHit.iterator(); ite.hasNext();) {
                SearchHitField next = ite.next();
                System.out.println(next.getValues());
            }
        }
        // 关闭连接
        client.close();
    }

}
