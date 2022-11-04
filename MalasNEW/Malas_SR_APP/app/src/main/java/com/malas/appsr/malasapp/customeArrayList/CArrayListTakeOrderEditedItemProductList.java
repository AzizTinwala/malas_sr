package com.malas.appsr.malasapp.customeArrayList;

import com.malas.appsr.malasapp.BeanClasses.TakeOrderEditedItemProductList;

import java.util.ArrayList;

public class CArrayListTakeOrderEditedItemProductList extends ArrayList<TakeOrderEditedItemProductList> {

    @Override
    public boolean add(TakeOrderEditedItemProductList object) {
        for (int i = 0; i < size(); i++) {
            if (object.getItem_id().equalsIgnoreCase(get(i).getItem_id())) {
                get(i).setItem_qty(object.getItem_qty());
                get(i).setProduct_name(object.getProduct_name());
                get(i).setCatId(object.getCatId());
                get(i).setCatName(object.getCatName());

                get(i).setOutletId(object.getOutletId());
                get(i).setOrder_uni_id(object.getOrder_uni_id());
                return true;
            }
        }
        return super.add(object);
    }
}
