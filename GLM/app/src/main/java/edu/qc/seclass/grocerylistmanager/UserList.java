package edu.qc.seclass.grocerylistmanager;

import java.util.List;

public class UserList {
    private int id;
    private String name;
    private List<String> groceryListNames;

    public UserList(String name, List<String> groceryListNames) {
        this.name = name;
        this.groceryListNames = groceryListNames;
    }

    public UserList(int id, String name, List<String> groceryListNames) {
        this.id = id;
        this.name = name;
        this.groceryListNames = groceryListNames;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGroceryListNames() {
        return groceryListNames;
    }


}