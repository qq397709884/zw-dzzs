package cn.longicorn.modules.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * 根据表名生成ID
 */
public class IdService {

    private final static Logger logger = LoggerFactory.getLogger(IdService.class);

    private ShardedJedisPool shardedJedisPool;

    /**
     *
     * @param tableName 表名
     * @return ID
     */
    public Long generateId(String tableName) {
        ShardedJedis shardedJedis = null;
        Long id = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            id = shardedJedis.hincrBy("azhixue_ids", tableName, 1);
        } finally {
            if (shardedJedis != null) {
                shardedJedisPool.returnResource(shardedJedis);
            }
        }
        return id;
    }


    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }
}