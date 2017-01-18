package maxoliynick.igimed.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.roboguice.shaded.goole.common.base.Preconditions;

import maxoliynick.igimed.core.repo.Position;
import maxoliynick.igimed.model.entity.Employee;

/**
 * Created by Максим on 1/18/2017.
 */

public final class EmployeeViewForm implements Parcelable {

    private String name;
    private String surname;
    private String city;
    private int birthdayYear;
    private Position position;

    public EmployeeViewForm() {}

    public EmployeeViewForm(Employee employee) {
        Preconditions.checkNotNull(employee);
        name = employee.getName();
        surname = employee.getSurname();
        city = employee.getCity();
        birthdayYear = employee.getBirthdayYear();
        position = employee.getPosition();
    }

    private EmployeeViewForm(Parcel in) {
        name = in.readString();
        surname = in.readString();
        city = in.readString();
        birthdayYear = in.readInt();
        final int ordinal = in.readInt();

        if (ordinal >= 0) {
            position = Position.values()[ordinal];
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(city);
        dest.writeInt(birthdayYear);
        if (position != null) {
            dest.writeInt(position.ordinal());
        } else {
            dest.writeInt(-1);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EmployeeViewForm> CREATOR = new Creator<EmployeeViewForm>() {
        @Override
        public EmployeeViewForm createFromParcel(Parcel in) {
            return new EmployeeViewForm(in);
        }

        @Override
        public EmployeeViewForm[] newArray(int size) {
            return new EmployeeViewForm[size];
        }
    };

    public String getName() {
        return name;
    }

    public EmployeeViewForm setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public EmployeeViewForm setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getCity() {
        return city;
    }

    public EmployeeViewForm setCity(String city) {
        this.city = city;
        return this;
    }

    public int getBirthdayYear() {
        return birthdayYear;
    }

    public EmployeeViewForm setBirthdayYear(int birthdayYear) {
        this.birthdayYear = birthdayYear;
        return this;
    }

    public Position getPosition() {
        return position;
    }

    public EmployeeViewForm setPosition(Position position) {
        this.position = position;
        return this;
    }

    @Override
    public String toString() {
        return "EmployeeViewForm{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", city='" + city + '\'' +
                ", birthdayYear=" + birthdayYear +
                ", position=" + position +
                '}';
    }
}
