package com.example.filemanager;

import java.io.File;
import java.util.List;

import com.example.filemanager.FileListAdapter.ViewHolderItem;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.webkit.MimeTypeMap;

class LoadImage extends AsyncTask<Object, Void, Bitmap> {

	private File file_;
	private ViewHolderItem holder_;
	private int position_;
	private ThumbCache cache_;
	private Context context_;

	public LoadImage(Context context, ViewHolderItem holder, ThumbCache cache, int position, String path) {
		this.holder_ = holder;
		this.position_ = position;
		this.cache_ = cache;
		this.context_ = context;
		this.file_ = new File(path);
	}

	@Override
	protected void onPreExecute() {
		if (!file_.isDirectory()) {
			holder_.fileIcon_.setImageResource(R.drawable.blank);
		} else {
			holder_.fileIcon_.setImageResource(R.drawable.ic_launcher);
		}
	}

	@Override
	protected Bitmap doInBackground(Object... params) {
		Bitmap bitmap = cache_.getBitmapFromMemCache(file_.getAbsolutePath());
		Drawable icon = null;
		if (file_.isFile() && bitmap == null) {
			bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(file_.getAbsolutePath()), 50, 50);
			if (bitmap == null) {
				bitmap = ThumbnailUtils.createVideoThumbnail(file_.getAbsolutePath(), 50);
			}
			if (bitmap == null && MimeTypeMap.getFileExtensionFromUrl(file_.getAbsolutePath()).equals("apk")) {
				PackageInfo packageInfo = context_.getPackageManager().getPackageArchiveInfo(file_.getAbsolutePath(),
						PackageManager.GET_ACTIVITIES);
				if (packageInfo != null) {
					ApplicationInfo appInfo = packageInfo.applicationInfo;
					appInfo.sourceDir = file_.getAbsolutePath();
					appInfo.publicSourceDir = file_.getAbsolutePath();
					icon = appInfo.loadIcon(context_.getPackageManager());
					bitmap = ((BitmapDrawable) icon).getBitmap();
				}
			}
			if (bitmap == null) {
				Intent intent = new Intent();
				intent.setAction(android.content.Intent.ACTION_VIEW);
				String fileName = file_.getName();
				String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
				String type = null;
				if (extension != null) {
					MimeTypeMap mime = MimeTypeMap.getSingleton();
					type = mime.getMimeTypeFromExtension(extension);
				}
				if (type != null) {
					intent.setDataAndType(Uri.fromFile(file_), type);
					final List<ResolveInfo> matches = context_.getPackageManager().queryIntentActivities(intent, 0);
					for (ResolveInfo match : matches) {
						icon = match.loadIcon(context_.getPackageManager());
						break;
					}
					if (icon != null) {
						bitmap = ((BitmapDrawable) icon).getBitmap();
					}
				}

			}
		}
		if (bitmap != null) {
			cache_.addBitmapToMemoryCache(file_.getAbsolutePath(), bitmap);
		}
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (result != null && position_ == holder_.position_) {
			holder_.fileIcon_.setImageBitmap(result);
		}
	}
}
