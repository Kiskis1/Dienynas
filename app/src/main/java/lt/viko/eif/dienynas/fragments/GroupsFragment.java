package lt.viko.eif.dienynas.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import lt.viko.eif.dienynas.R;
import lt.viko.eif.dienynas.adapters.GroupAdapter;
import lt.viko.eif.dienynas.adapters.GroupAdapter.Interaction;
import lt.viko.eif.dienynas.listeners.OnItemClickListener;
import lt.viko.eif.dienynas.models.Group;
import lt.viko.eif.dienynas.utils.ApplicationData;
import lt.viko.eif.dienynas.utils.Utils;

public class GroupsFragment extends Fragment implements Interaction {
    private final static String TAG = GroupsFragment.class.getSimpleName();

    private TextView mExplanation;
    private TextView mLogIn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mExplanation = view.findViewById(R.id.text_group_empty);
        mLogIn = view.findViewById(R.id.text_group_not_loggedin);

        final FloatingActionButton fab = view.findViewById(R.id.fab_add_group);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_nav_groups_to_nav_create_group);
            }
        });

        List<Group> list = ApplicationData.getDestytojas().getGroup();

        if (!list.isEmpty()) {
            if (mExplanation.getVisibility() == View.VISIBLE)
                mExplanation.setVisibility(View.INVISIBLE);

            if (mLogIn.getVisibility() == View.VISIBLE)
                mLogIn.setVisibility(View.INVISIBLE);

            if (fab.getVisibility() == View.INVISIBLE)
                fab.setVisibility(View.VISIBLE);

            GroupAdapter mGroupAdapter = new GroupAdapter(this);

            RecyclerView mRecyclerView = view.findViewById(R.id.recycler_group);
            mRecyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);

            mRecyclerView.setAdapter(mGroupAdapter);
            mGroupAdapter.submitList(list);

            mGroupAdapter.setOnItemCLickListener(new OnItemClickListener() {
                @Override
                public void OnItemClick(Group group) {
                    Bundle bundle = new Bundle();
                    bundle.putString("group", Utils.getGsonParser().toJson(group));
                    Log.i(TAG, group.toString());

                    Navigation.findNavController(view).navigate(R.id.action_nav_groups_to_nav_single_group, bundle);
                }

            });
        } else {
            mExplanation.setVisibility(View.VISIBLE);
            if (!ApplicationData.isSignedIn()) {
                mLogIn.setVisibility(View.VISIBLE);
                fab.setVisibility(View.INVISIBLE);
            }
        }
    }

}
