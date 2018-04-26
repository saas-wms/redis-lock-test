package recall.redis.lock.frame.result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import recall.redis.lock.frame.exception.BaseException;
import recall.redis.lock.frame.enums.EResultCode;

import java.io.Serializable;

/**
 * Result构建类
 */
public class ResultBuilder implements Serializable {

    private static final long serialVersionUID = -8153590796955513243L;
    private static Logger logger = LoggerFactory.getLogger(ResultBuilder.class);

    /**
     * 构建方法
     * @param cmd 业务封装内部类
     * @param <D> 业务返回值泛型类型
     * @return Result
     */
    public static <D> Result<D> build(Command<D> cmd) {
        D data = null;
        Result<D> result = new Result<>();
        try {
            if(cmd instanceof ResultCommand){
                data = ((ResultCommand<D>)cmd).execute();
            }else if(cmd instanceof VoidCommand){
                ((VoidCommand)cmd).execute();
            }
            result.setSuccess(true);
            result.setMessage("操作成功");
            result.setData(data);
            result.setCode(EResultCode.SUCCESS.code);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setCode(EResultCode.ERROR.code);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("系统错误");
            result.setCode(EResultCode.ERROR.code);
        }
        return result;
    }

}
