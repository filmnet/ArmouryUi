package dev.armoury.android.data

abstract class ArmouryListRowModel {

    abstract val type : Int

    abstract val id : Any

    //  Will be used in Grid layouts
    open val spanSize = 1

    //  Will be used in list with sticky headers
    open val headerId : Any? = null

}