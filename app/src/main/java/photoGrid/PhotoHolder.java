package photoGrid;

/**
 * Created by JW043373 on 10/23/2016.
 */

public class PhotoHolder {
    private static PhotoItem photo;

    public static void setPhoto(PhotoItem photo){
        PhotoHolder.photo = photo;
    }

    public static PhotoItem getPhoto(){
        return photo;
    }
}
