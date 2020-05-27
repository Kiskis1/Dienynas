package lt.viko.eif.dienynas.fragments;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.common.util.ArrayUtils;

import lt.viko.eif.dienynas.HorizontalScroll;
import lt.viko.eif.dienynas.R;
import lt.viko.eif.dienynas.VerticalScroll;
import lt.viko.eif.dienynas.dialogs.AddTaskDialog;
import lt.viko.eif.dienynas.models.Group;
import lt.viko.eif.dienynas.models.Student;
import lt.viko.eif.dienynas.utils.Utils;

//TODO: SET TITLE TO GROUP NAME
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

    private int width = SCREEN_WIDTH / 5;
    private int height = SCREEN_HEIGHT / 20;

    /*
         This is for counting how many columns are added in the row.
    */
    private int tableColumnCountB = 0;

    /*
         This is for counting how many row is added.
    */
    private int tableRowCountC = 0;

    public SingleGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //Log.i(TAG, getArguments().getString("group"));
        assert getArguments() != null;
        group = Utils.getGsonParser().fromJson(getArguments().getString("group"), Group.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_single_group, container, false);
//https://www.youtube.com/watch?v=iwpM2PppgH8
//        https://github.com/shabyWoks/DynamicTableLayout/blob/master/app/src/main/java/com/shaby/dynamictablelayout/MainActivity.java
//        https://stackoverflow.com/questions/10928288/getting-data-from-tablelayout


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

        for (int i = 0; i < group.getTask().size(); i++) {
            addColumnsToTableB(group.getTask().get(i), i);
        }

        for (int i = 0; i < group.getStudents().size(); i++) {
            initializeRowForTableD(i);
            addRowToTableC(group.getStudents().get(i).getCodeFullName());
            for (int j = 0; j < group.getStudents().get(i).getGrades().size(); j++) {
                addColumnToTableAtD(i, group.getStudents().get(i).getGrades().get(j));
            }
        }

        
        return root;
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

        relativeLayoutA.setLayoutParams(new RelativeLayout.LayoutParams(SCREEN_WIDTH / 3, SCREEN_HEIGHT / 9));
        this.relativeLayoutMain.addView(relativeLayoutA);


        RelativeLayout.LayoutParams layoutParamsRelativeLayoutB= new RelativeLayout.LayoutParams(SCREEN_WIDTH- (SCREEN_WIDTH/3), SCREEN_HEIGHT/9);
        layoutParamsRelativeLayoutB.addRule(RelativeLayout.RIGHT_OF, R.id.relativeLayoutA);
        relativeLayoutB.setLayoutParams(layoutParamsRelativeLayoutB);
        this.relativeLayoutMain.addView(relativeLayoutB);

        RelativeLayout.LayoutParams layoutParamsRelativeLayoutC= new RelativeLayout.LayoutParams(SCREEN_WIDTH/3, SCREEN_HEIGHT - (SCREEN_HEIGHT/9));
        layoutParamsRelativeLayoutC.addRule(RelativeLayout.BELOW, R.id.relativeLayoutA);
        relativeLayoutC.setLayoutParams(layoutParamsRelativeLayoutC);
        this.relativeLayoutMain.addView(relativeLayoutC);

        RelativeLayout.LayoutParams layoutParamsRelativeLayoutD= new RelativeLayout.LayoutParams(SCREEN_WIDTH- (SCREEN_WIDTH/3), SCREEN_HEIGHT - (SCREEN_HEIGHT/9));
        layoutParamsRelativeLayoutD.addRule(RelativeLayout.BELOW, R.id.relativeLayoutB);
        layoutParamsRelativeLayoutD.addRule(RelativeLayout.RIGHT_OF, R.id.relativeLayoutC);
        relativeLayoutD.setLayoutParams(layoutParamsRelativeLayoutD);
        this.relativeLayoutMain.addView(relativeLayoutD);

    }

    private void initializeScrollers() {
        horizontalScrollViewB = new HorizontalScroll(getContext());
        horizontalScrollViewB.setPadding(0, 0, 0, 0);

        horizontalScrollViewD = new HorizontalScroll(getContext());
        horizontalScrollViewD.setPadding(0, 0, 0, 0);

        scrollViewC = new VerticalScroll(getContext());
        scrollViewC.setPadding(0, 0, 0, 0);

        scrollViewD = new VerticalScroll(getContext());
        scrollViewD.setPadding(0, 0, 0, 0);

        horizontalScrollViewB.setLayoutParams(new ViewGroup.LayoutParams(SCREEN_WIDTH- (SCREEN_WIDTH/3), SCREEN_HEIGHT / 9));
        scrollViewC.setLayoutParams(new ViewGroup.LayoutParams(SCREEN_WIDTH/3 ,SCREEN_HEIGHT - (SCREEN_HEIGHT/9)));
        scrollViewD.setLayoutParams(new ViewGroup.LayoutParams(SCREEN_WIDTH- (SCREEN_WIDTH/3), SCREEN_HEIGHT - (SCREEN_HEIGHT/9)));
        horizontalScrollViewD.setLayoutParams(new ViewGroup.LayoutParams(SCREEN_WIDTH - (SCREEN_WIDTH / 3), SCREEN_HEIGHT - (SCREEN_HEIGHT / 9)));


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

        TableLayout.LayoutParams layoutParamsTableLayoutA = new TableLayout.LayoutParams(SCREEN_WIDTH / 3, SCREEN_HEIGHT / 9);
        tableLayoutA.setLayoutParams(layoutParamsTableLayoutA);
        tableLayoutA.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        this.relativeLayoutA.addView(tableLayoutA);

        TableLayout.LayoutParams layoutParamsTableLayoutB = new TableLayout.LayoutParams(SCREEN_WIDTH - (SCREEN_WIDTH / 3), SCREEN_HEIGHT / 9);
        tableLayoutB.setLayoutParams(layoutParamsTableLayoutB);
        tableLayoutB.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
        this.horizontalScrollViewB.addView(tableLayoutB);

        TableLayout.LayoutParams layoutParamsTableLayoutC = new TableLayout.LayoutParams(SCREEN_WIDTH / 3, SCREEN_HEIGHT - (SCREEN_HEIGHT / 9));
        tableLayoutC.setLayoutParams(layoutParamsTableLayoutC);
        tableLayoutC.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
        this.scrollViewC.addView(tableLayoutC);

        TableLayout.LayoutParams layoutParamsTableLayoutD = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
        tableLayoutD.setLayoutParams(layoutParamsTableLayoutD);
        this.horizontalScrollViewD.addView(tableLayoutD);

    }

    private void addRowToTableA() {
        tableRow = new TableRow(getContext());
        TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(SCREEN_WIDTH / 3, SCREEN_HEIGHT / 9);
        tableRow.setLayoutParams(layoutParamsTableRow);
        TextView label_date = new TextView(getContext());
        String rowText = "Kodas\nVardas\nPavarde";
        label_date.setText(rowText);
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

    private synchronized void addColumnsToTableB(String text, final int id) {
        tableRow = new TableRow(getContext());
        TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(SCREEN_WIDTH / 3, SCREEN_HEIGHT / 9);
        tableRow.setPadding(3, 3, 3, 4);
        tableRow.setLayoutParams(layoutParamsTableRow);
        TextView label_date = new TextView(getContext());
        label_date.setText(text);
        label_date.setTextSize(getResources().getDimension(R.dimen.cell_text_size));
        this.tableRow.addView(label_date);
        this.tableRow.setTag(id);
        this.tableRowB.addView(tableRow);
        tableColumnCountB++;
    }

    private synchronized void addRowToTableC(String text) {
        TableRow tableRow1 = new TableRow(getContext());
        TableRow.LayoutParams layoutParamsTableRow1 = new TableRow.LayoutParams(SCREEN_WIDTH / 3, SCREEN_HEIGHT / 9);
        tableRow1.setPadding(3, 3, 3, 4);
        tableRow1.setLayoutParams(layoutParamsTableRow1);
        TextView label_date = new TextView(getContext());
        label_date.setText(text);
        label_date.setTextSize(getResources().getDimension(R.dimen.cell_text_size));
        tableRow1.addView(label_date);

        TableRow tableRow = new TableRow(getContext());
        TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(SCREEN_WIDTH / 3, SCREEN_HEIGHT / 9);
        tableRow.setPadding(0, 0, 0, 0);
        tableRow.setLayoutParams(layoutParamsTableRow);
        tableRow.addView(tableRow1);
        this.tableLayoutC.addView(tableRow, tableRowCountC);
        tableRowCountC++;
    }

    private synchronized void initializeRowForTableD(int pos) {
        TableRow tableRowB = new TableRow(getContext());
        TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, SCREEN_HEIGHT / 9);
        tableRowB.setPadding(0, 0, 0, 0);
        tableRowB.setLayoutParams(layoutParamsTableRow);
        this.tableLayoutD.addView(tableRowB, pos);
    }

    private Spinner recyclableSpinner;
    private synchronized void addColumnToTableAtD(final int rowPos, int grade) {
        TableRow tableRowAdd = (TableRow) this.tableLayoutD.getChildAt(rowPos);
        tableRow = new TableRow(getContext());
        TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(SCREEN_WIDTH / 3, SCREEN_HEIGHT / 9);
        tableRow.setPadding(3, 3, 3, 4);
        tableRow.setBackground(getResources().getDrawable(R.drawable.border));
        tableRow.setLayoutParams(layoutParamsTableRow);

        Integer[] grades = ArrayUtils.toWrapperArray(getResources().getIntArray(R.array.grades));
        SpinnerAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, grades);
        recyclableSpinner = new Spinner(getContext());
        recyclableSpinner.setAdapter(adapter);
        recyclableSpinner.setSelection(grade);
        tableRow.setTag(recyclableSpinner);

        this.tableRow.addView(recyclableSpinner);
        tableRowAdd.addView(tableRow);
    }

    private void createCompleteColumn(String value) {
        int i = 0;
        int j = tableRowCountC - 1;
        for (int k = i; k <= j; k++) {
            //addColumnToTableAtD(k, value);
        }
    }

    private void createCompleteRow(String value) {
        initializeRowForTableD(0);
        int i = 0;
        int j = tableColumnCountB - 1;
        int pos = tableRowCountC - 1;
        for (int k = i; k <= j; k++) {
            //addColumnToTableAtD(pos, value);
        }
    }

    @Override
    public void onScrollChanged(HorizontalScroll scrollView, int x, int y, int oldx, int oldy) {
        if(scrollView == horizontalScrollViewB){
            horizontalScrollViewD.scrollTo(x,y);
        }
        else if(scrollView == horizontalScrollViewD){
            horizontalScrollViewB.scrollTo(x, y);
        }

    }

    @Override
    public void onScrollChanged(VerticalScroll scrollView, int x, int y, int oldx, int oldy) {
        if(scrollView == scrollViewC){
            scrollViewD.scrollTo(x,y);
        }
        else if(scrollView == scrollViewD){
            scrollViewC.scrollTo(x,y);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_students) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAddDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        AddTaskDialog addTaskDialog = new AddTaskDialog();
        addTaskDialog.show(fm, "dialog");
    }


}
