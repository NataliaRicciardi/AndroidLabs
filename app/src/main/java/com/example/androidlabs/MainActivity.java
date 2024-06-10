package com.example.androidlabs;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<ToDoItem> toDoList;
    private ToDoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // move linearlayout when keyboard opened
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView myList = findViewById(R.id.list);

        // Initialize the list and adapter
        toDoList = new ArrayList<>();
        adapter = new ToDoAdapter(this, toDoList);

        myList.setAdapter(adapter);

        myList.setOnItemLongClickListener(
                (parent, view, position, id) -> {
                    showDeleteDialog(position);
                    return true;
                }
        );

        Button add = findViewById(R.id.add);
        EditText box = findViewById(R.id.editbox);
        Switch urgent = findViewById(R.id.urgent);

        add.setOnClickListener(
                (click) -> {
                    String text = box.getText().toString();
                    boolean isUrgent = urgent.isChecked();

                    if (!text.isEmpty()) {
                        ToDoItem newItem = new ToDoItem(text, isUrgent);
                        toDoList.add(newItem);
                        adapter.notifyDataSetChanged();
                        box.setText("");
                        urgent.setChecked(false);
                    }
                }
        );

    }

    private void showDeleteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to delete this?");
        builder.setMessage("The selected row is: " + position);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                toDoList.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

}

class ToDoItem {
    private String text;
    private boolean isUrgent;

    public ToDoItem(String text, boolean isUrgent) {
        this.text = text;
        this.isUrgent = isUrgent;
    }

    public String getText() {
        return text;
    }

    public boolean isUrgent() {
        return isUrgent;
    }
}

class ToDoAdapter extends BaseAdapter {

    private Context context;
    private List<ToDoItem> toDoList;
    private LayoutInflater inflater;

    public ToDoAdapter(Context context, List<ToDoItem> toDoList) {
        this.context = context;
        this.toDoList = toDoList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return toDoList.size();
    }

    @Override
    public Object getItem(int position) {
        return toDoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate a new view every time
        convertView = inflater.inflate(R.layout.todo_items, parent, false);

        // Find the TextView within the newly inflated view
        TextView textView = convertView.findViewById(R.id.items);

        // Get the data item for this position
        ToDoItem toDoItem = toDoList.get(position);

        // Populate the data into the template view using the data object
        textView.setText(toDoItem.getText());

        // Set background and text color based on urgency
        if (toDoItem.isUrgent()) {
            convertView.setBackgroundColor(Color.RED);
            textView.setTextColor(Color.WHITE);
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT); // Default background color
            textView.setTextColor(Color.BLACK);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}