package com.nbicc.gywlw.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Set;

public class RedisAPI {
	
	 private static JedisPool pool = null;  
     
	    /** 
	     *
	     * @return JedisPool 
	     */  
	    public static JedisPool getPool() {  
	        if (pool == null) {  
	            JedisPoolConfig config = new JedisPoolConfig();  
	            
	            config.setMaxTotal(300);  
	            
	            config.setMaxIdle(10);  
	            
	            config.setMaxWaitMillis(1000 * 100);

	          //  config.setTestOnBorrow(true);
	            pool = new JedisPool(config, "120.55.171.132", 6379,10000);
	        }  
	        return pool;  
	    }  
	      
	    
	    
	  /*  @SuppressWarnings("deprecation")
		public static Jedis getJedis(){
	    	 Jedis jedis = null;
	    	 try {
	             jedis = pool.getResource();
	         } catch (Exception e) {
	             pool.returnBrokenResource(jedis);
	             e.printStackTrace();
	         } finally {
	             if(null != pool) {
	                 pool.returnResource(jedis);        
	             }
	         }
	    	return jedis;
	    	
	    }*/
	    
	    
	    /**
	     * @功能：通过Redis的key获取值，并释放连接资源
	     * @参数：key，键值
	     * @返回： 成功返回value，失败返回null
	     */
	    public String get(String key){
	        Jedis jedis = null;
	        String value = null;
	        try {
	        	 pool = getPool(); 
	            jedis = pool.getResource();
	            value = jedis.get(key);
	        } catch (Exception e) {
	            pool.returnBrokenResource(jedis);
	            e.printStackTrace();
	        } finally {
	            if(null != pool) {
	                pool.returnResource(jedis);        
	            }
	        }
	        return value;
	    }

	    /**
	     * @功能：向redis存入key和value（如果key已经存在 则覆盖），并释放连接资源
	     * @参数：key，键
	     * @参数：value，与key对应的值
	     * @返回：成功返回“OK”，失败返回“0”
	     */
	    public String set(String key,String value){
	        Jedis jedis = null;
	        try {
	        	 pool = getPool(); 
	            jedis = pool.getResource();
	            return jedis.set(key, value);
	        } catch (Exception e) {
	            pool.returnBrokenResource(jedis);
	            e.printStackTrace();
	            return "0";
	        } finally {
	            if(null != pool) {
	                pool.returnResource(jedis);        
	            }
	        }
	    }
	    
	    
	    public static String lindex(String key, long index){
	        Jedis jedis = null;
	        try {
	        	 pool = getPool(); 
	            jedis = pool.getResource();
	            return jedis.lindex(key, index);
	        } catch (Exception e) {
	            pool.returnBrokenResource(jedis);
	            e.printStackTrace();
	            return "0";
	        } finally {
	            if(null != pool) {
	                pool.returnResource(jedis);        
	            }
	        }
	    }
	    
	    public static List<String> lrange(String key, long start, long end){
	        Jedis jedis = null;
	        try {
	        	 pool = getPool(); 
	            jedis = pool.getResource();
	            return jedis.lrange(key, start,end);
	        } catch (Exception e) {
	            pool.returnBrokenResource(jedis);
	            e.printStackTrace();
	            return null;
	        } finally {
	            if(null != pool) {
	                pool.returnResource(jedis);        
	            }
	        }
	    }
	    
	    
	    public static void lpush(String key, String param){
	        Jedis jedis = null;
	        try {
	        	 pool = getPool(); 
	            jedis = pool.getResource();
	            jedis.lpush(key, param);
	        } catch (Exception e) {
	            pool.returnBrokenResource(jedis);
	            e.printStackTrace();
	        } finally {
	            if(null != pool) {
	                pool.returnResource(jedis);        
	            }
	        }
	    }
	    
	    public static void stringSet(String key){
	        Jedis jedis = null;
	        try {
	        	 pool = getPool(); 
	            jedis = pool.getResource();
	            jedis.set(key, "1");
	            jedis.expire(key, 600);
	        } catch (Exception e) {
	            pool.returnBrokenResource(jedis);
	            e.printStackTrace();
	        } finally {
	            if(null != pool) {
	                pool.returnResource(jedis);        
	            }
	        }
	    }
	    
