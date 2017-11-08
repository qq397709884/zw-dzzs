package cn.longicorn.modules.web.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RestException {
    private static final long serialVersionUID = 4408569273901848350L;

    public HttpStatus status = HttpStatus.NOT_FOUND;

    public NotFoundException(String msg) {
        super(msg);
    }

}