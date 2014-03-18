package com.example.filemanager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;

public class MainActivity extends Activity {

	public class SortFileNameAndType implements Comparator<File> {

		@Override
		public int compare(File file1, File file2) {
			if (file1.isDirectory()) {
				if (file2.isDirectory()) {
					return String.valueOf(file1.getName().toLowerCase()).compareTo(file2.getName().toLowerCase());
				} else {
					return -1;
				}
			} else {
				if (file2.isDirectory()) {
					return 1;
				} else {
					return String.valueOf(file1.getName().toLowerCase()).compareTo(file2.getName().toLowerCase());
				}
			}
		}
	}

	private static final String CURRENT_FOLDER_NAME = "current_folder_name";
	private static final String PREVIOUS_FOLDER_NAME = "previous_folder_name";

	private ListView fileList_;

	private String previousFolder_;
	private String currentFolder_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState != null) {
			currentFolder_ = savedInstanceState.getString(CURRENT_FOLDER_NAME);
			previousFolder_ = savedInstanceState.getString(PREVIOUS_FOLDER_NAME);
		} else {
			currentFolder_ = Environment.getExternalStorageDirectory().getAbsolutePath();
			previousFolder_ = Environment.getExternalStorageDirectory().getParentFile().getAbsolutePath();
		}
		fileList_ = (ListView) findViewById(R.id.file_list);
		changeTitle(new File(Environment.getExternalStorageDirectory().getAbsolutePath()).getName());
		openFolder(currentFolder_);
		fileList_.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (((FileListAdapter) fileList_.getAdapter()).getItem(position).isDirectory()) {
					changeTitle(((FileListAdapter) fileList_.getAdapter()).getItem(position).getName());
					previousFolder_ = ((FileListAdapter) fileList_.getAdapter()).getItem(position).getParentFile()
							.getAbsolutePath();
					openFolder(((FileListAdapter) fileList_.getAdapter()).getItem(position).getAbsolutePath());
				} else {
					Intent intent = new Intent();
					intent.setAction(android.content.Intent.ACTION_VIEW);
					String type = null;
					String fileName = ((FileListAdapter) fileList_.getAdapter()).getItem(position).getName();
					String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
					if (extension != null) {
						MimeTypeMap mime = MimeTypeMap.getSingleton();
						type = mime.getMimeTypeFromExtension(extension);
					}
					if (type != null) {
						intent.setDataAndType(
								Uri.fromFile(((FileListAdapter) fileList_.getAdapter()).getItem(position)), type);
						try {
							startActivity(intent);
						} catch (ActivityNotFoundException ex) {
							ex.printStackTrace();
						}
					} else {
						Toast.makeText(
								parent.getContext(),
								parent.getContext().getString(R.string.no_app_message) + " "
										+ ((FileListAdapter) fileList_.getAdapter()).getItem(position).getName(),
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (!previousFolder_.equals(Environment.getExternalStorageDirectory().getParentFile().getAbsolutePath())) {
			openFolder(previousFolder_);
			changeTitle(new File(previousFolder_).getName());
			previousFolder_ = new File(previousFolder_).getParentFile().getAbsolutePath();
		} else {
			this.finish();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(CURRENT_FOLDER_NAME, currentFolder_);
		outState.putString(PREVIOUS_FOLDER_NAME, previousFolder_);
		super.onSaveInstanceState(outState);
	}

	public void openFolder(String currentFolderName) {
		currentFolder_ = currentFolderName;
		File dir = new File(currentFolderName);
		File[] fileList = dir.listFiles();
		if (fileList != null) {
			List<File> directoryListing = new ArrayList<File>();
			directoryListing.addAll(Arrays.asList(fileList));
			Collections.sort(directoryListing, new SortFileNameAndType());
			if (fileList_.getAdapter() == null) {
				fileList_.setAdapter(new FileListAdapter(this, 0, directoryListing));
			} else {
				((FileListAdapter) fileList_.getAdapter()).clear();
				((FileListAdapter) fileList_.getAdapter()).addAll(directoryListing);
			}
		}
	}

	public void changeTitle(String title) {
		if (title.equals(new File(Environment.getExternalStorageDirectory().getAbsolutePath()).getName())) {
			setTitle(R.string.app_name);
		} else {
			setTitle(title);
		}
	}

}
