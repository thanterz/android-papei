package com.example.sidecurb;

import android.app.Activity;
import android.net.MailTo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.view.Menu;
import android.view.MenuItem;

public class LoginActivity2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login2);
		
		String emailString = getResources().getString(R.id.emailinput);
		
		
		class CallApi extends AsyncTask<Void, Void, Boolean> {

			
			@Override
			protected Boolean doInBackground(Void... params) {
				// TODO: attempt authentication against a network service.

				try {
					// Simulate network access.
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					return false;
				}
				return null;

			}

			@Override
			protected void onPostExecute(final Boolean success) {
				
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_activity2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
