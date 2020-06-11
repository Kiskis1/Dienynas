package lt.viko.eif.dienynas.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import lt.viko.eif.dienynas.R;
import lt.viko.eif.dienynas.adapters.GradeAdapter;
import lt.viko.eif.dienynas.adapters.GradeAdapter.Interaction;
import lt.viko.eif.dienynas.models.Group;
import lt.viko.eif.dienynas.utils.Utils;


public class GradesFragment extends Fragment implements Interaction {
    private final static String TAG = GradesFragment.class.getSimpleName();
    private List<Group> groupList = new ArrayList<>();

    public GradesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        groupList = Utils.getGsonParser().fromJson(getArguments().getString("group"), new TypeToken<List<Group>>() {
        }.getType());

        Log.i(TAG, "onCreate: grades " + groupList.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grades, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GradeAdapter mGradeAdapter = new GradeAdapter(this);
        RecyclerView mRecyclerView = view.findViewById(R.id.recycler_grades);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mGradeAdapter);
        mGradeAdapter.submitList(groupList);

    }

}
