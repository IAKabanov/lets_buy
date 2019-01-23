package net.sytes.kai_soft.letsbuyka.Lists;

import java.io.Serializable;

/**
 * Created by Лунтя on 06.06.2018.
 */

public class List implements Serializable {
    private int id;
    private String itemName;

    public List(int id, String itemName){
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
