package edu.qc.seclass.grocerylistmanager;


import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ArrayAdapter<String> listAdapter;
    private GroceryListDatabaseHelper dbHelper;

    private String currListName;
    private ListView listView;
    private  ArrayList<String> groceryItems;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grocery_list);

        // Initialize database helper
        dbHelper = new GroceryListDatabaseHelper(this);

        // Set up the list view, textview, and button.
        listView = findViewById(R.id.listView);
        Button addButton = findViewById(R.id.addButton);
        TextView title = findViewById(R.id.textViews);
        Button backButton = findViewById(R.id.back_button);
        String selectedListName = getIntent().getStringExtra("listName");
        currListName = selectedListName;
        title.setText(selectedListName);

        // Create the list of shopping items
        groceryItems = new ArrayList<>();

        // Setup the list adapter.
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, groceryItems);
        listView.setAdapter(listAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); // Enable multi-selection mode

        updateListView();
        Log.e("check", String.valueOf(groceryItems.size()));
        // Create a click listener for the add button.
        addButton.setOnClickListener(v -> showOptionsDialog());

        // Set a long click listener for the list items to handle item long press
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);
            showItemOptionsDialog(selectedItem);
            return true; // Return true if the long click is consumed, false otherwise
        });

        // Set an item click listener to toggle the check status
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);
            dbHelper.toggleItemCheck(selectedItem, currListName);
            updateListView(); // Update the list view after toggling the check status
        });
        for (int i = 0; i < groceryItems.size(); i++) {
           listView.setItemChecked(i, getCheckStatusFromDatabase(i));
            Log.e("check", String.valueOf(groceryItems.size()));
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Or any other action to navigate back
            }
        });
        updateListView();
    }


    private boolean getCheckStatusFromDatabase(int i) {
        boolean x;
        String itemNameWithQuantity = groceryItems.get(i);
        String[] parts = itemNameWithQuantity.split(" - Type: ");
        if (parts.length != 2) {
            // Invalid format, return false
            Log.e("toggleItemCheck", "Invalid format: " + itemNameWithQuantity);
        }

        String[] nameAndType = parts[1].split(" - Quantity: ");
        if (nameAndType.length != 2) {
            // Invalid format, return false
            Log.e("toggleItemCheck", "Invalid format: " + itemNameWithQuantity);
        }

        String itemName = parts[0].trim();
        String itemType = parts[1].split(" - Quantity: ")[0].trim();

        x = dbHelper.getCheckValue(itemName,itemType,currListName).equals("1");
        updateListView();
        return x;


    }


    private void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options");

        String[] options = {"Add New Item", "Delete List Items", "Clear all checkmarks"};
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    addGroceryItem();
                    break;
                case 1:
                    showDeleteListItemsDialog();
                    break;

                case 2:
                    clearAllCheck();
                    break;
            }
        });

        builder.show();
    }

    private void clearAllCheck(){
        dbHelper.uncheckAllItemsInList(currListName);
        for (int i = 0; i < groceryItems.size(); i++) {
            listView.setItemChecked(i, getCheckStatusFromDatabase(i));
            Log.e("check", String.valueOf(groceryItems.size()));
        }
    }



    private void showDeleteListItemsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All Items");
        builder.setMessage("Are you sure you want to delete all items from the grocery list?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Delete all items from the database associated with the list
            dbHelper.deleteAllItems(currListName);

            // Clear the list of grocery items
            groceryItems.clear();

            // Update the list view to reflect the changes
            updateListView();

            Toast.makeText(ListActivity.this, "All items deleted", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private List<String> getExistingItemNames() {
        List<String> itemNames = new ArrayList<>();
        Cursor cursor = dbHelper.getAllItems();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String itemName = cursor.getString(cursor.getColumnIndex("item"));
                if (!itemNames.contains(itemName)) { // Check if the item name is not already in the list
                    itemNames.add(itemName);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return itemNames;
    }


    private List<String> getExistingTypes() {
        List<String> Types = new ArrayList<>();
        Cursor cursor = dbHelper.getAllItems();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String typeName = cursor.getString(cursor.getColumnIndex("itemType"));
                if (!Types.contains(typeName)) {
                    Types.add(typeName);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return Types;
    }


    private void addGroceryItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Item");

        // Inflate the layout for the dialog from XML
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_grocery_item, null);
        builder.setView(dialogView);

        // Get references to views in the inflated layout
        AutoCompleteTextView itemTypeInput = dialogView.findViewById(R.id.item_type_input);
        AutoCompleteTextView itemNameInput = dialogView.findViewById(R.id.item_name_input);
        EditText quantityInput = dialogView.findViewById(R.id.quantity_input);
        Button nextButton = dialogView.findViewById(R.id.next_button);

        // Set threshold for item type input and show suggestions when focused
        itemTypeInput.setThreshold(0);
        itemTypeInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                itemTypeInput.showDropDown(); // Show suggestions when focused
            }
        });

        // Set threshold for item name input
        itemNameInput.setThreshold(1);

        // Set up adapter for AutoCompleteTextView for item type
        ArrayAdapter<String> itemTypeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, getExistingTypes());
        itemTypeInput.setAdapter(itemTypeAdapter);

        // Set up adapter for AutoCompleteTextView for item name
        ArrayAdapter<String> itemNameAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, getExistingItemNames());
        itemNameInput.setAdapter(itemNameAdapter);

        // Set positive and negative buttons
        builder.setPositiveButton("Add", null); // We'll handle this later
        builder.setNegativeButton("Cancel", (dialogInterface, which) -> dialogInterface.dismiss());

        AlertDialog dialog = builder.create();

        // Set click listener for "Add" button after dialog creation
        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String newItemName = itemNameInput.getText().toString().trim();
                String quantityString = quantityInput.getText().toString().trim();
                String selectedItemType = itemTypeInput.getText().toString().trim();
                int quantity = quantityString.isEmpty() ? 1 : Integer.parseInt(quantityString); // Default to 1 if quantity is empty
                if (!newItemName.isEmpty() && !selectedItemType.isEmpty()) {
                    addGroceryItemToList(newItemName, quantity, selectedItemType);
                    dialog.dismiss();
                } else {
                    Toast.makeText(ListActivity.this, "Please enter an item name and select an item type", Toast.LENGTH_SHORT).show();
                }
            });

            // Set click listener for "Next" button
            nextButton.setOnClickListener(v -> {
                if (itemNameInput.getVisibility() == View.GONE) {
                    itemNameInput.setVisibility(View.VISIBLE); // Show item name input
                    itemNameInput.requestFocus(); // Move focus to item name input
                } else if (quantityInput.getVisibility() == View.GONE) {
                    quantityInput.setVisibility(View.VISIBLE); // Show quantity input
                    quantityInput.requestFocus(); // Move focus to quantity input
                    nextButton.setVisibility(View.GONE); // Hide "Next" button after showing quantity input
                }
            });
        });

        // Set OnFocusChangeListener for item name input to show "Next" button when focused
        itemNameInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                nextButton.setVisibility(View.VISIBLE); // Show "Next" button when focused on item name input
            }
        });

        // Set OnDismissListener to reset visibility when dialog is dismissed
        dialog.setOnDismissListener(dialogInterface -> {
            itemNameInput.setVisibility(View.GONE); // Hide item name input
            quantityInput.setVisibility(View.GONE); // Hide quantity input
        });

        dialog.show();
    }


    public void addGroceryItemToList(String item, int quantity, String type) {
        // Check if the item already exists in the list
        if (dbHelper.doesItemExist(item, type, currListName)) {
            Toast.makeText(this, "Item '" + item + "' already exists in the list", Toast.LENGTH_SHORT).show();
            return;
        }

        // Item doesn't exist, add it to the list
        dbHelper.addItem(item, quantity, type , currListName );
        Toast.makeText(this, "Item added to the list: " + item, Toast.LENGTH_SHORT).show();
        updateListView();
        for (int i = 0; i < groceryItems.size(); i++) {
            listView.setItemChecked(i, getCheckStatusFromDatabase(i));
            Log.e("check", String.valueOf(groceryItems.size()));
        }
    }


    private void updateListView() {
        groceryItems.clear();
        Cursor cursor = dbHelper.getItemsForList(currListName); // Fetch items for the current list name

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String itemName = cursor.getString(cursor.getColumnIndex("item"));
                int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                String itemType = cursor.getString(cursor.getColumnIndex("itemType")); // Assuming "type" is the column name for item type
                String itemWithQuantity = itemName + " - Type: " + itemType + " - Quantity: " + quantity;
                groceryItems.add(itemWithQuantity);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Sort the grocery items by type
        sortGroceryItemsByType();
        listAdapter.notifyDataSetChanged();

    }
    private void sortGroceryItemsByType() {
        // Sort the groceryItems2 list by item type
        Collections.sort(groceryItems, (item1, item2) -> {
            String type1 = item1.split(" - Type: ")[1];
            String type2 = item2.split(" - Type: ")[1];
            return type1.compareTo(type2);
        });
        listAdapter.notifyDataSetChanged();
    }
    private void showItemOptionsDialog(String itemName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options");

        String[] options = {"Edit Quantity", "Delete Item"};
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    showEditItemQuantityDialog(itemName);
                    break;
                case 1:
                    deleteItem(itemName);
                    break;
            }
        });

        builder.show();
    }

    private void showEditItemQuantityDialog(String itemNameWithQuantity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Quantity");

        // Extract item quantity from the itemNameWithQuantity string
        String[] parts = itemNameWithQuantity.split(" - Quantity: ");
        String itemName = parts[0];
        String quantityString = parts[1];

        // Create a layout to hold the EditTexts
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // EditText for the new quantity
        final EditText quantityInput = new EditText(this);
        quantityInput.setHint("New Quantity");
        quantityInput.setInputType(InputType.TYPE_CLASS_NUMBER); // Set input type to numeric
        quantityInput.setText(quantityString); // Pre-fill the EditText with the current quantity
        layout.addView(quantityInput);

        // Add the layout to the dialog
        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
           // String updatedItem = itemNameInput.getText().toString().trim();
            String newQuantityString = quantityInput.getText().toString().trim();
            if (!newQuantityString.isEmpty()) {
                int newQuantity = Integer.parseInt(newQuantityString);
                changeItemQuantity(itemNameWithQuantity, newQuantity); // Call method to edit the item
            } else {
                Toast.makeText(ListActivity.this, "Please enter a valid item quantity", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void deleteItem(String itemName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Item");
        builder.setMessage("Are you sure you want to delete this item?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            dbHelper.deleteItem(itemName, currListName);
            updateListView();
            Toast.makeText(ListActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void changeItemQuantity(String oldName, int quantity) {
        dbHelper.updateItemQuantity(oldName, quantity, currListName);
        updateListView();
        for (int i = 0; i < groceryItems.size(); i++) {
            listView.setItemChecked(i, getCheckStatusFromDatabase(i));
            Log.e("check", String.valueOf(groceryItems.size()));
        }
        Toast.makeText(ListActivity.this, "Item Quantity Changed", Toast.LENGTH_SHORT).show();
    }


}

