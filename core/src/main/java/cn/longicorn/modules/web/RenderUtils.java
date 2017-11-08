package cn.longicorn.modules.web;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 
 * 实现获取Request/Response/Session与绕过jsp/freemaker直接输出文本的简化函数.
 * 
 * @author calvin
 * @author zhuchanglin
 */
public class RenderUtils {

	//header 常量定义
	private static final String ENCODING_PREFIX = "encoding";
	private static final String NOCACHE_PREFIX = "no-cache";
	private static final String ENCODING_DEFAULT = "UTF-8";
	private static final boolean NOCACHE_DEFAULT = true;

	private static Logger logger = LoggerFactory.getLogger(RenderUtils.class);

	//绕过jsp/freemaker直接输出文本的函数

	/**
	 * 直接输出内容的简便函数.
	 
	 * eg.
	 * render("text/plain", "hello", "encoding:GBK");
	 * render("text/plain", "hello", "no-cache:false");
	 * render("text/plain", "hello", "encoding:GBK", "no-cache:false");
	 * 
	 * @param headers 可变的header数组，目前接受的值为"encoding:"或"no-cache:",默认值分别为UTF-8和true.                 
	 */
	public static void render(HttpServletResponse response, final String contentType, final String content,
			final String... headers) {
		try {
			//分析headers参数
			String encoding = ENCODING_DEFAULT;
			boolean noCache = NOCACHE_DEFAULT;
			for (String header : headers) {
				String headerName = StringUtils.substringBefore(header, ":");
				String headerValue = StringUtils.substringAfter(header, ":");

				if (StringUtils.equalsIgnoreCase(headerName, ENCODING_PREFIX)) {
					encoding = headerValue;
				} else if (StringUtils.equalsIgnoreCase(headerName, NOCACHE_PREFIX)) {
					noCache = Boolean.parseBoolean(headerValue);
				} else {
					throw new IllegalArgumentException(headerName + "不是一个合法的header类型");
				}
			}

			//设置headers参数
			String fullContentType = contentType + ";charset=" + encoding;
			response.setContentType(fullContentType);
			if (noCache) {
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
			}
			response.getWriter().write(content);
		} catch (IOException e) {
			logger.error("Rendering error", e);
		}
	}

	/**
	 * 直接输出文本.
	 */
	public static void renderText(HttpServletResponse response, final String text, final String... headers) {
		render(response, "text/plain", text, headers);
	}

	/**
	 * 直接输出HTML.
	 */
	public static void renderHtml(HttpServletResponse response, final String html, final String... headers) {
		render(response, "text/html", html, headers);
	}

	/**
	 * 直接输出XML.
	 */
	public static void renderXml(HttpServletResponse response, final String xml, final String... headers) {
		render(response, "text/xml", xml, headers);
	}

	/**
	 * 直接输出JSON.
	 * 
	 * @param string json字符串.
	 */
	public static void renderJson(HttpServletResponse response, final String string, final String... headers) {
		render(response, "application/json", string, headers);
	}

	public static void error(HttpServletResponse response, Object id, String message) {
		result(response, "error", id, message);
	}

	public static void success(HttpServletResponse response, Object id, String message) {
		result(response, "ok", id, message);
	}

	public static void result(HttpServletResponse response, String status, Object id, String message) {
		String smg = "{\"status\":\"" + status + "\",\"msg\":\"" + message + "\",\"id\":\"" + id + "\"}";
		renderJson(response, smg);
	}

}