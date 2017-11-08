package cn.longicorn.dzzs.exception;

import cn.longicorn.dzzs.util.RspData;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常处理器
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月27日 下午10:16:19
 */
@ControllerAdvice
public class DataExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(DataException.class)
    @ResponseBody
    public RspData handleRRException(DataException e) {
        RspData r = new RspData();
        r.setCode(String.valueOf(e.getCode()));
        r.setMessage(e.getMessage());
        return r;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseBody
    public RspData handleDuplicateKeyException(DuplicateKeyException e) {
        logger.error(e.getMessage(), e);
        return new RspData("01", "数据库中已存在该记录");
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseBody
    public RspData handleAuthorizationException(AuthorizationException e) {
        logger.error(e.getMessage(), e);
        return new RspData("01", "没有权限，请联系管理员授权");
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RspData handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return new RspData("01", "错误，请联系管理员");
    }
}
