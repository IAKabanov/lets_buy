package net.sytes.kai_soft.letsbuyka.ProductModel;

import java.util.ArrayList;

/**
 * Created by Лунтя on 07.04.2018.
 */

public class Product {

    private long id;
    private String itemName;
    private String description;
    private String imagePaths;

    public Product(long id, String itemName, String description, String imagePaths){
        this.id = id;
        this.itemName = itemName;
        this.description = description;
        this.imagePaths = imagePaths;
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

    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    public String getImagePaths(){
        return this.imagePaths;
    }
    public String getFirstImagePath(){
        return this.imagePaths;
    }
    public void setImagePaths(String imagePaths){
        this.imagePaths = imagePaths;
    }



}
