package com.whoami.gcxhzz.until;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

import com.whoami.gcxhzz.R;


public class SoundPoolUtil {

    private static SoundPoolUtil instance;
    private SoundPool soundPool;
    private static MediaPlayer mediaPlayer;
    public static SoundPoolUtil getInstance(){
        if(instance ==null){
            synchronized (SoundPoolUtil.class){
                if(instance==null){
                    instance = new SoundPoolUtil();
                }
            }
        }
        return instance;
    }

    private SoundPoolUtil(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool= new SoundPool.Builder()
                    .setMaxStreams(100)
                    .build();
        } else {
            soundPool = new SoundPool(100, AudioManager.STREAM_MUSIC, 100);
        }

    }

    public void play(Context context,int id){
        final int soundId = soundPool.load(context,id,1);

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(soundId,1, 1, 0, 0, 1);
            }
        });
    }

   public void playMedia(Context context){
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(context, R.raw.start);
        }
       try {
           mediaPlayer.setLooping(false);
           mediaPlayer.start();
       } catch (Exception e) {
       }
   }
}
