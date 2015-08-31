package com.brush.grenadepicker.controller;

import com.brush.grenadepicker.component.Grenade;

public class PinchController implements Runnable {

	private static final int DISPATCH = 6;
    private Grenade grenade;
    private int currentTouchX;
    private int currentTouchY;
    private int firstTouchX;
    private int firstTouchY;
    private int prevTouchX;
    private int prevTouchY;
    private int deltaX;
    private int deltaY;
    private int minY;
    private float proportionM;
    private float pinchValue;
    private int screenWidth;
    private int screenHeight;

	public PinchController(int screenWidth, int screenHeight) {
	       // store parameter for later user
		this.grenade = null;
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
	}

	public boolean isActive() {
		if(grenade != null && this.grenade.isTouched() == true){
			return true;
		}else{
			return false;
		}
   }

	public void addPinchValue(float value){
		pinchValue = pinchValue + value;
	}
	
	public void setPinchValue(int value){
		pinchValue = value;
	}
	
	public float getPinchValue(){
		return this.pinchValue;
	}
	public int getDeltaX(){
		return this.deltaX;
	}
	
	public int getDeltaY(){
		return this.deltaY;
	}
	
	public void setPrevTouchX(int  prevTouchX){
		this.prevTouchX = prevTouchX;
	}
	
	public int getPrevTouchX(){
		return this.prevTouchX;
	}
	public void setPrevTouchY(int  prevTouchY){
		this.prevTouchY = prevTouchY;
	}
	
	public int getPrevTouchY(){
		return this.prevTouchY;
	}
	
	public void setFirstTouchX(int  firstTouchX, int deltaX){
		this.deltaX = deltaX;
		this.firstTouchX = firstTouchX;
		this.prevTouchX = firstTouchX;
	}
	
	public void setFirstTouchX(int  firstTouchX){
		this.firstTouchX = firstTouchX;
	}
	
	public int getFirstTouchY(){
		return this.firstTouchY;
	}
	
	public void setFirstTouchY(int  firstTouchY){
		this.firstTouchY = firstTouchY;
	}
	
	public int getFirstTouchX(){
		return this.firstTouchX;
	}
	
	public void setFirstTouchY(int  firstTouchY, int deltaY){
    	this.deltaY = deltaY;
		this.firstTouchY = firstTouchY;
		this.prevTouchY = firstTouchY;
	}
	
	public void setCurrentTouchX(int currentTouchX){
		this.currentTouchX = currentTouchX;
		this.grenade.getCoordinate().setX((int)(currentTouchX-deltaX));
	}
	
	public void setCurrentTouchY(int currentTouchY){
		this.currentTouchY = currentTouchY;
		this.grenade.getCoordinate().setY((int)(currentTouchY-deltaY));
	}
	
	public int getCurrentTouchY(){
		return this.currentTouchY;
	}
	
	public void setPinchActivate(Grenade grenade){
		this.grenade = grenade;
    	this.grenade.setTouched(true);
		pinchValue = 0;
	}
	
	public void setPinchDeActivate(){
		this.grenade.setTouched(false);
		pinchValue = 0;
	}
	
	public Grenade getContent(){
		return this.grenade;
	}
	
	public void start(int minY){
		this.minY = minY;
//		this.proportionM= (float)(couurentTouchX - firstTouchX)/(couurentTouchY - firstTouchY);
		Thread t1;
        t1=new Thread(this);
        t1.start();
	}
	
    @Override
    public void run() {
        try {
        	grenade.createPitcherCoordinate(this.currentTouchX, this.deltaX, this.currentTouchY, this.deltaY, this.minY, this.firstTouchX, this.firstTouchY);
        	grenade.setPinchActive(true);
        	grenade.setPinchValue(this.pinchValue, this.screenHeight, this.currentTouchY);
        	grenade.setStatus(DISPATCH);
        } catch (Exception e) {
        	
        }
    }
}
