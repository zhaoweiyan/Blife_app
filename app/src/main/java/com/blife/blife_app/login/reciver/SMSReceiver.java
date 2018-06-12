package com.blife.blife_app.login.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by w on 2016/6/13.
 */
public class SMSReceiver extends BroadcastReceiver {

    private static SMSReceiverListener smsReceiverListener = null;
    private static SMSReceiver smsReceiver;

    public static void setSmsReceiverListener(SMSReceiverListener listener) {
        smsReceiverListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] pduses = (Object[]) intent.getExtras().get("pdus");
        for (Object pdus : pduses) {
            byte[] pduMessage = (byte[]) pdus;
            SmsMessage sms = SmsMessage.createFromPdu(pduMessage);
//            String mobile = sms.getOriginatingAddress();//发送短信的手机号码
            String content = sms.getMessageBody(); //短信内容
            if (!TextUtils.isEmpty(content)) {
                String reg = "\\d{6}";
                Pattern p = Pattern.compile(reg);
                Matcher m = p.matcher(content);
                if (m.find()) {
                    String res = m.group();
                    if (!TextUtils.isEmpty(res) && smsReceiverListener != null) {
                        smsReceiverListener.onReceiverCode(res);
                    }
                }
            }
        }
    }

    public static void RegisterSmsBoardCast(Context context) {
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        smsReceiver = new SMSReceiver();
        context.registerReceiver(smsReceiver, filter);
    }

    public static void unRegisterSmsBoardCast(Context context) {
        if (smsReceiver != null) {
            try {
                context.unregisterReceiver(smsReceiver);
            } catch (Exception e) {

            }
        }

    }


}
