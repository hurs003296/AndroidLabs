package com.example.androidlabs;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TodoDatabaseHelper dbHelper;
    private List<TodoItem> todoList;
    private BaseAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the database helper
        dbHelper = new TodoDatabaseHelper(this);

        // Retrieve all todo items from the database
        todoList = dbHelper.getAllTodoItems();

        // Set up views
        ListView listView = findViewById(R.id.todoListView);
        EditText editText = findViewById(R.id.todoEditText);
        Switch urgentSwitch = findViewById(R.id.urgentSwitch);
        Button addButton = findViewById(R.id.addButton);

        // Set up the ListView adapter
        todoAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return todoList.size();
            }

            @Override
            public Object getItem(int position) {
                return todoList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.todo_item, parent, false);
                }

                TodoItem item = todoList.get(position);
                TextView textView = convertView.findViewById(R.id.todoTextView);
                textView.setText(item.getText());

                if (item.isUrgent()) {
                    convertView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    textView.setTextColor(getResources().getColor(android.R.color.white));
                } else {
                    convertView.setBackgroundColor(getResources().getColor(android.R.color.white));
                    textView.setTextColor(getResources().getColor(android.R.color.black));
                }

                return convertView;
            }
        };

        listView.setAdapter(todoAdapter);

        // Add new todo item
        addButton.setOnClickListener(v -> {
            String text = editText.getText().toString();
            boolean isUrgent = urgentSwitch.isChecked();

            if (!text.isEmpty()) {
                TodoItem newItem = new TodoItem(text, isUrgent);
                dbHelper.addTodoItem(newItem);  // Save to database
                todoList.add(newItem);  // Add to list in memory
                todoAdapter.notifyDataSetChanged();
                editText.setText("");
            }
        });

        // Delete a todo item on long press
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getString(R.string.delete_prompt))
                    .setMessage(getString(R.string.delete_selected_row) + position)
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        TodoItem itemToDelete = todoList.get(position);
                        dbHelper.deleteTodoItem(position);  // Delete from database
                        todoList.remove(position);  // Remove from list in memory
                        todoAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(getString(R.string.no), null)
                    .show();
            return true;
        });
    }
}