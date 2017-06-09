package com.example.user.mediaplayer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.user.mediaplayer.activities.MainActivity;

import java.io.File;
import java.io.IOException;

/**
 * Created by User on 6/7/2017.
 */

public class MediaPlayerService extends Service implements  MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener {
    String title;
    String path ;
    Intent intent;
    MediaPlayer mMediaPlayer  = new MediaPlayer() ;
    private final IBinder mBinder = new LocalBinder();
    ImageButton imageButton = MainActivity.getBackground();
    //Actions
    public static final String ACTION_START = "com.example.user.mediaplayer.action_play";
    public static final String ACTION_PAUSE = "com.example.user.mediaplayer.action_pause";
    public static final String ACTION_RESUME = "com.example.user.mediaplayer.action_resume";


    //NotificationID
    public static final int NOTIFICATION_ID = R.string.Notification_id;

    //Notification
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                 mMediaPlayer.reset();
//                mMediaPlayer = null;
                Toast.makeText(MediaPlayerService.this, "finished", Toast.LENGTH_SHORT).show();
                imageButton.setImageResource(android.R.drawable.ic_media_play);
                setUpNotification(intent,title,imageButton);
            }
        });
    }
    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
          path = intent.getStringExtra("path");
        this.intent = intent;
//         imageButton = intent.getParcelableExtra("mediaPlayerStatusBackground");
            if(intent.getAction().equals(MediaPlayerService.ACTION_START)) {
                   title = intent.getStringExtra("title");
                if(mMediaPlayer != null || mMediaPlayer.isPlaying()){
                    mMediaPlayer.reset();

                }


                    initMediaPlayer(path);
                    setUpNotification(intent, title,imageButton);

            }
            else if (intent.getAction().equals(MediaPlayerService.ACTION_PAUSE)){
              mMediaPlayer.pause();
                setUpNotification(intent, title,imageButton);

            }  else if (intent.getAction().equals(MediaPlayerService.ACTION_RESUME)){
                mMediaPlayer.start();
                setUpNotification(intent, title,imageButton);
            }
        return  START_STICKY;
    }

    private void setUpNotification(Intent intent,String title,ImageButton imageButton ) {
        mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intentNotif  = new Intent(this, MainActivity.class);
        intentNotif .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendIntent = PendingIntent.getActivity(this, 0, intentNotif , 0);

        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);

        if(intent.getAction().equals(ACTION_START)) {
            Intent pauseIntent = new Intent(this, MediaPlayerService.class);

            pauseIntent.setAction(ACTION_PAUSE);
            PendingIntent pendingPauseIntent = PendingIntent.getService(this, 0, pauseIntent, 0);

            mRemoteViews.setOnClickPendingIntent(R.id.play_pause_music_id, pendingPauseIntent);

            // notification's icon
            mRemoteViews.setImageViewResource(R.id.play_pause_music_id, android.R.drawable.ic_media_pause);
            // notification's title
            mRemoteViews.setTextViewText(R.id.notif_title_id, title);

            mBuilder = new NotificationCompat.Builder(this);

            mBuilder.setSmallIcon(android.R.drawable.ic_media_pause)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setContentIntent(pendIntent)
                    .setContent(mRemoteViews)
                    .setTicker(title);

            // starting service with notification in foreground mode
            startForeground(NOTIFICATION_ID, mBuilder.build());
        }else if(intent.getAction().equals(ACTION_PAUSE)){
            imageButton.setImageResource(android.R.drawable.ic_media_play);
            Intent playIntent = new Intent(this, MediaPlayerService.class);
            playIntent.setAction(ACTION_RESUME);
            PendingIntent pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0);

            mRemoteViews.setOnClickPendingIntent(R.id.play_pause_music_id,pendingPlayIntent);
            // update the icon
            mRemoteViews.setImageViewResource(R.id.play_pause_music_id, android.R.drawable.ic_media_play);
            // update the title
            mRemoteViews.setTextViewText(R.id.notif_title_id, title);

            mBuilder.setSmallIcon(android.R.drawable.ic_media_play)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setContentIntent(pendIntent)
                    .setContent(mRemoteViews)
                    .setTicker(title);
            startForeground(NOTIFICATION_ID, mBuilder.build());
        }else if (intent.getAction().equals(ACTION_RESUME)){
            imageButton.setImageResource(android.R.drawable.ic_media_pause);
            Intent resumeIntent = new Intent(this, MediaPlayerService.class);
            resumeIntent.setAction(ACTION_PAUSE);
            PendingIntent pendingResumeIntent = PendingIntent.getService(this, 0, resumeIntent, 0);

            mRemoteViews.setOnClickPendingIntent(R.id.play_pause_music_id,pendingResumeIntent);
            // update the icon
            mRemoteViews.setImageViewResource(R.id.play_pause_music_id, android.R.drawable.ic_media_pause);
            // update the title
            mRemoteViews.setTextViewText(R.id.notif_title_id, title);

            mBuilder.setSmallIcon(android.R.drawable.ic_media_pause)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setContentIntent(pendIntent)
                    .setContent(mRemoteViews)
                    .setTicker(title);
            startForeground(NOTIFICATION_ID, mBuilder.build());
        }
    }

    private void initMediaPlayer(String path) {
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(getApplicationContext(),Uri.fromFile(new File(path)));
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.prepareAsync();}
        catch (IOException e1) {
            e1.getMessage();
        }
    }

    @Override
    public void onDestroy() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        super.onDestroy();
    }
    public void resume(Intent intent ,ImageButton resumeButton){
         imageButton = resumeButton;
        mMediaPlayer.start();
        setUpNotification(intent,title,resumeButton);
    }
    public void pause(Intent intent, ImageButton pauseMusic){
         imageButton = pauseMusic;
        mMediaPlayer.pause();
        setUpNotification(intent,title,pauseMusic);
    }
    public class LocalBinder extends Binder {
       public MediaPlayerService getService(){
            return MediaPlayerService.this;
        }
    }

}
