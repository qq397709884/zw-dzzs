package cn.longicorn.modules.spring.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.util.CommonUtils;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * 支持Ajax访问的CasAuthenticationEntryPoint, 因为CasAuthenticationEntryPoint的官方实现将commence
 * 方法弄成了final, 所以只好将整个类完整copy过来修改。
 * Used by the <code>ExceptionTranslationFilter</code> to commence authentication via the JA-SIG Central
 * Authentication Service (CAS).
 * <p>
 * The user's browser will be redirected to the JA-SIG CAS enterprise-wide login page.
 * This page is specified by the <code>loginUrl</code> property. Once login is complete, the CAS login page will
 * redirect to the page indicated by the <code>service</code> property. The <code>service</code> is a HTTP URL
 * belonging to the current application. The <code>service</code> URL is monitored by the {CasAuthenticationFilter},
 * which will validate the CAS login was successful.
 *
 * @author Ben Alex
 * @author Scott Battaglia
 * @author zhuchanglin
 */
public class AjaxCasAuthenticationEntryPoint implements AuthenticationEntryPoint, InitializingBean {

	public static Logger logger = LoggerFactory.getLogger(AjaxCasAuthenticationEntryPoint.class);

	//~ Instance fields ================================================================================================
	private ServiceProperties serviceProperties;

	private String loginUrl;

	private String sidLoginUrl;

	private boolean useSessionId = false;

	private static final String SID_PARAM = "sessionId";
	/**
	 * Determines whether the Service URL should include the session id for the specific user.  As of CAS 3.0.5, the
	 * session id will automatically be stripped.  However, older versions of CAS (i.e. CAS 2), do not automatically
	 * strip the session identifier (this is a bug on the part of the older server implementations), so an option to
	 * disable the session encoding is provided for backwards compatibility.
	 *
	 * By default, encoding is enabled.
	 * @deprecated since 3.0.0 because CAS is currently on 3.3.5.
	 */
	@Deprecated
	private boolean encodeServiceUrlWithSessionId = true;

	//~ Methods ========================================================================================================

	public void afterPropertiesSet() throws Exception {
		Assert.hasLength(this.loginUrl, "loginUrl must be specified");
		Assert.notNull(this.serviceProperties, "serviceProperties must be specified");
		Assert.notNull(this.serviceProperties.getService(), "serviceProperties.getService() cannot be null.");
	}

	public final void commence(final HttpServletRequest servletRequest, final HttpServletResponse response,
			final AuthenticationException authenticationException) throws IOException, ServletException {
		if (isAjax(servletRequest)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Session Timout");
			return;
		}

		if (useSessionId) {
			String url = generateSidLoginUrl(servletRequest);
			if (url != null) {
				//重定向到cas
				response.sendRedirect(url);
				return;
			}
		}

		final String urlEncodedService = createServiceUrl(servletRequest, response);
		final String redirectUrl = createRedirectUrl(urlEncodedService);

		preCommence(servletRequest, response);

		response.sendRedirect(redirectUrl);
	}

	/**
	 * Constructs a new Service Url.  The default implementation relies on the CAS client to do the bulk of the work.
	 * @param request the HttpServletRequest
	 * @param response the HttpServlet Response
	 * @return the constructed service url.  CANNOT be NULL.
	 */
	protected String createServiceUrl(final HttpServletRequest request, final HttpServletResponse response) {
		return CommonUtils.constructServiceUrl(null, response, this.serviceProperties.getService(), null,
				this.serviceProperties.getArtifactParameter(), this.encodeServiceUrlWithSessionId);
	}

	/**
	 * Constructs the Url for Redirection to the CAS server.  Default implementation relies on the CAS client to do the bulk of the work.
	 *
	 * @param serviceUrl the service url that should be included.
	 * @return the redirect url.  CANNOT be NULL.
	 */
	protected String createRedirectUrl(final String serviceUrl) {
		return CommonUtils.constructRedirectUrl(this.loginUrl, this.serviceProperties.getServiceParameter(),
				serviceUrl, this.serviceProperties.isSendRenew(), false);
	}

	/**
	 * Template method for you to do your own pre-processing before the redirect occurs.
	 *
	 * @param request the HttpServletRequest
	 * @param response the HttpServletResponse
	 */
	protected void preCommence(final HttpServletRequest request, final HttpServletResponse response) {

	}

	/**
	 * The enterprise-wide CAS login URL. Usually something like
	 * <code>https://www.mycompany.com/cas/login</code>.
	 *
	 * @return the enterprise-wide CAS login URL
	 */
	public final String getLoginUrl() {
		return this.loginUrl;
	}

	public final ServiceProperties getServiceProperties() {
		return this.serviceProperties;
	}

	public final void setLoginUrl(final String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public final void setServiceProperties(final ServiceProperties serviceProperties) {
		this.serviceProperties = serviceProperties;
	}

	/**
	 * Sets whether to encode the service url with the session id or not.
	 *
	 * @param encodeServiceUrlWithSessionId whether to encode the service url with the session id or not.
	 * @deprecated since 3.0.0 because CAS is currently on 3.3.5.
	 */
	@Deprecated
	public final void setEncodeServiceUrlWithSessionId(final boolean encodeServiceUrlWithSessionId) {
		this.encodeServiceUrlWithSessionId = encodeServiceUrlWithSessionId;
	}

	/**
	 * Sets whether to encode the service url with the session id or not.
	 * @return whether to encode the service url with the session id or not.
	 *
	 * @deprecated since 3.0.0 because CAS is currently on 3.3.5.
	 */
	@Deprecated
	protected boolean getEncodeServiceUrlWithSessionId() {
		return this.encodeServiceUrlWithSessionId;
	}

	private boolean isAjax(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}

	public void setUseSessionId(boolean useSessionId) {
		this.useSessionId = useSessionId;
	}

	public void setSidLoginUrl(String sidLoginUrl) {
		this.sidLoginUrl = sidLoginUrl;
	}

	private String generateSidLoginUrl(HttpServletRequest request) {
        String sessionId = request.getParameter(SID_PARAM);
		if (sessionId != null) {
			try {
				StringBuffer sb = new StringBuffer();
				sb.append(sidLoginUrl).append("?").append(SID_PARAM).append("=").append(sessionId).append("&service=").append(createServiceUrl(request));
				return sb.toString();
			} catch (UnsupportedEncodingException e) {
				logger.error("构造sessionId登录错误!, sessionId为:{}", sessionId);
			}
		}
		return null;
	}

	private String createServiceUrl(HttpServletRequest request) throws UnsupportedEncodingException {
		String url = request.getRequestURL().toString();
		String q = getQueryString(request.getQueryString());
		if (q != null && !"".equals(q)) {
			url += "?" + q;
		}
		return URLEncoder.encode(url, "UTF-8");
	}

	private String getQueryString(String queryString) {
        if (queryString == null) return null;
		String[] querys = queryString.split("&");
		StringBuilder sb = new StringBuilder();
		String prefix = SID_PARAM + "=";
		for (String query : querys) {
			if (!query.contains(prefix)) {
				sb.append(query).append("&");
			}
		}
		if (sb.length() > 0) return sb.substring(0, sb.length() - 1); else return null;
	}

}