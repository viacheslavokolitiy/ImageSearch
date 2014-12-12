package me.imagesearch.model;

/**
 * Created by viacheslavokolitiy on 11.12.2014.
 */
public class PhotoItem {
    private String mPhotoURL;
    private String mTitle;

    public String getPhotoURL() {
        return mPhotoURL;
    }

    public void setPhotoURL(String mPhotoURL) {
        this.mPhotoURL = mPhotoURL;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhotoItem)) return false;

        PhotoItem photoItem = (PhotoItem) o;

        if (!mPhotoURL.equals(photoItem.mPhotoURL)) return false;
        if (!mTitle.equals(photoItem.mTitle)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mPhotoURL.hashCode();
        result = 31 * result + mTitle.hashCode();
        return result;
    }
}
