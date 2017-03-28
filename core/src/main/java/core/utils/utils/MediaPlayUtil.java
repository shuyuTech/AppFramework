package core.utils.utils;

import android.media.MediaPlayer;

import core.utils.utils.logger.Logger;


/**
 * Created by hfx on 14-10-15.
 */
public class MediaPlayUtil {
    private static MediaPlayUtil mMediaPlayUtil;
    private MediaPlayer mMediaPlayer;


    private MediaPlayUtil() {
        mMediaPlayer = new MediaPlayer();
    }

    public static MediaPlayUtil getInstance() {
        if (mMediaPlayUtil == null) {
            mMediaPlayUtil = new MediaPlayUtil();
        }
        return mMediaPlayUtil;
    }

    public void setPlayOnCompleteListener(MediaPlayer.OnCompletionListener playOnCompleteListener) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnCompletionListener(playOnCompleteListener);
        }
    }

    public void play(String soundFilePath) {
        if (mMediaPlayer == null) {
            Logger.i("这货是NULL");
            return;
        }
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(soundFilePath);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void prepare(String soundFilePath) {
        if (mMediaPlayer == null) {
            return;
        }
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(soundFilePath);
            mMediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public void stop() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    public int getCurrentPosition() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            return mMediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    public int getDutation() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            return mMediaPlayer.getDuration();
        } else {
            return 0;
        }
    }

    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        } else {
            return false;
        }
    }
}
