package lt.viko.eif.dienynas.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.common.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

import lt.viko.eif.dienynas.R;
import lt.viko.eif.dienynas.models.Group;
import lt.viko.eif.dienynas.models.Student;

//        https://github.com/shabyWoks/DynamicTableLayout/blob/master/app/src/main/java/com/shaby/dynamictablelayout/MainActivity.java
public class TableBuilder implements HorizontalScroll.ScrollViewListener, VerticalScroll.ScrollViewListener {
    private final static String TAG = TableBuilder.class.getSimpleName();
    private final int COLUMNS_PER_SCREEN = 3;
    private final int ROWS_PER_SCREEN = 9;
    private Group group;
    private int SCREEN_HEIGHT;
    private int SCREEN_WIDTH;
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
    private Context context;

    public TableBuilder(RelativeLayout relativeLayoutMain, Context context, int height, int width, Group group) {
        this.relativeLayoutMain = relativeLayoutMain;
        this.context = context;
        this.SCREEN_HEIGHT = height;
        this.SCREEN_WIDTH = width;
        this.group = group;
    }

    public void build() {
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
    }

    public Group getGrades() {
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
        return update;
    }

    private void initializeRelativeLayout() {
        relativeLayoutA = new RelativeLayout(context);
        relativeLayoutA.setId(R.id.relativeLayoutA);
        relativeLayoutA.setPadding(0, 0, 0, 0);

        relativeLayoutB = new RelativeLayout(context);
        relativeLayoutB.setId(R.id.relativeLayoutB);
        relativeLayoutB.setPadding(0, 0, 0, 0);

        relativeLayoutC = new RelativeLayout(context);
        relativeLayoutC.setId(R.id.relativeLayoutC);
        relativeLayoutC.setPadding(0, 0, 0, 0);

        relativeLayoutD = new RelativeLayout(context);
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
        horizontalScrollViewB = new HorizontalScroll(context);
        horizontalScrollViewB.setPadding(0, 0, 0, 0);
        horizontalScrollViewB.setHorizontalScrollBarEnabled(false);
        horizontalScrollViewB.setVerticalScrollBarEnabled(false);

        horizontalScrollViewD = new HorizontalScroll(context);
        horizontalScrollViewD.setPadding(0, 0, 0, 0);
        horizontalScrollViewD.setHorizontalScrollBarEnabled(false);
        horizontalScrollViewD.setVerticalScrollBarEnabled(false);

        scrollViewC = new VerticalScroll(context);
        scrollViewC.setPadding(0, 0, 0, 0);
        scrollViewC.setHorizontalScrollBarEnabled(false);
        scrollViewC.setVerticalScrollBarEnabled(false);

        scrollViewD = new VerticalScroll(context);
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
        tableLayoutA = new TableLayout(context);
        tableLayoutA.setPadding(0, 0, 0, 0);
        tableLayoutB = new TableLayout(context);
        tableLayoutB.setPadding(0, 0, 0, 0);
        tableLayoutB.setId(R.id.tableLayoutB);
        tableLayoutC = new TableLayout(context);
        tableLayoutC.setPadding(0, 0, 0, 0);
        tableLayoutD = new TableLayout(context);
        tableLayoutD.setPadding(0, 0, 0, 0);

        TableLayout.LayoutParams layoutParamsTableLayoutA = new TableLayout.LayoutParams(SCREEN_WIDTH / COLUMNS_PER_SCREEN, SCREEN_HEIGHT / ROWS_PER_SCREEN);
        tableLayoutA.setLayoutParams(layoutParamsTableLayoutA);
        tableLayoutA.setBackgroundResource(R.drawable.table_header);
        this.relativeLayoutA.addView(tableLayoutA);

        TableLayout.LayoutParams layoutParamsTableLayoutB = new TableLayout.LayoutParams(SCREEN_WIDTH - (SCREEN_WIDTH / COLUMNS_PER_SCREEN), SCREEN_HEIGHT / ROWS_PER_SCREEN);
        tableLayoutB.setLayoutParams(layoutParamsTableLayoutB);
        this.horizontalScrollViewB.addView(tableLayoutB);

        TableLayout.LayoutParams layoutParamsTableLayoutC = new TableLayout.LayoutParams(SCREEN_WIDTH / COLUMNS_PER_SCREEN, SCREEN_HEIGHT - (SCREEN_HEIGHT / ROWS_PER_SCREEN));
        tableLayoutC.setLayoutParams(layoutParamsTableLayoutC);

        this.scrollViewC.addView(tableLayoutC);

        TableLayout.LayoutParams layoutParamsTableLayoutD = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
        tableLayoutD.setLayoutParams(layoutParamsTableLayoutD);
        this.horizontalScrollViewD.addView(tableLayoutD);
    }

