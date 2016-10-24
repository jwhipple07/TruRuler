package photoGrid;

import android.graphics.Bitmap;

/**
 * Created by JW043373 on 10/23/2016.
 */

public class PhotoItem {
    private Bitmap image;
    private String title;

    public PhotoItem(Bitmap image, String title) {
        super();
        this.image = image;
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}