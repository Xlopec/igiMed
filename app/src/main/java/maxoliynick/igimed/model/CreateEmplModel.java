package maxoliynick.igimed.model;

import org.jetbrains.annotations.NotNull;
import org.roboguice.shaded.goole.common.base.Preconditions;

import javax.inject.Inject;

import maxoliynick.igimed.core.repo.EmployeeForm;
import maxoliynick.igimed.core.repo.IStaffRepository;
import maxoliynick.igimed.model.entity.Employee;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Максим on 1/18/2017.
 */

public final class CreateEmplModel implements ICreateEmplModel {

    private final IStaffRepository repository;

    @Inject
    public CreateEmplModel(IStaffRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<Employee> createEmployee(@NotNull EmployeeViewForm form) {
        return Observable.defer(() ->
                // employee creation may be time-consuming operation => execute in a separate thread
                Observable.create((Observable.OnSubscribe<Employee>) (subscriber) -> {
                    // will throw exception, so that it can be intercepted by onError();
                    Preconditions.checkNotNull(form);
                    // unchecked exceptions are automatically
                    // forwarded to onError()
                    subscriber.onStart();
                    subscriber.onNext(repository.create(CreateEmplModel.toRepoForm(form)));
                    subscriber.onCompleted();
                }))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static EmployeeForm toRepoForm(EmployeeViewForm form) {
        return new EmployeeForm(form.getName(), form.getSurname(), form.getCity(),
                form.getBirthdayYear(), form.getPosition());
    }

}