    private void addRowToTableA() {
        tableRow = new TableRow(context);
        TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(SCREEN_WIDTH / COLUMNS_PER_SCREEN, SCREEN_HEIGHT / ROWS_PER_SCREEN);
        tableRow.setLayoutParams(layoutParamsTableRow);
        TextView label_date = new TextView(context);
        label_date.setText(R.string.table_code_and_name);
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        label_date.setTextSize(context.getResources().getDimension(R.dimen.cell_text_size));
        tableRow.addView(label_date);
        this.tableLayoutA.addView(tableRow);
    }

    private void initializeRowForTableB() {
        tableRowB = new TableRow(context);
        tableRow.setPadding(0, 0, 0, 0);
        this.tableLayoutB.addView(tableRowB);
    }

    private synchronized void addColumnsToTableB(String text) {
        tableRow = new TableRow(context);
        TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(SCREEN_WIDTH / COLUMNS_PER_SCREEN, SCREEN_HEIGHT / ROWS_PER_SCREEN);
        tableRow.setPadding(3, 3, 3, 3);
        tableRow.setLayoutParams(layoutParamsTableRow);
        tableRow.setBackgroundResource(R.drawable.table_tasks);
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        TextView label = new TextView(context);
        label.setText(text);
        label.setTextSize(context.getResources().getDimension(R.dimen.cell_text_size));
        label.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        this.tableRow.addView(label);
        this.tableRowB.addView(tableRow);
        tableColumnCountB++;
    }

    private synchronized void addRowToTableC(String text) {
        TableRow tableRowC = new TableRow(context);
        TableRow.LayoutParams layoutParamsTableRow1 = new TableRow.LayoutParams(SCREEN_WIDTH / COLUMNS_PER_SCREEN, SCREEN_HEIGHT / ROWS_PER_SCREEN);
        tableRowC.setPadding(3, 3, 3, 3);
        tableRowC.setLayoutParams(layoutParamsTableRow1);
        tableRowC.setBackgroundResource(R.drawable.table_tasks);
        TextView label = new TextView(context);
        label.setText(text);
        label.setTextSize(context.getResources().getDimension(R.dimen.cell_text_size));
        label.setGravity(Gravity.CENTER);
        tableRowC.addView(label);

        TableRow tableRow = new TableRow(context);
        TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(SCREEN_WIDTH / COLUMNS_PER_SCREEN, SCREEN_HEIGHT / ROWS_PER_SCREEN);
        tableRow.setPadding(0, 0, 0, 0);
        tableRow.setLayoutParams(layoutParamsTableRow);

        tableRow.addView(tableRowC);
        this.tableLayoutC.addView(tableRow, tableRowCountC);
        tableRowCountC++;
    }

    private synchronized void initializeRowForTableD(int pos) {
        TableRow tableRowB = new TableRow(context);
        TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, SCREEN_HEIGHT / ROWS_PER_SCREEN);
        tableRowB.setPadding(0, 0, 0, 0);
        tableRowB.setLayoutParams(layoutParamsTableRow);
        this.tableLayoutD.addView(tableRowB, pos);
    }

    private synchronized void addColumnToTableAtD(final int rowPos, int grade) {
        TableRow tableRowAdd = (TableRow) this.tableLayoutD.getChildAt(rowPos);
        tableRow = new TableRow(context);
        TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(SCREEN_WIDTH / COLUMNS_PER_SCREEN, SCREEN_HEIGHT / ROWS_PER_SCREEN);
        tableRow.setPadding(3, 3, 3, 4);
        tableRow.setBackground(context.getResources().getDrawable(R.drawable.table_grades));
        tableRow.setLayoutParams(layoutParamsTableRow);

        Integer[] grades = ArrayUtils.toWrapperArray(context.getResources().getIntArray(R.array.grades));
        SpinnerAdapter adapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, grades);
        Spinner recyclableSpinner = new Spinner(context);
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
}
