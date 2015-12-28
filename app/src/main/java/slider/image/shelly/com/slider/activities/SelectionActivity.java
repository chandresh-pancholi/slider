package slider.image.shelly.com.slider.activities;

import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TabHost;

import java.util.ArrayList;

import slider.image.shelly.com.slider.R;
import slider.image.shelly.com.slider.model.ImageShow;
import slider.image.shelly.com.slider.model.SlideShow;
import slider.image.shelly.com.slider.utils.DbHelper;

/**
 * Created by chandresh.pancholi on 27/12/15.
 */
@SuppressWarnings("deprecation")
public class SelectionActivity extends TabActivity {
    int selShowID;
    DbHelper db;
    SlideShow selShow = new SlideShow();
    ArrayList<ImageShow> selShowImg = new ArrayList<ImageShow>();
    String sTitle;
    String sDesc;
    String sTitleDesc;
    Intent intent;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        intent = getIntent();

        // Get the ID of selected slide show from second screen
        selShowID = intent.getIntExtra("Selected Slide Show", 0);
        db = new DbHelper(this);
        selShow = db.getSlideShow(selShowID);//get the slide show from database
        db.closeDB();
        sTitle = selShow.getshowName(); //Get slide show title
        sDesc = selShow.getshowDescription(); //Get slide show title
        if (sDesc.length() > 15){
            // Cut the length of description if it is more than 15 characters
            sDesc = (String) sDesc.subSequence(0, 14) + "......";
        }
        sTitleDesc = (sTitle + (!sDesc.isEmpty() ? "-" : "") + sDesc);

        // Set the theme color to action bar
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0099FF")));
        bar.setTitle(sTitleDesc);

        TabHost host = getTabHost();
        Resources r = getResources();

        // Tab for images, sending the Slide show ID and Title description to ImageDisplayActivity
        TabHost.TabSpec imageTab = host.newTabSpec("Images");
        imageTab.setIndicator("Images", r.getDrawable(R.drawable.ic_action_picture));
        Intent intent = new Intent(this, ImageDisplayActivity.class);
        intent.putExtra("TAB", "Images");
        intent.putExtra("showID", selShowID);
        imageTab.setContent(intent);
        host.addTab(imageTab);


    }
}
