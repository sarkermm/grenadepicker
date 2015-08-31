package com.brush.grenadepicker;

import com.brush.grenadepicker.component.Grenade;
import com.brush.grenadepicker.controller.PinchController;
import com.brush.grenadepicker.imageprocess.BitmapLoader;
import com.brush.grenadepicker.imageprocess.RanderImage;

import java.util.ArrayList;
import java.util.Random;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;

public class GrenadePickerPanel extends SurfaceView implements SurfaceHolder.Callback, OnGameRecordListener{

	// Start position of the hole
	private int holeX1 = 0;
	private int holeY1 = 0;
	// End position of the hole
	private int holeX2 = 0;
	private int holeY2 = 0;
	
	private int screenWidth = 0;
	private int screenHeight = 0;
	private static final String TAG = GrenadePickerPanel.class.getSimpleName();
	private static boolean isTouched = false;
	private PinchController grenadePitcherController;
	private Handler mHandler;
	private static boolean isInterrupt = false;
	private  Grenade grenade;
	private GameViewActivity gameViewActivity = null;

    // List of graphics we already have to handle.
    private ArrayList<Grenade> grenades = new ArrayList<Grenade>();
    private ArrayList<Grenade> expired = new ArrayList<Grenade>();
    
    private BitmapLoader bitmapLoader;
    private RanderImage randerImage;
    private Bitmap bgBitmap;
    private int grenadeLaunchDelay;
    private int grenadeLaunchDelayCount;
    private int totalDelay;
    private static int flag;
    
    private VelocityTracker mVelocityTracker = null;
    private float yVelocity = 0;
    private int grenadeImageWidth = GameData.GRENADE_IMAGE_WIDTH;
    private int grenadeImageHeight = GameData.GRENADE_IMAGE_WIDTH;;
    private int pinchSpeed;
    private int borderWidth;
    private int borderHeigtht;

	public GrenadePickerPanel(Context context, GameViewActivity gameViewActivity, int flag) {
		super(context);
		this.gameViewActivity = gameViewActivity;
		// Adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);
		
		bitmapLoader = new BitmapLoader(getContext());
        bitmapLoader.loadBitmap(); //TODO  count of bitmap
        
        // Initialize Bitmap rander
        randerImage = new RanderImage(bitmapLoader);

		// Make the GamePanel focusable so it can handle events
		setFocusable(true);
		
		// Set the launch delay for grenade
		this.flag = flag;
	}
	
	/**
	 * Set the launch delay for grenade
	 * @param flag Game expertness mode  
	 */
	private void setGrenadeLaunchDelay(int gameType){
		GameTypeDetail gameTypeDetail = new GameTypeDetail(gameType, getContext());
		grenadeLaunchDelay = gameTypeDetail.getGrenadeLaunchDelay();
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// at this point the surface is created and
		// we can safely start the game loop
		setWillNotDraw(false);
		screenWidth  = this.getWidth();
		screenHeight = this.getHeight();
		pinchSpeed = (int) (screenHeight * 6.0f/700.0f); 
		borderWidth = (int)(GameData.BORDER_WIDTH_RATIO * screenWidth);
		borderHeigtht = (int)(GameData.BORDER_HEIGHT_RATIO * screenHeight);
		
		bgBitmap = bitmapLoader.getBitmap(100);
        setScoreArea(bgBitmap.getWidth(),bgBitmap.getHeight());
	    float scale = (float)bgBitmap.getHeight()/(float)screenHeight;
	    int newHeight = Math.round(bgBitmap.getHeight()/scale);
	    scale = (float)bgBitmap.getWidth()/(float)screenWidth;
	    int newWidth = Math.round(bgBitmap.getWidth()/scale);
	    bgBitmap = Bitmap.createScaledBitmap(bgBitmap, newWidth, newHeight, true);

	    Message msg = mHandler.obtainMessage(GameData.ON_LAUNCH);
        mHandler.sendMessage(msg);
	}

	public void setHandler(Handler mHandler){
		this.mHandler = mHandler;
	}
	
	private void setScoreArea(int bitmapWidth, int bitmapHeight ){
		holeX1 = (int)(GameData.ratioX1 * screenWidth);
		holeY1 = 0;
		
		holeX2 = (int)(GameData.ratioX2 * screenWidth);
		holeY2 = (int)(GameData.ratioY2 * screenHeight);
	}

	/**
	 * Set the interrupt value when external event occurs
	 *
	 */
	public void setInterruptGrenade(boolean interrupt) {
		try{
			for (Grenade grenade : grenades) {
				grenade.setInterrupted(interrupt);
			}
			this.isInterrupt = interrupt;
		}catch(Exception ex){
  		   Log.i(TAG, "setInterruptGrenade", ex);
  	   	}
	}
	
