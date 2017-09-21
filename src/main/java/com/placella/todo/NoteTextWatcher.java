package com.placella.todo;

import android.text.Editable;
import android.text.TextWatcher;
/**
 * Notifies activities when an EditText has changed value
 * Activities wishing to be notified must implement the Savable interface
 * 
 * @author Rouslan Placella
 */
public class NoteTextWatcher implements TextWatcher {
	/**
	 * The calling activity
	 */
	private Savable context;
	/**
	 * Construct
	 * 
	 * @param context The calling activity
	 */
	public NoteTextWatcher (Savable context) {
		this.context = context;
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		/**
		 * Notify activity about change
		 */
		context.save();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {}
}
