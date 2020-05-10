package com.jpventura.core.android.ui

import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

abstract class RecyclerViewArrayAdapter<T, VH : RecyclerView.ViewHolder>
/**
 * Constructor
 *
 * @param objects The objects to represent in the ListView.
 */
@JvmOverloads constructor(
    private val objects: ArrayList<T> = arrayListOf()
) : RecyclerView.Adapter<VH>() {

    fun getItem(position: Int): T {
        return objects[position]
    }

    override fun getItemCount(): Int {
        return objects.size
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     *
     * @return The position of the specified item.
     */
    fun getPosition(item: T): Int {
        return objects.indexOf(item)
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param item The object to add at the end of the array.
     */
    fun add(item: T) {
        synchronized(this) {
            objects.add(item)
            notifyDataSetChanged()
        }
    }

    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param collection The Collection to add at the end of the array.
     * @throws ClassCastException if the class of an element of the specified
     * collection prevents it from being added to this list
     * @throws NullPointerException if the specified collection contains one
     * or more null elements and this list does not permit null
     * elements, or if the specified collection is null
     * @throws IllegalArgumentException if some property of an element of the
     * specified collection prevents it from being added to this list
     */
    fun addAll(items: Collection<T>) {
        synchronized(this) {
            val from = objects.size
            objects.addAll(items)
            notifyItemRangeChanged(from, items.size)
        }
    }

    /**
     * Adds the specified items at the end of the array.
     *
     * @param items The items to add at the end of the array.
     */
    fun addAll(vararg items: T) {
        synchronized(this) {
            val from = objects.size
            objects.addAll(items)
            notifyItemRangeChanged(from, items.size)
        }
    }

    /**
     * Remove all elements from the list.
     *
     */
    fun clear() {
        synchronized(this) {
            objects.clear()
            notifyDataSetChanged()
        }
    }

    /**
     * Inserts the specified object at the specified index in the array.
     *
     * @param item The object to insert into the array.
     * @param index The index at which the object must be inserted.
     */
    fun insert(item: T, index: Int) {
        synchronized(this) {
            objects.add(index, item)
            notifyDataSetChanged()
        }
    }

    /**
     * Removes the specified object from the array.
     *
     * @param item The object to remove.
     */
    fun remove(item: T) {
        synchronized(this) {
            objects.remove(item)
            notifyDataSetChanged()
        }
    }
}
