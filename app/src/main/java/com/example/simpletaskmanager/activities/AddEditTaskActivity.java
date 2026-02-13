package com.example.simpletaskmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.simpletaskmanager.R;

// Screen to add a new task or edit an existing one.
public class AddEditTaskActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.simpletaskmanager.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.simpletaskmanager.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.simpletaskmanager.EXTRA_DESCRIPTION";

    private EditText editTextTitle;
    private EditText editTextDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        Button buttonSave = findViewById(R.id.button_save);

        // Check if we are editing an existing task to show the right title.
        if (getIntent() != null && getIntent().hasExtra(EXTRA_ID)) {
            setTitle(R.string.edit_task);
            editTextTitle.setText(getIntent().getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(getIntent().getStringExtra(EXTRA_DESCRIPTION));
            buttonSave.setText(R.string.update_task);
        } else {
            setTitle(R.string.add_task);
        }

        // Enable the back arrow in the toolbar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        buttonSave.setOnClickListener(v -> saveTask());
    }

    // Validate the input and send the result back.
    private void saveTask() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (title.isEmpty()) {
            editTextTitle.setError(getString(R.string.error_empty_title));
            editTextTitle.requestFocus();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);

        String id = getIntent().getStringExtra(EXTRA_ID);
        if (id != null) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
