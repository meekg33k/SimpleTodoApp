package com.uduakobongeren.simpletodo.ui;

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
import android.widget.Toast;
import com.uduakobongeren.simpletodo.R;
import com.uduakobongeren.simpletodo.dao.ToDoItemsDBHelper;
import com.uduakobongeren.simpletodo.model.Priority;
import com.uduakobongeren.simpletodo.model.ToDoItem;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * @author Uduak Obong-Eren
 * @since 8/13/17.
 */
public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    //ArrayList<String> items;
    //ArrayAdapter<String> itemsAdapter;
    ArrayList<ToDoItem> items;
    ArrayAdapter<ToDoItem> itemsAdapter;
    ToDoItemsAdapter toDoItemsAdapter;
    ListView listViewItems;
    ToDoItemsDBHelper dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dao = ToDoItemsDBHelper.getInstance(getApplicationContext());
        listViewItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<>();

        //readItems();
        readItemsFromDatabase();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        toDoItemsAdapter = new ToDoItemsAdapter(this, items, dao); //Creating custom adapter

        //listViewItems.setAdapter(itemsAdapter);
        listViewItems.setAdapter(toDoItemsAdapter);
        setUpViewListeners();

    }

    public void onAddItem(View v){
        EditText editTextView = (EditText) findViewById(R.id.enterNewItem);
        String itemText =  editTextView.getText().toString();

        if (!itemText.equals("")){
            ToDoItem newItem = new ToDoItem(itemText, Priority.LOW.getLevel(), "");

            //writeItems();
            if (writeItemToDatabase(newItem)){
                //itemsAdapter.add(newItem); //Update list for default adapter

                //Update list for custom adapter
                toDoItemsAdapter.add(newItem);

                //Clear field
                editTextView.setText("");
            }
        }

    }

    /** These listeners work with default listView adapter **/
    private void setUpViewListeners(){
        listViewItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View item, int itemPos, long itemId) {
                        ToDoItem itemClicked = items.get(itemPos);

                        //writeItems();
                        if (deleteItemFromDatabase(itemClicked.getId())){
                            items.remove(itemPos);
                            itemsAdapter.notifyDataSetChanged();
                        }
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
        ArrayList<String> items;

        try{
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        }
        catch (IOException ex){
            items = new ArrayList<>();
        }
    }

    private void readItemsFromDatabase(){
        try{
            items = dao.getAllItems();
        }
        catch (Exception ex){
            ex.printStackTrace();
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

    private boolean writeItemToDatabase(ToDoItem item){
        if (dao.createToDoItem(item)){
            Toast.makeText(this, "Item created successfully!", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private boolean deleteItemFromDatabase(long id){
        if (dao.deleteToDoItem(id)){
            Toast.makeText(this, "Item deleted successfully!", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                String newDescription = data.getStringExtra("NEW_ITEM_VAL");
                int itemPos = data.getIntExtra("ITEM_POS", -1);
                ToDoItem editedItem = items.get(itemPos);

                if (itemPos != -1){

                    if (dao.editToDoItem(editedItem, newDescription)){
                        Toast.makeText(this, "Changes saved successfully!", Toast.LENGTH_LONG).show();
                        editedItem.setDescription(newDescription);
                        itemsAdapter.notifyDataSetChanged();
                    }
                    //writeItems();
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Do nothing!
            }
        }
    }


}
