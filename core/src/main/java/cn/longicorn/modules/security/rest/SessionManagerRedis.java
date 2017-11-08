package cn.longicorn.modules.security.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.longicorn.modules.mapper.JsonMapper;
import cn.longicorn.modules.spring.security.IUser;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class SessionManagerRedis implements ISessionManager {

	private static final Logger logger = LoggerFactory.getLogger(SessionManagerRedis.class);
	private final static String USERKEY = "user:";
	private int expired = 60 * 60 * 24;

	private ShardedJedisPool shardedJedisPool;
	private JsonMapper jsonMapper = new JsonMapper();

	@Override
	public boolean isValid(final String sid) {
		ShardedJedis shardedJedis = null;
		if (sid != null) {
			try {
				shardedJedis = shardedJedisPool.getResource();
				if (shardedJedis.exists(sid)) {
					return true;
				}
			} catch (Exception e) {
				logger.error("Session id verify fail, {}", e.getMessage(), e);
			} finally {
				if (shardedJedis != null) {
					shardedJedisPool.returnResource(shardedJedis);
				}
			}
		}
		return false;
	}

	@Override
	public Session loadSession(final String sid) {
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = shardedJedisPool.getResource();
			String sessionStr = shardedJedis.get(sid);
			if (sessionStr != null) {
				return jsonMapper.fromJson(sessionStr, Session.class);
			}
		} catch (Exception e) {
			logger.error("Load Session {} fail, {}", sid, e.getMessage(), e);
		} finally {
			if (shardedJedis != null)
				shardedJedisPool.returnResource(shardedJedis);
		}
		return null;
	}

	@Override
	public boolean remove(String sid) {
		boolean ret = false;
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = shardedJedisPool.getResource();
			String sessionStr = shardedJedis.get(sid);
			if (sessionStr != null) {
				Session session = jsonMapper.fromJson(sessionStr, Session.class);
				String userKey = USERKEY + session.getLoginName();
				shardedJedis.del(userKey);
				shardedJedis.del(sid);
				ret = true;
			}
		} catch (Exception e) {
			logger.error("Remove session {} fail", sid, e);
		} finally {
			if (shardedJedis != null)
				shardedJedisPool.returnResource(shardedJedis);
		}
		return ret;
	}

	@Override
	public String saveOrUpdateSession(IUser user) {
		String sid = null;
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = shardedJedisPool.getResource();

			String userKey = USERKEY + user.getLoginName();
			sid = shardedJedis.get(userKey);
			if (sid != null) {
				shardedJedis.expire(userKey, expired);
				shardedJedis.expire(sid, expired);
			} else {
				Session session = new Session(user);
				sid = session.getSid();
				shardedJedis.setex(userKey, expired, sid);
				shardedJedis.setex(sid, expired, jsonMapper.toJson(session));
			}
		} catch (Exception e) {
			logger.error("Save or update session fail, {}", e.getMessage(), e);
		} finally {
			if (shardedJedis != null)
				shardedJedisPool.returnResource(shardedJedis);
		}
		return sid;
	}

	public void setExpired(int expired) {
		this.expired = expired;
	}

	public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
		this.shardedJedisPool = shardedJedisPool;
	}
}
