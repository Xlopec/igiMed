package maxoliynick.igimed.core.repo;

import java.util.ArrayList;
import java.util.Collection;

import maxoliynick.igimed.model.entity.Employee;

/**
 * Created by Максим on 1/18/2017.
 */

final class Mapper {

    private Mapper() {
        // restricts creation via reflection
        throw new RuntimeException("shouldn't be instantiated");
    }

    public static Collection<Employee> fromPo(Collection<EmployeePo> pos) {
        final Collection<Employee> result = new ArrayList<>(pos.size());

        for (final EmployeePo po : pos) {
            result.add(fromPo(po));
        }

        return result;
    }

    public static Employee fromPo(EmployeePo po) {
        // convert from persistence
        // object into domain model
        return new Employee.Builder(po.getId())
                .setName(po.getName())
                .setSurname(po.getSurname())
                .setBirthdayYear(po.getBirthdayYear())
                .setCity(po.getCity())
                .setPosition(po.getPosition())
                .build();
    }

    public static EmployeePo toPo(EmployeeForm form) {
        return new EmployeePo()
                .setName(form.getName())
                .setSurname(form.getSurname())
                .setCity(form.getCity())
                .setBirthdayYear(form.getBirthdayYear())
                .setPosition(form.getPosition());
    }

    public static EmployeePo toPo(Employee employee) {
        return new EmployeePo()
                .setId(employee.getId())
                .setName(employee.getName())
                .setSurname(employee.getSurname())
                .setCity(employee.getCity())
                .setBirthdayYear(employee.getBirthdayYear())
                .setPosition(employee.getPosition());
    }

}
