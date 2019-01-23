package net.sytes.kai_soft.letsbuyka.ProductModel;

import java.io.Serializable;

/**
 * Created by Лунтя on 07.04.2018.
 */

public class Product implements Serializable{

    private int id;
    private String name;
    private String description;
    private String imagePaths;

    public Product(int id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
        this.imagePaths = imagePaths;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
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

    @Override
    public String toString() {
        return getId() + ", " + getName() + ", " + getDescription();
    }
}
