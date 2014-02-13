package com.example.intentslauncher;

import java.io.File;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private static final String LATITUDE_ = "37.422006";
	private static final String LONGITUDE_ = "-122.084095";
	private static final String GALLERY_ = "com.android.gallery3d";
	private static final String BROWSER_ = "com.android.browser";
	private static final String VIDEO_ = "android.media.action.VIDEO_CAPTURE";
	private static final String IMAGE_TYPE_ = "image/*";
	private static final String ZOOM_ = "20";
	private static final String MAP_LABEL_TEXT_ = "This place \nLat 37.422006, Lon -122.084095";
	private static final String PHOTO_NAME_ = "myPhoto.jpg";
	private static final int SELECT_PICTURE_ = 1;
	private static final int TAKE_PICTURE_ = 2;
	private static final String DIR_NAME_ = "CameraAppDemo";
	private ImageView selectedImage_;
	private static Uri selectedImageUri_;
	private static File file_;
	private static File dir_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		CreateDirectoryForPictures();
		showPicture();
	}

	private void CreateDirectoryForPictures() {
		dir_ = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), DIR_NAME_);
		if (!dir_.exists()) {
			dir_.mkdirs();
		}
	}

	public void showPicture() {
		selectedImage_ = (ImageView) findViewById(R.id.selectedImage);
		if (selectedImageUri_ != null) {
			selectedImage_.setImageURI(selectedImageUri_);
			selectedImage_.setVisibility(View.VISIBLE);
		} else {
			selectedImage_.setVisibility(View.INVISIBLE);
		}
	}

	public void showDialog(String text) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Error!");
		alertDialog.setMessage(text);
		alertDialog.show();
	}

	public void makeIntentByPackageName(String packageName) {
		Intent intent;
		PackageManager manager = getPackageManager();
		intent = manager.getLaunchIntentForPackage(packageName);
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException ex) {
			showDialog("Something wrong(maybe you don't have app to do this)");
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case SELECT_PICTURE_:
				selectedImageUri_ = data.getData();
				showPicture();
				break;
			case TAKE_PICTURE_:
				selectedImageUri_ = Uri.fromFile(file_);
				showPicture();
				break;
			default:
				selectedImageUri_ = null;
				break;
			}
		}
	}

	public void onGalleryButtonClick(View view) {
		makeIntentByPackageName(GALLERY_);
	}

	public void onBrowserButtonClick(View view) {
		makeIntentByPackageName(BROWSER_);
	}

	public void onMapButtonClick(View view) {
		String uriBegin = "geo:" + LATITUDE_ + "," + LONGITUDE_;
		String query = LATITUDE_ + "," + LONGITUDE_ + "(" + MAP_LABEL_TEXT_ + ")";
		String encodedQuery = Uri.encode(query);
		String uriString = uriBegin + "?q=" + encodedQuery + "&z=" + ZOOM_;
		Uri uri = Uri.parse(uriString);
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException ex) {
			showDialog("Something wrong");
		}
	}

	public void onImageButtonClick(View view) {
		Intent intent = new Intent();
		intent.setType(IMAGE_TYPE_);
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE_);

	}

	public void onPhotoButtonClick(View view) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		file_ = new File(dir_, PHOTO_NAME_);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file_));
		startActivityForResult(intent, TAKE_PICTURE_);
	}

	public void onVideoButtonClick(View view) {
		Intent intent = new Intent(VIDEO_);
		startActivity(intent);
	}

	public void onAudioButtonClick(View view) {
		Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
		startActivity(intent);
	}

}
