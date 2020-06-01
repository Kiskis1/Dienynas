package lt.viko.eif.dienynas.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import lt.viko.eif.dienynas.R;
import lt.viko.eif.dienynas.models.Destytojas;
import lt.viko.eif.dienynas.models.Student;
import lt.viko.eif.dienynas.utils.ApplicationData;
import lt.viko.eif.dienynas.viewmodels.DestytojasViewModel;

//https://guides.codepath.com/android/using-dialogfragment
public class AddStudentDialog extends DialogFragment implements View.OnClickListener {
    private final static String TAG = AddStudentDialog.class.getSimpleName();

    private static final int OPEN_REQUEST_CODE = 41;

    private EditText mStudCode;
    private EditText mFullName;
    private DestytojasViewModel destytojasViewModel;
    private List<Integer> grades = new ArrayList<>();


    private long id;

    public AddStudentDialog() {
    }

    public static AddStudentDialog newInstance(long id) {

        Bundle args = new Bundle();
        args.putLong("groupid", id);
        AddStudentDialog fragment = new AddStudentDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        id = getArguments().getLong("groupid") - 1;
        this.setCancelable(true);
        destytojasViewModel = new ViewModelProvider(this).get(DestytojasViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_student, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mStudCode = view.findViewById(R.id.edit_student_code);
        mFullName = view.findViewById(R.id.edit_student_full_name);
        Button mAdd = view.findViewById(R.id.button_add_student);
        Button mBulkAdd = view.findViewById(R.id.button_import);

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Destytojas dest = ApplicationData.getDestytojas();
                int size = dest.getGroup().get((int) id).getTask().size();
                for (int i = 0; i < size; i++) {
                    grades.add(0);
                }
                Student stud = new Student(mStudCode.getText().toString(), mFullName.getText().toString(), grades);
                Log.i(TAG, stud.toString());
                Log.i(TAG, "ONCLICK" + id);
                dest.getGroup().get((int) id).getStudents().add(stud);
                destytojasViewModel.setDest(dest);
                dismiss();
            }
        });

        mBulkAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                ApplicationData.setGroupId(id);
                getActivity().startActivityForResult(intent, OPEN_REQUEST_CODE);
                dismiss();
            }
        });
    }


    @Override
    public void onClick(View view) {

    }
}
