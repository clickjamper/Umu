package com.example.vaspe.umu;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class ServantActivity extends Activity {

    ImageView ServantView;
    TextView ServantText;

    private SoundPool sp;
    private int servantselect, servantdefeated, servantfinish;
    private int StreamServantVoiceId;
    final String save_count = "save_count";

    SharedPreferences SaveCount;

    private int count ;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servant);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            NewServantSound();
        }
        else {
            OldServantSound();
        }

        servantselect = sp.load(this, R.raw.neroselect, 1);
        servantdefeated = sp.load(this, R.raw.nerodefeated, 1);
        servantfinish = sp.load(this, R.raw.nerofinish, 1);

        ServantView = findViewById(R.id.servant);
        ServantText = findViewById(R.id.count);

        final Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        final Animation bigServant = AnimationUtils.loadAnimation(this, R.anim.bigumu);
        final Animation servant_defeated = AnimationUtils.loadAnimation(this, R.anim.servantdefeated);

        loadCount();

        ServantText.setText(Integer.toString(count));
        ServantView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, final MotionEvent event) {

                int random_animation = new Random().nextInt(101);
                sp.stop(StreamServantVoiceId);

                if (random_animation <= 75 && random_animation > 1) {
                    playMusic(servantselect, shake);
                }

                if (random_animation == 1) {
                    playMusic(servantdefeated, servant_defeated);
                }

                if (random_animation > 75) {
                    playMusic(servantfinish, bigServant);
                }
                return false;
            }

        });
    }

    private void saveCount() {
        SaveCount = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor umu_editor = SaveCount.edit();
        umu_editor.putInt(save_count, count);
        umu_editor.apply();
        umu_editor.commit();
    }

    private void loadCount() {
        SaveCount = getPreferences(MODE_PRIVATE);
        count = SaveCount.getInt(save_count, count);
        ServantText.setText(Integer.toString(count));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void NewServantSound() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        sp = new SoundPool.Builder().setAudioAttributes(audioAttributes).build();
    }

    @SuppressWarnings("deprecation")
    private void OldServantSound(){
        sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
    }

    private void playMusic(Integer voiceID, Animation anim) {
        AudioManager audioManager=(AudioManager) getSystemService(AUDIO_SERVICE);
        float actualVolume=(float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume=(float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume= actualVolume/ maxVolume;
        StreamServantVoiceId = sp.play(voiceID, volume, volume, 1, 0, 1f);
        ServantView.startAnimation(anim);
        count++;
        saveCount();
        if (count > 999999) {
            count = 0;
        }
        ServantText.setText(Integer.toString(count));
    }


}
