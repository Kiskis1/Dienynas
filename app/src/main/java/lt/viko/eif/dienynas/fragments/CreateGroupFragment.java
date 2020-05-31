package lt.viko.eif.dienynas.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

    private EditText mGroupName;
    private EditText mGroupTask;
    private DestytojasViewModel destytojasViewModel;
    private List<String> task = new ArrayList<>();
    private List<Student> students = new ArrayList<>();

    public CreateGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        destytojasViewModel = new ViewModelProvider(this).get(DestytojasViewModel.class);

        mGroupName = view.findViewById(R.id.edit_group_name);
        mGroupTask = view.findViewById(R.id.edit_group_task);
        Button mCreate = view.findViewById(R.id.button_create_group);

        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.add(mGroupTask.getText().toString());
                Group group = new Group(ApplicationData.getLastId(), mGroupName.getText().toString(), task, students);
                destytojasViewModel.addGroup(group);
            }
        });

    }
}
