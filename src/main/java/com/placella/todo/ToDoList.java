package com.placella.todo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.content.Context;

public class ToDoList {
	private Db db;
	public ToDoList (Context context) {
		this.load(context);
	}
	public boolean load(Context context) {
		try {
			this.db = new Db(context);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public boolean add(Item item) {
		try {
			this.db.add(item);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public boolean delete(Item item) {
		try {
			this.db.delete(item);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public boolean replace(Item item) {
		return this.delete(item) && this.add(item);
	}
	public List<Item> getList() {
		List<Item> list;
		try {
			list = this.db.getList();
		} catch (Exception e) {
			list = new ArrayList<Item>();
		}
		if (list.size() > 0) {
	        Collections.sort(
	        	list,
	        	new Comparator<Item>() {
					@Override
					public int compare(Item arg0, Item arg1) {
	        			return arg0.getName().compareTo(arg1.getName());
					}
	            }
	        );
	    }
		return list;
	}
	public Item find(Object ido) {
		long id = Long.parseLong(ido.toString());
		List<Item> list = getList();
		for (Item i : list) {
			if (i.getId() == id) {
				return i;
			}
		}
		return null;
	}
	public void close() {
		try {
			this.db.close();
		} catch (Exception e) {
			// Whatever...
		}
	}
}