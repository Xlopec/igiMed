package maxoliynick.igimed.core.repo;

/**
 * Created by Максим on 1/18/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Максим on 11/25/2016.
 */
@Singleton
public final class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "rss_reader.db";
    private static final int DATABASE_VERSION = 1;

    @Inject
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, EmployeePo.class);
        } catch (final SQLException e) {
            Log.e(TAG, "error creating DB " + DATABASE_NAME, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer,
                          int newVer) {
        try {
            TableUtils.dropTable(connectionSource, EmployeePo.class, true);
            onCreate(db, connectionSource);
        } catch (final SQLException e) {
            Log.e(TAG, "error upgrading db " + DATABASE_NAME + "from ver " + oldVer);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        super.close();
    }

    public void clear(Class<?> cl) {

        try {
            TableUtils.clearTable(getConnectionSource(), cl);
        } catch (final SQLException e) {
            Log.e(TAG, "exception while clearing table", e);
            close();
            throw new RuntimeException(e);
        }
    }

}
