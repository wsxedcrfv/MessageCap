package aexyn.com.messagecap;

import java.util.Currency;

import javax.crypto.spec.OAEPParameterSpec;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.app.Activity;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Service {

	//	@Override
	//	protected void onCreate(Bundle savedInstanceState) {
	//		super.onCreate(savedInstanceState);
	//		setContentView(R.layout.activity_main);
	//		Log.d("1", "1");
	//		Count();
	//	}
	int TOTAL_MESSAGE_SENT = 0;
	int PREVIOUS_ID = 0;
	private static final String CONTENT_SMS = "content://sms";

	String TAG = "null";

	private class MyContentObserver extends ContentObserver {
		ContentValues values = new ContentValues();
		int threadId;

		public MyContentObserver() {
			super(null);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			Log.v(TAG, "****************************** SMS change detected *************************************");
			Log.v(TAG, "Notification on SMS observer");
			// save the message to the SD card here
			Uri uriSMSURI = Uri.parse("content://sms/sent");
			Cursor cur = getBaseContext().getContentResolver().query(uriSMSURI, null, null, null, null);
			// this will make it point to the first record, which is the last SMS sent
			cur.moveToNext();
			String content = cur.getString(cur.getColumnIndex("body"));
			//			String contentid = cur.getString(cur.getColumnIndex("_id"));
			int contentid = cur.getInt(cur.getColumnIndex("_id"));
			if (contentid != PREVIOUS_ID) {
				Count();
				Toast.makeText(getApplicationContext(), TOTAL_MESSAGE_SENT + " : " + PREVIOUS_ID + "  : " + contentid, Toast.LENGTH_LONG).show();
			}
			Log.v(TAG, "content: " + content + "id:" + contentid);
		}

		@Override
		public boolean deliverSelfNotifications() {
			return false;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.v(TAG, "starting........");
		MyContentObserver contentObserver = new MyContentObserver();
		ContentResolver contentResolver = getBaseContext().getContentResolver();
		contentResolver.registerContentObserver(Uri.parse("content://sms"), true, contentObserver);
		//	        DAO = new DAOaBlackList(getBaseContext());
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "stopping........");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v(TAG, "Received start id " + startId + ": " + intent);
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public void onStart(Intent intent, int startid) {
		Log.v(TAG, "onStart........");
	}

	//	@Override  
	//    public void onCreate() {

	//         SmsContent content = new SmsContent(new Handler());  
	//         // REGISTER ContetObserver
	//         SMSObserver o;
	//         log
	//         this.getContentResolver().
	//             registerContentObserver(Uri.parse("content://sms/"), true, o);  

	//    } 

	private void Count() {
		// TODO Auto-generated method stub
		Uri allMessages = Uri.parse("content://sms/sent");
		Log.d("1", "2");
		Cursor c = getContentResolver().query(allMessages, null, null, null, null);
		Log.d("1", "3");
		int totalMessages = 0;
		Log.d("1", "4");

		while (c.moveToNext()) {
			Log.d("While", "1");
			String messageBody = c.getString(c.getColumnIndex("body"));
			long numCharsinMessage = messageBody.length();
			//		    double numberOfMessages = messageLength / 160.0;

			//			double numCharsinMessage;
			double numPagesinMessage;

			if (numCharsinMessage <= 160)
				numPagesinMessage = 1;
			else {
				//				numPagesinMessage = 1 + (numChars - 1) / 153; // -1 because 0-153 is message 1, 154-307 is next ...
				numPagesinMessage = numCharsinMessage / 153.0;
			}
			//			totalSMSsent += numSMSthisMessage;

			double numberOfMessagesRoundedUp = Math.ceil(numPagesinMessage);

			//			totalMessages = (int) (totalMessages + numberOfMessagesRoundedUp);
			TOTAL_MESSAGE_SENT = (int) (TOTAL_MESSAGE_SENT + numberOfMessagesRoundedUp);
			Log.d("while", "2");
		}
		Log.d("1", "5");

		Toast.makeText(getApplicationContext(), totalMessages + "", Toast.LENGTH_LONG).show();
		Log.d("1", "6");
		c.close();
		Log.d("1", "7");
	}

}
