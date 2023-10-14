package com.malas.appsr.malasapp.BeanClasses

data class TakenOrderBean(val skuCode:String,val skuName:String,val categoryCode:String,val categoryName:String,val orderQty:String){
    override fun toString(): String {
        return "TakenOrderBean(skuCode='$skuCode', skuName='$skuName', categoryCode='$categoryCode', categoryName='$categoryName', orderQty='$orderQty')"
    }
}
