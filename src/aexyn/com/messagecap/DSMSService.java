package aexyn.com.messagecap;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class DSMSService extends Service {
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
			Uri uriSMSURI = Uri.parse("content://sms");
			Cursor cur = getBaseContext().getContentResolver().query(uriSMSURI, null, null, null, null);
			// this will make it point to the first record, which is the last SMS sent
			cur.moveToNext();
			String content = cur.getString(cur.getColumnIndex("body"));
			String contentid = cur.getString(cur.getColumnIndex("_id"));

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
		//        DAO = new DAOaBlackList(getBaseContext());
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
}