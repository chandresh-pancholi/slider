package slider.image.shelly.com.slider.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import slider.image.shelly.com.slider.R;
import slider.image.shelly.com.slider.model.SlideShow;
import slider.image.shelly.com.slider.utils.DbHelper;

/**
 * Created by chandresh.pancholi on 27/12/15.
 */
public class MyImageActivity extends Activity {
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final String TAG = "MyImage";
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private ViewFlipper mViewFlipper;
    ArrayList<String> myImageUris;
    DbHelper db;
    SlideShow selShow = new SlideShow();
    List<String> imgURI;
    private Context mContext;
    Long id;
    int position;
    String currentURI;
    LinearLayout btnlayout;

    ImageView currentImage;
    private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_image);

        Bundle b = this.getIntent().getExtras();
        myImageUris = b.getStringArrayList("ImageFilePaths");
        btnlayout = (LinearLayout) findViewById(R.id.btnlayout);
        id = b.getLong("id");
        position = b.getInt("position");
        currentURI = b.getString("currentURI");
        currentImage = (ImageView) findViewById(R.id.myimage);
        if(currentURI!= null){
            Bitmap myBitmap = BitmapFactory.decodeFile(currentURI);

            currentImage.setImageBitmap(myBitmap);
        }
        else{

            Bitmap myBitmap = BitmapFactory.decodeFile(myImageUris.get(0));
            currentImage.setImageBitmap(myBitmap);
        }


        Log.i(TAG, "ID  MYIMAGE URIS ARE " + id + "POSITION  " + position + "MYIMAGE" + myImageUris);


        // int id = intent.getIntExtra("id", 0);
        //  Log.i(TAG, " ID OF RECVD VALUE IS " + id) ;
        mContext = this;
        mViewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        final LayoutInflater inflater = getLayoutInflater();

        if (!myImageUris.isEmpty())

        {
            for (int i = 0; i < myImageUris.size(); i++) {
                final View typeView = inflater.inflate(R.layout.child_image_slider, null);
                Bitmap currentBitmap = BitmapFactory.decodeFile(myImageUris.get(i));
                Log.i(TAG, "adding view to FLIPPER AT INDEX " + i + "and CURRENT BITMAP IS " + currentBitmap);


                ImageView image = (ImageView) typeView.findViewById(R.id.imageview);
                image.setImageBitmap(currentBitmap);
                mViewFlipper.addView(typeView, i);

                // currentImage.setVisibility(View.GONE);


            }
        }

        //TODO ::

        mViewFlipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "btnlayout view clicked, image numbaer ");
                // scrollableLayout.setVisibility(scrollableLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                if (btnlayout.getVisibility() == View.VISIBLE) {
                    Log.i(TAG, "scrollable layout was VISIBLE , animation SLIDE DOWN ");

                    btnlayout.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slid_up));

                    btnlayout.setVisibility(View.GONE);


                } else {
                    Log.i(TAG, "scrollable layout was GONE , animation SLIDE UP");
                    btnlayout.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slid_down));
                    btnlayout.setVisibility(View.VISIBLE);
                }
            }

        });


        mViewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });


        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sets auto flipping
                mViewFlipper.setAutoStart(true);
                mViewFlipper.setFlipInterval(1000);
                mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_in));
                mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_out));
                mViewFlipper.startFlipping();
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


            }
        });

        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //stop auto flipping
                mViewFlipper.stopFlipping();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            }
        });
    }

    class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {


            try {
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_out));


                    mViewFlipper.showNext();
                    return true;
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_out));

                    mViewFlipper.showPrevious();
                    return true;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    Animation.AnimationListener mAnimationListener = new Animation.AnimationListener() {
        public void onAnimationStart(Animation animation) {
            //animation started event
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            //TODO animation stopped event
        }
    };
}
