package com.example.hospital_management;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    String patientId;
    String patientName;
    String bedNo;
    String emergencyReason;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

            if (message.getData().size() > 0) {
                // Extract data from the received message
                patientId = message.getData().get("patientId");
                patientName = message.getData().get("patientName");
                bedNo = message.getData().get("bedNo");
                emergencyReason = message.getData().get("emergencyReason");

                // Access the data as needed
                // For example, log the received data
                Log.d("FCM_DATA", "Patient ID: " + patientId);
                Log.d("FCM_DATA", "Patient Name: " + patientName);
                Log.d("FCM_DATA", "Bed No: " + bedNo);
                Log.d("FCM_DATA", "emergencyReason: " + emergencyReason);
            }

            // Handle notification messages if needed
            if (message.getNotification() != null) {
                // Extract notification message
                String title = message.getNotification().getTitle();
                String body = message.getNotification().getBody();

                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(5000);

                sendNotification(title,body);
            }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    private void sendNotification(String notificationTitle,String body) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_IMMUTABLE);

        String channelId = "fcm_default_channel";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setVibrate(new long[] { 1000, 1000, 1000, 1000 })
                        .setContentTitle(notificationTitle)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_emergency))
                        .setContentText(
                        "Patient Name: "+patientName+"\n"+
                        "Bed No: "+bedNo+"\n"+
                        "Emergency Reason:"+ emergencyReason
                        )
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
//                        .setStyle(
//                                new NotificationCompat.InboxStyle()
//                                        .addLine("Patient Name: "+patientName)
//                                        .addLine("Bed No: "+bedNo)
//                                        .addLine("Emergency Reason:"+ emergencyReason)
//                        )
                        .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}