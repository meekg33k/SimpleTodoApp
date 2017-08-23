package com.uduakobongeren.simpletodo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.uduakobongeren.simpletodo.model.ToDoItem;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import static android.content.ContentValues.TAG;


/**
 * @author Uduak Obong-Eren
 * @since 8/15/17.
 */

public class ToDoItemsDBHelper extends SQLiteOpenHelper {

    private static ToDoItemsDBHelper sInstance;
    private static Context appContext;

    // Database Details
    private static final String DATABASE_NAME = "toDoItemsDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_ITEMS= "items";

    // Table  Columns
    private static final String FIELD_ITEM_ID = "id";
    private static final String FIELD_ITEM_DATE = "date";
    private static final String FIELD_ITEM_DESCRIPTION = "description";
    private static final String FIELD_ITEM_IS_COMPLETED = "completed";
    private static final String FIELD_ITEM_PRIORITY = "priority";

    private ToDoItemsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        appContext = context;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    public static synchronized ToDoItemsDBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ToDoItemsDBHelper(context);
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TODO_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                FIELD_ITEM_ID + " INTEGER PRIMARY KEY," +
                FIELD_ITEM_DATE + " STRING, " +
                FIELD_ITEM_DESCRIPTION + " TEXT, " +
                FIELD_ITEM_IS_COMPLETED + " INTEGER, " +
                FIELD_ITEM_PRIORITY + " TEXT" +
                ")";

        db.execSQL(CREATE_TODO_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            onCreate(db);
        }
    }


    public boolean createToDoItem(ToDoItem item) {
        SQLiteDatabase db = getWritableDatabase();
        boolean status = false;
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            //Format Date
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            values.put(FIELD_ITEM_DATE, "");
            values.put(FIELD_ITEM_DESCRIPTION, item.getDescription());
            values.put(FIELD_ITEM_IS_COMPLETED, 0);
            values.put(FIELD_ITEM_PRIORITY, item.getDescription());

            long itemId = db.insertOrThrow(TABLE_ITEMS, null, values);
            item.setId(itemId);
            db.setTransactionSuccessful();
            status = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Error while trying to add new item to database");
        } finally {
            db.endTransaction();
        }
        return status;

    }

    public boolean editToDoItem(ToDoItem item, String newDesc) {
        SQLiteDatabase db = getWritableDatabase();
        long id = item.getId();
        boolean status = false;
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(FIELD_ITEM_DESCRIPTION, newDesc);
            db.update(TABLE_ITEMS, values, "id="+id, null);
            db.setTransactionSuccessful();
            status = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Error while trying to edit todo item in database");
        } finally {
            db.endTransaction();
        }
        return status;
    }

    public boolean editToDoItemDesc(String newDesc, int pos) {
        SQLiteDatabase db = getWritableDatabase();
        long id = pos;
        boolean status = false;
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(FIELD_ITEM_DESCRIPTION, newDesc);
            db.update(TABLE_ITEMS, values, "id="+id, null);
            db.setTransactionSuccessful();
            status = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Error while trying to edit todo item in database");
        } finally {
            db.endTransaction();
        }
        return status;

    }

    public boolean updateItemCompletedState(ToDoItem item, int completed) {
        SQLiteDatabase db = getWritableDatabase();
        long id = item.getId();
        boolean status = false;
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(FIELD_ITEM_IS_COMPLETED, completed);
            db.update(TABLE_ITEMS, values, "id="+id, null);
            db.setTransactionSuccessful();
            status = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Error while trying to update item's completed state  in database");
        } finally {
            db.endTransaction();
        }
        return status;
    }

    public boolean updateItemPriority(ToDoItem item, String priority) {
        SQLiteDatabase db = getWritableDatabase();
        long id = item.getId();
        boolean status = false;
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(FIELD_ITEM_PRIORITY, priority);
            db.update(TABLE_ITEMS, values, "id="+id, null);
            db.setTransactionSuccessful();
            status = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Error while trying to update item's priority state  in database");
        } finally {
            db.endTransaction();
            return status;
        }
    }

    public boolean updateItemCompletionDate(ToDoItem item, String date) {
        SQLiteDatabase db = getWritableDatabase();
        long id = item.getId();
        boolean status = false;
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(FIELD_ITEM_DATE, date);
            db.update(TABLE_ITEMS, values, "id="+id, null);
            db.setTransactionSuccessful();
            status = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Error while trying to update item's date  in database");
        } finally {
            db.endTransaction();
        }
        return status;
    }

    public boolean deleteToDoItem(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        boolean status = false;

        try {
            String whereClause = "id=?";
            String[] whereArgs = new String[] { String.valueOf(id) };
            db.delete(TABLE_ITEMS, whereClause, whereArgs);
            db.setTransactionSuccessful();
            status = true;
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete item from database");
        } finally {
            db.endTransaction();
            return status;
        }
    }

    public ArrayList<ToDoItem> getAllItems() {
        ArrayList<ToDoItem> items = new ArrayList<>();

        String ITEMS_SELECT_QUERY = String.format("SELECT * FROM %s", TABLE_ITEMS);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ITEMS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    ToDoItem toDoItem = new ToDoItem();

                    //Set item attributes
                    toDoItem.setId(cursor.getInt(cursor.getColumnIndex(FIELD_ITEM_ID)));
                    toDoItem.setCompletionDate(cursor.getString(cursor.getColumnIndex(FIELD_ITEM_DATE)));
                    toDoItem.setDescription(cursor.getString(cursor.getColumnIndex(FIELD_ITEM_DESCRIPTION)));
                    toDoItem.setCompleted(cursor.getInt(cursor.getColumnIndex(FIELD_ITEM_IS_COMPLETED)));
                    toDoItem.setPriority(cursor.getString(cursor.getColumnIndex(FIELD_ITEM_PRIORITY)));

                    items.add(toDoItem);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get items from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return items;
    }

    public ArrayList<ToDoItem> getAllPendingItems() {
        ArrayList<ToDoItem> items = new ArrayList<>();

        String ITEMS_SELECT_QUERY = "SELECT * FROM "+TABLE_ITEMS+" WHERE "+FIELD_ITEM_IS_COMPLETED+"=0";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ITEMS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    ToDoItem toDoItem = new ToDoItem();

                    //Set item attributes
                    toDoItem.setId(cursor.getInt(cursor.getColumnIndex(FIELD_ITEM_ID)));
                    toDoItem.setCompletionDate(cursor.getString(cursor.getColumnIndex(FIELD_ITEM_DATE)));
                    toDoItem.setDescription(cursor.getString(cursor.getColumnIndex(FIELD_ITEM_DESCRIPTION)));
                    toDoItem.setCompleted(cursor.getInt(cursor.getColumnIndex(FIELD_ITEM_IS_COMPLETED)));
                    toDoItem.setPriority(cursor.getString(cursor.getColumnIndex(FIELD_ITEM_PRIORITY)));

                    items.add(toDoItem);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get items from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return items;
    }

}
