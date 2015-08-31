package com.brush.grenadepicker;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class GameTypeDetail {
	
	private static final String PREFERENCE_SCORE_NAME_BEG = "begineerBestScore";
	private static final String PREFERENCE_SCORE_NAME_ADV = "advanceBestScore";
	private static final String PREFERENCE_SCORE_NAME_EXPT = "expartBestScore";
	private static final String PREFERENCE_NAME = "GrenadePickerPreferences";
	private String preferenceScoreName; 
	private String gameTypeLabel;
	private Context context;
	private int grenadeLaunchDelay;
	
	public GameTypeDetail(int gameType, Context context){
		this.context = context;
		setGameTypeInfo(gameType);
	}
	
	private void setGameTypeInfo(int type){
		switch (type) {
		case GameData.BEGINEER:
			preferenceScoreName = PREFERENCE_SCORE_NAME_BEG;
			gameTypeLabel = this.context.getResources().getString(R.string.str_easy);
			grenadeLaunchDelay = GameData.BEGINEER_DELAY_TIME;
			break;
			
		case GameData.ADVANCE:
			preferenceScoreName = PREFERENCE_SCORE_NAME_ADV;
			gameTypeLabel = this.context.getResources().getString(R.string.str_midium);
			grenadeLaunchDelay = GameData.ADVANCE_DELAY_TIME;
			break;
			
		case GameData.EXPERT:
			preferenceScoreName = PREFERENCE_SCORE_NAME_EXPT;
			gameTypeLabel = this.context.getResources().getString(R.string.str_hard);
			grenadeLaunchDelay = GameData.EXPERT_DELAY_TIME;
			break;
			
		default:
			break;
		}
	}
	
	public int getGrenadeLaunchDelay(){
		return grenadeLaunchDelay;
	}
	
	public String getGameTypeLabel(){
		return gameTypeLabel;
	}
	
	public int getBestScore(){
		SharedPreferences sharedpreferences;
		sharedpreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		int bestScore = sharedpreferences.getInt(preferenceScoreName, 0);
		return bestScore;
	}
	
	public void storeBestScore(int bestScore){
		SharedPreferences sharedpreferences;
		sharedpreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putInt(preferenceScoreName, bestScore);
		editor.commit();
	}
}
