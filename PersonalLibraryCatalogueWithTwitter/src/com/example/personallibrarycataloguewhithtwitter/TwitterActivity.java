package com.example.personallibrarycataloguewhithtwitter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class TwitterActivity extends Activity {

	public class AthorizationTask extends AsyncTask<Void, Void, Boolean> {

		String authUrl = null;

		protected void onPreExecute() {
		}

		protected Boolean doInBackground(Void... params) {
			try {
				authUrl = provider.retrieveRequestToken(consumer, CALLBACK_URL);
			} catch (OAuthMessageSignerException e) {
				e.printStackTrace();
				return false;
			} catch (OAuthNotAuthorizedException e) {
				e.printStackTrace();
				return false;
			} catch (OAuthExpectationFailedException e) {
				e.printStackTrace();
				return false;
			} catch (OAuthCommunicationException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (success == true) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
			}
		}
	}

	public class GetTokenTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			try {
				provider.retrieveAccessToken(consumer, verifier);
			} catch (OAuthMessageSignerException e) {
				e.printStackTrace();
				return null;
			} catch (OAuthNotAuthorizedException e) {
				e.printStackTrace();
				return null;
			} catch (OAuthExpectationFailedException e) {
				e.printStackTrace();
				return null;
			} catch (OAuthCommunicationException e) {
				e.printStackTrace();
				return null;
			}
			return new String[] { consumer.getToken(), consumer.getTokenSecret() };
		}

		@Override
		protected void onPostExecute(String[] values) {
			if (values != null) {
				Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
				editor.putString("token", consumer.getToken());
				editor.putString("token_secret", consumer.getTokenSecret());
				editor.commit();

				progress.cancel();
				twitButton_.setText(R.string.twit);
				twitButton_.setEnabled(true);
			}
		}

	}

	public class TwitTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			String accessToken = prefs.getString("token", null);
			String accessSecret = prefs.getString("token_secret", null);

			CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
			consumer.setTokenWithSecret(accessToken, accessSecret);

			String status = null;
			try {
				status = URLEncoder.encode(twitEdit_.getText().toString(), "utf-8");
			} catch (UnsupportedEncodingException e1) {
				return null;
			}

			HttpPost request = new HttpPost(UPDATE_STATUS_URL + status);

			try {
				consumer.sign(request);
			} catch (OAuthMessageSignerException e) {
				return null;
			} catch (OAuthExpectationFailedException e) {
				return null;
			} catch (OAuthCommunicationException e) {
				return null;
			}

			DefaultHttpClient httpClient = new DefaultHttpClient();
			try {
				HttpResponse response = httpClient.execute(request);
				return response.getStatusLine().getStatusCode();
			} catch (ClientProtocolException e) {
				return null;
			} catch (IOException e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result == null) {
				Toast.makeText(getApplicationContext(), getString(R.string.twit_error), Toast.LENGTH_SHORT).show();
			} else {
				if (result != HttpURLConnection.HTTP_OK) {
					Toast.makeText(getApplicationContext(),
							getString(R.string.twit_error) + "\n" + getString(R.string.error_code) + " " + result,
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(), getString(R.string.twit_ok), Toast.LENGTH_SHORT).show();
				}
			}
			progress.cancel();
			twitButton_.setEnabled(true);
			super.onPostExecute(result);
		}
	}

	private static final String CALLBACK_URL = "twit://app";
	private static final String CONSUMER_KEY = "OQJNz6jGIOEWAIgsCO7o3A";
	private static final String CONSUMER_SECRET = "Py7MwXxdZFIgctTC5vihlNP1Ka1m8xGqvefVax81k";
	private static final String REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
	private static final String ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
	private static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";
	private static final String UPDATE_STATUS_URL = "https://api.twitter.com/1.1/statuses/update.json?status=";

	public static final String STATUS_KEY = "status";

	private EditText twitEdit_;
	private Button twitButton_;
	private TextView charactersView_;
	private ProgressDialog progress;

	private CommonsHttpOAuthProvider provider = null;
	private CommonsHttpOAuthConsumer consumer = null;
	private String verifier;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter);

		twitEdit_ = (EditText) findViewById(R.id.twit_message);
		twitButton_ = (Button) findViewById(R.id.twit_button);
		charactersView_ = (TextView) findViewById(R.id.characters);

		progress = new ProgressDialog(this);
		progress.setMessage(getString(R.string.sending));
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setIndeterminate(true);

		consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		provider = new CommonsHttpOAuthProvider(REQUEST_TOKEN_URL, ACCESS_TOKEN_URL, AUTHORIZE_URL);

		twitEdit_.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				charactersView_.setText(String.valueOf(s.length()));
			}
		});

		Intent intent = getIntent();
		twitEdit_.setText(intent.getCharSequenceExtra(STATUS_KEY));
	}

	@Override
	protected void onNewIntent(Intent intent) {

		twitButton_.setText(R.string.wait);

		Uri uri = intent.getData();
		verifier = null;
		if (uri != null) {
			verifier = uri.getQueryParameter("oauth_verifier");
		}
		if (verifier != null) {
			new GetTokenTask().execute((Void) null);
		}
		super.onNewIntent(intent);
	}

	public void onTwitClicked(View view) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String accessToken = prefs.getString("token", null);
		String accessSecret = prefs.getString("token_secret", null);

		twitButton_.setEnabled(false);
		progress.show();
		if (accessToken == null || accessSecret == null) {
			twitButton_.setText(R.string.athorization);

			new AthorizationTask().execute((Void) null);
		} else {
			new TwitTask().execute((Void) null);
		}
	}
}
