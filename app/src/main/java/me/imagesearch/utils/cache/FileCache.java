package me.imagesearch.utils.cache;

import java.io.File;

import me.imagesearch.utils.Constants;

/**
 * Created by viacheslavokolitiy on 11.12.2014.
 */
public class FileCache {
    private File mFile;

    public File createCacheDirectory(){
        mFile = new File(Constants.CACHE_DIR);
        if(!mFile.exists()){
            mFile.mkdirs();
        }

        return mFile;
    }
}
