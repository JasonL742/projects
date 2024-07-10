package edu.qc.seclass.grocerylistmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private List<String> groceryListNames;
    private UserListDatabaseHelper userListDatabaseHelper;

    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActionBar(false);

        // Initialize the database helper
        userListDatabaseHelper = new UserListDatabaseHelper(this);


        // Initialize the list of grocery list names
        groceryListNames = new ArrayList<>();


        // Find views by their IDs
        ListView listView = findViewById(R.id.listView);
        Button addButton = findViewById(R.id.Editbutton);

        // Initialize the list adapter
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, groceryListNames);
        listView.setAdapter(listAdapter);

        // Load existing user lists from the database
        loadUserLists();

        // Set a click listener on the list items to open the corresponding grocery list
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedListName = groceryListNames.get(position);
            openGroceryList(selectedListName);
        });

        // Set a click listener on the edit button to display options
        addButton.setOnClickListener(v -> showOptionsDialog());
    }

    private void loadUserLists() {
        List<UserList> userLists = userListDatabaseHelper.getAllUserLists();
        List<String> tempList = new ArrayList<>(); // Temporary list to hold names from the database
        for (UserList userList : userLists) {
            tempList.addAll(userList.getGroceryListNames()); // Add names from user lists to the temporary list
        }
        Log.e("loadinglists",  tempList.toString());
        // Add names from the temporary list to groceryListNames if they don't already exist
        for (String name : tempList) {
            if (!groceryListNames.contains(name)) {
                groceryListNames.add(name);
            }
        }

        Log.e("loadinglists", groceryListNames.toString());
        updateListView();
    }



    private void updateListView() {
        listAdapter.notifyDataSetChanged();
    }

    private void showOptionsDialog() {
        //  AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle("Options");

        //  String[] options = {"Create new list"};
        //   builder.setItems(options, (dialog, which) -> showCreateListDialog());

        //    builder.show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options");

        String[] options = {"Create new list", "Edit name", "Delete"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        showCreateListDialog();
                        break;
                    case 1:
                        showEditNameDialog();
                        break;
                    case 2:
                        showDeleteListDialog();
                        break;
                }
            }
        });

        builder.show();
    }

    private void showCreateListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Grocery List");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Create", (dialog, which) -> {
            String listName = input.getText().toString().trim();
            if (!listName.isEmpty()) {
                // Check if the grocery list already exists in the current list array
                if (groceryListNames.contains(listName)) {
                    // Inform the user that the list with the same name already exists
                    Toast.makeText(MainActivity.this, "Grocery list with the same name already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    // Add the new grocery list to the list and database
                    groceryListNames.add(listName);
                    listAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "New grocery list created: " + listName, Toast.LENGTH_SHORT).show();
                    saveUserListToDatabase();
                }
            } else {
                Toast.makeText(MainActivity.this, "Please enter a name for the grocery list", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    private void saveUserListToDatabase() {
        UserList userList = new UserList("My Lists", groceryListNames);
        userListDatabaseHelper.addUserList(userList);
    }

    private void openGroceryList(String listName) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("listName", listName);
        startActivity(intent);
    }
    private void showEditNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit List Name");

        ListView listView = new ListView(this);
        listView.setAdapter(listAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        builder.setView(listView);

        builder.setPositiveButton("Edit Name", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = listView.getCheckedItemPosition();
                if (position >= 0 && position < groceryListNames.size()) {
                    // Get the selected list name
                    String selectedListName = groceryListNames.get(position);

                    // Create an EditText view for user input
                    final EditText input = new EditText(MainActivity.this);
                    input.setText(selectedListName);

                    // Create a dialog to edit the list name
                    AlertDialog.Builder editBuilder = new AlertDialog.Builder(MainActivity.this);
                    editBuilder.setTitle("Edit List Name");
                    editBuilder.setView(input);
                    editBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newName = input.getText().toString().trim();
                            if (!newName.isEmpty()) {
                                // Update the list name
                                boolean updated = updateUserListInDatabase(position, newName);
                                if (updated) {
                                    // Update the list name in the UI
                                    groceryListNames.set(position, newName);
                                    listAdapter.notifyDataSetChanged();
                                    Toast.makeText(MainActivity.this, "List name updated", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Failed to update list name", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    editBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    // Show the dialog to edit the list name
                    editBuilder.show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }



    private boolean updateUserListInDatabase(int position, String newName) {
        List<UserList> userLists = userListDatabaseHelper.getAllUserLists();
        if (position >= 0 && position < userLists.size()) {
            UserList userList = userLists.get(position);
            userListDatabaseHelper.updateUserList(userList.getId(), newName);
            return true;
        }

        return false;
    }
    private void showDeleteListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete List");

        ListView listView = new ListView(this);
        listView.setAdapter(listAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        builder.setView(listView);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = listView.getCheckedItemPosition();
                if (position >= 0 && position < groceryListNames.size()) {
                    groceryListNames.remove(position);
                    listAdapter.notifyDataSetChanged();
                    deleteUserListFromDatabase(position);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void deleteUserListFromDatabase(int position) {
        List<UserList> userLists = userListDatabaseHelper.getAllUserLists();
        if (position >= 0 && position < userLists.size()) {
            UserList userList = userLists.get(position);
            userListDatabaseHelper.deleteUserList(userList.getId());
        }
    }
}