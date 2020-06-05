package lt.viko.eif.dienynas.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import lt.viko.eif.dienynas.R;
import lt.viko.eif.dienynas.dialogs.AddStudentDialog;
import lt.viko.eif.dienynas.dialogs.AddTaskDialog;
import lt.viko.eif.dienynas.models.Group;
import lt.viko.eif.dienynas.models.Student;
import lt.viko.eif.dienynas.utils.HorizontalScroll;
import lt.viko.eif.dienynas.utils.Utils;
import lt.viko.eif.dienynas.utils.VerticalScroll;
import lt.viko.eif.dienynas.viewmodels.DestytojasViewModel;

//        https://github.com/shabyWoks/DynamicTableLayout/blob/master/app/src/main/java/com/shaby/dynamictablelayout/MainActivity.java
public class SingleGroupFragment extends Fragment implements HorizontalScroll.ScrollViewListener, VerticalScroll.ScrollViewListener {
    private final static String TAG = SingleGroupFragment.class.getSimpleName();

    private Group group;

    private static int SCREEN_HEIGHT;
    private static int SCREEN_WIDTH;

    private RelativeLayout relativeLayoutMain;
    private RelativeLayout relativeLayoutA;
    private RelativeLayout relativeLayoutB;
    private RelativeLayout relativeLayoutC;
    private RelativeLayout relativeLayoutD;

    private TableLayout tableLayoutA;
    private TableLayout tableLayoutB;
    private TableLayout tableLayoutC;
    private TableLayout tableLayoutD;

    private TableRow tableRow;
    private TableRow tableRowB;

    private HorizontalScroll horizontalScrollViewB;
    private HorizontalScroll horizontalScrollViewD;

    private VerticalScroll scrollViewC;
    private VerticalScroll scrollViewD;

    private int tableColumnCountB = 0;
    private int tableRowCountC = 0;

    private final int COLUMNS_PER_SCREEN = 3;
    private final int ROWS_PER_SCREEN = 9;

    private DestytojasViewModel destytojasViewModel;

