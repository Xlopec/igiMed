package maxoliynick.igimed.core.repo;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Максим on 1/18/2017.
 */
@DatabaseTable(tableName = "Employee")
public final class EmployeePo {

    @DatabaseField(generatedId = true, index = true, dataType = DataType.LONG)
    private long id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private String surname;

    @DatabaseField
    private int birthdayYear;

    @DatabaseField(canBeNull = false)
    private String city;

    @DatabaseField(canBeNull = false, dataType = DataType.ENUM_INTEGER)
    private Position position;

    public EmployeePo() {
        // empty constructor is required for ORM
    }

    public long getId() {
        return id;
    }

    public EmployeePo setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public EmployeePo setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public EmployeePo setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public int getBirthdayYear() {
        return birthdayYear;
    }

    public EmployeePo setBirthdayYear(int birthdayYear) {
        this.birthdayYear = birthdayYear;
        return this;
    }

    public String getCity() {
        return city;
    }

    public EmployeePo setCity(String city) {
        this.city = city;
        return this;
    }

    public Position getPosition() {
        return position;
    }

    public EmployeePo setPosition(Position position) {
        this.position = position;
        return this;
    }
}
