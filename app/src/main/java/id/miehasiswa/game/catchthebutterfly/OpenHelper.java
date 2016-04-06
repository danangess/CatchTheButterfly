package id.miehasiswa.game.catchthebutterfly;

/**
 * Created by danang on 05/04/16.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 *   helper untuk database
 */

public class OpenHelper extends SQLiteOpenHelper
{
    //kalau ini berubah, increment versi database
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "dbCTB.db";
    public static final String TABLE_CREATE =
            "CREATE TABLE ctb_location(id INTEGER PRIMARY KEY AUTOINCREMENT, location TEXT, latitude TEXT, longitude TEXT, range TEXT)";

    public OpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create database
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //jika   app   diupgrade   (diinstall   yang   baru)   maka   database   akan   dicreate   ulang   (data hilang)
        //jika tidak tidak ingin hilang, bisa diproses disini
        db.execSQL("DROP TABLE IF EXITS ctb_location");
        onCreate(db);
    }
}
