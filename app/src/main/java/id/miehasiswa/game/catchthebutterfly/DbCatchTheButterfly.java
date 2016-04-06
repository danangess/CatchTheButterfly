package id.miehasiswa.game.catchthebutterfly;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by danang on 05/04/16.
 */

public class DbCatchTheButterfly {
    private SQLiteDatabase db;
    private final OpenHelper dbHelper;

    public DbCatchTheButterfly(Context c) {
        dbHelper = new OpenHelper(c);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    public void insertLocation(LocationModel locate) {
        open();
        ContentValues newValues = new ContentValues();
        newValues.put("location", locate.getLocation());
        newValues.put("latitude", locate.getLatitude());
        newValues.put("longitude", locate.getLongitude());
        newValues.put("range", locate.getRange());
        db.insert("ctb_location", null, newValues);
        close();
    }

    public LocationModel getLocation(LocationModel locate) {
        open();
        Cursor cur = null;
        LocationModel L = new LocationModel();

        //kolom yang diambil​
        String[] cols = new String[]{"id", "location", "latitude", "longitude", "range"};

        if (locate.getId() != null) {
            String[] param = {locate.getId()};
            cur = db.query("ctb_location", cols, "id=?",param,null,null,null);
        } else if (locate.getLocation() != null) {
            String[] param = {locate.getLocation()};
            cur = db.query("ctb_location", cols, "location=?",param,null,null,null);
        } else if (locate.getLatitude() != null){
            String[] param = {locate.getLatitude()};
            cur = db.query("ctb_location", cols, "latitude=?",param,null,null,null);
        } else if(locate.getLongitude() != null){
            String[] param = {locate.getLongitude()};
            cur = db.query("ctb_location", cols, "longitude=?",param,null,null,null);
        }else if (locate.getRange() != null) {
            String[] param = {locate.getRange()};
            cur = db.query("ctb_location", cols, "range=?",param,null,null,null);
        }

        if (cur.getCount() > 0) {  //ada data? ambil
            cur.moveToFirst();
            L.setLocation(cur.getString(1));
            L.setLatitude(cur.getString(2));
            L.setLongitude(cur.getString(3));
            L.setRange(cur.getString(4));
        }

        cur.close();
        close();
        return L;
    }

    public ArrayList<LocationModel> getAllLocation(){
        open();
        Cursor cursor = db.query("ctb_location", null, null, null, null, null, null);

        ArrayList<LocationModel> locationList = new ArrayList<LocationModel>();
        LocationModel location;
        if (cursor.moveToFirst()){
            do {
                location = new LocationModel();
                location.setId(cursor.getString(0));
                location.setLocation(cursor.getString(1));
                location.setLatitude(cursor.getString(2));
                location.setLongitude(cursor.getString(3));
                location.setRange(cursor.getString(4));

                locationList.add(location);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return locationList;
    }

    public void updateLocation(LocationModel locate) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put("location", locate.getLocation());
        contentValues.put("latitude", locate.getLatitude());
        contentValues.put("longitude", locate.getLongitude());
        contentValues.put("range", locate.getRange());
        db.update("ctb_location", contentValues, "id=?", new String[]{locate.getId()});
        close();
    }

    public void deleteLocation(LocationModel locate) {
        open();
        db.delete("ctb_location", "id=?", new String[]{locate.getId()});
        close();
    }

    public boolean changes() {
        open();
        boolean ch = false;
        Cursor cur = null;
        cur = db.rawQuery("SELECT changes()", null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            if (0 < Integer.parseInt(cur.getString(0))) ch = true;
        }
        cur.close();
        close();
        return ch;
    }
}
