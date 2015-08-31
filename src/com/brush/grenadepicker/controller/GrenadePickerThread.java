/**
 *
 */
package com.brush.grenadepicker.controller;

import com.brush.grenadepicker.GrenadePickerPanel;
import com.brush.grenadepicker.imageprocess.BitmapLoader;

import android.graphics.Canvas;

import android.view.SurfaceHolder;

public class GrenadePickerThread extends Thread {

	// Surface holder that can access the physical surface
	private SurfaceHolder surfaceHolder;
	private GrenadePickerPanel gamePanel;
	// flag to hold game state
	private boolean running;
	private boolean isPause;
	
	public void setRunning(boolean running) {
		this.running = running;

	}
	public void setPause(boolean isPause) {
		this.isPause = isPause;
	}

	public GrenadePickerThread(SurfaceHolder surfaceHolder, GrenadePickerPanel gamePanel) {
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
		isPause = false;
	}

	@Override
	public void run() {
		Canvas canvas;
		while (running) {
			canvas = null;
			try {
				canvas = surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {
					// update game state
					this.gamePanel.update();
					// draws the canvas on the panel
					this.gamePanel.postInvalidate();
				}
			} finally {
				// in case of an exception the surface is not left in
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}

}
