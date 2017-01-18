package maxoliynick.igimed.core.repo;

import android.util.Log;

import com.google.inject.Singleton;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.ReferenceObjectCache;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

import maxoliynick.igimed.model.entity.Employee;

/**
 * Created by Максим on 1/18/2017.
 */
@Singleton
public final class StaffRepository implements IStaffRepository {

    private static final String TAG = StaffRepository.class.getSimpleName();

    private final DatabaseHelper helper;

    @Inject
    public StaffRepository(DatabaseHelper helper) {
        this.helper = helper;
    }

    @NotNull
    @Override
    public Collection<Employee> getStaff() {

        try {
            return Collections.unmodifiableCollection(Mapper.fromPo(createDao().queryForAll()));
        } catch (final SQLException e) {
            Log.e(TAG, "exception while querying category table", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(@NotNull Employee employee) {

        try {
            createDao().update(Mapper.toPo(employee));
        } catch (final Exception e) {
            Log.e(TAG, "exception while updating table", e);
            throw new RuntimeException(e);
        }
    }

    @NotNull
    @Override
    public Employee create(@NotNull EmployeeForm form) {

        final EmployeePo po = Mapper.toPo(form);

        try {
            // update or create and return new po, which will be converted
            // into domain model
            return Mapper.fromPo(createDao().createIfNotExists(po));
        } catch (final Exception e) {
            Log.e(TAG, "exception while updating table", e);
            throw new RuntimeException(e);
        }
    }

    private BaseDaoImpl<EmployeePo, Long> createDao() {
        // dao is cached by inner dao manager implementation!
        try {
            final BaseDaoImpl<EmployeePo, Long> dao = helper.getDao(EmployeePo.class);

            dao.setObjectCache(true);
            dao.setObjectCache(ReferenceObjectCache.makeSoftCache());
            return dao;
        } catch (final SQLException e) {
            Log.e(TAG, "Exception while creating dao", e);
            helper.close();
            throw new RuntimeException(e);
        }
    }

}
