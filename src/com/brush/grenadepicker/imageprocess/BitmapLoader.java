package com.brush.grenadepicker.imageprocess;

import java.util.HashMap;
import java.util.Map;

import com.brush.grenadepicker.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapLoader {

	private Map<Integer, Bitmap> bitmapCache = new HashMap<Integer, Bitmap>();
	private Context context;

	/**
     * Constructor called on instantiation.
     * @param context Context of calling activity.
     */
	public BitmapLoader(Context context){
		this.context = context;
	}

	 /**
     * Fill the bitmap cache.
     */
	public void loadBitmap(){
		bitmapCache.put(0, BitmapFactory.decodeResource(context.getResources(), R.drawable.b0));
		bitmapCache.put(1, BitmapFactory.decodeResource(context.getResources(), R.drawable.b1));
		bitmapCache.put(2, BitmapFactory.decodeResource(context.getResources(), R.drawable.b2));
		bitmapCache.put(3, BitmapFactory.decodeResource(context.getResources(), R.drawable.b3));
		bitmapCache.put(4, BitmapFactory.decodeResource(context.getResources(), R.drawable.b4));
		bitmapCache.put(5, BitmapFactory.decodeResource(context.getResources(), R.drawable.b5));
		bitmapCache.put(6, BitmapFactory.decodeResource(context.getResources(), R.drawable.b6));
		bitmapCache.put(7, BitmapFactory.decodeResource(context.getResources(), R.drawable.b7));
		bitmapCache.put(8, BitmapFactory.decodeResource(context.getResources(), R.drawable.b8));
		bitmapCache.put(9, BitmapFactory.decodeResource(context.getResources(), R.drawable.b9));
		bitmapCache.put(10, BitmapFactory.decodeResource(context.getResources(), R.drawable.b10));
		bitmapCache.put(11, BitmapFactory.decodeResource(context.getResources(), R.drawable.b11));
		bitmapCache.put(12, BitmapFactory.decodeResource(context.getResources(), R.drawable.b12));
		bitmapCache.put(13, BitmapFactory.decodeResource(context.getResources(), R.drawable.b13));
		bitmapCache.put(14, BitmapFactory.decodeResource(context.getResources(), R.drawable.b14));
		
		bitmapCache.put(15, BitmapFactory.decodeResource(context.getResources(), R.drawable.b15));
		bitmapCache.put(16, BitmapFactory.decodeResource(context.getResources(), R.drawable.b16));
		
		bitmapCache.put(17, BitmapFactory.decodeResource(context.getResources(), R.drawable.b17));
//		bitmapCache.put(18, BitmapFactory.decodeResource(context.getResources(), R.drawable.b18));
		bitmapCache.put(18, BitmapFactory.decodeResource(context.getResources(), R.drawable.b19));

		bitmapCache.put(19, BitmapFactory.decodeResource(context.getResources(), R.drawable.b20));
		bitmapCache.put(20, BitmapFactory.decodeResource(context.getResources(), R.drawable.b21));
		bitmapCache.put(21, BitmapFactory.decodeResource(context.getResources(), R.drawable.b22));
		bitmapCache.put(22, BitmapFactory.decodeResource(context.getResources(), R.drawable.b23));
		bitmapCache.put(23, BitmapFactory.decodeResource(context.getResources(), R.drawable.b24));
		bitmapCache.put(24, BitmapFactory.decodeResource(context.getResources(), R.drawable.b25));
		bitmapCache.put(25, BitmapFactory.decodeResource(context.getResources(), R.drawable.b26));
		bitmapCache.put(26, BitmapFactory.decodeResource(context.getResources(), R.drawable.b27));
		bitmapCache.put(27, BitmapFactory.decodeResource(context.getResources(), R.drawable.b28));

		bitmapCache.put(28, BitmapFactory.decodeResource(context.getResources(), R.drawable.b29));
		bitmapCache.put(29, BitmapFactory.decodeResource(context.getResources(), R.drawable.b30));
		bitmapCache.put(30, BitmapFactory.decodeResource(context.getResources(), R.drawable.b31));
		bitmapCache.put(31, BitmapFactory.decodeResource(context.getResources(), R.drawable.b32));
		bitmapCache.put(32, BitmapFactory.decodeResource(context.getResources(), R.drawable.b33));

		bitmapCache.put(99, BitmapFactory.decodeResource(context.getResources(), R.drawable.life));
		bitmapCache.put(100, BitmapFactory.decodeResource(context.getResources(), R.drawable.background));
	}

	public Bitmap getBitmap(int bitmapID){
		Bitmap bitmap = null;
		try{
			if(bitmapID >= 0){
				bitmap = bitmapCache.get(bitmapID);
			}
		}catch(Exception ex){
		}
		return bitmap;
	}
}
