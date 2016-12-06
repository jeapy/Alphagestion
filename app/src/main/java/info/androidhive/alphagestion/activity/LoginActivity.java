package info.androidhive.alphagestion.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import info.androidhive.alphagestion.R;
import info.androidhive.alphagestion.app.AppConfig;
import info.androidhive.alphagestion.app.AppController;
import info.androidhive.alphagestion.helper.SQLiteHandler;
import info.androidhive.alphagestion.helper.SessionManager;

/**
 * Created by Jean Philippe on 08/06/2016.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG =LoginActivity.class.getSimpleName();


    public Button login;
    public EditText inputNom,inputPassword;
    private SQLiteHandler db;
    public  SessionManager session;
    private ProgressDialog pDialog;
    private NetworkInfo netInfo;
    private  ConnectivityManager  connect;

    private String id,name,prenom,fonction,id_famille,lib_famille;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

     Context appContext = this.getApplicationContext();

        inputNom = (EditText) findViewById(R.id.nom);
        inputPassword = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.btnLogin);

        //**********************************************//
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // SQLite database handler
        db = new SQLiteHandler(appContext);
        // Session manager
        session = new SessionManager(appContext);
        // Check if user is already logged in or not
        if (session.isLoggedIn()) {

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                    }
        //**********************************************//

        login = (Button) findViewById(R.id.btnLogin);

        //*****************************************//
        // Login button Click Event
        login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //Network information
                connect = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = connect.getActiveNetworkInfo();
                if(netInfo != null && netInfo.isAvailable() && netInfo.isConnected()){
                String  nom = inputNom.getText().toString();
                  String  password = inputPassword.getText().toString();
                             if (nom.trim().length() > 0 && password.trim().length() > 0) {
                                 checkLogin(nom, password);

                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Attention! Champs vide.", Toast.LENGTH_LONG)
                                        .show();
                            }
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Aucune connexion INTERNET", Toast.LENGTH_LONG)
                            .show();
                }

            }
        });
        //************************************//
        }


//*************************************************//
    /**
     * function to verify login details and get livraison in mysql db
     * */
    private void checkLogin(final String nm, final String pass) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        pDialog.setMessage("Connexion en cours ...");
        showDialog();
        StringRequest infoconnect = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                            id       = jObj.getString("id");
                          name       = jObj.getString("nom");
                         prenom      = jObj.getString("prenom");
                        fonction     = jObj.getString("fonction");

                        session.createLoginSession(name, id, prenom, fonction);
                        GetFamilleMateriel();
                    }    else{
                        Toast.makeText(getApplicationContext(),
                                jObj.getString("message"), Toast.LENGTH_LONG).show();
                    }
                }	catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error

                        + ">>" + error.getCause()
                        + ">>" + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Connexion impossible,reessayer SVP", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
               // params.put("nom", "password");
                params.put("nom", nm);
                params.put("password", pass);

                return params;
            }

        };
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        infoconnect.setRetryPolicy(policy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(infoconnect, tag_string_req);

    }


    private void GetFamilleMateriel() {
        // Tag used to cancel the request
        String tag_string_fam = "req_login";
      //  pDialog.setMessage("Recherche...");
       // showDialog();
        StringRequest infofam = new StringRequest(Request.Method.GET,
                AppConfig.URL_FAMILLE_MATERIEL,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
               // hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONArray deliver = jObj.getJSONArray("famille");
                        for (int i = 0; i < deliver.length(); i++) {
                            JSONObject element = (JSONObject) deliver .get(i);
                            id_famille = element.getString("id_fam");
                            lib_famille=element.getString("lib_fam");

                            db.addFamille(id_famille,lib_famille);

                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }    else{
                        Toast.makeText(getApplicationContext(),
                                jObj.getString("message"), Toast.LENGTH_LONG).show();
                    }
                }	catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
               /* Toast.makeText(getApplicationContext(),
                        "Connexion impossible, reessayer SVP!", Toast.LENGTH_LONG).show();
              //  hideDialog();
               // finish();*/
            }
        }) ;
        int socketTimeout = 3000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        infofam.setRetryPolicy(policy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(infofam, tag_string_fam);
    }

    //***************************************************//
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}