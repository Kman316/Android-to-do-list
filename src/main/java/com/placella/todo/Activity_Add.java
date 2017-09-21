package com.placella.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;

public class Activity_Add extends Activity {
	Activity_Add self = this;
	private Item item;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		Button b = (Button) findViewById(R.id.ok);
		b.setOnClickListener(new OnClickListener () {
			@Override
			public void onClick(View arg0) {
				RadioGroup r = (RadioGroup) findViewById(R.id.type);
				if (r.getCheckedRadioButtonId() == R.id.note) {
			        Bundle b = new Bundle();
			        b.putInt("action", REQUEST.ADD);
			        item = new Item("", Item.NOTE);
			        b.putSerializable("item", item);
			        Intent intent = new Intent(self, Activity_EditNote.class);
			        intent.putExtras(b);
			        startActivityForResult(intent, REQUEST.ADD);
				} else {
			        Bundle b = new Bundle();
			        b.putInt("action", REQUEST.ADD);
			        item = new Item("", Item.LIST);
			        b.putSerializable("item", item);
			        Intent intent = new Intent(self, Activity_EditList.class);
			        intent.putExtras(b);
			        startActivityForResult(intent, REQUEST.ADD);
				}
			}
		});
		update(RESPONSE.CANCELLED);
	}
	
    private void update(int i) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("item", item);
        setResult(i, returnIntent);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	if (requestCode == REQUEST.EDIT && resultCode == RESPONSE.MODIFIED) {
	        item = (Item) intent.getSerializableExtra("item");
    	} else if (requestCode == REQUEST.ADD && resultCode == RESPONSE.ADDED) {
	        item = (Item) intent.getSerializableExtra("item");
		}
		update(resultCode);
		finish();
    }

}
