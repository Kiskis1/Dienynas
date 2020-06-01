package lt.viko.eif.dienynas.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lt.viko.eif.dienynas.R;
import lt.viko.eif.dienynas.models.Group;

import static lt.viko.eif.dienynas.utils.App.getContext;

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
        private RelativeLayout mRelativeLayout;
        private TableLayout mTableLayout;
        private TableRow mTableRow;
        private TableRow mTableRow2;
        private final int COLUMNS_PER_SCREEN = 3;
        private final int ROWS_PER_SCREEN = 9;
        private final Interaction interaction;

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

        private void getScreenDimension() {
            SCREEN_WIDTH = getContext().getResources().getDisplayMetrics().widthPixels;
            SCREEN_HEIGHT = getContext().getResources().getDisplayMetrics().heightPixels;
        }
//TODO: FIX TABLE
        public void bind(Group item) {
            getScreenDimension();
            mRelativeLayout = itemView.findViewById(R.id.item_grade_relative_layout);

            mTableLayout = new TableLayout(getContext());
            mTableLayout.setPadding(0, 0, 0, 0);
            TableLayout.LayoutParams layoutParamsTableLayoutA = new TableLayout.LayoutParams(SCREEN_WIDTH / COLUMNS_PER_SCREEN, SCREEN_HEIGHT / ROWS_PER_SCREEN);
            mTableLayout.setLayoutParams(layoutParamsTableLayoutA);
            mTableLayout.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));

            mTableRow = new TableRow(getContext());
            TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(SCREEN_WIDTH / COLUMNS_PER_SCREEN, SCREEN_HEIGHT / ROWS_PER_SCREEN);
            mTableRow.setLayoutParams(layoutParamsTableRow);
            mTableRow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

            mTableRow2 = new TableRow(getContext());
            mTableRow2.setLayoutParams(layoutParamsTableRow);
            mTableRow2.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

            TextView header = new TextView(getContext());
            header.setText(R.string.table_code_and_name);
            header.setTextSize(getContext().getResources().getDimension(R.dimen.cell_text_size));
            this.mTableRow.addView(header);

            TextView task;
            for(String s : item.getTask()){
                task = new TextView(getContext());
                task.setText(s);
                task.setTextSize(getContext().getResources().getDimension(R.dimen.cell_text_size));
                this.mTableRow.addView(task);
            }
            this.mTableLayout.addView(mTableRow);

            TextView code = new TextView(getContext());
            code.setText(item.getStudents().get(0).getCode());
            code.setTextSize(getContext().getResources().getDimension(R.dimen.cell_text_size));
            this.mTableRow2.addView(code);

            TextView grade;
            for(int g : item.getStudents().get(0).getGrades()){
                grade = new TextView(getContext());
                grade.setText(String.valueOf(g));
                grade.setTextSize(getContext().getResources().getDimension(R.dimen.cell_text_size));
                this.mTableRow2.addView(grade);
            }
            this.mTableLayout.addView(mTableRow2);


            this.mRelativeLayout.addView(mTableLayout);

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