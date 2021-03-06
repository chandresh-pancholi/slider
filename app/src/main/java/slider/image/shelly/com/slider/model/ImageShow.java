package slider.image.shelly.com.slider.model;

/**
 * Created by chandresh.pancholi on 27/12/15.
 */
public class ImageShow {

    private int id;
    private int showid;
    private String showimage;
    private boolean selected;

    public ImageShow() {}

    public ImageShow(int showid, int id) {
        this.showid = showid;
        this.id = id;
    }

    public ImageShow(int showid, int id, String img) {
        this.showid = showid;
        this.id = id;
        this.showimage = img;
    }

    public ImageShow(int showid, String img) {
        this.showid = showid;
        this.showimage = img;
    }

    public void setShowID(int d) {
        this.showid = d;
    }

    public void setShowImage(String i) {
        this.showimage = i;
    }

    public void setId(int i) {
        this.id = i;
    }

    // -- getter
    public int getShowID() {
        return this.showid;
    }

    public String getShowImage() {
        return this.showimage;
    }

    public int getId() {
        return this.id;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setselected(boolean b) {
        this.selected = b;
    }
}
