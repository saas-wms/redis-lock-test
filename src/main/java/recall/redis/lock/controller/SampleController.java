package recall.redis.lock.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import recall.redis.lock.frame.exception.BaseException;
import recall.redis.lock.frame.result.Result;
import recall.redis.lock.frame.result.ResultBuilder;
import recall.redis.lock.frame.result.ResultCommand;
import recall.redis.lock.frame.result.VoidCommand;
import recall.redis.lock.pojo.RedisLockResult;
import recall.redis.lock.service.RedisLockService;

import java.util.UUID;

/**
 * SampleController
 *
 * @author recall
 * @date 2018/4/27
 * @comment
 */
@RestController
@RequestMapping("/sample")
@Api(tags = "SampleController", description = "Redis锁测试接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SampleController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisLockService redisLockService;

    /**
     * 尝试加锁
     *
     * @param key key
     * @return 加锁结果
     */
    @RequestMapping(value = "/tryLock/{key}",method = RequestMethod.GET)
    @ApiOperation(value = "尝试加锁", notes = "尝试加锁，版本号1.0.0", httpMethod = "GET")
    public Result<RedisLockResult> tryLock(@PathVariable("key") String key) {
        return ResultBuilder.build((ResultCommand<RedisLockResult>) () -> redisLockService.tryLock(key, Expiration.seconds(60)));
    }

    /**
     * 尝试解锁
     *
     * @param redisLockResult 加锁结果
     * @return 解锁结果
     */
    @RequestMapping(value = "/tryUnLock",method = RequestMethod.POST)
    @ApiOperation(value = "尝试解锁", notes = "尝试解锁，版本号1.0.0", httpMethod = "POST")
    public Result<Boolean> lock(@RequestBody RedisLockResult redisLockResult) {
        return ResultBuilder.build((ResultCommand<Boolean>) () -> redisLockService.tryUnLock(redisLockResult));
    }

    /**
     * 常用的加锁和解锁例子
     *
     * @return 操作结果
     */
    @RequestMapping(value = "/demo1",method = RequestMethod.GET)
    @ApiOperation(value = "常用的加锁和解锁例子", notes = "常用的加锁和解锁例子，版本号1.0.0", httpMethod = "GET")
    public Result demo1() {
        return ResultBuilder.build((VoidCommand) () -> {
            String key = "key123123123123123123123123";
            RedisLockResult redisLockResult = null;
            try {
                redisLockResult = redisLockService.tryLock(key);
                if (redisLockResult == null) {
                    throw new BaseException("锁已被占用");
                }
            } finally {
                if (redisLockResult != null) {
                    boolean unLock = redisLockService.tryUnLock(redisLockResult);
                    logger.warn("demo1,key:{}.unLock:{}", key, unLock);
                }
            }
        });
    }

    /**
     * 模拟并发测试
     *
     * @return 操作结果
     */
    @RequestMapping(value = "/demo2",method = RequestMethod.GET)
    @ApiOperation(value = "模拟并发测试", notes = "模拟并发测试，版本号1.0.0", httpMethod = "GET")
    public Result demo3() {
        return ResultBuilder.build((VoidCommand) () -> {
            String key = UUID.randomUUID().toString();
            Runnable redisLockRunnable = () -> {
                RedisLockResult redisLockResult = null;
                try {
                    redisLockResult = redisLockService.tryLock(key);
                    if (redisLockResult == null) {
                        throw new BaseException("锁已被占用");
                    }
                } finally {
                    if (redisLockResult != null) {
                        boolean unLock = redisLockService.tryUnLock(redisLockResult);
                        logger.warn("demo1,key:{}.unLock:{},", key, unLock);
                    }
                }
            };
            for(int i = 0;i<10;i++){
                new Thread(redisLockRunnable).start();
            }
        });
    }

}
