package lt.viko.eif.dienynas.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import lt.viko.eif.dienynas.models.Group;
import lt.viko.eif.dienynas.utils.ApplicationData;
import lt.viko.eif.dienynas.utils.Utils;

public class GroupsFragment extends Fragment implements Interaction {
    private final static String TAG = GroupsFragment.class.getSimpleName();


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

        final FloatingActionButton fab = view.findViewById(R.id.fab_add_group);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_nav_groups_to_createGroupFragment);
            }
        });

        GroupAdapter mGroupAdapter = new GroupAdapter(this);

        RecyclerView mRecyclerView = view.findViewById(R.id.recycler_group);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(mGroupAdapter);

//        List<Group> list = StorageRepository.getInstance().getDest().getGroup();

        List<Group> list = ApplicationData.getDestytojas().getGroup();
        mGroupAdapter.submitList(list);

        mGroupAdapter.setOnItemCLickListener(new GroupAdapter.OnItemCLickListener() {
            @Override
            public void OnItemClick(Group group) {
                Bundle bundle = new Bundle();
                bundle.putString("group", Utils.getGsonParser().toJson(group));
                Log.i(TAG, group.toString());

                Navigation.findNavController(view).navigate(R.id.action_nav_groups_to_nav_single_group, bundle);
            }

        });
    }

}
