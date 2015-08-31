package com.brush.grenadepicker.imageprocess;

import com.brush.grenadepicker.component.Grenade;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class RanderImage {
	private BitmapLoader bitmapLoader;
	private Grenade.Coordinates coords;
	
	public RanderImage(BitmapLoader bitmapLoader){
		this.bitmapLoader = bitmapLoader;
	}

	public void draw(Canvas canvas, Grenade grenade ) {
		try{
			Bitmap bitmap;
			coords = grenade.getCoordinate();
			if(coords != null){
				bitmap = bitmapLoader.getBitmap(coords.getImageID());
				if(bitmap != null){
		            canvas.drawBitmap(bitmap, coords.getX(), coords.getY(), null);
				}
			}
		}catch(Exception ex){
			// Do nothing
		}
	}
}