	/**
	 * Game over action
	 *
	 */
	public void setGameOver() {
		try{
			isInterrupt = true;
			for (Grenade grenade : grenades) {
				grenade.setStatus(GameData.LAUNCHER_FINISH);
			}
			grenades.clear();
			expired.clear();
			grenadePitcherHandler.removeCallbacks(grenadePitcherRunnable);
			gameViewActivity.showGameOverDialog();
		}catch(Exception ex){
	  		   Log.i(TAG, "setGameOver", ex);
		}
	}
	
	/**
	 * Show launch start dialog
	 *
	 */
	public boolean getLaunchConfirmation(int gameStatus) {
		try{
			return gameViewActivity.showStartDialog(gameStatus);
		}catch(Exception ex){
			
		}
		return true;
	}
	
	public void startRunning() {
		createRandomDelay();
		grenadePitcherHandler.postDelayed(grenadePitcherRunnable, grenadeLaunchDelay);
	}
	
	private void createRandomDelay(){
		setGrenadeLaunchDelay(flag);
		Random rand = new Random();
		int m = rand.nextInt(4-2) + 2;
		totalDelay = GameData.BEGINEER_DELAY_TIME * m;
		int minDelay = grenadeLaunchDelay - 300;
		grenadeLaunchDelay= rand.nextInt(grenadeLaunchDelay -minDelay) + minDelay;
		grenadeLaunchDelayCount = 0;
	}
	
	@Override
	public boolean onGetTouchStatus(){
		return isTouched;
	}
	
	@Override
	public boolean onIsInsideHoleArea(int x, int y){
		if( x > this.holeX1 && x < this.holeX2 && y > this.holeY1 && y < (this.holeY2-10)){  //-10 is not logical
			return true;
		}
		return false;
	}
	
	@Override
	public void onVibrate(){
		gameViewActivity.doVibrate();
	}
	
	@Override
	public void onGameOver(){
		gameViewActivity.doGameOver();
	}
	
	@Override
	public void onPlaySoundBlast(){
		gameViewActivity.playSoundBlast();
	}
	
	@Override
	public void onUpdateScoreCount(int value){
		gameViewActivity.updateScore(value);
	}
	
	@Override
	public void setImageSize(int bitmapId){
		Bitmap bitmap = bitmapLoader.getBitmap(bitmapId);
		grenadeImageWidth = bitmap.getWidth();
		grenadeImageHeight = bitmap.getHeight();
	}
	
	private void craeteNewGrenadeInstance(){
	     grenade = new Grenade();
		 grenade.setLauncherFrameCount(14);   // 15 image
		 grenade.setDroppedFrameCount(2);
		 grenade.setTimerFrameCount(2);
		 grenade.setExploreFrameCount(9);
		 grenade.setExploreFrameCountHand(5);
		 grenade.setLauncherSpeed(3);
		 grenade.setPinchSpeed(pinchSpeed);
		 grenade.setStatus(GameData.LAUNCHER_LAUNCHED);
		 grenade.setLauncherCoordinate(screenWidth,screenHeight, borderWidth, borderHeigtht, holeX1, holeX2, holeY1, holeY2, grenadeImageWidth, grenadeImageHeight);
		 grenade.setListener(this);
	}
	
	private Handler grenadePitcherHandler = new Handler();
	private Runnable grenadePitcherRunnable = new Runnable() {
       public void run() {
    	   try{
    		   if(!isInterrupt){
    			    craeteNewGrenadeInstance();
    			    grenades.add(grenade);
    			    
    			    grenadeLaunchDelayCount = grenadeLaunchDelayCount + grenadeLaunchDelay;
    			    if(grenadeLaunchDelayCount < totalDelay){
    			    	grenadePitcherHandler.postDelayed(grenadePitcherRunnable, grenadeLaunchDelay);
    			    }else{
    			    	grenadePitcherHandler.postDelayed(grenadePitcherRunnable, 2000);
    			    	createRandomDelay();
    			    }
    		   }
    	   }catch(Exception ex){
    		   Log.i(TAG, "grenadePitcherRunnable", ex);
    	   }
       }
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);
        switch(action) {
        	case MotionEvent.ACTION_DOWN:
		    	isTouched = true;
   			 	// Set the initial value of pitcher
   			 	initializePitcher(event); 
        		break;
        		
        	case MotionEvent.ACTION_UP:
        	case MotionEvent.ACTION_CANCEL:
        		actionStartPinch();
				isTouched = false;
        		break;
        		
        	case MotionEvent.ACTION_MOVE:
        		actionMoveGrenade(event, pointerId);
        		break;	
        		
        	default:
        		break;
        }
		
