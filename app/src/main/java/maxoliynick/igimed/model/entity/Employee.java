package maxoliynick.igimed.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;
import org.roboguice.shaded.goole.common.base.Preconditions;

import java.util.Calendar;
import java.util.Locale;

import maxoliynick.igimed.core.repo.Position;
import maxoliynick.igimed.core.util.IBuilder;

/**
 * Immutable class to preserve modifications
 * in created instance
 * Created by Максим on 1/18/2017.
 */

public final class Employee implements Parcelable {

    private final long id;
    private final String name;
    private final String surname;
    private final int birthdayYear;
    private final String city;
    private final Position position;

    private Employee(Builder builder) {
        // birthday year < current year
        Preconditions.checkArgument(builder.getBirthdayYear() < Calendar.getInstance().get(Calendar.YEAR),
                String.format(Locale.getDefault(), "Invalid birthday year, was %d", builder.getBirthdayYear()));

        this.id = builder.getId();
        this.name = Preconditions.checkNotNull(builder.getName());
        this.surname = Preconditions.checkNotNull(builder.getSurname());
        this.birthdayYear = builder.getBirthdayYear();
        this.city = Preconditions.checkNotNull(builder.getCity());
        this.position = Preconditions.checkNotNull(builder.getPosition());
    }

    private Employee(Parcel in) {
        id = in.readLong();
        name = in.readString();
        surname = in.readString();
        birthdayYear = in.readInt();
        city = in.readString();
        position = Position.values()[in.readInt()];
    }

    public static final Creator<Employee> CREATOR = new Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeInt(birthdayYear);
        dest.writeString(city);
        dest.writeInt(position.ordinal());
    }

    public static class Builder implements IBuilder<Employee> {

        private final long id;
        private String name;
        private String surname;
        private int birthdayYear;
        private String city;
        private Position position;

        public Builder(long id) {
            Preconditions.checkArgument(id > 0);
            this.id = id;
        }

        /**
         * Copies fields of specified instance of {@link Employee}
         * @param employee instance to make copy from
         */
        public Builder(@NotNull Employee employee) {
            Preconditions.checkNotNull(employee);

            this.id = employee.getId();
            setName(employee.getName())
                    .setSurname(employee.getSurname())
                    .setCity(employee.getCity())
                    .setBirthdayYear(employee.getBirthdayYear())
                    .setPosition(employee.getPosition());
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public String getSurname() {
            return surname;
        }

        public Builder setSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public int getBirthdayYear() {
            return birthdayYear;
        }

        public Builder setBirthdayYear(int birthdayYear) {
            this.birthdayYear = birthdayYear;
            return this;
        }

        public String getCity() {
            return city;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Position getPosition() {
            return position;
        }

        public Builder setPosition(Position position) {
            this.position = position;
            return this;
        }

        @Override
        public Employee build() {
            return new Employee(this);
        }
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getBirthdayYear() {
        return birthdayYear;
    }

    public String getCity() {
        return city;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        if (id != employee.id) return false;
        if (birthdayYear != employee.birthdayYear) return false;
        if (!name.equals(employee.name)) return false;
        if (!surname.equals(employee.surname)) return false;
        if (!city.equals(employee.city)) return false;
        return position == employee.position;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + surname.hashCode();
        result = 31 * result + birthdayYear;
        result = 31 * result + city.hashCode();
        result = 31 * result + position.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthdayYear=" + birthdayYear +
                ", city='" + city + '\'' +
                ", position=" + position +
                '}';
    }
}
