package cn.longicorn.modules.data.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Cahce Service Implementation with Redis
 * @author liukehua
 */
public class CacheService {

	private ShardedJedisPool shardedJedisPool;

	public boolean exists(String key) {
		boolean isExists = false;
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = shardedJedisPool.getResource();
			isExists = shardedJedis.exists(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (shardedJedis != null)
				shardedJedisPool.returnResource(shardedJedis);
		}
		return isExists;
	}

	public <T> T get(String key, Class<T> classType) {
		T obj = null;
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = shardedJedisPool.getResource();
			String value = shardedJedis.get(key);
			if (value != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				obj = objectMapper.readValue(value, classType);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (shardedJedis != null)
				shardedJedisPool.returnResource(shardedJedis);
		}
		return obj;
	}

	public String set(String key, Object object) {
		String statusCode = null;
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = shardedJedisPool.getResource();
			ObjectMapper objectMapper = new ObjectMapper();
			statusCode = shardedJedis.set(key, objectMapper.writeValueAsString(object));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (shardedJedis != null)
				shardedJedisPool.returnResource(shardedJedis);
		}
		return statusCode;
	}

	public String setex(String key, int seconds, String value) {
		String statusCode = null;
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = shardedJedisPool.getResource();
			statusCode = shardedJedis.setex(key, seconds, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (shardedJedis != null)
				shardedJedisPool.returnResource(shardedJedis);
		}
		return statusCode;
	}

	public long del(String key) {
		long statusCode = 0;
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = shardedJedisPool.getResource();
			statusCode = shardedJedis.del(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (shardedJedis != null)
				shardedJedisPool.returnResource(shardedJedis);
		}
		return statusCode;
	}
	
	public long incr(String key) {
		long value = 0;
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = shardedJedisPool.getResource();
			value = shardedJedis.incr(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (shardedJedis != null)
				shardedJedisPool.returnResource(shardedJedis);
		}
		return value;
	}

	public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
		this.shardedJedisPool = shardedJedisPool;
	}

}