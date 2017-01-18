package maxoliynick.igimed.model;

import android.content.Context;
import android.content.Intent;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import maxoliynick.igimed.model.entity.Employee;
import rx.Observable;

/**
 * Created by Максим on 1/18/2017.
 */

public interface IMainModel {

    Observable<Collection<Employee>> getStaff();

    Intent onEmployeeSelected(@NotNull Context context, @NotNull Employee employee);

    Intent onCreateEmployee(@NotNull Context context);

}
