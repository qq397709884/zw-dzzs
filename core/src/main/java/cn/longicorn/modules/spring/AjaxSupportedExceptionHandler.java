package cn.longicorn.modules.spring;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

public class AjaxSupportedExceptionHandler extends SimpleMappingExceptionResolver {

	private Map<String, Logger> cachedLogger = new HashMap<String, Logger>();

	private String ajaxErrorView;
	private String ajaxDefaultErrorMessage = "An error has occurred";
	private boolean ajaxShowTechMessage = true;

	/* @param handler the executed handler, or <code>null</code> if none chosen at the time of the exception 
	 * (for example, if multipart resolution failed)
	 * @param e the exception that got thrown during handler execution
	 * @return a corresponding ModelAndView to forward to, or <code>null</code> for default processing
	 */
	@Override
	public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception e) {
		//Log the message in log file and put it to console if necessary.
		logException(handler, e);
		//Send the error message to user with appropriate format.
		if (isAjax(req)) {
			String exceptionMessage = e.getMessage() == null ? ajaxDefaultErrorMessage : e.getMessage();
			if (ajaxShowTechMessage) {
				exceptionMessage += ";" + getExceptionMessage(e);
			}
			ModelAndView m = new ModelAndView(ajaxErrorView);
			m.addObject("exceptionMessage", exceptionMessage);
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return m;
		} else {
			return super.resolveException(req, resp, handler, e);
		}
	}

	private String getExceptionMessage(Throwable e) {
		String message = "";
		while (e != null) {
			message += e.getMessage() + ";";
			e = e.getCause();
		}
		message = StringUtils.remove(message, '\r');
		message = StringUtils.remove(message, '\n');
		message = StringUtils.replaceChars(message, '"', '\'');
		return message;
	}

	private boolean isAjax(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}

	private void logException(Object handler, Exception e) {
		try {
			String occurClassName = "rootLogger";
			String methodName = "";
			if (handler != null && handler instanceof HandlerMethod) {
				HandlerMethod m = (HandlerMethod) handler;
				Class<?> beanType = m.getBeanType();
				occurClassName = beanType.getCanonicalName();
				methodName = m.getMethod().getName();
			}
			Logger log = getLogger(occurClassName);
			log.error("{}发生错误，异常类型:{}, 错误信息:{}", methodName, e.getClass().getCanonicalName(), e.getMessage(), e);
		} catch (Exception ex) {
			e.printStackTrace();
			ex.printStackTrace();
		}
	}

	public void setAjaxErrorView(String ajaxErrorView) {
		this.ajaxErrorView = ajaxErrorView;
	}

	public void setAjaxDefaultErrorMessage(String ajaxDefaultErrorMessage) {
		this.ajaxDefaultErrorMessage = ajaxDefaultErrorMessage;
	}

	public void setAjaxShowTechMessage(boolean ajaxShowTechMessage) {
		this.ajaxShowTechMessage = ajaxShowTechMessage;
	}

	private synchronized Logger getLogger(String className) {
		Logger log = cachedLogger.get(className);
		if (log == null) {
			log = LoggerFactory.getLogger(className);
			cachedLogger.put(className, log);
		}
		return log;
	}
}
