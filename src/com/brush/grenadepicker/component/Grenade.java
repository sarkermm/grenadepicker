package com.brush.grenadepicker.component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import java.lang.Math;
import com.brush.grenadepicker.GameData;
import com.brush.grenadepicker.OnGameRecordListener;

/**
 * Class which contains a object we want to draw on specific position.
 *
 * @author Sarker
 */
public class Grenade {
	
	private static final String LOG_TAG = "Grenade";
	private OnGameRecordListener onGameRecordListener;
	private Map<Integer, Coordinates> grenadeLaunchers = new HashMap<Integer, Coordinates>();
	private int currentStatus = GameData.LAUNCHER_READY;
	private Coordinates coordinate;
	private int launcherSide;

	private int launcherFrameCount = 0;
	private int droppedFrameCount = 0;
	private int exploreFrameCount = 0;
	private int exploreFrameCountHand = 0;
	private int timerFrameCount = 0;
    
	private int timerFrameIndex = 0;
	private int launcherFrameIndex = 0;
	private int launcherIndexCount = 0;
	private boolean isInterrupted = false;
	private boolean pinchActive = false;
	private boolean isTouched = false;
	private int pinchValue;
	private int holeY2;
	
	// The speed of the travel of grenade after throw
	private int  launcherSpeed = 0;
	// The speed when pinch by finger
	private int pinchSpeed = 0;
	
	/**
     * Contains the coordinates of the instance.
     *
     * @author Sarker
     */
	public class Coordinates {
        private int x = 0;
        private int y = 0;
        private int imageID;

        public int getImageID() {
            return imageID;
        }
        public void setImageID(int Id) {
            imageID = Id;
        }
        
        public int getX() {
            return x;
        }

        public void setX(int value) {
            x = value;
        }

        public int getY() {
            return y;
        }

        public void setY(int value) {
            y = value; 
        }
    }
	/** End of coordinate */
	
	public void setLauncherSpeed(int value) {
		launcherSpeed = value;
    }
	public void setPinchSpeed(int value) {
		pinchSpeed = value;
    }
	
	public void setLauncherFrameCount(int value) {
		launcherFrameCount = value;
    }
	
	public void setTimerFrameCount(int value) {
		timerFrameCount = value;
    }
	
	public void setExploreFrameCount(int value) {
		exploreFrameCount = value;
    }
	
	public void setExploreFrameCountHand(int value) {
		exploreFrameCountHand = value;
    }
	
	public void setStatus(int value) {
		currentStatus = value;
		isTouched = false;
		if(currentStatus == GameData.LAUNCHER_FINISH){
			activateTimerHandler.removeCallbacks(activateTimerRunnable);
			exploreHandler.removeCallbacks(exploreRunnable);
		}
    }
	
	public void  setDroppedFrameCount(int value){
		droppedFrameCount = value;
	}
	
	public int getStatus() {
		return currentStatus;
    }
	
	public int getGrenadeLauncherSide() {
		return launcherSide;
    }

	public void setInterrupted(boolean isInterrupted){
		this.isInterrupted = isInterrupted;
	}
	
	public void setPinchActive(boolean pinchActive){
		this.pinchActive = pinchActive;
	}
	
	public boolean getPinchActive(){
		return this.pinchActive;
	}
	
	public void setPinchValue(float pinchValue, int screenHeight, int touchY){
		float value = (float)touchY/screenHeight * 30;
		this.pinchValue = (int)value;
	}
	
	public void setTouched(boolean isTouched){
		this.isTouched = isTouched;
	}
	
	public boolean isTouched(){
		return this.isTouched;
	}
	
	/**
	 * Constructor.
	 *
	 * @param bitmap Bitmap which should be drawn.
	 */
	public Grenade() {
	     currentStatus = GameData.LAUNCHER_READY;
	     coordinate = new Coordinates();
	}

	public void setListener(OnGameRecordListener onGameRecordListener) {
		this.onGameRecordListener = onGameRecordListener;
	}
	
