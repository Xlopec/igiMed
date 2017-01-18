package maxoliynick.igimed.view.util;

import org.roboguice.shaded.goole.common.base.Preconditions;

/**
 * Created by Максим on 1/18/2017.
 */

public final class MyTextUtil {

    private MyTextUtil() {
        throw new RuntimeException();
    }

    public static String capitalize(final String text) {
        Preconditions.checkNotNull(text, "text == null");

        if(text.length() == 0) return text;

        final char upperCase = Character.toUpperCase(text.charAt(0));
        return text.length() >= 2 ? upperCase + text.substring(1) : String.valueOf(upperCase);
    }

    public static String valueOrEmpty(final String str) {
        return str == null ? "" : str;
    }

}