    public SingleGroupFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //Log.i(TAG, getArguments().getString("group"));
        assert getArguments() != null;
        group = Utils.getGsonParser().fromJson(getArguments().getString("group"), Group.class);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(group.getName());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_single_group, container, false);


        destytojasViewModel = new ViewModelProvider(this).get(DestytojasViewModel.class);
        relativeLayoutMain = root.findViewById(R.id.relativeLayoutMain);
        getScreenDimension();
        initializeRelativeLayout();
        initializeScrollers();
        initializeTableLayout();
        horizontalScrollViewB.setScrollViewListener(this);
        horizontalScrollViewD.setScrollViewListener(this);
        scrollViewC.setScrollViewListener(this);
        scrollViewD.setScrollViewListener(this);
        addRowToTableA();
        initializeRowForTableB();

        for (String s : group.getTask()) {
            addColumnsToTableB(s);
        }

        int i = 0;
        for (Student stud : group.getStudents()) {
            initializeRowForTableD(i);
            addRowToTableC(stud.getCodeFullName());
            for (int g : stud.getGrades()) {
                addColumnToTableAtD(i, g);
            }
            i++;
        }

        return root;
    }

    private void saveGrades() {
        Group update = new Group();
        update.setId(group.getId());
        update.setName(group.getName());
        update.setTask(group.getTask());
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < tableRowCountC; i++) {
            TableRow row = (TableRow) tableLayoutC.getChildAt(i);
            TableRow row2 = (TableRow) row.getChildAt(0);
            TextView view = (TextView) row2.getChildAt(0);
            Student student = new Student();
            student.setStudentByCodeFullName(view.getText().toString());
//            Log.i(TAG, "saveGrades: "+ student.toString());
            List<Integer> grades = new ArrayList<>();
            for (int j = 0; j < tableColumnCountB; j++) {
                TableRow row3 = (TableRow) tableLayoutD.getChildAt(i);
                TableRow row4 = (TableRow) row3.getChildAt(j);
                Spinner spin = (Spinner) row4.getChildAt(0);
                grades.add(Integer.valueOf(spin.getSelectedItem().toString()));
//                Log.i(TAG, "saveGrades: " + grades.toString());
            }
            student.setGrades(grades);
            students.add(student);
//            Log.i(TAG, "saveGrades: "+students.toString());
        }
        update.setStudents(students);
        destytojasViewModel.saveGrades(update);
    }

    private void getScreenDimension() {
        SCREEN_WIDTH = getResources().getDisplayMetrics().widthPixels;
        SCREEN_HEIGHT = getResources().getDisplayMetrics().heightPixels;
    }

    private void initializeRelativeLayout() {
        relativeLayoutA = new RelativeLayout(getContext());
        relativeLayoutA.setId(R.id.relativeLayoutA);
        relativeLayoutA.setPadding(0, 0, 0, 0);

        relativeLayoutB = new RelativeLayout(getContext());
        relativeLayoutB.setId(R.id.relativeLayoutB);
        relativeLayoutB.setPadding(0, 0, 0, 0);

        relativeLayoutC = new RelativeLayout(getContext());
        relativeLayoutC.setId(R.id.relativeLayoutC);
        relativeLayoutC.setPadding(0, 0, 0, 0);

        relativeLayoutD = new RelativeLayout(getContext());
        relativeLayoutD.setId(R.id.relativeLayoutD);
        relativeLayoutD.setPadding(0, 0, 0, 0);

        relativeLayoutA.setLayoutParams(new RelativeLayout.LayoutParams(SCREEN_WIDTH / COLUMNS_PER_SCREEN, SCREEN_HEIGHT / ROWS_PER_SCREEN));
        this.relativeLayoutMain.addView(relativeLayoutA);

        RelativeLayout.LayoutParams layoutParamsRelativeLayoutB = new RelativeLayout.LayoutParams(SCREEN_WIDTH - (SCREEN_WIDTH / COLUMNS_PER_SCREEN), SCREEN_HEIGHT / ROWS_PER_SCREEN);
        layoutParamsRelativeLayoutB.addRule(RelativeLayout.RIGHT_OF, R.id.relativeLayoutA);
        relativeLayoutB.setLayoutParams(layoutParamsRelativeLayoutB);
        this.relativeLayoutMain.addView(relativeLayoutB);

        RelativeLayout.LayoutParams layoutParamsRelativeLayoutC = new RelativeLayout.LayoutParams(SCREEN_WIDTH / COLUMNS_PER_SCREEN, SCREEN_HEIGHT - (SCREEN_HEIGHT / ROWS_PER_SCREEN));
        layoutParamsRelativeLayoutC.addRule(RelativeLayout.BELOW, R.id.relativeLayoutA);
        relativeLayoutC.setLayoutParams(layoutParamsRelativeLayoutC);
        this.relativeLayoutMain.addView(relativeLayoutC);

        RelativeLayout.LayoutParams layoutParamsRelativeLayoutD = new RelativeLayout.LayoutParams(SCREEN_WIDTH - (SCREEN_WIDTH / COLUMNS_PER_SCREEN), SCREEN_HEIGHT - (SCREEN_HEIGHT / ROWS_PER_SCREEN));
        layoutParamsRelativeLayoutD.addRule(RelativeLayout.BELOW, R.id.relativeLayoutB);
        layoutParamsRelativeLayoutD.addRule(RelativeLayout.RIGHT_OF, R.id.relativeLayoutC);
        relativeLayoutD.setLayoutParams(layoutParamsRelativeLayoutD);
        this.relativeLayoutMain.addView(relativeLayoutD);
    }

    private void initializeScrollers() {
        horizontalScrollViewB = new HorizontalScroll(getContext());
        horizontalScrollViewB.setPadding(0, 0, 0, 0);
        horizontalScrollViewB.setHorizontalScrollBarEnabled(false);
        horizontalScrollViewB.setVerticalScrollBarEnabled(false);

        horizontalScrollViewD = new HorizontalScroll(getContext());
        horizontalScrollViewD.setPadding(0, 0, 0, 0);
        horizontalScrollViewD.setHorizontalScrollBarEnabled(false);
        horizontalScrollViewD.setVerticalScrollBarEnabled(false);

        scrollViewC = new VerticalScroll(getContext());
        scrollViewC.setPadding(0, 0, 0, 0);
        scrollViewC.setHorizontalScrollBarEnabled(false);
        scrollViewC.setVerticalScrollBarEnabled(false);

        scrollViewD = new VerticalScroll(getContext());
        scrollViewD.setPadding(0, 0, 0, 0);
        scrollViewD.setHorizontalScrollBarEnabled(false);
        scrollViewD.setVerticalScrollBarEnabled(false);

        horizontalScrollViewB.setLayoutParams(new ViewGroup.LayoutParams(SCREEN_WIDTH - (SCREEN_WIDTH / COLUMNS_PER_SCREEN), SCREEN_HEIGHT / ROWS_PER_SCREEN));
        scrollViewC.setLayoutParams(new ViewGroup.LayoutParams(SCREEN_WIDTH / COLUMNS_PER_SCREEN, SCREEN_HEIGHT - (SCREEN_HEIGHT / ROWS_PER_SCREEN)));
        scrollViewD.setLayoutParams(new ViewGroup.LayoutParams(SCREEN_WIDTH - (SCREEN_WIDTH / COLUMNS_PER_SCREEN), SCREEN_HEIGHT - (SCREEN_HEIGHT / ROWS_PER_SCREEN)));
        horizontalScrollViewD.setLayoutParams(new ViewGroup.LayoutParams(SCREEN_WIDTH - (SCREEN_WIDTH / COLUMNS_PER_SCREEN), SCREEN_HEIGHT - (SCREEN_HEIGHT / ROWS_PER_SCREEN)));

        this.relativeLayoutB.addView(horizontalScrollViewB);
        this.relativeLayoutC.addView(scrollViewC);
        this.scrollViewD.addView(horizontalScrollViewD);
        this.relativeLayoutD.addView(scrollViewD);
    }

    private void initializeTableLayout() {
        tableLayoutA = new TableLayout(getContext());
        tableLayoutA.setPadding(0, 0, 0, 0);
        tableLayoutB = new TableLayout(getContext());
        tableLayoutB.setPadding(0, 0, 0, 0);
        tableLayoutB.setId(R.id.tableLayoutB);
        tableLayoutC = new TableLayout(getContext());
        tableLayoutC.setPadding(0, 0, 0, 0);
        tableLayoutD = new TableLayout(getContext());
        tableLayoutD.setPadding(0, 0, 0, 0);

        TableLayout.LayoutParams layoutParamsTableLayoutA = new TableLayout.LayoutParams(SCREEN_WIDTH / COLUMNS_PER_SCREEN, SCREEN_HEIGHT / ROWS_PER_SCREEN);
        tableLayoutA.setLayoutParams(layoutParamsTableLayoutA);
        tableLayoutA.setBackgroundColor(getResources().getColor(R.color.primaryColor));
        this.relativeLayoutA.addView(tableLayoutA);

        TableLayout.LayoutParams layoutParamsTableLayoutB = new TableLayout.LayoutParams(SCREEN_WIDTH - (SCREEN_WIDTH / COLUMNS_PER_SCREEN), SCREEN_HEIGHT / ROWS_PER_SCREEN);
        tableLayoutB.setLayoutParams(layoutParamsTableLayoutB);
        tableLayoutB.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
        this.horizontalScrollViewB.addView(tableLayoutB);

        TableLayout.LayoutParams layoutParamsTableLayoutC = new TableLayout.LayoutParams(SCREEN_WIDTH / COLUMNS_PER_SCREEN, SCREEN_HEIGHT - (SCREEN_HEIGHT / ROWS_PER_SCREEN));
        tableLayoutC.setLayoutParams(layoutParamsTableLayoutC);
        tableLayoutC.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
        this.scrollViewC.addView(tableLayoutC);

        TableLayout.LayoutParams layoutParamsTableLayoutD = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
        tableLayoutD.setLayoutParams(layoutParamsTableLayoutD);
        this.horizontalScrollViewD.addView(tableLayoutD);
    }

    private void addRowToTableA() {
        tableRow = new TableRow(getContext());
        TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(SCREEN_WIDTH / COLUMNS_PER_SCREEN, SCREEN_HEIGHT / ROWS_PER_SCREEN);
        tableRow.setLayoutParams(layoutParamsTableRow);
        TextView label_date = new TextView(getContext());
        label_date.setText(R.string.table_code_and_name);
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        label_date.setTextSize(getResources().getDimension(R.dimen.cell_text_size));
        tableRow.addView(label_date);
        this.tableLayoutA.addView(tableRow);
    }

    private void initializeRowForTableB() {
        tableRowB = new TableRow(getContext());
        tableRow.setPadding(0, 0, 0, 0);
        this.tableLayoutB.addView(tableRowB);
    }

    private synchronized void addColumnsToTableB(String text) {
        tableRow = new TableRow(getContext());
        TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(SCREEN_WIDTH / COLUMNS_PER_SCREEN, SCREEN_HEIGHT / ROWS_PER_SCREEN);
        tableRow.setPadding(3, 3, 3, 4);
        tableRow.setLayoutParams(layoutParamsTableRow);
        TextView label_date = new TextView(getContext());
        label_date.setText(text);
        label_date.setTextSize(getResources().getDimension(R.dimen.cell_text_size));
        this.tableRow.addView(label_date);
        this.tableRowB.addView(tableRow);
        tableColumnCountB++;
    }

    private synchronized void addRowToTableC(String text) {
        TableRow tableRow1 = new TableRow(getContext());
        TableRow.LayoutParams layoutParamsTableRow1 = new TableRow.LayoutParams(SCREEN_WIDTH / COLUMNS_PER_SCREEN, SCREEN_HEIGHT / ROWS_PER_SCREEN);
        tableRow1.setPadding(3, 3, 3, 4);
        tableRow1.setLayoutParams(layoutParamsTableRow1);
        TextView label_date = new TextView(getContext());
        label_date.setText(text);
        label_date.setTextSize(getResources().getDimension(R.dimen.cell_text_size));
        tableRow1.addView(label_date);

        TableRow tableRow = new TableRow(getContext());
        TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(SCREEN_WIDTH / COLUMNS_PER_SCREEN, SCREEN_HEIGHT / ROWS_PER_SCREEN);
        tableRow.setPadding(0, 0, 0, 0);
        tableRow.setLayoutParams(layoutParamsTableRow);
        tableRow.addView(tableRow1);
        this.tableLayoutC.addView(tableRow, tableRowCountC);
        tableRowCountC++;
    }

    private synchronized void initializeRowForTableD(int pos) {
        TableRow tableRowB = new TableRow(getContext());
        TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, SCREEN_HEIGHT / ROWS_PER_SCREEN);
        tableRowB.setPadding(0, 0, 0, 0);
        tableRowB.setLayoutParams(layoutParamsTableRow);
        this.tableLayoutD.addView(tableRowB, pos);
    }

    private synchronized void addColumnToTableAtD(final int rowPos, int grade) {
        TableRow tableRowAdd = (TableRow) this.tableLayoutD.getChildAt(rowPos);
        tableRow = new TableRow(getContext());
        TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(SCREEN_WIDTH / COLUMNS_PER_SCREEN, SCREEN_HEIGHT / ROWS_PER_SCREEN);
        tableRow.setPadding(3, 3, 3, 4);
        tableRow.setBackground(getResources().getDrawable(R.drawable.border));
        tableRow.setLayoutParams(layoutParamsTableRow);

        Integer[] grades = ArrayUtils.toWrapperArray(getResources().getIntArray(R.array.grades));
        SpinnerAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, grades);
        Spinner recyclableSpinner = new Spinner(getContext());
        recyclableSpinner.setAdapter(adapter);
        recyclableSpinner.setSelection(grade);
        tableRow.setTag(recyclableSpinner);

        this.tableRow.addView(recyclableSpinner);
        tableRowAdd.addView(tableRow);
    }

    @Override
    public void onScrollChanged(HorizontalScroll scrollView, int x, int y, int oldx, int oldy) {
        if (scrollView == horizontalScrollViewB) {
            horizontalScrollViewD.scrollTo(x, y);
        } else if (scrollView == horizontalScrollViewD) {
            horizontalScrollViewB.scrollTo(x, y);
        }
    }

    @Override
    public void onScrollChanged(VerticalScroll scrollView, int x, int y, int oldx, int oldy) {
        if (scrollView == scrollViewC) {
            scrollViewD.scrollTo(x, y);
        } else if (scrollView == scrollViewD) {
            scrollViewC.scrollTo(x, y);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_students:
                showAddStudentDialog();
                return true;
            case R.id.action_add_task:
                showAddTaskDialog();
                return true;
            case R.id.action_save:
                saveGrades();
                return true;
            case R.id.action_export:
                //destytojasViewModel.exportGroupToPDF(group);
                if (destytojasViewModel.exportGroupToPdf(group)) {
                    Snackbar.make(getView(), R.string.single_export_success, Snackbar.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), R.string.single_export_fail, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAddTaskDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Log.i(TAG, "showAddTaskDialog: " + group.getId());
        AddTaskDialog addTaskDialog = AddTaskDialog.newInstance(group.getId());
        addTaskDialog.show(fm, "task");
    }

    private void showAddStudentDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        AddStudentDialog addStudentDialog = AddStudentDialog.newInstance(group.getId());
        addStudentDialog.show(fm, "student");
    }


}