	 /**
     * Set the coordinates of frame when launching.
     */
    public void setLauncherCoordinate(int screenWidth, int screenHeight, int marginX, int marginY, int holeX1, int holeX2, int holeY1, int holeY2, int grenadeImageWidth, int grenadeImageHeight){
    	try{
    		this.holeY2 = holeY2;
    		int y;
    		int x;
    		Random rand = new Random();
    		Time time = new Time();
    		time.setToNow();
    		int  radius = rand.nextInt(screenWidth-marginX-grenadeImageWidth); 
    		if(radius < marginX){
    			radius = marginX;
    		}
    		int holeArea = holeY2 + marginY;
    		int deltaY = rand.nextInt(screenHeight- holeArea - marginY - grenadeImageHeight - 40) + holeArea + 40; // value:40 is for safety of pinch
    		double value;

    		int  side = rand.nextInt(2);
    		int i = 0;
    		if(side == 0){
    			launcherSide = GameData.LEFT_SIDE_GRENADE;
    		    // To create coordinate for launcher
		    	for(int angle = 180; angle <= 360; angle++){
		    		Coordinates coordinates = new Coordinates();
		    		value = radius * Math.sin(angle * Math.PI/180);
		    		y = (int)value;
		    		value = radius * Math.cos(angle* Math.PI/180);
		    		x = (int)value;
		    		coordinates.setX(x);
		    		coordinates.setY(y+deltaY);
		    		grenadeLaunchers.put(i, coordinates);
		    		i++;
		    	}
    		}else{
        		if(radius < marginX  + grenadeImageWidth){
        			radius = marginX + grenadeImageWidth;
        		}
    			launcherSide = GameData.RIGHT_SIDE_GRENADE;
		    	for(int angle = 360; angle >=180; angle--){
		    		Coordinates coordinates = new Coordinates();
		    		value = radius * Math.sin(angle * Math.PI/180);
		    		y = (int)value;
		    		value = radius * Math.cos(angle* Math.PI/180);
		    		x = (int)value;
		    		coordinates.setX(x+screenWidth-grenadeImageWidth);
		    		coordinates.setY(y+deltaY);
		    		grenadeLaunchers.put(i, coordinates);
		    		i++;
		    	}
    		}
	    	launcherIndexCount = i;

	    	// Set image priodically //
	    	int imageId= 0;
	    	int framedelay = launcherIndexCount/(launcherFrameCount);
	    	value = framedelay;
	    	for (i = 0 ; i < launcherIndexCount; i++ ) {
	    		Coordinates coordinates = grenadeLaunchers.get(i);
	    		coordinates.setImageID(imageId);
	    		grenadeLaunchers.put(i, coordinates);
	    		if(i >= value){
	    			imageId = imageId + 1;
	    			value = value + framedelay;
	    		}
			}
    	}catch(Exception ex){
    	}
    }

    public void createPitcherCoordinate(int touchX, int deltaX, int touchY, int deltaY, int minY, int firstX, int firstY){
	    grenadeLaunchers.clear();
	    launcherFrameIndex = 0;	
		float m = (float)(touchY - firstY) / (float)(touchX - firstX); 
		// Straignt line OK
		float minX = (float)(minY - firstY) / m + firstX;
		
		float diffY = touchY - minY;
		float diffX = touchX - minX;
		float halfDiffY = diffY/4 * 3;
		float halfDiffX = diffX/3;
		float ratio = halfDiffX/halfDiffY;
		
		boolean reverseCount = false;
		
    	try{
	    	int i = 0;
	    	float tempX = 0;
	    	float x = touchX;
	    	for(int y = touchY; y >= minY; y--){
	    		Coordinates coordinates = new Coordinates();
	    		
	    		x = touchX + (float)(y - touchY)/(float)(minY - touchY) * (float)(minX - touchX) ;
	    		x = x + tempX * ratio;
	    		coordinates.setX((int)x - deltaX);
	    		coordinates.setY(y - deltaY);
	    		grenadeLaunchers.put(i, coordinates);
	    		i++;
	    		
	    		if(i >= halfDiffY){
	    			reverseCount = true;
	    		}
	    		if(reverseCount == false){
	    			tempX = tempX + 1;
	    		}else{
	    			tempX = tempX - 1;
	    		}
	    	}
	    	
	    	int y = minY;
	    	for(int k = 0 ; k < 15; k++){
	    		if(k> 10){
	    			y = y-1;
	    		}else{
	    			y = y+1;
	    		}
	    		Coordinates coordinates = new Coordinates();
	    		if(minX > touchX){
	    			x = x + 2;
	    		}else{
	    			x = x - 2;
	    		}
	    		coordinates.setX((int)x - deltaX);
	    		coordinates.setY(y - deltaY);
	    		grenadeLaunchers.put(i, coordinates);
	    		i++;
	    	}
	    	launcherIndexCount = i;
	    	
	    	// Set image priodically //
	    	int imageId= launcherFrameCount;
	    	int framedelay = launcherIndexCount/(launcherFrameCount-2);
	    	int value = framedelay;
	    	for (i = 0 ; i < launcherIndexCount-2; i++ ) {
	    		Coordinates coordinates = grenadeLaunchers.get(i);
	    		coordinates.setImageID(imageId);
	    		grenadeLaunchers.put(i, coordinates);
	    		if(i >= value){
	    			imageId = imageId - 1;
	    			value = value + framedelay;
	    		}
			}
	    	
	    }catch(Exception ex){
		}
    }

    private Handler activateTimerHandler = new Handler();
	private Runnable activateTimerRunnable = new Runnable() {
       public void run() {
    	   try{
    		   if(isInterrupted){
    			   currentStatus = GameData.LAUNCHER_GROUNDED;
    			   return;
    		   }
    		   if(currentStatus != GameData.LAUNCHER_ACTIVATED){
    			   return;
    		   }
    		   timerFrameIndex = timerFrameIndex + 1;
    		   if(timerFrameIndex >= timerFrameCount){
    			   currentStatus = GameData.LAUNCHER_EXPLORING;
    			   timerFrameIndex = 0;
    		   }else{
    			   coordinate.setImageID(coordinate.getImageID()+1);
    			   activateTimerHandler.postDelayed(activateTimerRunnable, 1000);
    		   }
    	   }catch(Exception ex){
    		   Log.d(LOG_TAG, "activateTImerRunnable", ex);
    	   }
       }
	};

