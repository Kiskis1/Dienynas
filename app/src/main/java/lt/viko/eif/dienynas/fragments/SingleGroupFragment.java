package lt.viko.eif.dienynas.fragments;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.common.util.ArrayUtils;

import lt.viko.eif.dienynas.R;
import lt.viko.eif.dienynas.dialogs.AddTaskDialog;
import lt.viko.eif.dienynas.models.Group;
import lt.viko.eif.dienynas.models.Student;
import lt.viko.eif.dienynas.utils.Utils;


/**
 * A simple {@link Fragment} subclass.
 */

//TODO: SET TITLE TO GROUP NAME
public class SingleGroupFragment extends Fragment {
    private final static String TAG = SingleGroupFragment.class.getSimpleName();

    private Group group;

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
        TableRow.LayoutParams wrapWrapTableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        int[] fixedColumnWidths = new int[]{20, 20, 20, 20, 20};
        int[] scrollableColumnWidths = new int[]{20, 20, 20, 30, 30};
        int fixedHeaderHeight = 300;
        int fixedRowHeight = 250;

        TableRow row = new TableRow(getContext());
        //header (fixed horiz)
        TableLayout header = root.findViewById(R.id.table_header);
        row.setLayoutParams(wrapWrapTableRowParams);
        row.setGravity(Gravity.CENTER);
        row.setBackgroundColor(Color.YELLOW);
//        row.addView(makeTableRowWithText("col 5", fixedColumnWidths[4], fixedHeaderHeight));
        String rowText = "Kodas\nVardas\nPavarde";
        row.addView(makeTableRowWithText(rowText, fixedColumnWidths[0], fixedHeaderHeight));
        for (String s : group.getTask()) {
            row.addView(makeTableRowWithText(s, fixedColumnWidths[0], fixedHeaderHeight));
        }
        header.addView(row);

        //header (fixed vert)
        TableLayout fixedColumn = root.findViewById(R.id.fixed_column);
        //get child count

        //rest of the table (within a scroll view)
        TableRow.LayoutParams test = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableLayout scrollablePart = root.findViewById(R.id.scrollable_part);
        for (Student stud : group.getStudents()) {
            //student code + name
//            TextView fixedView = makeTableRowWithText(stud.getCodeFullName(), scrollableColumnWidths[0], fixedRowHeight);
            TextView fixedViewtest = new TextView(getContext());
            fixedViewtest.setText(stud.getCodeFullName());
            fixedViewtest.setBackgroundColor(Color.CYAN);
            fixedViewtest.setTextColor(Color.BLACK);
            fixedViewtest.setTextSize(15);
            fixedColumn.addView(fixedViewtest, test);

            //grades
            TableRow.LayoutParams spinnerParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            row = new TableRow(getContext());
            row.setGravity(Gravity.CENTER);
            row.setBackgroundColor(Color.WHITE);
            row.setLayoutParams(spinnerParams);
            for (int g : stud.getGrades()) {
                //TODO: CHECK FOR NULL
                row.addView(makeTableRowWithSpinner(g, fixedColumnWidths[0], fixedHeaderHeight));
            }
            scrollablePart.addView(row);
        }
        return root;
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

    private void showEditDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        AddTaskDialog addTaskDialog = new AddTaskDialog();
        addTaskDialog.show(fm, "dialog");
    }

    //util method
    private TextView recyclableTextView;

    private TextView makeTableRowWithText(String text, int widthInPercentOfScreenWidth, int fixedHeightInPixels) {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        //Log.i(TAG, String.valueOf(screenWidth));
        recyclableTextView = new TextView(getContext());
        recyclableTextView.setText(text);
        recyclableTextView.setTextColor(Color.BLACK);
        recyclableTextView.setTextSize(15);
        recyclableTextView.setWidth(widthInPercentOfScreenWidth * screenWidth / 100);
        recyclableTextView.setHeight(fixedHeightInPixels);
        return recyclableTextView;
    }

    private Spinner recyclableSpinner;

    private Spinner makeTableRowWithSpinner(int grade, int widthInPercentOfScreenWidth, int fixedHeightInPixels) {
        Integer[] grades = ArrayUtils.toWrapperArray(getResources().getIntArray(R.array.grades));
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        SpinnerAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, grades);
        recyclableSpinner = new Spinner(getContext());
        recyclableSpinner.setAdapter(adapter);
        recyclableSpinner.setSelection(grade);
        recyclableSpinner.setMinimumHeight(fixedHeightInPixels);
        recyclableSpinner.setMinimumWidth(widthInPercentOfScreenWidth * screenWidth / 100);

        return recyclableSpinner;
    }


}
