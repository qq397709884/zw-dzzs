package cn.longicorn.modules.data.mongo;

public class UpdateException extends RuntimeException {

    private static final long serialVersionUID = 8528858523430601943L;

    public UpdateException(String msg) {
        super(msg);
    }

    public UpdateException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
