package maxoliynick.igimed.model;

import android.content.Context;
import android.content.Intent;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import javax.inject.Inject;

import maxoliynick.igimed.core.repo.IStaffRepository;
import maxoliynick.igimed.model.entity.Employee;
import maxoliynick.igimed.view.CreateEmployeeActivity;
import maxoliynick.igimed.view.EditEmployeeActivity;
import roboguice.inject.ContextSingleton;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Максим on 1/18/2017.
 */
@ContextSingleton
public final class MainModel implements IMainModel {

    private final IStaffRepository repository;

    @Inject
    public MainModel(IStaffRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<Collection<Employee>> getStaff() {
        return Observable.defer(() ->
                Observable.create((Observable.OnSubscribe<Collection<Employee>>) (subscriber) -> {
                    // unchecked exceptions are automatically
                    // forwarded to onError()
                    subscriber.onStart();
                    subscriber.onNext(repository.getStaff());
                    subscriber.onCompleted();
                }))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Intent onEmployeeSelected(@NotNull Context context, @NotNull Employee employee) {
        final Intent intent = new Intent(context, EditEmployeeActivity.class);

        intent.putExtra(EditEmployeeActivity.ARG_EMPLOYEE, employee);
        return intent;
    }

    @Override
    public Intent onCreateEmployee(@NotNull Context context) {
        return new Intent(context, CreateEmployeeActivity.class);
    }


}
