package lt.viko.eif.dienynas.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lt.viko.eif.dienynas.R;
import lt.viko.eif.dienynas.listeners.OnItemClickListener;
import lt.viko.eif.dienynas.models.Group;

public class GroupAdapter
        extends ListAdapter<Group, GroupAdapter.GroupViewHolder> {


    private OnItemClickListener mOnItemClickListener;

    public void setOnItemCLickListener(OnItemClickListener onItemCLickListener) {
        this.mOnItemClickListener = onItemCLickListener;
    }

    private Interaction interaction;

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

        GroupViewHolder(View inflate, Interaction interaction) {
            super(inflate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
            Group clicked = getItem(getAdapterPosition());
            Log.i("clicked", clicked.toString());

            mOnItemClickListener.OnItemClick(clicked);
        }

        void bind(Group item) {
            TextView mGroupName = itemView.findViewById(R.id.text_group_name);

            mGroupName.setText(item.getName());
        }
    }

    public interface Interaction {
    }


    private static class GroupDC extends DiffUtil.ItemCallback<Group> {
        @Override
        public boolean areItemsTheSame(@NonNull Group oldItem,
                                       @NonNull Group newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Group oldItem,
                                          @NonNull Group newItem) {
            return false;
        }
    }
}