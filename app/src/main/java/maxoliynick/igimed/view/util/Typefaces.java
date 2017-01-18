package maxoliynick.igimed.view.util;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by Максим on 10/27/2016.
 */

public final class Typefaces {

    private static final Hashtable<String, Typeface> CACHE = new Hashtable<>();

    private Typefaces() {
        // shouldn't be instantiated
        throw new RuntimeException();
    }

    public static Typeface get(Context c, String name) {
        synchronized (CACHE) {
            if (!CACHE.containsKey(name)) {
                Typeface t = Typeface.createFromAsset(
                        c.getAssets(),
                        String.format("fonts/%s.ttf", name)
                );
                CACHE.put(name, t);
            }
            return CACHE.get(name);
        }
    }

}
