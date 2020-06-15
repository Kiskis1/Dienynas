package lt.viko.eif.dienynas.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import lt.viko.eif.dienynas.R;
import lt.viko.eif.dienynas.dialogs.AddStudentDialog;
import lt.viko.eif.dienynas.dialogs.AddTaskDialog;
import lt.viko.eif.dienynas.models.Group;
import lt.viko.eif.dienynas.utils.ApplicationData;
import lt.viko.eif.dienynas.utils.TableBuilder;
import lt.viko.eif.dienynas.utils.Utils;
import lt.viko.eif.dienynas.viewmodels.DestytojasViewModel;

public class SingleGroupFragment extends Fragment {
    private final static String TAG = SingleGroupFragment.class.getSimpleName();
    private static final int STORAGE_WRITE_PERMISSION_CODE = 32;

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
        assert getArguments() != null;
        group = Utils.getGsonParser().fromJson(getArguments().getString("group"), Group.class);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(group.getName());
        destytojasViewModel = new ViewModelProvider(this).get(DestytojasViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_single_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RelativeLayout relativeLayoutMain = view.findViewById(R.id.relativeLayoutMain);
        getScreenDimension();

        builder = new TableBuilder(relativeLayoutMain, getContext(), SCREEN_HEIGHT, SCREEN_WIDTH, group);
        builder.build();
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
                ApplicationData.setGroupId(group.getId() - 1);
                export();
//                if (destytojasViewModel.exportGroupToPdf(group))
//                    Snackbar.make(getView(), R.string.single_export_success, Snackbar.LENGTH_LONG).show();
//                else
//                    Toast.makeText(getContext(), R.string.single_export_fail, Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void export() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_WRITE_PERMISSION_CODE);
        } else if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (destytojasViewModel.exportGroupToPdf(group))
                Snackbar.make(getView(), R.string.single_export_success, Snackbar.LENGTH_LONG).show();
            else
                Toast.makeText(getContext(), R.string.single_export_fail, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), R.string.perms_grant_permission, Toast.LENGTH_LONG).show();
        }
    }

    private void showAddTaskDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        AddTaskDialog addTaskDialog = AddTaskDialog.newInstance(group.getId());
        addTaskDialog.show(fm, "task");
    }

    private void showAddStudentDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        AddStudentDialog addStudentDialog = AddStudentDialog.newInstance(group.getId());
        addStudentDialog.show(fm, "student");
    }

    private void saveGrades() {
        boolean result = destytojasViewModel.saveGrades(builder.getGrades());
//        if (result)
        Snackbar.make(getView(), R.string.single_save_success, Snackbar.LENGTH_LONG).show();
//        else Toast.makeText(getActivity(), R.string.single_save_failure, Toast.LENGTH_LONG).show();
    }


}