	    public static String stringGet(String key){
	        Jedis jedis = null;
	        try {
	        	 pool = getPool(); 
	            jedis = pool.getResource();
	            String result=jedis.get(key);
	            return result;
	        } catch (Exception e) {
	            pool.returnBrokenResource(jedis);
	            e.printStackTrace();
	            return null;
	        } finally {
	            if(null != pool) {
	                pool.returnResource(jedis);        
	            }
	        }
	    }
	    
	    public static void del(String key){
	        Jedis jedis = null;
	        try {
	        	 pool = getPool(); 
	            jedis = pool.getResource();
	            jedis.del(key);
	        } catch (Exception e) {
	            pool.returnBrokenResource(jedis);
	            e.printStackTrace();
	        } finally {
	            if(null != pool) {
	                pool.returnResource(jedis);        
	            }
	        }
	    }
	    
	    
	    public static void ltrim(String key, long start, long end){
	        Jedis jedis = null;
	        try {
	        	 pool = getPool(); 
	            jedis = pool.getResource();
	            jedis.ltrim(key, start,end);
	        } catch (Exception e) {
	            pool.returnBrokenResource(jedis);
	            e.printStackTrace();
	        } finally {
	            if(null != pool) {
	                pool.returnResource(jedis);        
	            }
	        }
	    }
	    
	    public static String hget(String key, String field){
	        Jedis jedis = null;
	        try {
	        	 pool = getPool(); 
	            jedis = pool.getResource();
	            String result=jedis.hget(key, field);
	            return result;
	        } catch (Exception e) {
	            pool.returnBrokenResource(jedis);
	            e.printStackTrace();
	            return "0";
	        } finally {
	            if(null != pool) {
	                pool.returnResource(jedis);        
	            }
	        }
	    }
	    
	    public static void zincrby(String key, int increment, String member){
	        Jedis jedis = null;
	        try {
	        	 pool = getPool(); 
	            jedis = pool.getResource();
	            jedis.zincrby(key, increment, member);
	        } catch (Exception e) {
	            pool.returnBrokenResource(jedis);
	            e.printStackTrace();
	        } finally {
	            if(null != pool) {
	                pool.returnResource(jedis);        
	            }
	        }
	    }
	    
	    public static void hdel(String key, String field){
	        Jedis jedis = null;
	        try {
	        	 pool = getPool(); 
	            jedis = pool.getResource();
	            jedis.hdel(key, field);
	        } catch (Exception e) {
	            pool.returnBrokenResource(jedis);
	            e.printStackTrace();
	        } finally {
	            if(null != pool) {
	                pool.returnResource(jedis);        
	            }
	        }
	    }
	    


	    
	    public static void zrem(String key,String member){
	        Jedis jedis = null;
	        try {
	        	 pool = getPool(); 
	            jedis = pool.getResource();
	            jedis.zrem(key, member);
	        } catch (Exception e) {
	            pool.returnBrokenResource(jedis);
	            e.printStackTrace();
	        } finally {
	            if(null != pool) {
	                pool.returnResource(jedis);        
	            }
	        }
	    }
	    
	    public static Set<String> hkey(String key){
	        Jedis jedis = null;
	        try {
	        	 pool = getPool(); 
	            jedis = pool.getResource();
	            Set<String> set=jedis.hkeys(key);
	            return set;
	        } catch (Exception e) {
	            pool.returnBrokenResource(jedis);
	            e.printStackTrace();
	            return null;
	        } finally {
	            if(null != pool) {
	                pool.returnResource(jedis);        
	            }
	        }
	    }
	    
	    public static double zscore(String key,String member){
	        Jedis jedis = null;
	        try {
	        	 pool = getPool(); 
	            jedis = pool.getResource();
	            double score=jedis.zscore(key, member);
	            return score;
	        } catch (Exception e) {
	            pool.returnBrokenResource(jedis);
	            e.printStackTrace();
	            return 0;
	        } finally {
	            if(null != pool) {
	                pool.returnResource(jedis);        
	            }
	        }
	    }
}
