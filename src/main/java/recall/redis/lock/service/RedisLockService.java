package recall.redis.lock.service;

import org.springframework.data.redis.core.types.Expiration;
import recall.redis.lock.pojo.RedisLockResult;

/**
 * Redis锁Service
 *
 * @author recall
 * @date 2018/4/5
 */
public interface RedisLockService {

    /**
     * 尝试加锁(非堵塞锁)
     *
     * @param lockKey    锁定的key
     * @param expiration 超时时间
     * @return RedisLockResult 如果失败则返回NULL
     */
    RedisLockResult tryLock(String lockKey, Expiration expiration);

    /**
     * 尝试加锁（超时时间默认为3秒）(非堵塞锁)
     *
     * @param lockKey 锁定的key
     * @return RedisLockResult
     */
    RedisLockResult tryLock(String lockKey);

    /**
     * 尝试解锁(非堵塞锁)
     *
     * @param redisLockResult redis锁
     * @return 是否解锁成功
     */
    boolean tryUnLock(RedisLockResult redisLockResult);

}
