package cn.longicorn.modules.web.exception;

import cn.longicorn.modules.beanvalidator.BeanValidators;
import cn.longicorn.modules.mapper.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    private JsonMapper jsonMapper = new JsonMapper();

    @ExceptionHandler(value = {RestException.class})
    public final ResponseEntity<?> handleException(RestException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/plain; charset=UTF-8"));
        logger.error(ex.getMessage(), ex);
        return handleExceptionInternal(ex, ex.getMessage(), headers, ex.status, request);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public final ResponseEntity<?> handleException(ConstraintViolationException ex, WebRequest request) {
        Map<String, String> errors = BeanValidators.extractPropertyAndMessage(ex.getConstraintViolations());
        String body = jsonMapper.toJson(errors);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        logger.error(ex.getMessage(), ex);
        return handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {InputException.class})
    public final ResponseEntity<?> handleException(InputException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        logger.error(ex.getMessage(), ex);
        return handleExceptionInternal(ex, ex.getMessage(), headers, ex.status, request);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public final ResponseEntity<?> handleException(NotFoundException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        logger.error(ex.getMessage(), ex);
        return handleExceptionInternal(ex, ex.getMessage(), headers, ex.status, request);
    }

    @ExceptionHandler(value = {ForbiddenException.class})
    public final ResponseEntity<?> handleException(ForbiddenException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        logger.error(ex.getMessage(), ex);
        return handleExceptionInternal(ex, ex.getMessage(), headers, ex.status, request);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public final ResponseEntity<?> handleException(RuntimeException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        logger.error(ex.getMessage(), ex);
        return handleExceptionInternal(ex, ex.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}