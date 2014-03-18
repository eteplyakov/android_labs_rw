package com.example.filemanager;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class ThumbCache {

	private LruCache<String, Bitmap> memoryCache_;

	public ThumbCache(){
		memoryCache_ = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 8192));
	}
	
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			memoryCache_.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(String key) {
		return memoryCache_.get(key);
	}

}
