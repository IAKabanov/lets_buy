package net.sytes.kai_soft.letsbuyka.Trash;

import java.io.Serializable;

/**
 * Created by Лунтя on 06.06.2018.
 */

public class List1 implements Serializable {
    private int id;
    private String itemName;

    public List1(int id, String itemName){
        this.id = id;
        this.itemName = itemName;
    }

    public int getId(){
        return this.id;
    }

    public String getItemName(){
        return this.itemName;
    }
    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    @Override
    public String toString() {
        return getId() + ", " + getItemName();
    }
}
