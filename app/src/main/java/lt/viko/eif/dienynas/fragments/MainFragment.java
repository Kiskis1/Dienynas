package lt.viko.eif.dienynas.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import lt.viko.eif.dienynas.R;
import lt.viko.eif.dienynas.viewmodels.DestytojasViewModel;

public class MainFragment extends Fragment {
    private final static String TAG = MainFragment.class.getSimpleName();


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        DestytojasViewModel destytojasViewModel = new ViewModelProvider(this).get(DestytojasViewModel.class);

        EditText mCode = view.findViewById(R.id.main_edit_code);
        Button mSearch = view.findViewById(R.id.button_search_by_id);

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
            }
        });
    }
}
