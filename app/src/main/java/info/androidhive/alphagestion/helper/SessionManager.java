package info.androidhive.alphagestion.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Created by Jean Philippe on 23/05/2016.
 */
public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;
    Editor editor;
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "AndroidHiveLogin";
    private static final String IS_LOGIN = "isLoggedIn";

    public static final String KEY_NAME = "nom";
    public static final String KEY_ID = "id";
    public static final String KEY_SURNAME = "prenom";
    public static final String KEY_FUNCTION = "fonction";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    /**
     * Create login session
     */
    public void createLoginSession(String nom,String id,String prenom,String fonction) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, nom);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_SURNAME, prenom);
        editor.putString(KEY_FUNCTION, fonction);

        // commit changes
        editor.commit();
        Log.d(TAG, "User login session modified!"+nom+"" +id);
    }

    public String getName() {
        return pref.getString(KEY_NAME, null);
    }
    public String getId() {
        return pref.getString(KEY_ID, null);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }
}
