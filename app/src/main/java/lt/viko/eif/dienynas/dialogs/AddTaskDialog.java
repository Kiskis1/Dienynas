package lt.viko.eif.dienynas.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import lt.viko.eif.dienynas.R;
//https://guides.codepath.com/android/using-dialogfragment
public class AddTaskDialog extends DialogFragment implements View.OnClickListener {
    private final static String TAG = AddTaskDialog.class.getSimpleName();

    private EditText mEditText;
    private Button mCancel;
    private Button mSubmit;

    public AddTaskDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_add_task, container);
        mEditText = v.findViewById(R.id.dialog_edit_task);
        //get edittext content

        mCancel = v.findViewById(R.id.dialog_button_cancel);
        mSubmit = v.findViewById(R.id.dialog_button_okay);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "CANCEL", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "SUBMIT", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        return v;
    }

    @Override
    public void onClick(View view) {

    }
}
