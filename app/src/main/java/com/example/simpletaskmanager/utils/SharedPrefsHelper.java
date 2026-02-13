package com.example.simpletaskmanager.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.simpletaskmanager.models.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

// Helps save and load tasks using the phone's local storage.
public class SharedPrefsHelper {

    private static final String PREF_NAME = "task_manager_prefs";
    private static final String KEY_TASKS = "key_tasks";
    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public SharedPrefsHelper(Context context) {
        // We use MODE_PRIVATE so only this app can read the data.
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    // Convert the list to text (JSON) and save it.
    public void saveTasks(List<Task> tasks) {
        String json = gson.toJson(tasks);
        sharedPreferences.edit().putString(KEY_TASKS, json).apply();
    }

    // Read the text and convert it back to a list of tasks.
    public List<Task> getTasks() {
        String json = sharedPreferences.getString(KEY_TASKS, null);
        if (json == null) {
            return new ArrayList<>();
        }

        Type type = new TypeToken<List<Task>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}
