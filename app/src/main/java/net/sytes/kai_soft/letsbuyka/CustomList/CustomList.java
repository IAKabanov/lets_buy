package net.sytes.kai_soft.letsbuyka.CustomList;

import java.io.Serializable;

/**
 * Created by Лунтя on 06.06.2018.
 */

public class CustomList implements Serializable {
    private long id, id_product, id_list;
    private int deprecated;
    public static final int DEPRECATED_TRUE = 1;
    public static final int DEPRECATED_FALSE = 2;
}
