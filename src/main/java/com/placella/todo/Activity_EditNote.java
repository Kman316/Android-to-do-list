package com.placella.todo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Activity_EditNote extends Activity implements Savable {
    private final Activity_EditNote self = this;
    private Item item;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnote);

        item = (Item) getIntent().getSerializableExtra("item");
        mode = getIntent().getIntExtra("action", 0);

        update(RESPONSE.CANCELLED);

        EditText et;
        et = (EditText) findViewById(R.id.title);
        if (mode == REQUEST.EDIT) {
            et.setText(item.getName());
        }
        et.addTextChangedListener(new NoteTextWatcher(self));
        et = (EditText) findViewById(R.id.content);
        if (mode == REQUEST.EDIT) {
            et.setText(item.getNotecontent());
        }
        et.addTextChangedListener(new NoteTextWatcher(self));

        if (mode == REQUEST.ADD) {
            TextView t = (TextView) findViewById(R.id.heading);
            t.setText("Add Note");
        }

        Button b;
        b = (Button) findViewById(R.id.ok);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mode == REQUEST.ADD) {
                    update(RESPONSE.ADDED);
                } else {
                    update(RESPONSE.MODIFIED);
                }
                finish();
            }
        });
        b = (Button) findViewById(R.id.cancel);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }

    public void save() {
        TextView tv;
        tv = (TextView) findViewById(R.id.title);
        item.setName(tv.getText().toString());
        tv = (TextView) findViewById(R.id.content);
        item.setNotecontent(tv.getText().toString());
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
}