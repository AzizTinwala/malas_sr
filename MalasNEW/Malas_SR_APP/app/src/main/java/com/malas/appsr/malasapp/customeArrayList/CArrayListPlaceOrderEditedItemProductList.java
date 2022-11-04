package com.malas.appsr.malasapp.customeArrayList;

import com.malas.appsr.malasapp.BeanClasses.PlaceOrderEditedItemProductList;

import java.util.ArrayList;

public class CArrayListPlaceOrderEditedItemProductList extends ArrayList<PlaceOrderEditedItemProductList> {

    @Override
    public boolean add(PlaceOrderEditedItemProductList object) {
        for (int i = 0; i < size(); i++) {
            if (object.getItem_id().equalsIgnoreCase(get(i).getItem_id())) {
                get(i).setItem_qty(object.getItem_qty());
                return true;
            }
        }
        return super.add(object);
    }
}
