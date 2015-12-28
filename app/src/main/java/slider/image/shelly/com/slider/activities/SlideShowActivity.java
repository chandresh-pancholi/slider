package slider.image.shelly.com.slider.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

import slider.image.shelly.com.slider.R;


/**
 * Created by chandresh.pancholi on 27/12/15.
 */
public class SlideShowActivity extends Activity{

    ArrayList<String> myImageUris;
    Random randomNumGenerator = new Random();
    private int filePathIndex = 0;
    ImageView mySlidingImage;
    OrientationEventListener orientationListener;
    AdapterView.AdapterContextMenuInfo info;
    int firstDuration = 2000, secDuration = 2000;


    @SuppressWarnings("static-access")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_slideshow);

        Bundle b = this.getIntent().getExtras();
        myImageUris = b.getStringArrayList("ImageFilePaths");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Play the music and start animating images if both images and music
        // files exist
        if (!myImageUris.isEmpty()) {
            // if music and image lists are not empty

            animateImage();
            // try to set orientation change not to stop
            orientationListener = new OrientationEventListener(getApplicationContext(), SensorManager.SENSOR_DELAY_UI) {
                public void onOrientationChanged(int orientation) {

                }
            };
            // If music files do not exist unlike images
        }

    }


    @SuppressWarnings("static-access")
    @SuppressLint("NewApi")
	/*
	 * Start animating images - Ken Burns + more animation effects
	 */
    public void animateImage() {
        final int imageCount = myImageUris.size();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        Bitmap currentBitmap = BitmapFactory.decodeFile(myImageUris.get(filePathIndex % imageCount));
        mySlidingImage = (ImageView) findViewById(R.id.slide_show);
        mySlidingImage.setImageBitmap(currentBitmap); // Set the current image
        // to image view
        filePathIndex++;

        mySlidingImage.setScaleX(1.0f); // Initial scale of image in X direction
        mySlidingImage.setScaleY(1.0f); // Initial scale of image in Y direction
        //mySlidingImage.setAlpha(0f); // Set brightness to 0
        mySlidingImage.setTranslationX(0f); // Set initial translation in X direction to 0
        mySlidingImage.setTranslationY(0f); // Set initial translation in Y direction to 0

        mySlidingImage.setVisibility(View.VISIBLE); // Make the image visible
        final float nextScale = 1.0f + (randomNumGenerator.nextFloat() - 0.5f) * 2 / 5;

        final AnimatorListenerAdapter nextImageAnimationListener = new AnimatorListenerAdapter() {
            // Call the animateImage() method on end of animating each image
            @Override
            public void onAnimationEnd(Animator animation) {
                animateImage();
            }
        };

        AnimatorListenerAdapter secondAnimationListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                mySlidingImage.animate().translationX(-800f)
                        .setDuration(secDuration)
                        .setListener(nextImageAnimationListener).start();

            }
        };

        mySlidingImage.animate().alpha(1.0f).setDuration(firstDuration).setListener(secondAnimationListener).start();
    }

    @SuppressWarnings("static-access")
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressWarnings("static-access")
    public void onResume() {
        super.onResume();

    }

    @SuppressWarnings("static-access")
    public void onPause() {
        super.onPause();

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }

}
