package ma.lias.app.util;

import java.util.regex.Pattern;

public class ValidationUtil {

    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return !isEmpty(email) && Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isNumeric(String value) {
        return value != null && value.matches("\\d+");
    }

    public static boolean maxLength(String value, int max) {
        return value != null && value.length() <= max;
    }
}