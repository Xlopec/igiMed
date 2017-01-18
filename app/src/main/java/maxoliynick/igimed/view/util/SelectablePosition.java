package maxoliynick.igimed.view.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import maxoliynick.igimed.core.repo.Position;

/**
 * Created by Максим on 1/18/2017.
 */

public class SelectablePosition {

    private static final Collection<SelectablePosition> POSITIONS;

    static {

        final Collection<SelectablePosition> tmp = new ArrayList<>(Position.values().length);

        for (final Position position : Position.values()) {
            tmp.add(new SelectablePosition(MyTextUtil.capitalize(position.toString()), position));
        }

        POSITIONS = Collections.unmodifiableCollection(tmp);
    }

    public static Collection<SelectablePosition> getPositions() {
        return POSITIONS;
    }

    public final String title;
    public final Position value;

    public SelectablePosition(String title, Position value) {
        this.title = title;
        this.value = value;
    }

    @Override
    public String toString() {
        return "SelectablePosition{" +
                "title='" + title + '\'' +
                ", value=" + value +
                '}';
    }
}
