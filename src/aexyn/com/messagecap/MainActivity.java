package aexyn.com.messagecap;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {

	int TOTAL_MESSAGE_SENT = 0;
	int PREVIOUS_ID = 0;
	private static final String CONTENT_SMS = "content://sms";

	String TAG = "null";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("1", "1");
		startService(new Intent(this, DSMSService.class));
		
		Count();
	}

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
				//numPagesinMessage = 1 + (numChars - 1) / 153; // -1 because 0-153 is message 1, 154-307 is next ...
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
