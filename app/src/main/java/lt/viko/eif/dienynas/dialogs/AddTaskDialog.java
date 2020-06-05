package lt.viko.eif.dienynas.dialogs;

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
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import lt.viko.eif.dienynas.R;
import lt.viko.eif.dienynas.models.Destytojas;
import lt.viko.eif.dienynas.models.Student;
import lt.viko.eif.dienynas.utils.ApplicationData;
import lt.viko.eif.dienynas.viewmodels.DestytojasViewModel;

//https://guides.codepath.com/android/using-dialogfragment
public class AddTaskDialog extends DialogFragment implements View.OnClickListener {
    private final static String TAG = AddTaskDialog.class.getSimpleName();

    private TextInputLayout mEditTextLayout;
    private TextInputEditText mEditText;
    private DestytojasViewModel destytojasViewModel;
    private long id;

    public AddTaskDialog() {
    }

    public static AddTaskDialog newInstance(long id) {

        Bundle args = new Bundle();
        args.putLong("groupid", id);
        AddTaskDialog fragment = new AddTaskDialog();
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
        return inflater.inflate(R.layout.dialog_add_task, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditTextLayout = view.findViewById(R.id.dialog_edit_task_layout);
        mEditText = view.findViewById(R.id.dialog_edit_task);
        Button mCancel = view.findViewById(R.id.dialog_button_cancel);
        Button mSubmit = view.findViewById(R.id.dialog_button_okay);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mEditText.getText())) {
                    mEditTextLayout.setError(getString(R.string.adding_enter_task_name));
                    return;
                } else mEditTextLayout.setError(null);

                Destytojas dest = ApplicationData.getDestytojas();
                for (Student stud : dest.getGroup().get((int) id).getStudents())
                    stud.getGrades().add(0);
                dest.getGroup().get((int) id).getTask().add(mEditText.getText().toString());
                destytojasViewModel.setDest(dest);
                Snackbar.make(view, R.string.adding_success_task, Snackbar.LENGTH_LONG).show();
                dismiss();
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(mEditText.getText())) mEditTextLayout.setError(null);
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
