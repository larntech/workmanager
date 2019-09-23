package net.larntech.workmanager;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationHandler extends Worker {

    public final String CHANNEL_ID = "12";
    public final int notificatioId = 1;

    public NotificationHandler(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        //our work
        createNofication();


        //indicate whether work was successful
        return Result.success();
    }

    public void createNofication(){

        //intent to open our activity
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);


        //notifications
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm_white_24dp)
                .setContentTitle("Event Reminder")
                .setContentText("Hello, 4 days remaining to event xyz")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        //show notification
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(notificatioId,builder.build());


    }

    //create work request
    public static void oneOffRequest(){


        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(NotificationHandler.class)
                .setInitialDelay(2, TimeUnit.MINUTES)
                .setConstraints(setConstraints())
                .build();

        WorkManager.getInstance().enqueue(oneTimeWorkRequest);

    }

    public static void periodicWorkRequest(){
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(NotificationHandler.class)
                .setInitialDelay(2,TimeUnit.MINUTES)
                .setConstraints(setConstraints())
                .build();

        WorkManager.getInstance().enqueue(periodicWorkRequest);



    }

    public static Constraints setConstraints(){

        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();

        return constraints;

    }


}
