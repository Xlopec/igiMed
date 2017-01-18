package maxoliynick.igimed.core.repo;

/**
 * <p>Immutable form for repository</p>
 * Created by Максим on 1/18/2017.
 */

public final class EmployeeForm {

    private final String name;
    private final String surname;
    private final String city;
    private final int birthdayYear;
    private final Position position;

    public EmployeeForm(String name, String surname, String city, int birthdayYear, Position position) {
        this.name = name;
        this.surname = surname;
        this.city = city;
        this.birthdayYear = birthdayYear;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getCity() {
        return city;
    }

    public int getBirthdayYear() {
        return birthdayYear;
    }

    public Position getPosition() {
        return position;
    }
}
