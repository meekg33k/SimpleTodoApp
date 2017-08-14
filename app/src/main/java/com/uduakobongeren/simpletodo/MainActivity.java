package com.uduakobongeren.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView listViewItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<>();
        readItems();

        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listViewItems.setAdapter(itemsAdapter);

        //Add items
        items.add("Turn in CodePath Project");
        items.add("Get groceries");
        setUpViewListener();

    }

    public void onAddItem(View v){
        EditText editTextView = (EditText) findViewById(R.id.enterNewItem);
        String itemText =  editTextView.getText().toString();
        itemsAdapter.add(itemText);

        //Clear field
        editTextView.setText("");
        writeItems();
    }

    private void setUpViewListener(){
        listViewItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View item, int itemPos, long itemId) {
                        items.remove(itemPos);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
                    }
                }
        );

        listViewItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View item, int itemPos, long itemId) {
                        Intent editItemIntent = new Intent(getApplicationContext(), EditItemActivity.class);

                        TextView editText = (TextView) item;
                        String itemToBeEdited = editText.getText().toString();
                        System.out.println("Item to be edited is "+itemToBeEdited);
                        editItemIntent.putExtra("EDIT_ITEM", itemToBeEdited);
                        editItemIntent.putExtra("ITEM_POS", itemPos);

                        startActivityForResult(editItemIntent, REQUEST_CODE);
                    }
                }
        );
    }

    private void readItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todoList.txt");

        try{
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        }
        catch (IOException ex){
            items = new ArrayList<>();
        }
    }

    private void writeItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todoList.txt");

        try{
            FileUtils.writeLines(todoFile, items);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                String newItem = data.getStringExtra("NEW_ITEM_VAL");
                int itemPos = data.getIntExtra("ITEM_POS", -1);

                if (itemPos != -1){
                    items.set(itemPos, newItem);
                    itemsAdapter.notifyDataSetChanged();
                    writeItems();
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Do nothing!
            }
        }
    }


}
