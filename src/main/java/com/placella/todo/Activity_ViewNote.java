package com.placella.todo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Activity_ViewNote extends Activity {
	private final Activity_ViewNote self = this;
	private Item item;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewnote);

		item = (Item) getIntent().getSerializableExtra("item");
		TextView t = (TextView) findViewById(R.id.title);
		t.setText(item.getName());
		t = (TextView) findViewById(R.id.content);
		t.setText(item.getNotecontent());
		
		update(RESPONSE.CANCELLED);
		
		Button b;
		b = (Button) findViewById(R.id.back);
		b.setOnClickListener(new OnClickListener () {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
        b = (Button) findViewById(R.id.edit_note);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putInt("action", REQUEST.EDIT);
                b.putSerializable("item", item);
                Intent intent = new Intent(self, Activity_EditNote.class);
                intent.putExtras(b);
                startActivityForResult(intent, REQUEST.EDIT);
            }
        });
        b = (Button) findViewById(R.id.delete_note);
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
    	} else if (requestCode == REQUEST.EDIT && resultCode == RESPONSE.DELETED) {
	        item = (Item) intent.getSerializableExtra("item");
    	}
		update(resultCode);
    	finish();
    }
}
