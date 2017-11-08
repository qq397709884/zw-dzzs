package cn.longicorn.modules.web.exception;

import org.springframework.http.HttpStatus;

public class InputException extends RestException {

    private static final long serialVersionUID = -6702121115019786590L;

    public HttpStatus status = HttpStatus.BAD_REQUEST;

    public InputException(String msg) {
        super(msg);
    }
}
