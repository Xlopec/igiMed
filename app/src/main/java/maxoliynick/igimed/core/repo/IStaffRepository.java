package maxoliynick.igimed.core.repo;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import maxoliynick.igimed.model.entity.Employee;

/**
 * <p>Repository to store employee staff</p>
 * Created by Максим on 1/18/2017.
 */

public interface IStaffRepository {

    /**
     * @return all stored users
     */
    @NotNull
    Collection<Employee> getStaff();

    /**
     * Updates user
     *
     * @param employee user to update
     */
    void update(@NotNull Employee employee);

    /**
     * Creates a new user
     *
     * @param form user form to store
     */
    @NotNull
    Employee create(@NotNull EmployeeForm form);

}
