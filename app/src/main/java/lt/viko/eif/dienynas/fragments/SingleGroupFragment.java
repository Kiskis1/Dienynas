package lt.viko.eif.dienynas.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import lt.viko.eif.dienynas.R;
import lt.viko.eif.dienynas.dialogs.AddStudentDialog;
import lt.viko.eif.dienynas.dialogs.AddTaskDialog;
import lt.viko.eif.dienynas.models.Group;
import lt.viko.eif.dienynas.utils.TableBuilder;
import lt.viko.eif.dienynas.utils.Utils;
import lt.viko.eif.dienynas.viewmodels.DestytojasViewModel;

public class SingleGroupFragment extends Fragment {
    private final static String TAG = SingleGroupFragment.class.getSimpleName();

    private Group group;

    private static int SCREEN_HEIGHT;
    private static int SCREEN_WIDTH;

    private TableBuilder builder;

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
        destytojasViewModel = new ViewModelProvider(this).get(DestytojasViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_single_group, container, false);

        RelativeLayout relativeLayoutMain = root.findViewById(R.id.relativeLayoutMain);
        getScreenDimension();

        builder = new TableBuilder(relativeLayoutMain, getContext(), SCREEN_HEIGHT, SCREEN_WIDTH, group);
        builder.build();

        return root;
    }

    private void saveGrades() {
        destytojasViewModel.saveGrades(builder.getGrades());
    }

    private void getScreenDimension() {
        SCREEN_WIDTH = getResources().getDisplayMetrics().widthPixels;
        SCREEN_HEIGHT = getResources().getDisplayMetrics().heightPixels;
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
