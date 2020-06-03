package lt.viko.eif.dienynas.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lt.viko.eif.dienynas.R;
import lt.viko.eif.dienynas.models.Group;

public class GradeAdapter
        extends ListAdapter<Group, GradeAdapter.GradeViewHolder> {

    private Interaction interaction;
    private Context context;

    public GradeAdapter(Interaction interaction) {
        super(new GroupDC());
        this.interaction = interaction;
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GradeViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grades, parent, false),
                interaction);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    void swapData(List<Group> data) {
        submitList(data);
    }

    public class GradeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int SCREEN_HEIGHT;
        private int SCREEN_WIDTH;
        private Interaction interaction;

        GradeViewHolder(View view, Interaction interaction) {
            super(view);
            this.interaction = interaction;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
            Group clicked = getItem(getAdapterPosition());
            switch (v.getId()) {
                //TODO handle clicks
                default:
            }

        }

        //TODO: FIX TABLE
        public void bind(Group group) {
            getScreenDimension();
            LinearLayout mHeaderLayout = itemView.findViewById(R.id.grades_header);
            LinearLayout mGradesLayout = itemView.findViewById(R.id.grades_grades);
            TextView mGroupName = itemView.findViewById(R.id.item_grades_group_name);

            mGroupName.setText(group.getName());


            TextView mTextView = new TextView(itemView.getContext());
            mTextView.setBackgroundResource(R.drawable.border);
            mTextView.setText("Code");
            mTextView.setMinimumHeight(SCREEN_HEIGHT / 12);
            mTextView.setMinimumWidth(SCREEN_WIDTH / 3);
//            mTextView.setTextSize(R.dimen.cell_text_size);
            mTextView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

            mHeaderLayout.addView(mTextView, 0);

            for (int i = 1; i <= group.getTask().size(); i++) {
                TextView mTextView2 = new TextView(itemView.getContext());
                mTextView2.setBackgroundResource(R.drawable.border);
                mTextView2.setText(group.getTask().get(i - 1));
                mTextView2.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
//                mTextView.setTextSize(R.dimen.cell_text_size);
                mTextView2.setMinimumWidth(SCREEN_WIDTH / 3);
                mTextView2.setMinimumHeight(SCREEN_HEIGHT / 12);
                mTextView2.setHeight(SCREEN_HEIGHT / 12);
                mTextView2.setWidth(SCREEN_WIDTH / 3);
                mHeaderLayout.addView(mTextView2, i);
            }
            mTextView = new TextView(itemView.getContext());
            mTextView.setBackgroundResource(R.drawable.border);
            mTextView.setMinimumHeight(SCREEN_HEIGHT / 12);
            mTextView.setMinimumWidth(SCREEN_WIDTH / 3);
//            mTextView.setTextSize(R.dimen.cell_text_size);
            mTextView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            mTextView.setText(group.getStudents().get(0).getCode());

            mGradesLayout.addView(mTextView, 0);
            for (int i = 1; i <= group.getStudents().get(0).getGrades().size(); i++) {
                TextView mTextView2 = new TextView(itemView.getContext());
                mTextView2.setBackgroundResource(R.drawable.border);
                mTextView2.setText(String.valueOf(group.getStudents().get(0).getGrades().get(i - 1)));
                mTextView2.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
//                mTextView2.setTextSize(R.dimen.cell_text_size);
                mTextView2.setMinimumWidth(SCREEN_WIDTH / 3);
                mTextView2.setMinimumHeight(SCREEN_HEIGHT / 12);
                mGradesLayout.addView(mTextView2, i);
            }

        }

        private void getScreenDimension() {
            SCREEN_WIDTH = itemView.getResources().getDisplayMetrics().widthPixels;
            SCREEN_HEIGHT = itemView.getResources().getDisplayMetrics().heightPixels;
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
