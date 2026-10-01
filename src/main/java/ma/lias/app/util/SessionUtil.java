package ma.lias.app.util;

import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    public static void setUserSession(HttpSession session, Long userId, String role) {
        session.setAttribute("userId", userId);
        session.setAttribute("role", role);
    }

    public static Long getUserId(HttpSession session) {
        return (Long) session.getAttribute("userId");
    }

    public static String getUserRole(HttpSession session) {
        return (String) session.getAttribute("role");
    }

    public static boolean isLoggedIn(HttpSession session) {
        return session != null && session.getAttribute("userId") != null;
    }

    public static void invalidate(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }
}