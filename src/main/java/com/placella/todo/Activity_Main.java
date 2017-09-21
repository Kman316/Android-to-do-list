package com.placella.todo;

import java.util.List;

import android.os.*;
import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewGroup.LayoutParams;
import android.view.View.*;
import android.widget.*;

public class Activity_Main extends Activity {
	private final ToDoList todo = new ToDoList(this);
	private final Activity_Main self = this;
	private final int dataListId = 0x00ffff00;
    private float scale;
	private Item currentItem;
    private List<Item> mainList;
    private Button sync;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        scale = self.getResources().getDisplayMetrics().density;
		super.onCreate(savedInstanceState);
		LinearLayout l = new LinearLayout(self);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setLayoutParams(
        	new LayoutParams(
        		LayoutParams.MATCH_PARENT,
        		LayoutParams.MATCH_PARENT
        	)
        );
        l.setPadding(10, 10, 10, 10);
        
        TextView t = new TextView(self);
        t.setText("To Do List App");
        t.setTextSize(20);
        t.setLayoutParams(
        	new LayoutParams(
        		LayoutParams.MATCH_PARENT,
        		LayoutParams.WRAP_CONTENT
        	)
        );
        t.setGravity(Gravity.CENTER);
        t.setPadding(0, 0, 0, 10);
        l.addView(t);

        LinearLayout ll = new LinearLayout(self);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        l.addView(ll);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT,
            1.0f
        );

        Button button = new Button(self);
        button.setLayoutParams(params);
        button.setText("Add");
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putInt("action", REQUEST.ADD);
                Intent intent = new Intent(self, Activity_Add.class);
                intent.putExtras(b);
                startActivityForResult(intent, REQUEST.ADD);
            }
        });
        ll.addView(button);

        sync = new Button(self);
        sync.setLayoutParams(params);
        sync.setText("Synchronise");
        sync.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(self, "You will be notified when the synchronisation has completed", Toast.LENGTH_SHORT).show();
                Synchronise.start(self, todo.getList());
            }
        });
        ll.addView(sync);
        
        ScrollView s = new ScrollView(self);
        s.setLayoutParams(
        	new LayoutParams(
        		LayoutParams.MATCH_PARENT,
        		LayoutParams.MATCH_PARENT
        	)
        );
        s.addView(getList());
    	l.addView(s);

        if (mainList.isEmpty()) {
            sync.setVisibility(View.GONE);
        } else {
            sync.setVisibility(View.VISIBLE);
        }
        
		setContentView(l);
	}
	
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	if (requestCode == REQUEST.EDIT && resultCode == RESPONSE.MODIFIED) {
	        Item item = (Item) intent.getSerializableExtra("item");
	    	if (todo.replace(item)) {
	    		refresh();
	    	} else {
	    		new Dialog_Message(self, "A fatal database error has occurred").show();
	    	}
    	} else if (requestCode == REQUEST.EDIT && resultCode == RESPONSE.DELETED) {
	        Item item = (Item) intent.getSerializableExtra("item");
	        if (todo.delete(item)) {
	    		refresh();
	    	} else {
	    		new Dialog_Message(self, "A fatal database error has occurred").show();
	    	}
    	} else if (requestCode == REQUEST.ADD && resultCode == RESPONSE.ADDED) {
	        Item item = (Item) intent.getSerializableExtra("item");
	        if (todo.add(item)) {
	    		refresh();
	    	} else {
	    		new Dialog_Message(self, "A fatal database error has occurred").show();
	    	}
    	}
    }
    
	public LinearLayout getList() {
		TextView t;
		mainList = todo.getList();
		LinearLayout l = new LinearLayout(self);
		l.setId(dataListId);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setLayoutParams(
        	new LayoutParams(
        		LayoutParams.MATCH_PARENT,
        		LayoutParams.MATCH_PARENT
        	)
        );
		if (mainList.size() > 0) {
			Util.hr(l, this);
			for (Item i : mainList) {
				LinearLayout innerLayout = new LinearLayout(self);
				innerLayout.setOrientation(LinearLayout.HORIZONTAL);
				innerLayout.setTag(i.getId());

				ImageView img = new ImageView(self);
				if (i.getType() == Item.NOTE) {
					img.setImageDrawable(getResources().getDrawable(R.drawable.ic_note));
				} else {
					img.setImageDrawable(getResources().getDrawable(R.drawable.ic_list));
				}
				innerLayout.addView(img);
				
				TextView b = new TextView(this);
				String name = i.getName();
				if (name.length() == 0) {
					name = "{No name}";
				}
				b.setText(name);
	            b.setTextSize(18);
	            b.setPadding(
                    (int) (5 * scale + 0.5f),
                    (int) (12 * scale + 0.5f),
                    (int) (5 * scale + 0.5f),
                    (int) (12 * scale + 0.5f)
                );

                innerLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						viewItem(todo.find(arg0.getTag()));
					}
				});
	            registerForContextMenu(innerLayout);
	            innerLayout.addView(b);
	            l.addView(innerLayout);
				Util.hr(l, this);
			}
		} else {
			t = new TextView(self);
			t.setText("No Items found, click \"Add\" to create a new item");
			l.addView(t);
		}
		return l;
	}
	
	@Override
    public void onPause() {
        super.onPause();
    	this.todo.close();
    }
    
    public void refresh() {
	    LinearLayout l = (LinearLayout) findViewById(dataListId);
	    l.removeAllViews();
	    
        ScrollView s = new ScrollView(self);
        s.setLayoutParams(
        	new LayoutParams(
        		LayoutParams.MATCH_PARENT,
        		LayoutParams.MATCH_PARENT
        	)
        );
        s.addView(getList());
    	l.addView(s);
        if (mainList.isEmpty()) {
            sync.setVisibility(View.GONE);
        } else {
            sync.setVisibility(View.VISIBLE);
        }
    }
    
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {  
    	super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.context_main, menu);
        currentItem = todo.find(v.getTag());
    }  
    
    @Override  
    public boolean onContextItemSelected(MenuItem item) {  
        if(item.getItemId() == R.id.menu_view){
			viewItem(currentItem);
        	return true;  
        } else if(item.getItemId() == R.id.menu_edit){
    		Class<?> target;
    		if (currentItem.getType() == Item.NOTE) {
    			target = Activity_EditNote.class;
    		} else {
    			target = Activity_EditList.class;
    		}
	        Bundle b = new Bundle();
	        b.putInt("action", REQUEST.EDIT);
	        b.putSerializable("item", currentItem);
	        Intent intent = new Intent(self, target);
	        intent.putExtras(b);
	        startActivityForResult(intent, REQUEST.EDIT);
        	return true;  
        } else if(item.getItemId() == R.id.menu_delete){
			new Dialog_Confirm(self, "Are you sure you want to delete this item?", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					todo.delete(currentItem);
					refresh();
				}
			}).show();
        	return true;  
        } else {
        	return false;
        }  
    }  
    
    public void viewItem(Item i) {
		Class<?> target;
		if (i.getType() == Item.NOTE) {
			target = Activity_ViewNote.class;
		} else {
			target = Activity_ViewList.class;
		}
        Bundle b = new Bundle();
        b.putInt("action", REQUEST.EDIT);
        b.putSerializable("item", i);
        Intent intent = new Intent(self, target);
        intent.putExtras(b);
        startActivityForResult(intent, REQUEST.EDIT);
    }
}
