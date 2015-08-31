package com.brush.grenadepicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.ScrollView;

public class GameGuideline {

	private static final String GUIDELINE_PREFIX = "guideline_";
	private Activity mActivity;
	private int gameMode;
	
	public GameGuideline(Activity context, int gameMode) {
		this.mActivity = context;
		this.gameMode = gameMode;
	}

	private PackageInfo getPackageInfo() {
		PackageInfo pi = null;
		try {
		     pi = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), PackageManager.GET_ACTIVITIES);
		} catch (PackageManager.NameNotFoundException e) {
		    e.printStackTrace();
		}
		return pi;
    }

     public boolean show() {
        PackageInfo versionInfo = getPackageInfo();
        final String guidelineKey = GUIDELINE_PREFIX + versionInfo.versionCode;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        boolean hasBeenShown = prefs.getBoolean(guidelineKey, false);
        if(hasBeenShown == false){

            String title = mActivity.getString(R.string.how_to);
            final ScrollView s_view = new ScrollView(mActivity);
            final ImageView t_view = new ImageView(mActivity); 
            t_view.setBackgroundResource(R.drawable.pinch);
            s_view.addView(t_view);
            
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(guidelineKey, true);
            editor.commit();
            
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                    .setTitle(title)
                    .setView(s_view)
                    .setPositiveButton(android.R.string.ok, new Dialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Mark this version as read.
                        	 dialogInterface.dismiss();
                        	 startGameViewActivity();
                        }
                    });
            builder.create().show();
        }else{
        	startGameViewActivity();
        }
        return true;
    }
     
    private void startGameViewActivity(){
    	Intent intent = new Intent();
        intent.setClass(mActivity, GameViewActivity.class);
        intent.setFlags(gameMode);
        mActivity.startActivityForResult(intent, 0);
    }
}