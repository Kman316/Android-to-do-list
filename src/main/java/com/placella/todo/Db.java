package com.placella.todo;
import java.util.ArrayList;
import java.util.List;
import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;

public class Db extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	 
    public Db(Context context) {
        super(context, "todo", null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
        	"CREATE TABLE list"
        	+ "("
	        	+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
	        	+ "name TEXT,"
	        	+ "type TEXT,"
	        	+ "note TEXT,"
	        	+ "lid INTEGER" // List items reference
        	+ ");"
        );
        db.execSQL(
        	"CREATE TABLE items"
        	+ "("
	        	+ "lid INTEGER,"
	        	+ "content TEXT,"
	        	+ "checked INTEGER"
        	+ ");"
        );
    }
	 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS list;");
        db.execSQL("DROP TABLE IF EXISTS items;");
	    onCreate(db);
	}

	public void delete(Item item) {
	    SQLiteDatabase db = this.getWritableDatabase();
		if (item.getType() == Item.LIST) {
		    String query = "SELECT lid FROM list WHERE id = '" + item.getId() + "'";
		    Cursor cursor = db.rawQuery(query, null);
		    long lid;
		    if (cursor.moveToFirst()) {      
		    	lid = cursor.getInt(0) + 1;                  
		    } else {
		    	lid = -1;
		    }
		    cursor.close();
	        db.execSQL("DELETE FROM items WHERE lid = '" + lid  + "';");
		}
        db.execSQL("DELETE FROM list WHERE id = '" + item.getId()  + "';");
	}

	public long add(Item item) throws Exception {
		long id, lid = -1, retval;
	    SQLiteDatabase db = this.getWritableDatabase();
	    ContentValues values = new ContentValues();
	    values.put("name", item.getName());
	    values.put("type", item.getType());
    	values.put("note", item.getNotecontent());
	    if (item.getType() != Item.NOTE) {
	    	lid = this.getNextListTableId();
	    	values.put("lid", lid);
	    }
	    id = db.insert("list", null, values);
	    if (id == -1) {
	    	throw new Exception("Can't insert into db.items");
	    } else {
	    	item.setId(id);
	    }
	    if (item.getType() == Item.LIST) {
	    	for (Item i : item.getListcontent()) {
	    	    values = new ContentValues();
	    	    values.put("lid", lid);
	    	    values.put("content", i.getName());
	    	    values.put("checked", i.getState());
	    	    retval = db.insert("items", null, values);
	    	    if (retval == -1) {
	    	    	throw new Exception("Can't insert into db.list");
	    	    }
	    	}
	    }
		return id;
	}

	private long getNextListTableId() {
	    String query = "SELECT MAX(lid) FROM items";
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(query, null);
	    long id;
	    if (cursor.moveToFirst()) {      
	    	id = cursor.getInt(0) + 1;                  
	    } else {
	    	id = 0;
	    }
	    cursor.close();
	    return id;
	}

	public List<Item> getList() {
		List<Item> list = new ArrayList<Item>();
		SQLiteDatabase db = this.getReadableDatabase();
		String query = "SELECT id, name, type, note, lid FROM list";
		Cursor c = db.rawQuery(query, null);
	    if (c.moveToFirst()) {
			do {
				Item item = new Item(
					c.getString(1),
					c.getInt(2)
				);
				List<Item> innerList = new ArrayList<Item>();
				if (c.getInt(2) == Item.LIST) {
					Cursor cc = db.rawQuery(
						"SELECT content, checked FROM items WHERE lid = '" + c.getInt(4) + "'",
						null
					);
				    if (cc.moveToFirst()) {      
						do {
							innerList.add(
								new Item(cc.getString(0), Item.NOTE).setState(cc.getInt(1))
							);
						} while (cc.moveToNext());
				    }
				    cc.close();
				}
				item.setNotecontent(c.getString(3));
				item.setId(c.getInt(0));
				list.add(item);
				item.setListcontent(innerList);
			} while (c.moveToNext());
	    }
		c.close();
	    return list;
	}
}