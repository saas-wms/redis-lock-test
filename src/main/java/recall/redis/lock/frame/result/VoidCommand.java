package recall.redis.lock.frame.result;

import recall.redis.lock.frame.result.Command;

/**
 * 无返回值
 * @author recall
 * @date 2018/4/22
 */
public interface VoidCommand extends Command<Object> {

    /**
     * 业务执行接口
     * @throws Throwable 可能出现的异常
     */
    void execute() throws Throwable;

}
