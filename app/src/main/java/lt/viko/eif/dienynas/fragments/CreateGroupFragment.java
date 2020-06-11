package lt.viko.eif.dienynas.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import lt.viko.eif.dienynas.R;
import lt.viko.eif.dienynas.models.Group;
import lt.viko.eif.dienynas.models.Student;
import lt.viko.eif.dienynas.utils.ApplicationData;
import lt.viko.eif.dienynas.viewmodels.DestytojasViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateGroupFragment extends Fragment {
    private final static String TAG = CreateGroupFragment.class.getSimpleName();

    private TextInputLayout mGroupNameLayout;
    private TextInputEditText mGroupName;
    private TextInputLayout mGroupTaskLayout;
    private TextInputEditText mGroupTask;
    private DestytojasViewModel destytojasViewModel;
    private List<String> task = new ArrayList<>();
    private List<Student> students = new ArrayList<>();

    public CreateGroupFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        destytojasViewModel = new ViewModelProvider(this).get(DestytojasViewModel.class);

        mGroupNameLayout = view.findViewById(R.id.edit_group_name_layout);
        mGroupName = view.findViewById(R.id.edit_group_name);
        mGroupTaskLayout = view.findViewById(R.id.edit_group_task_layout);
        mGroupTask = view.findViewById(R.id.edit_group_task);
        Button mCreate = view.findViewById(R.id.button_create_group);

        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mGroupName.getText())) {
                    mGroupNameLayout.setError(getString(R.string.error_enter_name));
                    return;
                } else {
                    mGroupNameLayout.setError(null);
                }
                if (TextUtils.isEmpty(mGroupTask.getText())) {
                    mGroupTaskLayout.setError(getString(R.string.error_enter_task));
                    return;
                } else {
                    mGroupTaskLayout.setError(null);
                }
                task.add(mGroupTask.getText().toString());
                Group group = new Group(ApplicationData.getLastId(), mGroupName.getText().toString(), task, students);
                destytojasViewModel.addGroup(group);
            }
        });

        mGroupTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(mGroupTask.getText())) mGroupTaskLayout.setError(null);
            }
        });

        mGroupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(mGroupName.getText())) mGroupNameLayout.setError(null);
            }
        });

    }
}
