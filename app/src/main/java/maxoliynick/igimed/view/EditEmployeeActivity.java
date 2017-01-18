package maxoliynick.igimed.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;
import org.roboguice.shaded.goole.common.base.Preconditions;

import java.util.List;
import java.util.Locale;

import maxoliynick.igimed.R;
import maxoliynick.igimed.core.repo.Position;
import maxoliynick.igimed.model.EmployeeViewForm;
import maxoliynick.igimed.model.IEditEmplModel;
import maxoliynick.igimed.model.entity.Employee;
import maxoliynick.igimed.view.util.MyTextUtil;
import maxoliynick.igimed.view.util.SelectablePosition;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import rx.Subscription;

/**
 * Created by Максим on 1/18/2017.
 */
@ContentView(R.layout.activity_create_employee)
public final class EditEmployeeActivity extends BaseActivity
        implements Validator.ValidationListener, DatePickerDialog.OnDateSetListener {

    public static final String ARG_RESULT = "argResult";
    public static final String ARG_EMPLOYEE = "argEmployee";
    private static final String ARG_FORM = "argForm";
    private static final String ARG_EMPLOYEE_ID = "argEmployeeId";
    private static final String ARG_POSITION = "argPosition";

    @InjectView(R.id.empl_name)
    @Length(min = 3, max = 128)
    private EditText nameEditText;

    @InjectView(R.id.empl_surname)
    @Length(min = 3, max = 128)
    private EditText surnameEditText;

    @InjectView(R.id.empl_city)
    @Length(min = 3, max = 128)
    private EditText cityEditText;

    @InjectView(R.id.empl_birthday_root)
    private View birthdayRoot;

    @InjectView(R.id.empl_birthday)
    private TextView birthdayTextView;

    @InjectView(R.id.empl_position_root)
    private View positionRoot;

    @InjectView(R.id.empl_position)
    private TextView positionTextView;

    @InjectView(R.id.fab)
    private FloatingActionButton fab;

    @Inject
    private IEditEmplModel model;

    private EmployeeViewForm form;
    private long employeeId;
    private Subscription subscription;

    private final Validator validator;
    private ProgressDialog progressBar;

    public EditEmployeeActivity() {
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // check and assign
            final Employee employee = Preconditions.checkNotNull(
                    getIntent().getParcelableExtra(EditEmployeeActivity.ARG_EMPLOYEE), "employee wasn't passed!");
            employeeId = employee.getId();
            form = new EmployeeViewForm(employee);
            setup(employee);
        } else {
            // restore state
            employeeId = savedInstanceState.getLong(EditEmployeeActivity.ARG_EMPLOYEE_ID);
            form = savedInstanceState.getParcelable(EditEmployeeActivity.ARG_FORM);
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        progressBar = new ProgressDialog(this);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        // setup listener
        fab.setOnClickListener(v -> validator.validate());
        birthdayRoot.setOnClickListener(this::onPickDate);
        positionRoot.setOnClickListener(v -> {

            final PopupMenu menu = createPopupMenu(positionTextView);

            menu.setOnMenuItemClickListener(item -> {
                // set position by enum's ordinal, it is more
                // effective than enum instance serializing
                form.setPosition(Position.values()[item.getIntent().getIntExtra(ARG_POSITION, 0)]);
                positionTextView.setText(item.getTitle());
                return true;
            });
            menu.show();
        });
    }

    @Override
    protected void onStop() {
        // avoid memory leaks
        hideProgress();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // release subscription; avoid memory leaks
        if (subscription != null
                && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        form.setBirthdayYear(year);
        birthdayTextView.setText(getString(R.string.create_birthday_set, year));
    }

    @Override
    public void onValidationSucceeded() {

        form.setName(MyTextUtil.capitalize(nameEditText.getText().toString()));
        form.setSurname(MyTextUtil.capitalize(surnameEditText.getText().toString()));
        form.setCity(MyTextUtil.capitalize(cityEditText.getText().toString()));

        subscription = model.editEmployee(employeeId, form)
                .doOnSubscribe(() -> showProgress(getString(R.string.edit_progress_msg)))
                .doOnTerminate(this::hideProgress)
                .subscribe(employee -> {
                            final Intent intent = new Intent();

                            intent.putExtra(EditEmployeeActivity.ARG_RESULT, employee);
                            setResult(RESULT_OK, intent);
                            hideProgress();
                            finish();
                        }, // show exception
                        th -> {
                            hideProgress();
                            Toast.makeText(EditEmployeeActivity.this,
                                    getString(R.string.edit_err), Toast.LENGTH_SHORT).show();
                        }
                );
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (final ValidationError error : errors) {

            final View view = error.getView();
            final String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(EditEmployeeActivity.ARG_FORM, form);
        outState.putLong(EditEmployeeActivity.ARG_EMPLOYEE_ID, employeeId);
        super.onSaveInstanceState(outState);
    }

    private void setup(Employee employee) {
        nameEditText.setText(employee.getName());
        surnameEditText.setText(employee.getSurname());
        cityEditText.setText(employee.getCity());
        birthdayTextView.setText(getString(R.string.create_birthday_set, employee.getBirthdayYear()));
        positionTextView.setText(MyTextUtil.capitalize(employee.getPosition().toString()));
    }

    private PopupMenu createPopupMenu(View v) {

        final PopupMenu popupMenu = new PopupMenu(this, v);

        for (final SelectablePosition position : SelectablePosition.getPositions()) {
            final Intent data = new Intent();
            data.putExtra(EditEmployeeActivity.ARG_POSITION, position.value.ordinal());
            // add menu item and set data
            popupMenu.getMenu()
                    .add(0, position.value.ordinal(), 0, position.title)
                    .setIntent(data);
        }

        return popupMenu;
    }

    private void showProgress(String message) {
        progressBar.setMessage(message);
        progressBar.show();
    }

    private void hideProgress() {
        progressBar.dismiss();
    }

    private void onPickDate(final View v) {

        final DateTime now = new DateTime();
        final DatePickerDialog pickerDialog = DatePickerDialog.newInstance(this, 1970, 1, 1);

        pickerDialog.setMaxDate(now.toCalendar(Locale.getDefault()));
        pickerDialog.show(getFragmentManager(), "Select your birth date");
    }

}
