package com.example.simpletaskmanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.simpletaskmanager.R;
import com.example.simpletaskmanager.models.Task;
import java.util.ArrayList;
import java.util.List;

// Connects our task list to the RecyclerView to display items.
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks = new ArrayList<>();
    private final OnTaskActionListener listener;

    public interface OnTaskActionListener {
        void onDeleteClick(Task task);

        void onCheckChanged(Task task, boolean isChecked);

        void onTaskClick(Task task);

        void onEditClick(Task task);
    }

    public TaskAdapter(OnTaskActionListener listener) {
        this.listener = listener;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task currentTask = tasks.get(position);
        holder.bind(currentTask);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    // Holds the view for each task item.
    class TaskViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final TextView textViewDescription;
        private final CheckBox checkBoxCompleted;
        private final ImageView imageViewDelete;
        private final ImageView imageViewEdit;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            checkBoxCompleted = itemView.findViewById(R.id.checkbox_completed);
            imageViewDelete = itemView.findViewById(R.id.image_view_delete);
            imageViewEdit = itemView.findViewById(R.id.image_view_edit);

            // Set up all the click listeners for interactions.
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onTaskClick(tasks.get(position));
                }
            });

            imageViewEdit.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onEditClick(tasks.get(position));
                }
            });

            imageViewDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDeleteClick(tasks.get(position));
                }
            });

            checkBoxCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    if (buttonView.isPressed()) {
                        listener.onCheckChanged(tasks.get(position), isChecked);
                    }
                }
            });
        }

        public void bind(Task task) {
            textViewTitle.setText(task.getTitle());
            textViewDescription.setText(task.getDescription());
            checkBoxCompleted.setChecked(task.isCompleted());
        }
    }
}
