package maxoliynick.igimed.di;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Roboguice nullable annotation
 * </p>
 * Created by Максим on 6/13/2016.
 */

@Target({ElementType.FIELD, ElementType.METHOD,
        ElementType.PARAMETER,
        ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Nullable {
}
