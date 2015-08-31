package com.brush.grenadepicker.controller;

import com.brush.grenadepicker.GameData;
import com.brush.grenadepicker.GameViewActivity;
import com.brush.grenadepicker.GrenadePickerPanel;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;

public class GrenadePickerController implements Callback {
	private static final String TAG = GrenadePickerController.class.getSimpleName();
	private GrenadePickerPanel grenadePickerPanel;
	private Handler mHandler = new Handler(this);
    private static int gameStatus;
    private GrenadePickerThread grenadePickerThread;
    
	public int getGameStatus() {
		return gameStatus;
	}
	
	public void setGameStatus(int gameStatus) {
		this.gameStatus = gameStatus;
	}
	
	public Handler getHandler() {
		return this.mHandler;
	}
	
	public GrenadePickerController(GrenadePickerPanel grenadePickerPanel) {
		this.grenadePickerPanel = grenadePickerPanel;
		gameStatus = GameData.ON_LAUNCH;
	}
	
	 @Override
	  public boolean handleMessage(Message msg) {
		try {
		    switch(msg.what){
			    case GameData.ON_RESUME:
			    	if(gameStatus == GameData.ON_PAUSE){
			    		grenadePickerPanel.getLaunchConfirmation(gameStatus);
			    	}
			       return true;
			       
			    case GameData.ON_PAUSE:
			    	if(gameStatus == GameData.GAME_OVER){
			    		return true;
			    	}
			    	if(gameStatus == GameData.ON_RESUME || gameStatus == GameData.ON_RUNNING){
			    		grenadePickerThread.setRunning(false);
						grenadePickerThread.interrupt();
						grenadePickerPanel.setInterruptGrenade(true);
						gameStatus = GameData.ON_PAUSE;
			    	}
			    	return true;
			    	
			    case GameData.ON_LAUNCH:
			    	if(gameStatus == GameData.ON_STOP || gameStatus == GameData.ON_LAUNCH || gameStatus == GameData.GAME_OVER){
			    		boolean isOK = grenadePickerPanel.getLaunchConfirmation(gameStatus);
			    	}
			    	return true;
			    	
			    case GameData.ON_START:
			    	grenadePickerPanel.setInterruptGrenade(false);
			    	initializeGameThtread();
			    	grenadePickerPanel.startRunning();
			    	gameStatus = GameData.ON_RUNNING;
			    	return true;	
			    	
			    case GameData.ON_DESTROY:
			    	destroyGameThtread();
			    	return true;
			    	
			    case GameData.GAME_OVER:
			    	destroyGameThtread();
			    	gameStatus = GameData.GAME_OVER;
			    	grenadePickerPanel.setGameOver();
			    	return true;
			    	
			    default:
			      return false;
		    }
		} catch (Exception ex) {
			Log.i(TAG, "handleMessage", ex);
		}
		 return false;
	  }
	 
	 private void initializeGameThtread() {
		 if(!isThreadAlive()){
			grenadePickerThread = new GrenadePickerThread(grenadePickerPanel.getHolder(), grenadePickerPanel);
	    	grenadePickerThread.setRunning(true);
			grenadePickerThread.start();
		 }
	 }
	 
	 private void destroyGameThtread() {
		 try{
			grenadePickerThread.setRunning(false);
			grenadePickerThread.join();
			grenadePickerThread.interrupt();
			gameStatus = GameData.ON_STOP;
		 } catch (Exception ex) {
			 Log.i(TAG, "destroyGameThtread", ex);
		}
	 }
	 
	 private boolean isThreadAlive() {
		if(grenadePickerThread != null){
			return grenadePickerThread.isAlive();
		}
		return false;
	}

}
