package com.example.simpletaskmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.simpletaskmanager.R;
import com.example.simpletaskmanager.adapters.TaskAdapter;
import com.example.simpletaskmanager.models.Task;
import com.example.simpletaskmanager.viewmodel.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

// This is the main screen where we show the list of tasks.
public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskActionListener {

    private TaskViewModel taskViewModel;
    private TaskAdapter taskAdapter;
    private TextView textViewNoTasks;

    // Handle what happens when we come back from adding or editing a task.
    private final ActivityResultLauncher<Intent> addTaskLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    // Get the data sent back from the other screen.
                    String title = result.getData().getStringExtra(AddEditTaskActivity.EXTRA_TITLE);
                    String description = result.getData().getStringExtra(AddEditTaskActivity.EXTRA_DESCRIPTION);
                    String id = result.getData().getStringExtra(AddEditTaskActivity.EXTRA_ID);

                    if (title != null && !title.isEmpty()) {
                        if (id != null) {
                            taskViewModel.updateTask(id, title, description);
                        } else {
                            taskViewModel.addTask(title, description);
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the list and the 'Add' button here.
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        FloatingActionButton fab = findViewById(R.id.fab_add_task);
        textViewNoTasks = findViewById(R.id.text_view_no_tasks);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(this);
        recyclerView.setAdapter(taskAdapter);

        // Connect the ViewModel to keep our data safe.
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Watch for any changes in the task list.
        taskViewModel.getTasks().observe(this, tasks -> {
            taskAdapter.setTasks(tasks);

            // Show a message if the list is empty.
            if (tasks.isEmpty()) {
                textViewNoTasks.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                textViewNoTasks.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            addTaskLauncher.launch(intent);
        });
    }

    @Override
    public void onDeleteClick(Task task) {
        taskViewModel.deleteTask(task);
    }

    @Override
    public void onCheckChanged(Task task, boolean isChecked) {
        // Only save if something actually changed.
        if (task.isCompleted() != isChecked) {
            taskViewModel.updateTaskStatus(task, isChecked);
        }
    }

    @Override
    public void onTaskClick(Task task) {
        // Prepare to edit the task when clicked.
        Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
        intent.putExtra(AddEditTaskActivity.EXTRA_ID, task.getId());
        intent.putExtra(AddEditTaskActivity.EXTRA_TITLE, task.getTitle());
        intent.putExtra(AddEditTaskActivity.EXTRA_DESCRIPTION, task.getDescription());
        addTaskLauncher.launch(intent);
    }

    @Override
    public void onEditClick(Task task) {
        onTaskClick(task);
    }
}
