package lt.viko.eif.dienynas.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import lt.viko.eif.dienynas.R;
import lt.viko.eif.dienynas.models.Group;
import lt.viko.eif.dienynas.utils.Utils;
import lt.viko.eif.dienynas.viewmodels.DestytojasViewModel;

//https://stackoverflow.com/questions/1109022/close-hide-android-soft-keyboard
public class MainFragment extends Fragment {
    private final static String TAG = MainFragment.class.getSimpleName();
    private MainFragment mainFragment;
    private ProgressBar mProgressBar;

    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainFragment = this;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final DestytojasViewModel destytojasViewModel = new ViewModelProvider(this).get(DestytojasViewModel.class);

        final TextInputLayout mCodeLayout = view.findViewById(R.id.main_edit_code_layout);
        final TextInputEditText mCode = view.findViewById(R.id.main_edit_code);
        Button mSearch = view.findViewById(R.id.button_search_by_id);
        mProgressBar = view.findViewById(R.id.progressBar);

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = mCode.getText().toString();
                mCode.onEditorAction(EditorInfo.IME_ACTION_DONE);
                if (TextUtils.isEmpty(code) || !TextUtils.isDigitsOnly(code)) {
                    mCodeLayout.setError(getString(R.string.error_enter_code));
                    return;
                } else {
                    mCodeLayout.setError(null);
                }
                code = String.format("s%s", code);
                destytojasViewModel.searchForGrades2(code, mainFragment, mProgressBar);
            }
        });
    }

    public void postGrades(List<Group> groupList) {
        if (groupList.isEmpty()) {
            Toast.makeText(getContext(), R.string.main_no_grades_found, Toast.LENGTH_LONG).show();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("group", Utils.getGsonParser().toJson(groupList));
        Navigation.findNavController(getView()).navigate(R.id.action_nav_home_to_nav_grades, bundle);
    }

}
