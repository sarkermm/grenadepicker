package com.brush.grenadepicker;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GrenadePickerActivity extends Activity {
	private static Button buttonEasyStartListen;
	private static Button buttonMediumStartListen;
	private static Button buttonHardStartListen;

	private static String webLink =
			"For more help,<br>please visit <a href=http://www.sarker-mm.com> www.sarker-mm.com</a>";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Easy modes
		buttonEasyStartListen = (Button) findViewById(R.id.btn_easy);
		buttonEasyStartListen.setOnClickListener(btnEasyHandlerListen);
		// Medium modes
		buttonMediumStartListen = (Button) findViewById(R.id.btn_midium);
		buttonMediumStartListen.setOnClickListener(btnMediumHandlerListen);
		// Hard modes
		buttonHardStartListen = (Button) findViewById(R.id.btn_hard);
		buttonHardStartListen.setOnClickListener(btnHardHandlerListen);
	}

	private void showGuideline(int gameMode){
		new GameGuideline(this, gameMode).show();
	}
	
	/**
	 * Button click event for easy mode
	 */
	private View.OnClickListener btnEasyHandlerListen = new View.OnClickListener() {
	    public void onClick(View v) {

	    	try {
	    		 showGuideline(GameData.BEGINEER);
	    	}catch(Exception ex){
	    		Log.d("LOG_TAG", "Cannot open surfaceciew", ex);
	    	}
	    }
	};

	/**
	 * Button click event for medium mode
	 */
	private View.OnClickListener btnMediumHandlerListen = new View.OnClickListener() {
	    public void onClick(View v) {

	    	try {
	    		 showGuideline(GameData.ADVANCE);
	    	}catch(Exception ex){
	    		Log.d("LOG_TAG", "Cannot open surfaceciew", ex);
	    	}
	    }
	};
	
	/**
	 * Button click event for hard mode
	 */
	private View.OnClickListener btnHardHandlerListen = new View.OnClickListener() {
	    public void onClick(View v) {

	    	try {
	    		 showGuideline(GameData.EXPERT);
	    	}catch(Exception ex){
	    		Log.d("LOG_TAG", "Cannot open surfaceciew", ex);
	    	}
	    }
	};

	/**
	 * Create menu
	 * @param menu   Menu  
	 */ 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 * Menu Item select listener
	 * @param item   Menu Item 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
  	    // TODO Menu item select
		switch (item.getItemId()) {
			case R.id.about:
				showAboutBox();
				return true;	
			default:
				return super.onOptionsItemSelected(item);	
		}
	}
	
    private void showAboutBox(){
    	final Dialog dialog = new Dialog(this, R.style.cust_dialog);
		dialog.setContentView(R.layout.about);
		dialog.setTitle(this.getString(R.string.about_dialog_title));
		
		TextView webLink = (TextView) dialog.findViewById(R.id.webinfo);
		webLink.setMovementMethod(LinkMovementMethod.getInstance());
		webLink.setText(Html.fromHtml(this.webLink));
		dialog.show();
    }
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


}