	 private Handler exploreHandler = new Handler();
		private Runnable exploreRunnable = new Runnable() {
	       public void run() {
	    	   try{
	    		   if(isInterrupted){
	    			   currentStatus = GameData.LAUNCHER_FINISH;
	    			   return;
	    		   }
	    		   if(currentStatus != GameData.LAUNCHER_EXPLORED && currentStatus != GameData.FINGER_CRASH){
	    			   return;
	    		   }
	    		   timerFrameIndex = timerFrameIndex + 1;
	    		   if(timerFrameIndex < exploreFrameCount){
	    			   coordinate.setImageID(coordinate.getImageID()+1);
	    			   exploreHandler.postDelayed(exploreRunnable, 50);
	    		   }else{
	    			   if(currentStatus == GameData.FINGER_CRASH){
	    				   currentStatus = GameData.GAME_OVER;
	    			   }else{
	    				   currentStatus = GameData.LAUNCHER_FINISH;
	    			   }
	    			   updateGameRecord();
	    		   }
	    	   }catch(Exception ex){
	    		   Log.d(LOG_TAG, "exploreRunnable", ex);
	    	   }
	       }
		};

	private void updateGameRecord(){
		if(currentStatus == GameData.GAME_OVER){
			onGameRecordListener.onGameOver();
			return;
		}
		// Blast grenade on the fields
		if(coordinate.getY() > holeY2){
			onGameRecordListener.onUpdateScoreCount(-1);
		}
	}
	
    /**
     * Set the coordinates of frame when launching.
     */
    public Coordinates getCoordinate(){
    	try{
    		// Grenade exploring
    		if(currentStatus == GameData.LAUNCHER_EXPLORING){
    			currentStatus = GameData.LAUNCHER_EXPLORED;
    			onGameRecordListener.onPlaySoundBlast();
    			
    			if(onGameRecordListener.onIsInsideHoleArea(coordinate.getX(), coordinate.getY())){
    				holeExplore();
    			}else{
    				flatExplore();
    			}
    			
    			// This is small grenade so adjust x,y 
    			if(coordinate.getY() < holeY2){
    				coordinate.setX(coordinate.getX()-40);
    				coordinate.setY(coordinate.getY()-70);
    			}
    			
    			// Grenade  blast under the touch fingers
    			if(isTouched && coordinate.getY() > holeY2){
    				onGameRecordListener.onVibrate();
    				currentStatus = GameData.FINGER_CRASH;
    			}
    			exploreHandler.postDelayed(exploreRunnable, 0);
    		}

    		// Grenade grounded
    		if(currentStatus == GameData.LAUNCHER_GROUNDED){
    			currentStatus  = GameData.LAUNCHER_ACTIVATED;
    			activateTimerHandler.postDelayed(activateTimerRunnable, 1000);
    		}

    		// Grenade grounded
    		if(currentStatus == GameData.LAUNCHER_DROPPED){
    			coordinate.setImageID(coordinate.getImageID()+1);
    			onGameRecordListener.setImageSize(coordinate.getImageID());
    			if(coordinate.getImageID() > launcherFrameCount + droppedFrameCount){
    				currentStatus = GameData.LAUNCHER_GROUNDED;
    			}
    		}
    		
    		// After launched a grenade
    		if(currentStatus == GameData.LAUNCHER_LAUNCHED){
    			coordinate = grenadeLaunchers.get(launcherFrameIndex);
    			if(launcherFrameIndex+launcherSpeed >= launcherIndexCount){
        			currentStatus = GameData.LAUNCHER_DROPPED;
        			grenadeLaunchers.clear();
        		}
    			launcherFrameIndex += launcherSpeed;
    		}
    		
    		// After pinch grenade
    		if(currentStatus == GameData.LAUNCHER_DISPATCH){
    			coordinate = grenadeLaunchers.get(launcherFrameIndex);
    			if(launcherFrameIndex+pinchValue + pinchSpeed >= launcherIndexCount){
        			currentStatus = GameData.LAUNCHER_EXPLORING;
        			coordinate.setImageID(launcherFrameCount+droppedFrameCount+timerFrameCount + 1);
        			return null;
        		}
    			launcherFrameIndex += pinchValue + pinchSpeed;
    			
    			// Control the speed
    			if(this.pinchValue > 0) {
    				this.pinchValue--;
    			}
    		}
    	}catch(Exception ex){
    	}

    	return coordinate;
    }

    /**
     * Explore grenade inside the hole
     */
    private void holeExplore(){
    	onGameRecordListener.onUpdateScoreCount(1);
    }
    
    /**
     * Explore grenade inside the hole
     */
    private void flatExplore(){
    	coordinate.setImageID(coordinate.getImageID()+exploreFrameCount);
		exploreFrameCount = exploreFrameCountHand;
    }
    
    
}
