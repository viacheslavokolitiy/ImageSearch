package me.imagesearch.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tjeannin.provigen.ProviGenProvider;
import com.tjeannin.provigen.helper.TableBuilder;
import com.tjeannin.provigen.helper.TableUpdater;
import com.tjeannin.provigen.model.Constraint;

/**
 * Created by viacheslavokolitiy on 11.12.2014.
 */
public class ImageSearchContentProvider extends ProviGenProvider {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "imagesearchdatabase";
    private static Class[] contracts = new Class[]{
            ImageContract.class};

    @Override
    public SQLiteOpenHelper openHelper(Context context) {
        return new SQLiteHelper(getContext(), DB_NAME, null, DB_VERSION);
    }

    @Override
    public Class[] contractClasses() {
        return contracts;
    }

    private static class SQLiteHelper extends SQLiteOpenHelper {

        public SQLiteHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // Automatically creates table and needed columns.
            //createTables(db);
            new TableBuilder(ImageContract.class)
                    .addConstraint(ImageContract._ID, Constraint.UNIQUE, Constraint.OnConflict.REPLACE)
                    .createTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //updateTables(db);
            TableUpdater.addMissingColumns(db, ImageContract.class);
        }
    }
}
