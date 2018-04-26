package recall.redis.lock.pojo;

import lombok.Data;

/**
 * Redis锁的结果
 * @author recall
 * @date 2018/4/5
 */
@Data
public class RedisLockResult {

    /**
     * 锁定的key
     */
    private String lockKey;

    /**
     * 对应key的值
     */
    private String value;
}
