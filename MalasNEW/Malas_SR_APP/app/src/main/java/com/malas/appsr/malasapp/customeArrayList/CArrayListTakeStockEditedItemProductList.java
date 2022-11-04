package com.malas.appsr.malasapp.customeArrayList;

import com.malas.appsr.malasapp.BeanClasses.TakeStockEditedItemProductList;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Krishna on 11/20/2016.
 */

public class CArrayListTakeStockEditedItemProductList extends ArrayList<TakeStockEditedItemProductList> implements Serializable{

    @Override
    public boolean add(TakeStockEditedItemProductList object) {
        for(int i=0;i<size();i++){
            if(object.getItem_id().equalsIgnoreCase(get(i).getItem_id())){
                get(i).setItem_qty(object.getItem_qty());
                return true;
            }
        }
        return super.add(object);
    }
}
