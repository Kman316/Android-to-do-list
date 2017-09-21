package com.placella.todo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;

public class Activity_Notification extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    String response = getIntent().getStringExtra("response");
	    boolean success = getIntent().getBooleanExtra("success", false);
	    
	    LinearLayout l = new LinearLayout(this);
	    l.setOrientation(LinearLayout.VERTICAL);
        l.setLayoutParams(
        	new LayoutParams(
        		LayoutParams.MATCH_PARENT,
        		LayoutParams.MATCH_PARENT
        	)
        );
        l.setPadding(10, 10, 10, 10);
            
	    TextView t = new TextView(this);
        t.setLayoutParams(
        	new LayoutParams(
        		LayoutParams.MATCH_PARENT,
        		LayoutParams.WRAP_CONTENT
        	)
        );
        t.setGravity(Gravity.CENTER);
	    if (success) {
	    	t.setText("Synchronisation Successful");
	    } else {
	    	t.setText("Synchronisation Failed");
	    }
	    t.setTextSize(22);
	    l.addView(t);
	    t = new TextView(this);
        t.setLayoutParams(
        	new LayoutParams(
        		LayoutParams.MATCH_PARENT,
        		LayoutParams.WRAP_CONTENT
        	)
        );
	    t.setText(response);
	    l.addView(t);
	    setContentView(l);
	}
}
