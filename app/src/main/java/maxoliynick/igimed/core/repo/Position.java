package maxoliynick.igimed.core.repo;

/**
 * Created by Максим on 1/18/2017.
 */

public enum Position {

    DOCTOR, PHARMACIST, NURSE, PARAMEDIC, DRIVER;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
