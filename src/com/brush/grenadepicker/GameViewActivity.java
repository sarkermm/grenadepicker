package com.brush.grenadepicker;

import com.brush.grenadepicker.controller.GrenadePickerController;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameViewActivity extends Activity {

	private static final String TAG = GameViewActivity.class.getSimpleName();
	private GrenadePickerPanel grenadePickerPanel;
	private Handler mHandler;
	private SoundPool soundPool;
	private float volume;
	private int playbackFile = 0;
	private Button btnPlay;
	private Button btnStartAgain;
	
	private int score = 0;
	private static int bestScore = 0;
	private int lifeCount = GameData.LIFE_COUNT;
	private LinearLayout lifeLayout;
	private String scoreText;
	private TextView scoreView;
	private TextView bestScoreTextView;
	private String bestScoreText;
	private static int flag;
	private Dialog dialog1;
	private Dialog gameOverdialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_view);
		final FrameLayout preview = (FrameLayout) findViewById(R.id.game_panel);
		
		flag = getIntent().getFlags();
		grenadePickerPanel = new GrenadePickerPanel(getApplicationContext(), this, flag);
		preview.removeAllViews();
	    preview.addView(grenadePickerPanel);
	    
		GrenadePickerController grenadePickerController = new GrenadePickerController(grenadePickerPanel);
		mHandler = grenadePickerController.getHandler();
		grenadePickerPanel.setHandler(mHandler);
		
		// Set life parameter
		setLifeParameter();
		
		// Set score view 
		setScoreView();
        
		// Set game type label
		setGameTypeLabel(flag);
		
		// Set the best score
		searchBestScore(flag);
		
		// Sound pool to blast grenade
		soundPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 0);
		volume = getVolume();
        playbackFile = soundPool.load(this.getApplicationContext(), R.raw.blast, 0);
	}
	
	private float getVolume(){
		AudioManager mgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		int streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		int maxStreamVolume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		Log.d(TAG, "vloume:" + streamVolume + " max: " + maxStreamVolume);
		return (float)streamVolume / maxStreamVolume;
	}
	
	public boolean showStartDialog(int gameStatus){
		try{
			
			if(dialog1 != null && dialog1.isShowing()){
				return false;
			}
			if(gameOverdialog != null && gameOverdialog.isShowing()){
				return false;
			}
			dialog1 = new Dialog(this, R.style.cust_dialog);
			dialog1.setContentView(R.layout.start_dialog);
			dialog1.setTitle(this.getString(R.string.game_start));
			dialog1.setCancelable(false);
			
			btnPlay = (Button)dialog1.findViewById(R.id.play);
			if(gameStatus == GameData.ON_LAUNCH || gameStatus == GameData.GAME_OVER){
				btnPlay.setText(R.string.play);
			}else{
				btnPlay.setText(R.string.resume);
			}
			
			btnPlay.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			        // Do something in response to button click
			    	btnPlay.setClickable(false);
			    	final TextView countDownView = (TextView)dialog1.findViewById(R.id.count_info); 
			    	new CountDownTimer(GameData.TIMER_COUNT* 1000, 800) {      
			    		   int timer_count = GameData.TIMER_COUNT;
			    		   public void onTick(long millisUntilFinished) {	  
			    			   countDownView.setText("" + timer_count);      
			    			   timer_count = timer_count - 1;
			    		}       

			    		public void onFinish() {         
			    			dialog1.cancel();
							Message msg = mHandler.obtainMessage(GameData.ON_START);
					        mHandler.sendMessage(msg);
			    		}   
		    		}.start();  
			    }
			});
			
			Button btnCancel = (Button)dialog1.findViewById(R.id.cancel);
			btnCancel.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			    	dialog1.cancel();
			    	finish();
			    }
			});
			dialog1.show();
			
		}catch(Exception ex){
			Log.d(TAG, "could open surfaceciew", ex);
		}
		
		return true;
	}
	
	public void doVibrate(){
		try{
		  Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
          vibrator.vibrate(500);
//          doGameOver();
		}catch(Exception ex){
			Log.d(TAG, "dotVibrate", ex);
		}
	}
	
	public void playSoundBlast(){
		try{
			soundPool.play(playbackFile, volume, volume, 0, 0, 1f);
		}catch(Exception ex){
			Log.d(TAG, "playSoundBlast", ex);
		}
	}
	
	public void updateScore(int value){
		if(value < 0 ){
			lifeLayout.removeViewAt(lifeLayout.getChildCount() -1);
			lifeLayout.refreshDrawableState();
			int childCount = lifeLayout.getChildCount();
			if(childCount == 0){
				// Game over
				doGameOver();
			}
		}else{
			score = score + 1;
			scoreView.setText(scoreText+ score);
		}
	}
	
	/**
	 * Set the life bitmap image
	 *
	 */
	private void setLifeParameter(){
		try{
			lifeLayout = (LinearLayout) findViewById(R.id.lifeLayout);
			lifeLayout.removeAllViews();
			for(int i = 0; i < lifeCount; i++){
				LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
			                LayoutParams.WRAP_CONTENT);
				 ImageView imgView = new ImageView(this);
				 imgView.setLayoutParams(lparams);
				 imgView.setPadding(0, 1, 0, 1);
				 Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.life);
				 imgView.setImageBitmap(bm); 
				 lifeLayout.addView(imgView, i);
			}
		}catch(Exception ex){
			Log.d(TAG, "setLifeParameter", ex);
		}
	}
	
	private void setScoreView(){
		try{
			scoreText = getResources().getString(R.string.score) + " ";
			scoreView = (TextView) findViewById(R.id.score);
			scoreView.setText(scoreText + score);
		}catch(Exception ex){
			Log.d(TAG, "setScoreView", ex);
		}
	}
	
	public void doGameOver(){
		Message msg = mHandler.obtainMessage(GameData.GAME_OVER);
        mHandler.sendMessage(msg);		
	}
	
	private Handler udateBestScoreHandler = new Handler();
	private Runnable udateBestScoreRunnable = new Runnable() {
       public void run() {
    	   try{
    		   bestScore = bestScore + 1;
    		   bestScoreTextView.setText(bestScoreText + bestScore);
    		   
    		   if(bestScore < score){
    			   udateBestScoreHandler.postDelayed(udateBestScoreRunnable, 100);
    		   }else{
    			   btnStartAgain.setClickable(true);
    			   storeBestScore(flag);
    		   }
    	   }catch(Exception ex){
    		   Log.d(TAG, "udateBestScoreRunnable", ex);
    	   }
       }
	};
	
	public void showGameOverDialog(){
		try{
			 
			gameOverdialog = new Dialog(this, R.style.cust_dialog);
			gameOverdialog.setContentView(R.layout.game_over);
			gameOverdialog.setTitle(this.getString(R.string.game_over_title));
			gameOverdialog.setCancelable(false);
			
			// Count from prev best to current best score
			bestScoreText = getResources().getString(R.string.best_score) + " ";
			bestScoreTextView = (TextView)gameOverdialog.findViewById(R.id.best_score_text); 
			bestScoreTextView.setText(bestScoreText + bestScore);  
			
			btnStartAgain = (Button)gameOverdialog.findViewById(R.id.start_again);
			if(bestScore < score){
				btnStartAgain.setClickable(false);
				udateBestScoreHandler.postDelayed(udateBestScoreRunnable, 100);
			}
	    
    		// Restart game
    		btnStartAgain.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			        // Do something in response to button click
			    	// Set life parameter
					setLifeParameter();
					score = 0;
					// Set score view 
					setScoreView();
					gameOverdialog.cancel();
			    	Message msg = mHandler.obtainMessage(GameData.ON_LAUNCH);
			        mHandler.sendMessage(msg);
			    }
			});	
    		
    		// Cancel game
			Button btnCancel = (Button)gameOverdialog.findViewById(R.id.cancel);
			btnCancel.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			        // Do something in response to button click
			    	gameOverdialog.cancel();
			    	finish();
			    }
			});
			
			gameOverdialog.show();
			
		}catch(Exception ex){
			Log.d(TAG, "showGameOverDialog", ex);
		}
	}
	
	/**
	 * Set the game type label at the top of the view
	 * @param gameType Game expertness type  
	 */
	private void setGameTypeLabel(int gameType){
		try{
			GameTypeDetail gameTypeDetail = new GameTypeDetail(gameType, this);
			String gameTypeLabel = gameTypeDetail.getGameTypeLabel();
			TextView tv =  (TextView)findViewById(R.id.game_type);
			tv.setText(gameTypeLabel);
			
		}catch(Exception ex){
			Log.d(TAG, "", ex);
		}
	}
	
	/**
	 * Search best score base on game type
	 * @param gameType Game expertness type  
	 */
	private void searchBestScore(int gameType){
		try{
			GameTypeDetail gameTypeDetail = new GameTypeDetail(gameType, this);
			bestScore = gameTypeDetail.getBestScore();
			TextView tv =  (TextView)findViewById(R.id.best_score);
			tv.setText("Best: " + bestScore);
			
		}catch(Exception ex){
			Log.d(TAG, "", ex);
		}
	}
	
	/**
	 * Store game type
	 * @param gameType Game expertness type  
	 */
	private void storeBestScore(int gameType){
		try{
			TextView tv =  (TextView)findViewById(R.id.best_score);
			tv.setText("Best: " + bestScore);
			GameTypeDetail gameTypeDetail = new GameTypeDetail(gameType, this);
			gameTypeDetail.storeBestScore(bestScore);

		}catch(Exception ex){
			Log.d(TAG, "Shared Preferance set value", ex);
		}
	}
	
	@Override
	protected void onResume() {
		Message msg = mHandler.obtainMessage(GameData.ON_RESUME);
        mHandler.sendMessage(msg);
		super.onResume();
	}

	@Override
	protected void onPause() {
		Message msg = mHandler.obtainMessage(GameData.ON_PAUSE);
        mHandler.sendMessage(msg);
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
