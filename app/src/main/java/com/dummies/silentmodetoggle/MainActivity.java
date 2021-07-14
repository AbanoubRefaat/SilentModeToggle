package com.dummies.silentmodetoggle;

import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);


        FrameLayout contentView = (FrameLayout)findViewById(R.id.content);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RingerHelper.performToggle(audioManager);

                // Update the UI to reflect the new state
                updateUi();

                /*
                Log.d("SilentModeApp", "This is a test"); →9
                 */
            }
        });
    }

    private void updateUi() {
        // Find the view named phone_icon in our layout. We know it’s
        // an ImageView in the layout, so downcast it to an ImageView.
        ImageView imageView = (ImageView) findViewById(R.id.icon_image);
        // Set phoneImage to the ID of image that represents ringer on
        // or off. These are found in res/drawable-xxhdpi
        int phoneImage = RingerHelper.isPhoneSilent(audioManager)
                ? R.drawable.ringer_off
                : R.drawable.ringer_on;
        // Set the imageView to the image in phoneImage
        imageView.setImageResource(phoneImage);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Update our UI in case anything has changed.
        updateUi();
    }
}
