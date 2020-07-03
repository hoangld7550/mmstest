package vn.tapbi.testmms;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent){
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;

        String str = "";
        String address = "";
        if (bundle != null){
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                str = msgs[i].getMessageBody().toString();
                address = msgs[i].getOriginatingAddress().toString();
            }
            Toast.makeText(context,address+": "+ str, Toast.LENGTH_LONG).show();
            Log.e("inbox: ", address+": "+ str);

            ContentValues sentSms = new ContentValues();
            sentSms.put("address", address);
            sentSms.put("body", str);
            ContentResolver contentResolver = context.getContentResolver();
            contentResolver.insert(Uri.parse("content://sms/inbox"), sentSms);
        }
    }
}

