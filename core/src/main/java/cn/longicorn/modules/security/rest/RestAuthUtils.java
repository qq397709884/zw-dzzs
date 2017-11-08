package cn.longicorn.modules.security.rest;

import cn.longicorn.modules.utils.Encodes;

import javax.servlet.http.HttpServletRequest;

public class RestAuthUtils {

    private static final ThreadLocal<Session> threadSession = new ThreadLocal<>();

    public static void cleanThreadLocalSession() {
        threadSession.remove();
    }

    public static String[] getAuthorization(HttpServletRequest request) throws RestAuthticationException {
        String authorization = request.getHeader("authorization");
        if (authorization == null || authorization.equals("")) {
            throw new RestAuthticationException("Http header has no authorization information!");
        }

        String userAndPass = new String(Encodes.decodeBase64(authorization.split(" ")[1]));

        if (userAndPass.split(":").length < 2) {
            throw new RestAuthticationException("Authrozation information is invalid.");
        }

        return userAndPass.split(":");
    }

    public static Session getSession() {
        return threadSession.get();
    }

    public static void setSession(Session s) {
        threadSession.set(s);
    }

    public static String currentUsername() {
        Session s = getSession();
        return s != null ? s.getLoginName() : null;
    }

    public static String currentUserDisplayName() {
        Session s = getSession();
        return s != null ? s.getDisplayName() : null;
    }

    public static String currentSid() {
        Session s = getSession();
        return s != null ? s.getSid() : null;
    }

    public static String currentUid() {
        Session s = getSession();
        return s != null ? s.getUid() : null;
    }

}