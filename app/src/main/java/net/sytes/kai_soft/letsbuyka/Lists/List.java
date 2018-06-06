package net.sytes.kai_soft.letsbuyka.Lists;

import java.io.Serializable;

/**
 * Created by Лунтя on 06.06.2018.
 */

public class List implements Serializable {
    private long id;
    private String itemName;

    public List(long id, String itemName){
        this.id = id;
        this.itemName = itemName;
    }

    public long getId(){
        return this.id;
    }

    public String getItemName(){
        return this.itemName;
    }
    public void setItemName(String itemName){
        this.itemName = itemName;
    }

}
