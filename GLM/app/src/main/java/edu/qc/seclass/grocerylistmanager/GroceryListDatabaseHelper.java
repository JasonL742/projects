package edu.qc.seclass.grocerylistmanager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class GroceryListDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "grocery_list.db";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "groceryTBL";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ITEM = "item";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_ITEM_TYPE = "itemType";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ITEM_CHECKED = "checked";



    public GroceryListDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_ITEM + " TEXT NOT NULL," +
                COLUMN_QUANTITY + " INTEGER NOT NULL," +
                COLUMN_ITEM_TYPE + " TEXT," +
                COLUMN_ITEM_CHECKED + " INTEGER DEFAULT 0," +
                COLUMN_NAME + " TEXT)");
        insertPresetItems(db);
    }

    private void insertPresetItems(SQLiteDatabase db) {
        // Insert preset items directly into the table

        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_ITEM + ", " + COLUMN_QUANTITY + ", " + COLUMN_ITEM_TYPE + ", " + COLUMN_NAME + ") VALUES ('Ketchup', 1, 'CONDIMENTS', '')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_ITEM + ", " + COLUMN_QUANTITY + ", " + COLUMN_ITEM_TYPE + ", " + COLUMN_NAME + ") VALUES ('Cheerios', 1, 'GRAIN', '')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_ITEM + ", " + COLUMN_QUANTITY + ", " + COLUMN_ITEM_TYPE + ", " + COLUMN_NAME + ") VALUES ('Oatmeal', 1, 'GRAIN', '')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_ITEM + ", " + COLUMN_QUANTITY + ", " + COLUMN_ITEM_TYPE + ", " + COLUMN_NAME + ") VALUES ('Bread', 1, 'GRAIN', '')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_ITEM + ", " + COLUMN_QUANTITY + ", " + COLUMN_ITEM_TYPE + ", " + COLUMN_NAME + ") VALUES ('Milk', 1, 'DAIRY', '')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_ITEM + ", " + COLUMN_QUANTITY + ", " + COLUMN_ITEM_TYPE + ", " + COLUMN_NAME + ") VALUES ('Yogurt', 1, 'DAIRY', '')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_ITEM + ", " + COLUMN_QUANTITY + ", " + COLUMN_ITEM_TYPE + ", " + COLUMN_NAME + ") VALUES ('Ground beef', 1, 'MEAT', '')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_ITEM + ", " + COLUMN_QUANTITY + ", " + COLUMN_ITEM_TYPE + ", " + COLUMN_NAME + ") VALUES ('Black Beans', 1, 'LEGUMES ', '')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_ITEM + ", " + COLUMN_QUANTITY + ", " + COLUMN_ITEM_TYPE + ", " + COLUMN_NAME + ") VALUES ('Soy Beans', 1, 'LEGUMES ', '')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_ITEM + ", " + COLUMN_QUANTITY + ", " + COLUMN_ITEM_TYPE + ", " + COLUMN_NAME + ") VALUES ('Chickpeas', 1, 'LEGUMES ', '')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_ITEM + ", " + COLUMN_QUANTITY + ", " + COLUMN_ITEM_TYPE + ", " + COLUMN_NAME + ") VALUES ('Blueberries', 1, 'FRUITS', '')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_ITEM + ", " + COLUMN_QUANTITY + ", " + COLUMN_ITEM_TYPE + ", " + COLUMN_NAME + ") VALUES ('Bananas', 1, 'FRUITS', '')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_ITEM + ", " + COLUMN_QUANTITY + ", " + COLUMN_ITEM_TYPE + ", " + COLUMN_NAME + ") VALUES ('Orange', 1, 'FRUITS', '')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_ITEM + ", " + COLUMN_QUANTITY + ", " + COLUMN_ITEM_TYPE + ", " + COLUMN_NAME + ") VALUES ('Spinach', 1, 'VEGETABLES', '')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_ITEM + ", " + COLUMN_QUANTITY + ", " + COLUMN_ITEM_TYPE + ", " + COLUMN_NAME + ") VALUES ('Broccoli', 1, 'VEGETABLES', '')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_ITEM + ", " + COLUMN_QUANTITY + ", " + COLUMN_ITEM_TYPE + ", " + COLUMN_NAME + ") VALUES ('Cucumber', 1, 'VEGETABLES', '')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_ITEM + ", " + COLUMN_QUANTITY + ", " + COLUMN_ITEM_TYPE + ", " + COLUMN_NAME + ") VALUES ('Oatmeal Cookies', 1, 'DESSERT', '')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_ITEM + ", " + COLUMN_QUANTITY + ", " + COLUMN_ITEM_TYPE + ", " + COLUMN_NAME + ") VALUES ('Ices', 1, 'DESSERT', '')");
        // Add more preset items as needed
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This method is called when the database version changes.
        // We'll simply drop and recreate the table for simplicity.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addItem(String item, int quantity, String itemType, String listName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM, item);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_ITEM_TYPE, itemType);
        values.put(COLUMN_NAME, listName);
        db.insert(TABLE_NAME, null, values);
        Log.e("addItem", "Added: " + values);
        db.close();
    }

    public void toggleItemCheck(String itemNameWithQuantity, String listName) {
        SQLiteDatabase db = this.getWritableDatabase();

            // Split item name, type, and quantity
            String[] parts = itemNameWithQuantity.split(" - Type: ");
            if (parts.length != 2) {
                // Invalid format, return false
                Log.e("toggleItemCheck", "Invalid format: " + itemNameWithQuantity);
                return;
            }

            String[] nameAndType = parts[1].split(" - Quantity: ");
            if (nameAndType.length != 2) {
                // Invalid format, return false
                Log.e("toggleItemCheck", "Invalid format: " + itemNameWithQuantity);
                return;
            }

            String itemName = parts[0].trim();
            String itemType = parts[1].split(" - Quantity: ")[0].trim();

        // Query the current check status of the item
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ITEM_CHECKED + " FROM " + TABLE_NAME + " WHERE "
                        + COLUMN_ITEM + "=? AND " + COLUMN_ITEM_TYPE + "=? AND " + COLUMN_NAME + "=?",
                new String[]{itemName, itemType, listName});

        if (cursor != null && cursor.moveToFirst()) {
            int currentCheckStatus = cursor.getInt(0); // Get the current check status
            int newCheckStatus = (currentCheckStatus == 1) ? 0 : 1; // Toggle the check status

            // Update the check status in the database
            ContentValues values = new ContentValues();
            values.put(COLUMN_ITEM_CHECKED, newCheckStatus);
            String whereClause = COLUMN_ITEM + "=? AND " + COLUMN_ITEM_TYPE + "=? AND " + COLUMN_NAME + "=?";
            String[] whereArgs = new String[]{itemName, itemType, listName};
            db.update(TABLE_NAME, values, whereClause, whereArgs);
        }

        // Close cursor and database
        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }

    public void uncheckAllItemsInList(String listName) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Update the check status of all items in the specified list to 0 (unchecked)
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_CHECKED, 0); // Set check status to 0 (unchecked)

        String whereClause = COLUMN_NAME + "=?";
        String[] whereArgs = new String[]{listName};

        db.update(TABLE_NAME, values, whereClause, whereArgs);

        // Close database
        db.close();
    }

    public String getCheckValue(String itemName, String itemType, String listName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String checkValue = "0"; // Default value if not found

        // Define the columns to retrieve
        String[] columns = {COLUMN_ITEM_CHECKED};

        // Define the selection criteria
        String selection = COLUMN_ITEM + " = ? AND " + COLUMN_ITEM_TYPE + " = ? AND " + COLUMN_NAME + " = ?";
        String[] selectionArgs = {itemName, itemType, listName};

        // Query the database
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        // Check if the cursor has data
        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve the check value from the cursor
            checkValue = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_CHECKED));
            cursor.close(); // Close the cursor to release resources
        }

        db.close(); // Close the database connection

        return checkValue;
    }
    public Cursor getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
    public Cursor getItemsForList(String listName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM groceryTBL WHERE name=?", new String[]{listName});
    }



    public boolean deleteItem(String itemNameWithQuantity, String listName) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Split item name, type, and quantity
            String[] parts = itemNameWithQuantity.split(" - Type: ");
            if (parts.length != 2) {
                // Invalid format, return false
                Log.e("DeleteItem", "Invalid format: " + itemNameWithQuantity);
                return false;
            }

            String[] nameAndType = parts[1].split(" - Quantity: ");
            if (nameAndType.length != 2) {
                // Invalid format, return false
                Log.e("DeleteItem", "Invalid format: " + itemNameWithQuantity);
                return false;
            }

            String itemName = parts[0].trim();
            String itemType = parts[1].split(" - Quantity: ")[0].trim();

            // Check if the item exists in the specified list
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ITEM + "=? AND " + COLUMN_ITEM_TYPE + "=? AND " + COLUMN_NAME + "=?", new String[]{itemName, itemType, listName});
            if (cursor.getCount() > 0) {
                // Item exists, proceed with deletion
                String whereClause = COLUMN_ITEM + "=? AND " + COLUMN_ITEM_TYPE + "=? AND " + COLUMN_NAME + "=?";
                String[] whereArgs = {itemName, itemType, listName};
                int rowsDeleted = db.delete(TABLE_NAME, whereClause, whereArgs);
                Log.d("DeleteItem", "Deleted " + rowsDeleted + " rows for item: " + itemName + ", of Type: " + itemType);
                return rowsDeleted > 0;
            } else {
                // Item does not exist in the specified list
                Log.d("DeleteItem", "Item not found in list: " + itemName);
                return false;
            }
        } catch (Exception e) {
            Log.e("DeleteItem", "Error deleting item: " + e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    public boolean updateItemQuantity(String itemNameWithQuantity, int newQuantity, String listName) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Split item name, type, and quantity
            String[] parts = itemNameWithQuantity.split(" - Type: ");
            if (parts.length != 2) {
                // Invalid format, return false
                Log.e("DeleteItem", "Invalid format: " + itemNameWithQuantity);
                return false;
            }

            String[] nameAndType = parts[1].split(" - Quantity: ");
            if (nameAndType.length != 2) {
                // Invalid format, return false
                Log.e("DeleteItem", "Invalid format: " + itemNameWithQuantity);
                return false;
            }
            String itemName = parts[0].trim();
            String itemType = parts[1].split(" - Quantity: ")[0].trim();

            // Check if the item exists in the specified list
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ITEM + "=? AND " + COLUMN_ITEM_TYPE + "=? AND " + COLUMN_NAME + "=?", new String[]{itemName, itemType, listName});
            if (cursor.getCount() > 0) {
                // Item exists, proceed with updating quantity
                ContentValues values = new ContentValues();
                values.put(COLUMN_QUANTITY, newQuantity);
                String whereClause = COLUMN_ITEM + "=? AND " + COLUMN_NAME + "=?";
                String[] whereArgs = {itemName, listName};
                int rowsUpdated = db.update(TABLE_NAME, values, whereClause, whereArgs);
                Log.d("UpdateItem", "Updated " + rowsUpdated + " rows for item: " + itemName);
                return rowsUpdated > 0;
            } else {
                // Item does not exist in the specified list
                Log.d("UpdateItem", "Item not found in list: " + itemName);
                return false;
            }
        } catch (Exception e) {
            Log.e("UpdateItem", "Error updating item: " + e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }




    public boolean doesItemExist(String itemName, String itemType, String listName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            // Query the database to check if the item exists
            cursor = db.query(TABLE_NAME,
                    new String[]{COLUMN_ITEM},
                    COLUMN_ITEM + "=? AND " + COLUMN_ITEM_TYPE + "=? AND " + COLUMN_NAME + "=?",
                    new String[]{itemName, itemType, listName},
                    null, null, null);

            // Check if the cursor contains any rows
            return cursor.getCount() > 0;
        } finally {
            // Close the cursor and database connection
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }



    public void deleteAllItems(String listName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_NAME + "=?";
        String[] whereArgs = {listName};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }



}

