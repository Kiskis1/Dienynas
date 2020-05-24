package lt.viko.eif.dienynas.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import lt.viko.eif.dienynas.R;
import lt.viko.eif.dienynas.models.Group;
import lt.viko.eif.dienynas.utils.App;

public class GroupAdapter
        extends ListAdapter<Group, GroupAdapter.GroupViewHolder> {

    private Interaction interaction;

    private TextView mGroupName;

    public GroupAdapter(Interaction interaction) {
        super(new GroupDC());
        this.interaction = interaction;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false),
                interaction);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    void swapData(List<Group> data) {
        submitList(data);
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final Interaction interaction;

        GroupViewHolder(View inflate, Interaction interaction) {
            super(inflate);
            this.interaction = interaction;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
            Group clicked = getItem(getAdapterPosition());
            Log.i("clicked", clicked.toString());
            switch (v.getId()) {
                //TODO handle clicks
                default:
            }
        }

        public void bind(Group item) {
            //TODO use itemView and set data
            mGroupName = itemView.findViewById(R.id.text_group_name);

            mGroupName.setText(item.getName());
        }
    }

    public interface Interaction {
    }

    private static class GroupDC extends DiffUtil.ItemCallback<Group> {
        @Override
        public boolean areItemsTheSame(@NonNull Group oldItem,
                                       @NonNull Group newItem) {
            //TODO "not implemented"
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Group oldItem,
                                          @NonNull Group newItem) {
            //TODO "not implemented"
            return false;
        }
    }
}