package maxoliynick.igimed.view;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import maxoliynick.igimed.R;
import maxoliynick.igimed.model.IMainModel;
import maxoliynick.igimed.model.entity.Employee;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import rx.Subscription;

@ContentView(R.layout.activity_main)
public final class MainActivity extends BaseActivity {

    private static final class ItemHolder extends RecyclerView.ViewHolder {

        final TextView title;
        final TextView description;
        final TextView imageTitle;

        public ItemHolder(View itemView) {
            super(itemView);
            imageTitle = (TextView) itemView.findViewById(R.id.img_title);
            title = (TextView) itemView.findViewById(R.id.item_title);
            description = (TextView) itemView.findViewById(R.id.item_body);
        }
    }

    private class Adapter extends RecyclerView.Adapter<ItemHolder> {

        private final ArrayList<Employee> content;

        Adapter() {
            content = new ArrayList<>();
            setHasStableIds(true);
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.employee_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {

            final Employee employee = content.get(position);
            // setup description and title
            holder.imageTitle.setText(String.valueOf(employee.getName().charAt(0)));
            holder.title.setText(String.format("%s %s", employee.getName(), employee.getSurname()));
            holder.description.setText(getString(R.string.main_item_body,
                    employee.getPosition(), employee.getBirthdayYear(), employee.getCity()));
            // setup listener
            holder.itemView.setOnClickListener(v ->
                    startActivityForResult(model.onEmployeeSelected(MainActivity.this, employee), EDIT_EMPL_REQ));
        }

        @Override
        public long getItemId(int position) {
            return content.get(position).getId();
        }

        @Override
        public int getItemCount() {
            return content.size();
        }

        void addItem(Employee model) {
            content.add(model);
        }

        int updateItem(Employee model) {
            // todo optimize by returning sorted collection from db
            int i = 0;
            for (; i < content.size(); ++i) {
                if (content.get(i).getId() == model.getId()) {
                    break;
                }
            }
            content.set(i, model);
            return i;
        }

        void clearItems() {
            content.clear();
        }

        void addItem(@NotNull Collection<Employee> models) {
            content.addAll(models);
        }

        void onSaveInstanceState(Bundle outState) {
            outState.putParcelableArrayList(MainActivity.ARG_LIST_DATA, content);
        }

        void onRestoreInstanceState(Bundle savedState) {
            content.addAll(savedState.getParcelableArrayList(MainActivity.ARG_LIST_DATA));
            notifyDataSetChanged();
        }

    }

    private static final String ARG_LIST_DATA = "argListData";
    private static final int CREATE_EMPL_REQ = 0x1;
    private static final int EDIT_EMPL_REQ = 0x2;

    @InjectView(R.id.fab)
    private FloatingActionButton fab;

    @InjectView(R.id.employee_list)
    private RecyclerView recyclerView;

    @InjectView(R.id.swipe_refresh)
    private SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    private IMainModel model;

    private Subscription subscription;

    private final LinearLayoutManager layoutManager;
    private final Adapter adapter;

    public MainActivity() {
        adapter = new Adapter();
        layoutManager = new LinearLayoutManager(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        final int viewDelta = dpToPx(5);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy >= viewDelta) {
                    fab.hide();
                } else if (-dy >= viewDelta) {
                    fab.show();
                }
            }
        });

        if (savedInstanceState == null) {
            // get data from db if first
            // time created
            retrieveData();
        }

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.primary));
        swipeRefreshLayout.setOnRefreshListener(this::retrieveData);
        // setup listener
        fab.setOnClickListener(v -> startActivityForResult(model.onCreateEmployee(MainActivity.this), CREATE_EMPL_REQ));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == MainActivity.CREATE_EMPL_REQ) {
                // new employee was created
                final Employee employee = data.getParcelableExtra(CreateEmployeeActivity.ARG_RESULT);
                final int lastPos = adapter.getItemCount() - 1;

                adapter.addItem(employee);
                adapter.notifyItemInserted(lastPos);
            } else if (requestCode == MainActivity.EDIT_EMPL_REQ) {
                // employee info was changed
                final Employee employee = data.getParcelableExtra(CreateEmployeeActivity.ARG_RESULT);

                adapter.notifyItemChanged(adapter.updateItem(employee));
            } else throw new RuntimeException(
                    String.format(Locale.getDefault(), "unknown request code %d", requestCode));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        adapter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
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

    private void retrieveData() {

        subscription = model.getStaff()
                .doOnSubscribe(() -> swipeRefreshLayout.setRefreshing(true))
                .doOnTerminate(() -> swipeRefreshLayout.setRefreshing(false))
                .subscribe(employees -> {
                            // add items
                            adapter.clearItems();
                            adapter.addItem(employees);
                            adapter.notifyDataSetChanged();
                        },  // show exception
                        th -> Toast.makeText(MainActivity.this, getString(R.string.main_load_err), Toast.LENGTH_SHORT).show()
                );
    }

    public static int dpToPx(int dp) {
        return (int) Resources.getSystem().getDisplayMetrics().density * dp;
    }

}
