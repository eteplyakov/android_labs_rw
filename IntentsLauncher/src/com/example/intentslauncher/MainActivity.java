package com.example.intentslauncher;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private static final String LATITUDE = "37.422006";
	private static final String LONGITUDE = "-122.084095";
	private static final String BROWSER = "com.android.browser";
	private static final String VIDEO = "android.media.action.VIDEO_CAPTURE";
	private static final String IMAGE_TYPE = "image/*";
	private static final String ZOOM = "20";
	private static final String MAP_LABEL_TEXT = "This place \nLat 37.422006, Lon -122.084095";
	private static final String PHOTO_NAME = "myPhoto.jpg";
	private static final int SELECT_PICTURE = 1;
	private static final int TAKE_PICTURE = 2;
	private static final String photoDirectory_NAME = "CameraAppDemo";
	private static Uri selectedImageUri_;
	private static File selectPhoto;
	private static File photoDirectory_;
	private ImageView selectedImage_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		photoDirectory_ = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), photoDirectory_NAME);
		if (!photoDirectory_.exists()) {
			photoDirectory_.mkdirs();
		}
		showPicture();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case SELECT_PICTURE:
				selectedImageUri_ = data.getData();
				showPicture();
				break;
			case TAKE_PICTURE:
				selectedImageUri_ = Uri.fromFile(selectPhoto);
				showPicture();
				break;
			default:
				selectedImageUri_ = null;
				break;
			}
		}
	}

	public void showPicture() {
		selectedImage_ = (ImageView) findViewById(R.id.selected_image);
		if (selectedImageUri_ != null) {
			selectedImage_.setImageURI(selectedImageUri_);
			selectedImage_.setVisibility(View.VISIBLE);
		} else {
			selectedImage_.setVisibility(View.INVISIBLE);
		}
	}

	public void showDialog(String text) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(getString(R.string.app_error_message));
		alertDialog.setMessage(text);
		alertDialog.show();
	}

	public void onGalleryButtonClick(View view) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"));
		startActivity(intent);
	}

	public void onBrowserButtonClick(View view) {
		PackageManager manager = getPackageManager();
		Intent intent = manager.getLaunchIntentForPackage(BROWSER);
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException ex) {
			showDialog(getResources().getString(R.string.app_error_message));
		}
	}

	public void onMapButtonClick(View view) {
		String uriBegin = "geo:" + LATITUDE + "," + LONGITUDE;
		String query = LATITUDE + "," + LONGITUDE + "(" + MAP_LABEL_TEXT + ")";
		String encodedQuery = Uri.encode(query);
		String uriString = uriBegin + "?q=" + encodedQuery + "&z=" + ZOOM;
		Uri uri = Uri.parse(uriString);
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException ex) {
			showDialog(getResources().getString(R.string.map_error_message));
		}
	}

	public void onImageButtonClick(View view) {
		Intent intent = new Intent();
		intent.setType(IMAGE_TYPE);
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), SELECT_PICTURE);
	}

	public void onPhotoButtonClick(View view) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		selectPhoto = new File(photoDirectory_, PHOTO_NAME);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(selectPhoto));
		startActivityForResult(intent, TAKE_PICTURE);
	}

	public void onVideoButtonClick(View view) {
		Intent intent = new Intent(VIDEO);
		startActivity(intent);
	}

	public void onAudioButtonClick(View view) {
		Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
		startActivity(intent);
	}

}
