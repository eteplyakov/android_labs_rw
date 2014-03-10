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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity {

	public class SortFileName implements Comparator<File> {
		@Override
		public int compare(File f1, File f2) {
			return f1.getName().compareTo(f2.getName());
		}
	}

	public class SortFolder implements Comparator<File> {
		@Override
		public int compare(File f1, File f2) {
			if ((f1.isDirectory() && f2.isDirectory()) || (!f1.isDirectory() && !f2.isDirectory())) {
				return 0;
			} else {
				if (f1.isDirectory() && !f2.isDirectory()) {
					return -1;
				} else {
					return 1;
				}
			}

		}
	}

	private ListView fileList_;
	private FileListAdapter fileListAdapter_;

	private String previousFolder_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fileList_ = (ListView) findViewById(R.id.file_list);
		previousFolder_ = Environment.getExternalStorageDirectory().getParentFile().getAbsolutePath();
		changeTitle(new File(Environment.getExternalStorageDirectory().getAbsolutePath()).getName());
		getFileList(Environment.getExternalStorageDirectory().getAbsolutePath());
		fileList_.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (fileListAdapter_.getItem(position).isDirectory()) {
					changeTitle(fileListAdapter_.getItem(position).getName());
					previousFolder_ = fileListAdapter_.getItem(position).getParentFile().getAbsolutePath();
					getFileList(fileListAdapter_.getItem(position).getAbsolutePath());
				} else {
					Intent intent = new Intent();
					intent.setAction(android.content.Intent.ACTION_VIEW);
					intent.setData(Uri.fromFile(fileListAdapter_.getItem(position)));
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (!previousFolder_.equals(Environment.getExternalStorageDirectory().getParentFile().getAbsolutePath())) {
			getFileList(previousFolder_);
			changeTitle(new File(previousFolder_).getName());
			previousFolder_ = new File(previousFolder_).getParentFile().getAbsolutePath();
		} else {
			this.finish();
		}
	}

	public void getFileList(String currentFolderName) {
		File dir = new File(currentFolderName);
		File[] fileList = dir.listFiles();
		if (fileList != null) {
			List<File> directoryListing = new ArrayList<File>();
			directoryListing.addAll(Arrays.asList(fileList));
			Collections.sort(directoryListing, new SortFileName());
			Collections.sort(directoryListing, new SortFolder());
			fileList = directoryListing.toArray(new File[0]);
			fileListAdapter_ = new FileListAdapter(this, 0, fileList);
			fileList_.setAdapter(fileListAdapter_);
		}
	}
	
	public void changeTitle(String title){
		if(title.equals(new File(Environment.getExternalStorageDirectory().getAbsolutePath()).getName())){
			setTitle(R.string.app_name);
		} else {
			setTitle(title);
		}
	}

}
