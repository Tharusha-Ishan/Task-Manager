package com.example.simpletaskmanager.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.simpletaskmanager.models.Task;
import com.example.simpletaskmanager.utils.SharedPrefsHelper;
import java.util.ArrayList;
import java.util.List;

// Manages the data for the UI and keeps it safe when the screen rotates.
public class TaskViewModel extends AndroidViewModel {

    private final SharedPrefsHelper repository;
    private final MutableLiveData<List<Task>> tasksLiveData = new MutableLiveData<>();
    private List<Task> currentTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new SharedPrefsHelper(application.getApplicationContext());
        loadTasks();
    }

    // Get the task list from storage.
    private void loadTasks() {
        currentTasks = repository.getTasks();
        if (currentTasks == null) {
            currentTasks = new ArrayList<>();
        }
        tasksLiveData.setValue(currentTasks);
    }

    public LiveData<List<Task>> getTasks() {
        return tasksLiveData;
    }

    // Add a new task to the top of the list.
    public void addTask(String title, String description) {
        Task newTask = new Task(title, description);
        currentTasks.add(0, newTask);
        repository.saveTasks(currentTasks);
        tasksLiveData.setValue(currentTasks);
    }

    // Remove a task.
    public void deleteTask(Task task) {
        currentTasks.remove(task);
        repository.saveTasks(currentTasks);
        tasksLiveData.setValue(currentTasks);
    }

    // Save the new completion status.
    public void updateTaskStatus(Task task, boolean isCompleted) {
        task.setCompleted(isCompleted);
        repository.saveTasks(currentTasks);
        tasksLiveData.setValue(currentTasks);
    }

    // Edit an existing task's details.
    public void updateTask(String id, String title, String description) {
        for (Task task : currentTasks) {
            if (task.getId().equals(id)) {
                task.setTitle(title);
                task.setDescription(description);
                repository.saveTasks(currentTasks);
                tasksLiveData.setValue(currentTasks);
                return;
            }
        }
    }
}
