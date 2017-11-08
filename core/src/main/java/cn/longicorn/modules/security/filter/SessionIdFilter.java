package cn.longicorn.modules.security.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by lkh on 2016/1/26.
 */

public class SessionIdFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String sessionId = request.getParameter("sessionId");
        if (sessionId != null) {
            HttpServletRequest hrequest = (HttpServletRequest) request;
            HttpSession session = hrequest.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
