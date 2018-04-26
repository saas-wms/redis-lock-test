package recall.redis.lock.frame.result;

import recall.redis.lock.frame.result.Command;

/**
 * 有返回值
 * @author recall
 * @date 2018/4/22
 */
public interface ResultCommand<R> extends Command<R> {

    /**
     * 业务执行接口
     * @return 业务执行返回值
     * @throws Throwable 业务可能出现的异常
     */
    R execute() throws Throwable;

}