		return true;
	}
	
	private void initializePitcher(MotionEvent event){
		try{
			int touchX = (int)event.getX();
			int touchY = (int)event.getY();
	    	grenadePitcherController = new PinchController(screenWidth,screenHeight);
	    	
	    	if(mVelocityTracker == null) {
                mVelocityTracker = VelocityTracker.obtain();
            } else {
                mVelocityTracker.clear();
            }
		 	mVelocityTracker.addMovement(event);
	    	
			for (Grenade grenade : grenades) {
				if(grenade.getPinchActive()){
					continue;
				}
				int x = grenade.getCoordinate().getX();
				int y = grenade.getCoordinate().getY();
				// pixelBuffer = extra position for smooth pick
			    if (touchX >= x - GameData.PIXEL_BUFFER && touchX <= x + grenadeImageWidth + GameData.PIXEL_BUFFER && 
			    		touchY >= y - GameData.PIXEL_BUFFER && touchY <= y + grenadeImageHeight + GameData.PIXEL_BUFFER) {
			    	
			    	grenadePitcherController.setPinchActivate(grenade);
			    	grenadePitcherController.setFirstTouchX(touchX, touchX-x);
			    	grenadePitcherController.setFirstTouchY(touchY, touchY-y);
			    	break;
			    }
			}
		}catch( Exception ex){
			Log.i(TAG, "initializePitcher" + ex);
		}
	}

	private void actionMoveGrenade(MotionEvent event, int pointerId){
		try{
			 mVelocityTracker.addMovement(event);
			 mVelocityTracker.computeCurrentVelocity(1000);
			 yVelocity = VelocityTrackerCompat.getYVelocity(mVelocityTracker, pointerId);
			 
			if(isTouched && grenadePitcherController.isActive()){
				int touchX = (int)event.getX();
				int touchY = (int)event.getY();
				
				// Do not get touched when pinch come to hole area
				if(touchY <= holeY2 + borderHeigtht || touchY >= screenHeight - borderHeigtht){
					grenadePitcherController.setPinchDeActivate();
					isTouched = false;
					return;
				}

				// When touch goes out of x border
				if(touchX <= borderWidth || touchX >= screenWidth - borderWidth){
					grenadePitcherController.setPinchDeActivate();
					isTouched = false;
					return;
				}
				
				grenadePitcherController.setCurrentTouchX(touchX);
				grenadePitcherController.setCurrentTouchY(touchY);
				 
				if(yVelocity >= 0) {    // pinch going to backward 
					grenadePitcherController.setFirstTouchX(touchX);
			    	grenadePitcherController.setFirstTouchY(touchY);
				}
	
				grenadePitcherController.setPrevTouchX(touchX);
				grenadePitcherController.setPrevTouchY(touchY);
			}
		}catch( Exception ex){
			Log.i(TAG, "actionMoveGrenade" + ex);
		}
	}
	
	private void actionStartPinch(){
		try{
			if(grenadePitcherController.isActive()){
				if(yVelocity < 0 && 
						(grenadePitcherController.getFirstTouchY() - grenadePitcherController.getCurrentTouchY() > 20)){
					grenadePitcherController.start(GameData.MIN_PINCH_VALUE_Y);
				}else{
					grenadePitcherController.setPinchDeActivate();
				}
			}
		}catch( Exception ex){
			Log.i(TAG, "actionMoveGrenade" + ex);
		}
	}
	
	/**
	 * Draw the display content
	 *
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(bgBitmap, 0,0, null);
		try{
			for (Grenade grenade : this.grenades) {
				randerImage.draw(canvas, grenade);
			}
		}catch( Exception ex){
			Log.i(TAG, "onDraw" + ex);
		}
	}
	
	/**
	 * Update the display content
	 *
	 */
	public void update(){
		try{
			for (Grenade grenade : grenades) {
				if (grenade.getStatus() == GameData.LAUNCHER_FINISH){
					expired.add(grenade);
				}
			}
			if(!expired.isEmpty()){
				grenades.removeAll(expired);
				expired.clear();
			}
		}catch( Exception ex){

		}
	}
	
	/**
	 * Surface terminated
	 * @param holder SurfaceHolder
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		boolean retry = true;
		while (retry) {
			try {
				Message msg = mHandler.obtainMessage(GameData.ON_DESTROY);
				mHandler.sendMessage(msg);
				retry = false;
				
				// Stop all granede status change activity
				for (Grenade grenade : grenades) {
					grenade.setStatus(GameData.LAUNCHER_FINISH);
				}
				grenades.clear();
				expired.clear();
				grenadePitcherHandler.removeCallbacks(grenadePitcherRunnable);
				isInterrupt = false;
			} catch (Exception e) {
				
			}
		}
	}

}
