package recall.redis.lock.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import recall.redis.lock.pojo.RedisLockResult;
import recall.redis.lock.service.RedisLockService;

import java.util.Collections;
import java.util.UUID;

/**
 * Redis锁
 *
 * @author recall
 * @date 2018/4/5
 */
@Service
public class RedisLockServiceImpl implements RedisLockService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 默认的超时时间（3秒）
     */
    private static Expiration DEFAULT_EXPIRATION = Expiration.seconds(3);

    /**
     * 尝试解锁脚本
     */
    private static String TRY_UNLOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then if redis.call('del', KEYS[1]) == 1 then return true else return false end else return false end";

    @Override
    public RedisLockResult tryLock(String lockKey, Expiration expiration) {
        String value = UUID.randomUUID().toString();

        byte[] rawKey = rawKey(lockKey);
        byte[] rawValue = rawValue(value);

        Boolean success = stringRedisTemplate.execute(connection -> connection.set(rawKey, rawValue, expiration, RedisStringCommands.SetOption.ifAbsent()), true);
        if (!success) {
            logger.info("tryLock,lockKey:{},fail", lockKey);
            return null;
        }

        logger.info("tryLock,lockKey:{},success,value:{}", lockKey, value);

        RedisLockResult redisLockResult = new RedisLockResult();
        redisLockResult.setLockKey(lockKey);
        redisLockResult.setValue(value);
        return redisLockResult;
    }

    @Override
    public RedisLockResult tryLock(String lockKey) {
        return tryLock(lockKey, DEFAULT_EXPIRATION);
    }

    @Override
    public boolean tryUnLock(RedisLockResult redisLockResult) {
        if(redisLockResult == null){
            return false;
        }

        String lockKey = redisLockResult.getLockKey();
        String value = redisLockResult.getValue();
        return stringRedisTemplate.execute(new RedisScript<Boolean>() {
            @Override
            public String getSha1() {
                return DigestUtils.sha1DigestAsHex(getScriptAsString());
            }

            @Override
            public Class<Boolean> getResultType() {
                return Boolean.class;
            }

            @Override
            public String getScriptAsString() {
                return TRY_UNLOCK_SCRIPT;
            }
        }, Collections.singletonList(lockKey), value);
    }

    /**
     * 序列化key
     *
     * @param key key
     * @return 序列化后的byte数组
     */
    @SuppressWarnings("unchecked")
    private byte[] rawKey(Object key) {
        Assert.notNull(key, "non null key required");
        RedisSerializer keySerializer = stringRedisTemplate.getKeySerializer();
        return keySerializer.serialize(key);
    }

    /**
     * 序列化value
     *
     * @param value value
     * @return 序列化后的byte数组
     */
    @SuppressWarnings("unchecked")
    private byte[] rawValue(Object value) {
        RedisSerializer valueSerializer = stringRedisTemplate.getValueSerializer();
        return valueSerializer.serialize(value);
    }

}
