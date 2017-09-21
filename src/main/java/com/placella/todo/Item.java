package com.placella.todo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Item implements Serializable {
	private static final long serialVersionUID = 5877839774997086815L;
	private static int ai = 0;
	public final static int NOTE = 0;
	public final static int LIST = 1;
	public final static int UNCHECKED = 0;
	public final static int CHECKED = 1;
	private int type;
	private String name;
	private long id;
	private List<Item> listcontent;
	private String notecontent;
	private int state;
	public Item(String name, int type) {
		this.setState(UNCHECKED);
		this.setType(type);
		this.setName(name);
		this.setId(Item.ai++);
		this.setListcontent(new ArrayList<Item>(0));
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public List<Item> getListcontent() {
		return listcontent;
	}
	public void setListcontent(List<Item> listcontent) {
		this.listcontent = listcontent;
	}
	public String getNotecontent() {
		return notecontent;
	}
	public Item setNotecontent(String notecontent) {
		this.notecontent = notecontent;
		return this;
	}
	public int getState() {
		return state;
	}
	public Item setState(int state) {
		this.state = state;
		return this;
	}
}
