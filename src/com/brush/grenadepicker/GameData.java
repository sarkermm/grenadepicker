

package com.brush.grenadepicker;

public class GameData  {
	
	// Game status
	public static final int ON_START = 198;
	public static final int ON_STOP = 199;
	public static final int ON_LAUNCH = 200;
	public static final int ON_RUNNING = 201;
	public static final int ON_RESUME = 202;
	public static final int ON_PAUSE = 203;
	public static final int GAME_OVER = 204;
	public static final int FINGER_CRASH = 205;
	public static final int ON_DESTROY = 400;
	
	// Level of game
	public static final int BEGINEER = 103;
	public static final int ADVANCE = 105;
	public static final int EXPERT = 107;
	
	// Delay time to launch grenade- Begineer
	public static final int BEGINEER_DELAY_TIME = 900;
	// Delay time to launch grenade- Advance
	public static final int ADVANCE_DELAY_TIME = 650;
	// Delay time to launch grenade- Expart
	public static final int EXPERT_DELAY_TIME = 500;
	
	// Launcher grenade status 
	public static final int LAUNCHER_READY = -1;
	public static final int LAUNCHER_LAUNCHED = 0;
	public static final int LAUNCHER_DROPPED = 1;
	public static final int LAUNCHER_GROUNDED = 2;
	public static final int LAUNCHER_ACTIVATED = 3;
	public static final int LAUNCHER_EXPLORING = 4;
	public static final int LAUNCHER_EXPLORED = 5;
	public static final int LAUNCHER_DISPATCH = 6;
	public static final int LAUNCHER_RUNNING = 7;
	public static final int LAUNCHER_FINISH = 8;
	public static final int LAUNCHER_PAUSE = 20;
	public static final int LEFT_SIDE_GRENADE = 1;
	public static final int RIGHT_SIDE_GRENADE = 2;
	
	// Margin for all side where grenade does not fall
	public static final int MARGIN = 180;
	
	// y length of the score area
	public static final int SCORE_AREA = 200;
	
	// Width of each image of grenade
	public static final int GRENADE_IMAGE_WIDTH = 50;
	
	// Height of each image of grenade
	public static final int GRENADE_IMAGE_HEIGHT = 40;
	
	// Buffer size of grenade width and height used for smooth youch 
	public static final int PIXEL_BUFFER = 20;
	
	// Value where pinch ended
	public static int MIN_PINCH_VALUE_Y = 100;   
	
	// Width of the border 30
	// Used for grenade move, grenade size is 50 x38
	public static final int BORDER_WIDTH = 50;
	public static final float BORDER_WIDTH_RATIO = (float)(40)/1085;
	public static final float BORDER_HEIGHT_RATIO = (float)(70)/1529;
	
	// Game start timer
	public static final int TIMER_COUNT = 3;

	// How many life
	public static final int LIFE_COUNT = 5;
	
	// Screen Height 1529 
	// Y : Start from top: border 51 , hole 263, border 69, ground 1088, border 58
	// Screen Width 1085 
	// X : Start from left: border 30 , free space before hole 242, hole border 32, hole 471, hole border 32
	// free space before last border 248, border 30 
	public static final float ratioX1 = (float)(30+242+32)/1085;
	public static final float ratioX2 = (float)(30+242+32+471)/1085;
	public static final float ratioY2 = (float)(51+263)/1529;
	
	
	
}
