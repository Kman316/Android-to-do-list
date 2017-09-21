package com.placella.todo;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

/**
 * A class to show the user a simple
 * pop-up dialog with a message
 *
 * @author Rouslan Placella
 */
public class Dialog_Confirm extends Dialog_Message {
    /**
     * Creates a new dialog builder and sets the message
     *
     * @param context Context for the dialog
     * @param message A reference to the string resource to display
     * @param callback What to do when user presses OK
     */
    public Dialog_Confirm(Context context, int message, OnClickListener callback) {
    	super(context, message, callback);
    	init();
    }
    /**
     * Creates a new dialog builder and sets the message
     *
     * @param context Context for the dialog
     * @param message The string to display
     * @param callback What to do when user presses OK
     */
    public Dialog_Confirm(Context context, String message, OnClickListener callback) {
    	super(context, message, callback);
    	init();
    }
    /**
     * Initializes the dialog builder
     *
     * @param context Context for the dialog
     * @param callback What to do when user presses OK
     */
    private void init()
    {
        builder.setNegativeButton("Cancel", null);
    }
    /**
     * Adds a view to the dialog
     *
     * @param view Some widget
     */
    public Dialog_Confirm addView(View view) {
    	builder.setView(view);
    	return this;
    }
}
