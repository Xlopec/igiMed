package maxoliynick.igimed.model;

import org.jetbrains.annotations.NotNull;
import org.roboguice.shaded.goole.common.base.Preconditions;

import java.util.Locale;

import javax.inject.Inject;

import maxoliynick.igimed.core.repo.IStaffRepository;
import maxoliynick.igimed.model.entity.Employee;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Максим on 1/18/2017.
 */

public final class EditEmplModel implements IEditEmplModel {

    private final IStaffRepository repository;

    @Inject
    public EditEmplModel(IStaffRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<Employee> editEmployee(long id, @NotNull EmployeeViewForm form) {
        return Observable.defer(() ->
                // employee creation may be time-consuming operation => execute in a separate thread
                Observable.create((Observable.OnSubscribe<Employee>) (subscriber) -> {
                    // will throw exception, so that it can be intercepted by onError();
                    Preconditions.checkNotNull(form);
                    Preconditions.checkArgument(id > 0,
                            String.format(Locale.getDefault(), "Invalid id, was %d", id));
                    // unchecked exceptions are automatically
                    // forwarded to onError()
                    subscriber.onStart();

                    final Employee updated = new Employee.Builder(id)
                            .setName(form.getName())
                            .setSurname(form.getSurname())
                            .setCity(form.getCity())
                            .setBirthdayYear(form.getBirthdayYear())
                            .setPosition(form.getPosition())
                            .build();

                    repository.update(updated);
                    subscriber.onNext(updated);
                    subscriber.onCompleted();
                }))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
