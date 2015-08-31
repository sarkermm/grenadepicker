package com.brush.grenadepicker;

public interface OnGameRecordListener {
	 public abstract boolean onGetTouchStatus();
	 public abstract boolean onIsInsideHoleArea(int x, int y);
	 public abstract void onVibrate();
	 public abstract void onPlaySoundBlast();
	 public abstract void onUpdateScoreCount(int count);
	 public abstract void onGameOver();
	 public abstract void setImageSize(int bitmapId);
}
