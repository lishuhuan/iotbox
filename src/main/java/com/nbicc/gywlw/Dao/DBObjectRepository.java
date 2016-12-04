package com.nbicc.gywlw.Dao;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.nbicc.gywlw.util.PropertyConfig;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by BigMao on 2016/12/3.
 */
@Repository("dBObjectRepository")
public class DBObjectRepository {
    private MongoClient mongoClient;

    private static final String DB_NAME = "cloudserver";

    @Autowired
    private PropertyConfig propertyConfig;

    @PostConstruct
    public void init(){
        MongoClientOptions.Builder build = new MongoClientOptions.Builder();
        build.connectionsPerHost(50);   //与目标数据库能够建立的最大connection数量为50
//        build.autoConnectRetry(true);   //自动重连数据库启动
        build.threadsAllowedToBlockForConnectionMultiplier(50); //如果当前所有的connection都在使用中，则每个connection上可以有50个线程排队等待
        build.maxWaitTime(1000*60*2);
        build.connectTimeout(1000*60*1);    //与数据库建立连接的timeout设置为1分钟

        MongoClientOptions myOptions = build.build();
        try {
            //数据库连接实例
            mongoClient = new MongoClient(propertyConfig.getMongodb(), myOptions);
        } catch (MongoException e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings("finally")
    public boolean insert(String collectionName,BasicDBObject object){
        MongoDatabase db = mongoClient.getDatabase(DB_NAME);
        MongoCollection<BasicDBObject> collection = db.getCollection(collectionName, BasicDBObject.class);
        boolean result = true;
        try{
            collection.insertOne(object);
        }catch(Exception e){
            e.printStackTrace();
            result = false;
        }finally{
            return result;
        }
    }

    public List<BasicDBObject> find(String collectionName, Map<String, Object> queryMap){
        List<BasicDBObject> result = new ArrayList<BasicDBObject>();
        MongoDatabase db = mongoClient.getDatabase(DB_NAME);
        MongoCollection<BasicDBObject> collection = db.getCollection(collectionName, BasicDBObject.class);
        Document queryDocument = new Document(queryMap);
        collection.find(queryDocument).into(result);
        return result;
    }

    public List<BasicDBObject> findAndSortAndLimit(String collectionName,BasicDBObject condition,BasicDBObject sort,int limit){
        List<BasicDBObject> result = new ArrayList<BasicDBObject>();
        MongoDatabase db = mongoClient.getDatabase(DB_NAME);
        MongoCollection<BasicDBObject> collection = db.getCollection(collectionName, BasicDBObject.class);
        collection.find(condition).sort(sort).limit(limit).into(result);
        return result;
    }

    public List<BasicDBObject> findAndSortAndLimitByPage(String collectionName,BasicDBObject condition,BasicDBObject sort,int limit,int currentPage){
        List<BasicDBObject> result = new ArrayList<BasicDBObject>();
        MongoDatabase db = mongoClient.getDatabase(DB_NAME);
        MongoCollection<BasicDBObject> collection = db.getCollection(collectionName, BasicDBObject.class);
        collection.find(condition).sort(sort).skip((currentPage-1)*limit).limit(limit).into(result);
        return result;
    }

    public long getCount(String collectionName,BasicDBObject condition){
        MongoDatabase db = mongoClient.getDatabase(DB_NAME);
        MongoCollection<BasicDBObject> collection = db.getCollection(collectionName, BasicDBObject.class);
        return collection.count(condition);
    }



}

