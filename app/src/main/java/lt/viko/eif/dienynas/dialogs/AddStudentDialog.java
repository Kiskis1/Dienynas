package lt.viko.eif.dienynas.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import lt.viko.eif.dienynas.R;
import lt.viko.eif.dienynas.models.Destytojas;
import lt.viko.eif.dienynas.models.Student;
import lt.viko.eif.dienynas.utils.ApplicationData;
import lt.viko.eif.dienynas.viewmodels.DestytojasViewModel;

//https://guides.codepath.com/android/using-dialogfragment
public class AddStudentDialog extends DialogFragment {
    private final static String TAG = AddStudentDialog.class.getSimpleName();

    private static final int OPEN_REQUEST_CODE = 41;

    private TextInputLayout mStudCodeLayout;
    private TextInputLayout mFullNameLayout;
    private TextInputEditText mStudCode;
    private TextInputEditText mFullName;
    private DestytojasViewModel destytojasViewModel;

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

        mStudCodeLayout = view.findViewById(R.id.edit_student_code_layout);
        mFullNameLayout = view.findViewById(R.id.edit_student_full_name_layout);
        mStudCode = view.findViewById(R.id.edit_student_code);
        mFullName = view.findViewById(R.id.edit_student_full_name);
        Button mAdd = view.findViewById(R.id.button_add_student);
        Button mBulkAdd = view.findViewById(R.id.button_import);

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mStudCode.getText())) {
                    mStudCodeLayout.setError(getString(R.string.error_enter_code));
                    return;
                } else mStudCodeLayout.setError(null);

                if (TextUtils.isEmpty(mFullName.getText())) {
                    mFullNameLayout.setError(getString(R.string.error_enter_name));
                    return;
                } else mFullNameLayout.setError(null);

                if (mFullName.getText().toString().split(" ").length != 2) {
                    Log.i(TAG, "onClick: " + mFullName.getText().toString());
                    mFullNameLayout.setError(getString(R.string.error_enter_full_name));
                    return;
                } else mFullNameLayout.setError(null);

                List<Integer> grades = new ArrayList<>();
                Destytojas dest = ApplicationData.getDestytojas();
                int size = dest.getGroup().get((int) id).getTask().size();
                for (int i = 0; i < size; i++) {
                    grades.add(0);
                }
                Student stud = new Student(mStudCode.getText().toString(), mFullName.getText().toString(), grades);
                dest.getGroup().get((int) id).getStudents().add(stud);
                //destytojasViewModel.setDestytojas(dest);
                Snackbar.make(getActivity().getCurrentFocus(), R.string.adding_success_student, Snackbar.LENGTH_LONG).show();
                dismiss();
            }
        });

        mStudCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(mStudCode.getText())) mStudCodeLayout.setError(null);
            }
        });

        mFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mStudCode.getText().toString().split(" ").length < 2)
                    mFullNameLayout.setError(null);

                if (!TextUtils.isEmpty(mFullName.getText())) mFullNameLayout.setError(null);
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
}
