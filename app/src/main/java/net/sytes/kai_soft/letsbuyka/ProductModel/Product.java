package net.sytes.kai_soft.letsbuyka.ProductModel;

import android.support.annotation.NonNull;
import android.util.Log;

import net.sytes.kai_soft.letsbuyka.DataBase;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

/**
 * Created by Лунтя on 07.04.2018.
 */

public class Product implements Serializable{

    private static String tagID = "id";
    private static String tagName = "name";
    private static String tagDescription = "description";
    private static String myTag = "letsbuy_product";

    private int id;
    private String name;
    private String description;
    //private String imagePaths;

    public Product(int id, String name, String description){
        Log.i(myTag, "Product(" + id + ", " + name + ", " + description + ")");
        this.id = id;
        this.name = name;
        this.description = description;
        //this.imagePaths = imagePaths;
    }

    public Product(Product product){
        Log.i(myTag, "Product(product)");
        this.id = product.id;
        this.name = product.name;
        this.description = product.description;
        //this.imagePaths = imagePaths;
    }

    public Product(){
        Log.i(myTag, "Product(product)");
        this.id = 0;
        this.name = "";
        this.description = "";
        //this.imagePaths = imagePaths;
    }

    public Product(@Nullable Dictionary<String, Object> dictionary){
        if (dictionary != null){
            this.id = (int) dictionary.get(tagID);
            this.name = dictionary.get(tagName).toString();
            this.description = dictionary.get(tagDescription).toString();
        }
    }

    public int getId(){
        Log.i(myTag, "getId()");
        return this.id;
    }

    public String getName(){
        Log.i(myTag, "getName()");
        return this.name;
    }
    public void setName(String name){
        Log.i(myTag, "setName(" + name + ")");
        this.name = name;
    }

    public String getDescription(){
        Log.i(myTag, "getDescription()");
        return this.description;
    }
    public void setDescription(String description){
        Log.i(myTag, "getDescription(" + description + ")");
        this.description = description;
    }

    /*public String getImagePaths(){
        return this.imagePaths;
    }
    public String getFirstImagePath(){
        return this.imagePaths;
    }
    public void setImagePaths(String imagePaths){
        this.imagePaths = imagePaths;
    }*/

    public Map<String, Object> toMap(){
        Log.i(myTag, "toDictionary()");
        Map<String, Object> product;
        product = new Map<String, Object>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean containsKey(Object key) {
                return false;
            }

            @Override
            public boolean containsValue(Object value) {
                return false;
            }

            @Override
            public Object get(Object key) {
                return null;
            }

            @Override
            public Object put(String key, Object value) {
                return null;
            }

            @Override
            public Object remove(Object key) {
                return null;
            }

            @Override
            public void putAll(@NonNull Map<? extends String, ?> m) {

            }

            @Override
            public void clear() {

            }

            @NonNull
            @Override
            public Set<String> keySet() {
                return null;
            }

            @NonNull
            @Override
            public Collection<Object> values() {
                return null;
            }

            @NonNull
            @Override
            public Set<Entry<String, Object>> entrySet() {
                return null;
            }
        };
        product.put(tagID, id);
        product.put(tagName, name);
        product.put(tagDescription, description);
        return product;
    }

    @Override
    public String toString() {
        Log.i(myTag, "toString()");
        return getId() + ", " + getName() + ", " + getDescription();
    }
}
