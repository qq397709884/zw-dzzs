package cn.longicorn.modules.web.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends RestException {
    private static final long serialVersionUID = 4344863324700646306L;

    public HttpStatus status = HttpStatus.FORBIDDEN;

    public ForbiddenException(String msg) {
        super(msg);
    }

}
