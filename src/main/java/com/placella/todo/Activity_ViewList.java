package com.placella.todo;

import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Activity_ViewList extends Activity {
	private final Activity_ViewList self = this;
	private Item item;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewlist);

		item = (Item) getIntent().getSerializableExtra("item");
		TextView t = (TextView) findViewById(R.id.title);
		t.setText(item.getName());
		
		TableLayout table = (TableLayout) findViewById(R.id.content);
		int count = 0;
		for (Item i : item.getListcontent()) {
			TableRow tr = new TableRow(self);
			CheckBox x = new CheckBox(self);
			if (i.getState() == Item.CHECKED) {
				x.setChecked(true);
			}
			x.setTag(count);
			x.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean newState) {
					List<Item> list = item.getListcontent();
					Item i = list.get(Integer.parseInt(arg0.getTag().toString()));
					if (newState) {
						i.setState(Item.CHECKED);
					} else {
						i.setState(Item.UNCHECKED);
					}
					item.setListcontent(list);
					update(RESPONSE.MODIFIED);
				}
			});
			tr.addView(x);
			t = new TextView(self);
			t.setPadding(10, 5, 10, 5);
			t.setText(i.getName());
			t.setTextSize(18);
			tr.addView(t);
			table.addView(tr);
			count++;
		}
		
		update(RESPONSE.CANCELLED);
		
		Button b;
		b = (Button) findViewById(R.id.back);
		b.setOnClickListener(new OnClickListener () {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
        b = (Button) findViewById(R.id.edit_list);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putInt("action", REQUEST.EDIT);
                b.putSerializable("item", item);
                Intent intent = new Intent(self, Activity_EditList.class);
                intent.putExtras(b);
                startActivityForResult(intent, REQUEST.EDIT);
            }
        });
        b = (Button) findViewById(R.id.delete_list);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialog_Confirm(self, "Are you sure you want to delete this item?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        update(RESPONSE.DELETED);
                        finish();
                    }
                }).show();
            }
        });
	}
	
    /**
	* Saves the current input data in order to later
	* pass it on to the calling activity
	*/
    private void update(int i) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("item", item);
        setResult(i, returnIntent);
    }
	
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	if (requestCode == REQUEST.EDIT && resultCode == RESPONSE.MODIFIED) {
	        item = (Item) intent.getSerializableExtra("item");
    		update(resultCode);
    		finish();
    	} else if (requestCode == REQUEST.EDIT && resultCode == RESPONSE.DELETED) {
	        item = (Item) intent.getSerializableExtra("item");
    		update(resultCode);
    		finish();
    	}
    }
}
