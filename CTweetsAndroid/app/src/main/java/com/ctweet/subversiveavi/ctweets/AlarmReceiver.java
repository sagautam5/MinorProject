package com.ctweet.subversiveavi.ctweets;


import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;



public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {

        String title = "CTweets";
        String message = "New Tweet Received!";

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (newTweet(context)) {
            notificationManager.notify(0, notificationBuilder.build());
        }
    }
    public Boolean newTweet (Context context) {
        Boolean b = false;
        try

        {
            StoreDB temp = new StoreDB();
            temp.create(context);
            UserDetails ud = temp.getUserDetails();
            NewTweet db = new NewTweet();
            db.create(ud);
            b = db.check(context);
        } catch (Exception ignored) {
        }
        return b;
    }
}