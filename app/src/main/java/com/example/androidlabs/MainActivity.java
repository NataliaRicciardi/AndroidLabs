package com.example.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

    SQLiteDatabase db;

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

        Button add = findViewById(R.id.add);
        EditText box = findViewById(R.id.editbox);
        Switch urgent = findViewById(R.id.urgent);
        ListView myList = findViewById(R.id.list);

        // Initialize the list
        toDoList = new ArrayList<>();

        loadDataFromDatabase();

        adapter = new ToDoAdapter(this, toDoList);

        myList.setAdapter(adapter);

        myList.setOnItemLongClickListener(
                (parent, view, position, id) -> {
                    showDeleteDialog(position);
                    return true;
                }
        );

        add.setOnClickListener(
                (click) -> {
                    String text = box.getText().toString();
                    boolean isUrgent = urgent.isChecked();

                    if (!text.isEmpty()) {
                        // Insert the new item into the database
                        ContentValues newRowValues = new ContentValues();
                        newRowValues.put(MyOpener.COL_ITEM, text);
                        newRowValues.put(MyOpener.COL_URGENT, isUrgent ? 1 : 0); // 1 if true, 0 if false
                        long id = db.insert(MyOpener.TABLE_NAME, null, newRowValues);


                        ToDoItem newItem = new ToDoItem(id, text, isUrgent);

                        toDoList.add(newItem);
                        adapter.notifyDataSetChanged();
                        box.setText("");
                        urgent.setChecked(false);


                    }
                }
        );

    }

    private void printCursor(Cursor c) { // debugging
        // priting the database version number
        System.out.println("Database version: " + db.getVersion());

        // printing the number of columns
        System.out.println("Number of columns: " + c.getColumnCount());

        // printing the names of the columns
        String[] columnames = c.getColumnNames();
        System.out.println("Column names: ");
        for (String columnName : columnames) {
            System.out.println(columnName);
        }

        // printing the number of results
        System.out.println("Number of results: " + c.getCount());

        // printing each row of the cursor
        int urgentColIndex = c.getColumnIndex(MyOpener.COL_URGENT);
        int itemColIndex = c.getColumnIndex(MyOpener.COL_ITEM);
        int idColIndex = c.getColumnIndex(MyOpener.COL_ID);

        while (c.moveToNext()) {
            long id = c.getLong(idColIndex);
            String item = c.getString(itemColIndex);
            boolean urgent = c.getInt(urgentColIndex) == 1; // integer to boolean

            System.out.println("Row: " + id + ", " + item + ", " + urgent);
        }
    }

    private void loadDataFromDatabase() {
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase();

        String[] columns = {MyOpener.COL_ID, MyOpener.COL_ITEM, MyOpener.COL_URGENT};

        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null,
                null, null, null, null);

        // Print the cursor for debugging
        printCursor(results);

        // get column indexes
        int urgentColIndex = results.getColumnIndex(MyOpener.COL_URGENT);
        int itemColIndex = results.getColumnIndex(MyOpener.COL_ITEM);
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);

        if (results.moveToFirst()) { // Set cursor to the first result
            do {
                String item = results.getString(itemColIndex);
                int urgent = results.getInt(urgentColIndex);
                long id = results.getLong(idColIndex);

                boolean isUrgent = urgent == 1;

                toDoList.add(new ToDoItem(id, item, isUrgent));
            } while (results.moveToNext()); // Move to the next result
        }

        results.close();


        // Debugging
        System.out.println("Loaded " + toDoList.size() + " items from the database.");
        for (ToDoItem item : toDoList) {
            Log.d("MainActivity", item.toString());
        }
    }

    private void showDeleteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.deletetitle));
        builder.setMessage(getResources().getString(R.string.selected) + " " + position);

        builder.setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> {
            ToDoItem itemToDelete = toDoList.get(position);
            long idToDelete = itemToDelete.getId();

            db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {String.valueOf(idToDelete)});
            toDoList.remove(position);
            adapter.notifyDataSetChanged();
        });
        builder.setNegativeButton(getResources().getString(R.string.no), null);
        builder.show();
    }

}

class ToDoItem {
    private final String text;
    private final boolean isUrgent;
    private final long id;

    public ToDoItem(long id, String text, boolean isUrgent) {
        this.id = id;
        this.text = text;
        this.isUrgent = isUrgent;
    }

    public long getId() { return id; }

    public String getText() {
        return text;
    }

    public boolean isUrgent() {
        return isUrgent;
    }
}

class ToDoAdapter extends BaseAdapter {

    private Context context;
    private final List<ToDoItem> toDoList;
    private final LayoutInflater inflater;

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

        TextView textView = convertView.findViewById(R.id.items);

        // Get the data item for this position
        ToDoItem toDoItem = toDoList.get(position);

        // Populate the data into the data object
        textView.setText(toDoItem.getText());

        // Set background and text color
        if (toDoItem.isUrgent()) {
            convertView.setBackgroundColor(Color.RED);
            textView.setTextColor(Color.WHITE);
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT); // Default background color
            textView.setTextColor(Color.BLACK);
        }

        return convertView;
    }
}