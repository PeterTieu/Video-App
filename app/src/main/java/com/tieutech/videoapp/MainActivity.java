package com.tieutech.videoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {


    // ============= INITIALISE/ASSIGN VARIABLES ===================================================

    //Name variable
    public static final String MEDIA_NAME = "sample_mp4_file"; //Name of the video file

    //View variable
    VideoView videoView; //Video view

    //Data variables
    private int mCurrentPosition = 0; //Current position of the video (in ms)
    private int mStatePosition ;

    //Key variable
    public static final String PLAYBACK_TIME = "playback_time";

    //Get the video
    private Uri getMedia(String mediaName) {
        return Uri.parse("android.resource://" + getPackageName() + "/raw/" + mediaName); //Get file path in Uri
    }



    // ============= OVERRIDE ACTIVITY LIFECYCLE CALLBACK METHODS ==================================

    //NOTE: Gets called every time after the app undergoes a state change (e.g. screen orientation change)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("ActivityLifecycle", "onCreate(..)");

        //Obtain view
        videoView = findViewById(R.id.videoView);

        //Obtain the current position of the video after a state change (i.e. change in orientation) occurs
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }

        //Add a Media Controller to the video (i.e. play, rewind, seek bar, etc.)
        MediaController mediaController = new MediaController(this);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);
    }

    //Initialise the video
    @Override
    protected void onStart() {
        super.onStart();

        Log.i("ActivityLifecycle", "onStart()");

        initialisePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("ActivityLifecycle", "onPause()");

        //Save the current position of the video upon a state change (i.e. change in orientation)
        mCurrentPosition = videoView.getCurrentPosition();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            videoView.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i("ActivityLifecycle", "onStop()");

        releasePlayer();
    }

    //Invoked right after onStop(), e.g. upon a state change such as screen orientation change
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.i("ActivityLifecycle", "onSaveInstanceState(..)");

        //Save the position of the video to the instance state
        outState.putInt(PLAYBACK_TIME, mCurrentPosition);
    }


    // ============= DEFINE METHODS ================================================================

    //Start the media player
    private void initialisePlayer() {

        //Obtain the video
        Uri videoUri = getMedia(MEDIA_NAME);
        videoView.setVideoURI(videoUri);

        //If the current position of the
        if (mCurrentPosition > 0) {
            videoView.seekTo(mCurrentPosition);
        }
        else {
            videoView.seekTo(1);
        }

        videoView.start();
    }

    //Release the video
    private void releasePlayer() {
        videoView.stopPlayback();
    }
}