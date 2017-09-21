package com.placella.todo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

/**
 * A class to show the user a simple
 * pop-up dialog with a message
 *
 * @author Rouslan Placella
 */
public class Dialog_Message {
    protected AlertDialog.Builder builder;
    /**
     * Creates a new dialog builder and sets the message
     *
     * @param context Context for the dialog
     * @param message A reference to the string resource to display
     */
    public Dialog_Message(Context context, int message) {
		init(context, null);
        builder.setMessage(message);
    }
    /**
     * Creates a new dialog builder and sets the message
     *
     * @param context Context for the dialog
     * @param message A reference to the string resource to display
     * @param callback What to do when user presses OK
     */
    public Dialog_Message(Context context, int message, OnClickListener callback) {
		init(context, callback);
        builder.setMessage(message);
    }
    /**
     * Creates a new dialog builder and sets the message
     *
     * @param context Context for the dialog
     * @param message The string to display
     */
    public Dialog_Message(Context context, String message) {
		init(context, null);
        builder.setMessage(message);
    }
    /**
     * Creates a new dialog builder and sets the message
     *
     * @param context Context for the dialog
     * @param message The string to display
     * @param callback What to do when user presses OK
     */
    public Dialog_Message(Context context, String message, OnClickListener callback) {
		init(context, callback);
        builder.setMessage(message);
    }
    /**
     * Initializes the dialog builder
     *
     * @param context Context for the dialog
     * @param callback What to do when user presses OK
     */
    private void init(Context context, OnClickListener callback)
    {
    	builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("OK", callback);
    }
	/**
     * Shows the dialog
     */
    public void show() {
        builder.create().show();
    }
}
