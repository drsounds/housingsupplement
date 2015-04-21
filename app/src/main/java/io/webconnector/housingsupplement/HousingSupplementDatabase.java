package io.webconnector.housingsupplement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;

/**
 * Created by Alecca on 2015-04-19.
 */
public class HousingSupplementDatabase extends SQLiteOpenHelper {
    public HousingSupplementDatabase(Context context) {
        super(context, "housingsupplement.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE housing_applications(_id integer not null primary key autoincrement, time datetime, work_income float default 0, pensions float default 0, child_allowance float, study_allowance float, tax_free_income float default 0, rent float, age integer, work_leave float default 0, rent_allowance float default 0);");

    }
    public Cursor getHousingApplicationsForYear(int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM housing_applications ORDER BY time DESC", new String[]{});

        return cursor;

    }

    /**
     * http://stackoverflow.com/questions/4719594/checking-if-a-column-exists-in-an-application-database-in-android
     * @param inTable
     * @param columnToCheck
     * @return
     */
    public boolean hasColumnInTable( String inTable, String columnToCheck) {
        try{
            //query 1 row
            Cursor mCursor  = this.getReadableDatabase().rawQuery( "SELECT * FROM " + inTable + " LIMIT 0", null );

            //getColumnIndex gives us the index (0 to ...) of the column - otherwise we get a -1
            if(mCursor.getColumnIndex(columnToCheck) != -1)
                return true;
            else
                return false;

        }catch (Exception Exp){
            //something went wrong. Missing the database? The table?
            Log.d("... - existsColumnInTable", "When checking whether a column exists in the table, an error occurred: " + Exp.getMessage());
            return false;
        }
    }
    public long addPeriod(HousingSupplementApplication application, Context context) {
        ContentValues cv = application.toContentValues(context);
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert("housing_applications", null, cv);

        return id;
    }

    public HousingSupplementApplication getLastApplication() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("housing_applications", new String[] { "*" }, null, null, null, null, "time desc", "1");
        HousingSupplementApplication application = null;
        if (cursor.moveToNext())
            application = new HousingSupplementApplication(cursor);

        return application;
    }
    public void updateApplicationWithId(Context context, HousingSupplementApplication application, long id) {
        ContentValues cv = application.toContentValues(context);
        SQLiteDatabase database = this.getWritableDatabase();
        database.update("housing_applications", cv, "_id=?", new String[]{String.valueOf(id)});
        database.close();
    }
    public HousingSupplementApplication getApplicationById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("housing_applications", new String[] { "*" }, "_id=?", new String[]{String.valueOf(id)}, null, null, "time desc", "1");
        HousingSupplementApplication application = null;
        if (cursor.moveToNext())
            application = new HousingSupplementApplication(cursor);

        return application;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
