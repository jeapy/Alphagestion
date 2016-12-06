package info.androidhive.alphagestion.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.alphagestion.Model.Model_Bon;
import info.androidhive.alphagestion.Model.Model_Materiels;
import info.androidhive.alphagestion.Model.Model_Rqst;
import info.androidhive.alphagestion.Model.Model_interne;

/**
 * Created by Jean Philippe on 23/05/2016.
 */
public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "alphadb";
    // Login table name
    private static final String TABLE_REQUEST = "requete";
    private static final String TABLE_FAMILLEMATERIELS = "famillemat";
    private static final String TABLE_LIGNECOMMANDE="lignecom";

    private static final String TABLE_BONSORTIE="bonsortie";
    private static final String TABLE_LIGNEBONSORTIE="lignebonsortie";

    private static final String TABLE_SORTIEINTERNE="sortieinterne";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_REFERENCE = "ref_reqst";
    private static final String KEY_DATE = "date";
    private static final String KEY_USER= "user";
    private static final String KEY_STATUS= "status";

    private static final String KEY_IDFAM = "idfam";
    private static final String KEY_LIBELLE = "lib_famille";

    private static final String KEY_REFCOM="ref_com";
    private static final String KEY_REFART="ref_art";
    private static final String KEY_LIBART="lib_art";
    private static final String KEY_QT="qt";

    private static final String KEY_IDSORTIE = "idsortie";
    private static final String KEY_REFSORTIE = "refsortie";
    private static final String KEY_NOMLIV="nom_liv";
    private static final String KEY_NUMLIV="num_liv";
    private static final String KEY_STATSORTIE="status";
    private static final String KEY_DTBON="datebon";


    private static final String KEY_REFARTSTOCK="ref_artstock";
    private static final String KEY_STSTOCK="lib_ststock";
    private static final String KEY_QTSTOCK="qt_stock";


    private static final String KEY_REFSI="ref_si";
    private static final String KEY_DATESI="date_si";
    private static final String KEY_NOMEMPSI="nomemp_si";
    private static final String KEY_RETOUR="retour";
    private static final String KEY_COMMENT="commentaire";
    private static final String KEY_DATERI="date_ri";


    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_REQ_TABLE = "CREATE TABLE " + TABLE_REQUEST + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_REFERENCE + " TEXT UNIQUE," + KEY_USER + " TEXT,"
                + KEY_DATE + " TEXT," + KEY_STATUS + " TEXT" + ")";


        String CREATE_FM_TABLE = "CREATE TABLE " + TABLE_FAMILLEMATERIELS + "("
                + KEY_IDFAM + " INTEGER PRIMARY KEY," + KEY_LIBELLE + " TEXT" + ")";


        String CREATE_LC_TABLE = "CREATE TABLE " + TABLE_LIGNECOMMANDE + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_REFCOM + " TEXT," + KEY_REFART + " TEXT ,"+ KEY_LIBART + " TEXT ,"
                + KEY_QT + " INTEGER" + ")";

        String CREATE_BS_TABLE = "CREATE TABLE " + TABLE_BONSORTIE+ "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_IDSORTIE + " TEXT UNIQUE," + KEY_REFSORTIE + " TEXT ,"+ KEY_NOMLIV + " TEXT ,"+ KEY_NUMLIV + " TEXT ,"+ KEY_STATSORTIE + " TEXT,"+ KEY_DTBON + " TEXT " + ")";


        String CREATE_LBS_TABLE = "CREATE TABLE " + TABLE_LIGNEBONSORTIE+ "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_REFARTSTOCK + " TEXT," +KEY_QTSTOCK  + "  INTEGER ,"+ KEY_STSTOCK + " TEXT  " + ")";

        String CREATE_SI_TABLE = "CREATE TABLE " + TABLE_SORTIEINTERNE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_REFSI + " TEXT UNIQUE," + KEY_NOMEMPSI + " TEXT,"
                + KEY_DATESI + " TEXT," + KEY_STATUS + " TEXT," + KEY_RETOUR + " TEXT,"
                        + KEY_DATERI + " TEXT," + KEY_COMMENT + " TEXT" + ")";

        db.execSQL(CREATE_REQ_TABLE);
        db.execSQL(CREATE_FM_TABLE);
        db.execSQL(CREATE_LC_TABLE);
        db.execSQL(CREATE_BS_TABLE);
        db.execSQL(CREATE_LBS_TABLE);
        db.execSQL(CREATE_SI_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQUEST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAMILLEMATERIELS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIGNECOMMANDE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BONSORTIE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIGNEBONSORTIE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SORTIEINTERNE);

        // Create tables again
        onCreate(db);
    }
    /**
     * Storing user details in database
     * */
    public void addsortieinterne( String rsi,String dsi,String nesi, String ssi,String ri,String dri, String cri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_REFSI, rsi); //
        values.put(KEY_DATESI, dsi); //
        values.put(KEY_NOMEMPSI, nesi); //
        values.put(KEY_STATUS, ssi); //
        values.put(KEY_RETOUR, ri); //
        values.put(KEY_DATERI, dri); //
        values.put(KEY_COMMENT, cri); //


        // Inserting Row
        long id = db.insert(TABLE_SORTIEINTERNE, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New sortie interne inserted into sqlite: " + id);
    }
    /**
     * Storing user details in database
     * */
    public void addlignebonsortie(String ids, String rs,String st) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_REFARTSTOCK, ids); //
        values.put(KEY_QTSTOCK , rs); //
        values.put(KEY_STSTOCK, st); //


        // Inserting Row
        long id = db.insert(TABLE_LIGNEBONSORTIE, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New ligne bon inserted into sqlite: " + id);
    }

    /**
     * Storing user details in database
     * */
    public void addbonsortie(String ids, String rs, String noml, String numl,String st, String dt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IDSORTIE, ids); //
        values.put(KEY_REFSORTIE, rs); //
        values.put(KEY_NOMLIV, noml); //
        values.put(KEY_NUMLIV, numl);
        values.put(KEY_STATSORTIE, st); //
        values.put(KEY_DTBON, dt); //

        // Inserting Row
        long id = db.insert(TABLE_BONSORTIE, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New bon inserted into sqlite: " + id);
    }

        /*Add Ligne commande*/
        public void addLigncommande(String refcom, String refart, String libart, int qtt) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_REFCOM, refcom); //
            values.put(KEY_REFART, refart); //
            values.put(KEY_LIBART, libart); //
            values.put(KEY_QT, qtt); //

            // Inserting Row
            long id = db.insert(TABLE_LIGNECOMMANDE, null, values);
            db.close(); // Closing database connection

            Log.d(TAG, "New command line inserted into sqlite: " + refcom+"/"+refart+"/"+qtt);
        }

    /**
     * Storing user details in database
     * */
    public void addRequest(String ref, String date, String usr, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_REFERENCE, ref); //
        values.put(KEY_DATE, date); //
        values.put(KEY_USER, usr); //
         values.put(KEY_STATUS,status);
        // Inserting Row
        long id = db.insert(TABLE_REQUEST, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }
    public void DeleteRequest() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_FAMILLEMATERIELS, null, null);
        db.close();

        Log.d(TAG, "Deleted all materiels famille from sqlite");
    }
    public void Delete(SQLiteDatabase db, String oldVersion) {
        db.execSQL("delete * from  " + TABLE_REQUEST + " where" + KEY_REFERENCE + "= " + oldVersion);
                    }
    /**
     * Getting user data from database
     * */
    /*
    public HashMap<String, String> getRequestDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_REQUEST;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("reference", cursor.getString(1));
            user.put("date", cursor.getString(2));
            user.put("user", cursor.getString(3));

        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }*/

    public List<Model_interne> getRetourInterne() {

        List<Model_interne> requestinfo = new ArrayList<>();
        //String REQUEST_DETAIL_SELECT_QUERY = "SELECT * FROM " + TABLE_REQUEST ;
        String SORTIE = "SELECT * FROM " + TABLE_SORTIEINTERNE + " WHERE " + KEY_STATUS + " = 1 AND "+KEY_RETOUR+" = 1 ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SORTIE, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Model_interne reqstData = new Model_interne();
                    reqstData.setRefsrt(cursor.getString(cursor.getColumnIndex(KEY_REFSI)));
                    reqstData.setNomouvrier(cursor.getString(cursor.getColumnIndex(KEY_COMMENT)));
                    reqstData.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATERI)));

                    requestinfo.add(reqstData);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return requestinfo;

    }
    /**
     /*
     fetch all data from UserTable
     */

    public List<Model_interne> getValideSortieInterne() {

        List<Model_interne> requestinfo = new ArrayList<>();
        //String REQUEST_DETAIL_SELECT_QUERY = "SELECT * FROM " + TABLE_REQUEST ;
        String SORTIE = "SELECT * FROM " + TABLE_SORTIEINTERNE + " WHERE " + KEY_STATUS + " = 1 AND "+KEY_RETOUR+" = 0 ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SORTIE, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Model_interne reqstData = new Model_interne();
                    reqstData.setRefsrt(cursor.getString(cursor.getColumnIndex(KEY_REFSI)));
                    reqstData.setNomouvrier(cursor.getString(cursor.getColumnIndex(KEY_NOMEMPSI)));
                    reqstData.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATESI)));

                    requestinfo.add(reqstData);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return requestinfo;

    }

    public List<Model_interne> getOldSortieInterne() {

        List<Model_interne> requestinfo = new ArrayList<>();
        //String REQUEST_DETAIL_SELECT_QUERY = "SELECT * FROM " + TABLE_REQUEST ;
        String SORTIE = "SELECT * FROM " + TABLE_SORTIEINTERNE + " WHERE " + KEY_STATUS + " = 1 ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SORTIE, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Model_interne reqstData = new Model_interne();
                    reqstData.setRefsrt(cursor.getString(cursor.getColumnIndex(KEY_REFSI)));
                    reqstData.setNomouvrier(cursor.getString(cursor.getColumnIndex(KEY_NOMEMPSI)));
                    reqstData.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATESI)));

                    requestinfo.add(reqstData);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return requestinfo;

    }
    /**
     /*
     fetch all data from UserTable
     */

    public List<Model_interne> getSortieInterne() {

        List<Model_interne> requestinfo = new ArrayList<>();
        //String REQUEST_DETAIL_SELECT_QUERY = "SELECT * FROM " + TABLE_REQUEST ;
        String SORTIE = "SELECT * FROM " + TABLE_SORTIEINTERNE + " WHERE " + KEY_STATUS + " = 0 ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SORTIE, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Model_interne reqstData = new Model_interne();
                    reqstData.setRefsrt(cursor.getString(cursor.getColumnIndex(KEY_REFSI)));
                    reqstData.setNomouvrier(cursor.getString(cursor.getColumnIndex(KEY_NOMEMPSI)));
                    reqstData.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATESI)));

                    requestinfo.add(reqstData);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return requestinfo;

    }
    /**
     /*
     fetch all data from UserTable
     */

    public List<Model_Rqst> getRequestDetails() {

        List<Model_Rqst> requestinfo = new ArrayList<>();

        //String REQUEST_DETAIL_SELECT_QUERY = "SELECT * FROM " + TABLE_REQUEST ;
        String REQUEST_DETAIL_SELECT_QUERY = "SELECT * FROM " + TABLE_REQUEST + " WHERE " + KEY_STATUS + " = 0 ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(REQUEST_DETAIL_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Model_Rqst reqstData = new Model_Rqst();
                    reqstData.setRef(cursor.getString(cursor.getColumnIndex(KEY_REFERENCE)));
                    reqstData.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));

                    requestinfo.add(reqstData);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return requestinfo;

    }

    public List<Model_Rqst> getOldRequestDetails() {

        List<Model_Rqst> requestinfo = new ArrayList<>();

        //String REQUEST_DETAIL_SELECT_QUERY = "SELECT * FROM " + TABLE_REQUEST ;
        String REQUEST_DETAIL_SELECT_QUERY = "SELECT * FROM " + TABLE_REQUEST + " WHERE " + KEY_STATUS + " = 1 ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(REQUEST_DETAIL_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Model_Rqst reqstData = new Model_Rqst();
                    reqstData.setRef(cursor.getString(cursor.getColumnIndex(KEY_REFERENCE)));
                    reqstData.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));

                    requestinfo.add(reqstData);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return requestinfo;

    }

    public List<Model_Rqst> getRequestLineDetails(String refer) {

        List<Model_Rqst> requestinfo = new ArrayList<>();

        //String REQUEST_DETAIL_SELECT_QUERY = "SELECT * FROM " + TABLE_REQUEST ;
        String REQUEST_DETAIL_SELECT_QUERY = "SELECT * FROM " + TABLE_LIGNECOMMANDE + " WHERE " + KEY_REFCOM + " = ?";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(REQUEST_DETAIL_SELECT_QUERY, new String[] { String.valueOf(refer)});
        Log.d(TAG, "Error while trying to get posts from database"+REQUEST_DETAIL_SELECT_QUERY);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Model_Rqst reqstData = new Model_Rqst();
                    reqstData.setRef(cursor.getString(cursor.getColumnIndex(KEY_LIBART)));
                    reqstData.setDate(cursor.getString(cursor.getColumnIndex(KEY_QT)));
                    reqstData.setId(cursor.getString(cursor.getColumnIndex(KEY_REFART)));

                    requestinfo.add(reqstData);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return requestinfo;

    }
    public List<Model_Materiels> getMateriels() {

        List<Model_Materiels> requestinfo = new ArrayList<>();


        String REQUEST_SELECT_QUERY = "SELECT DISTINCT "+ KEY_REFART + ","+ KEY_LIBART + " FROM " + TABLE_LIGNECOMMANDE ;


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(REQUEST_SELECT_QUERY, null);
        Log.d(TAG, "Error while trying to get posts from database"+REQUEST_SELECT_QUERY);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Model_Materiels reqstData = new Model_Materiels();
                    reqstData.setLibelle(cursor.getString(cursor.getColumnIndex(KEY_LIBART)));
                    reqstData.setRef(cursor.getString(cursor.getColumnIndex(KEY_REFART)));

                    requestinfo.add(reqstData);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return requestinfo;

    }
    public void updateRequest(String ref,String us) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //
        cv.put(KEY_STATUS, us);//
      //  db.update(TABLE_REQUEST, cv, KEY_REFERENCE + " = " + ref, null);
        db.update(TABLE_REQUEST, cv, KEY_REFERENCE + " = ?",
                new String[] { String.valueOf(ref) });
        //  db.execSQL("UPDATE " + TABLE_REQUEST + " SET " + KEY_STATUS + " = " + 1 + " WHERE " + KEY_REFERENCE + "=" + val);
        db.close(); // Closing database connection

        Log.d(TAG, "Request update into sqlite: ok" );
    }

    public void addFamille(String ref, String usr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IDFAM, ref); //
        values.put(KEY_LIBELLE, usr); //

        // Inserting Row
        long id = db.insert(TABLE_FAMILLEMATERIELS, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New materiels famille inserted into sqlite: " + id);
    }

    public void DeleteAllFamille() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_FAMILLEMATERIELS, null, null);
        db.close();

        Log.d(TAG, "Deleted all materiels famille from sqlite");
    }
    public void DeleteAllBon() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_BONSORTIE, null, null);
        db.close();

        Log.d(TAG, "Deleted all bon from sqlite");
    }

    public void DeleteFamille(SQLiteDatabase db, String id) {
        db.execSQL("delete * from  " + TABLE_FAMILLEMATERIELS + " where" + KEY_IDFAM + "= " + id);
    }

    public List< Model_Rqst> getFamilleDetails() {

        List< Model_Rqst> requestinfo = new ArrayList<>();

        String REQUEST_DETAIL_SELECT_QUERY = "SELECT * FROM " + TABLE_FAMILLEMATERIELS ;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(REQUEST_DETAIL_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Model_Rqst famData = new  Model_Rqst();
                    famData.setRef(cursor.getString(cursor.getColumnIndex(KEY_IDFAM)));
                    famData.setDate(cursor.getString(cursor.getColumnIndex(KEY_LIBELLE)));

                    requestinfo.add(famData);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return requestinfo;

    }

    public List<Model_Bon> getBonDetails() {

        List< Model_Bon> requestinfo = new ArrayList<>();

        String REQUEST_DETAIL_SELECT_QUERY = "SELECT * FROM " + TABLE_BONSORTIE +" WHERE "+  KEY_STATSORTIE+" = 0";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(REQUEST_DETAIL_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Model_Bon famData = new  Model_Bon();
                    famData.setIdbon(cursor.getString(cursor.getColumnIndex(KEY_IDSORTIE)));
                    famData.setRefbon(cursor.getString(cursor.getColumnIndex( KEY_REFSORTIE)));

                    famData.setNomlivreur(cursor.getString(cursor.getColumnIndex( KEY_NOMLIV)));
                    famData.setNumlivreur(cursor.getString(cursor.getColumnIndex(KEY_NUMLIV)));

                    famData.setDate(cursor.getString(cursor.getColumnIndex(KEY_DTBON)));


                    requestinfo.add(famData);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return requestinfo;

    }

    public List<Model_Bon> getOldBonDetails() {

        List< Model_Bon> requestinfo = new ArrayList<>();

        String REQUEST_DETAIL_SELECT_QUERY = "SELECT * FROM " + TABLE_BONSORTIE +" WHERE "+  KEY_STATSORTIE +" = 1";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(REQUEST_DETAIL_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Model_Bon famData = new  Model_Bon();
                    famData.setIdbon(cursor.getString(cursor.getColumnIndex(KEY_IDSORTIE)));
                    famData.setRefbon(cursor.getString(cursor.getColumnIndex( KEY_REFSORTIE)));

                    famData.setNomlivreur(cursor.getString(cursor.getColumnIndex( KEY_NOMLIV)));
                    famData.setNumlivreur(cursor.getString(cursor.getColumnIndex(KEY_NUMLIV)));

                    famData.setDate(cursor.getString(cursor.getColumnIndex(KEY_DTBON)));


                    requestinfo.add(famData);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return requestinfo;

    }

}
