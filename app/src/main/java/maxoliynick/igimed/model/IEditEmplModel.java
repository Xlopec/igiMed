package maxoliynick.igimed.model;

import org.jetbrains.annotations.NotNull;

import maxoliynick.igimed.model.entity.Employee;
import rx.Observable;

/**
 * Created by Максим on 1/18/2017.
 */

public interface IEditEmplModel {

    Observable<Employee> editEmployee(long id, @NotNull EmployeeViewForm form);

}
