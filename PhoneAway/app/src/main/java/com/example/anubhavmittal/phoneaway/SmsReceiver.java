package com.example.anubhavmittal.phoneaway;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.MediaStore.Audio;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class SmsReceiver extends BroadcastReceiver {
    private AudioManager am;
    String[] codes={"msg","profile"};
    public int correctpass(String message)  //Checks for correct password
    {
        try {
            String pass="password";
            int passl=pass.length();
            if (message.length() >=passl)
            {

                if((message.substring(0,passl)).equals(pass))
                {
                    int i=0;
                    while(i<codes.length) {
                        if ((message.substring(passl + 1)).equals(codes[i]))   //needs for message to be exactly as per format.
                        {
                            return i;
                        }
                        i++;
                    }
                }
            }
        }
        catch(Exception e)
        {
            return -1;
        }
        return -1;
    }
    public void sendsms(Context context,String reqmsg,String sendernumber)
    {
        SmsManager smsmanager = SmsManager.getDefault();
        smsmanager.sendTextMessage(sendernumber, null, "Yo Bro", null, null);
        Toast.makeText(context,"Message sent by Phoneaway",Toast.LENGTH_SHORT).show();
    }
    public void ringerOn(Context context)
    {
        am=(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        Toast.makeText(context,"Ringer Mode ON",Toast.LENGTH_SHORT).show();
    }
 /*   public char correctpass2(String message)  //Checks for correct password
    {
        try {
            String pass="password";
            String code="status";
            int passl=pass.length();
            if (message.length() >=passl)
            {

                if((message.substring(0,passl)).equals(pass))
                {
                    if((message.substring(passl+1)).equals(code))   //needs for message to be exactly as per format.
                    {
                        return 'd';
                    }
                    else return 'e';

                }
                else
                {
                    return 'c';
                }
            }
            else
            {
                return 'b';
            }
        }
        catch(Exception e)
        {
            return 'a';
        }
    }*/
    public void performTask(Context context,String message,String sendernumber)
    {

        try
        {
            int i=correctpass(message);
            if(i!=-1) {
                if(i==0){
                    String reqsms="Hey "+sendernumber;
                    sendsms(context,reqsms,sendernumber);
                }
                else if(i==1)
                {
                    ringerOn(context);
                }


            }

        }catch(Exception e)
        {
            return;
        }
        //Toast.makeText(context,"Message Received",Toast.LENGTH_SHORT).show();//showing a toast for confirmation of receive message.

        return;
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle=intent.getExtras();//get bundles
        try {
            if (bundle != null)                //if bundles(ie here sms) are present,proceed further.
            {
                SmsMessage sms[] = null;

                Object pdus[] = (Object[]) bundle.get("pdus");     //array is initialised because for characters greater than threshold value,
                //it will be stored in multiple pdus.
                sms = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    sms[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    String message = sms[i].getMessageBody();
                    String sendernumber = sms[i].getOriginatingAddress();
                    performTask(context, message, sendernumber);
                }
            }
        }
        catch(Exception e)
        {

        }
    }
}