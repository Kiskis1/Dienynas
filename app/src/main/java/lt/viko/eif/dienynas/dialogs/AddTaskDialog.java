package lt.viko.eif.dienynas.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import lt.viko.eif.dienynas.R;
import lt.viko.eif.dienynas.models.Destytojas;
import lt.viko.eif.dienynas.utils.ApplicationData;
import lt.viko.eif.dienynas.viewmodels.DestytojasViewModel;

//https://guides.codepath.com/android/using-dialogfragment
public class AddTaskDialog extends DialogFragment implements View.OnClickListener {
    private final static String TAG = AddTaskDialog.class.getSimpleName();

    private EditText mEditText;
    private Button mCancel;
    private Button mSubmit;
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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_task, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        destytojasViewModel = new ViewModelProvider(this).get(DestytojasViewModel.class);
        mEditText = view.findViewById(R.id.dialog_edit_task);
        mCancel = view.findViewById(R.id.dialog_button_cancel);
        mSubmit = view.findViewById(R.id.dialog_button_okay);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Destytojas dest = ApplicationData.getDestytojas();
                dest.getGroup().get((int) id).getTask().add(mEditText.getText().toString());
                destytojasViewModel.setDest(dest);
                Toast.makeText(getContext(), "SUBMIT", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "CANCEL", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
