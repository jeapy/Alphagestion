package info.androidhive.alphagestion.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.alphagestion.Adapter.Adapter_List;
import info.androidhive.alphagestion.Adapter.Adapter_famille;
import info.androidhive.alphagestion.Model.Model_Rqst;
import info.androidhive.alphagestion.R;
import info.androidhive.alphagestion.app.AppConfig;
import info.androidhive.alphagestion.app.AppController;
import info.androidhive.alphagestion.helper.DBAdapter;
import info.androidhive.alphagestion.helper.SQLiteHandler;

/**
 * Created by Jean Philippe on 25/07/2016.
 */
public class NotificationLine extends AppCompatActivity {
    private static final String TAG =NotificationLine.class.getSimpleName();
    private Toolbar toolbar;
    public DBAdapter dbAdapter;
    private String ref,id,qt;
    private ProgressDialog pDialog;
    public SQLiteHandler db;
    private NetworkInfo netInfo;
    private ConnectivityManager connect;
    private List<Model_Rqst> matList = new ArrayList<>();
    public Adapter_List LAdapter;
    private ListView listView;
    private Button valider;

    private Bundle req ;
    private String reqid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifline);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new SQLiteHandler(getApplicationContext());

        req = getIntent().getExtras();
        reqid =	(String) req.get("lignebon");

        valider= (Button) findViewById(R.id.cButton);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        connect = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = connect.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isAvailable() && netInfo.isConnected()) {

            GetBonLine( reqid);
            listView = (ListView) findViewById(R.id.list_commande);
            LAdapter = new Adapter_List(this,matList );
            listView.setAdapter(LAdapter);
            LAdapter.notifyDataSetChanged();
       }else{    Toast.makeText(getApplicationContext(),
                "Aucune connexion INTERNET", Toast.LENGTH_LONG)
                .show();
            finish();}

        dbAdapter= DBAdapter.getDBAdapterInstance(NotificationLine.this);
        try {
            dbAdapter.createDataBase();
        } catch (IOException e) {
            Log.i("*** select ",e.getMessage());
        }

                                /*VALIDATION DU  BON DE SORTIE*/
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(NotificationLine.this);
                alertDialog.setTitle("VALIDATION LE BON");
                alertDialog.setMessage("Entrez un commentaire");

                final EditText input = new EditText(NotificationLine.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.ic_logo);

                alertDialog.setPositiveButton("VALIDEZ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final String pass = input.getText().toString();
                                //    int n = Integer.parseInt(pass);
                                if (!pass.equals("")) {
                                    /****DEBUT DE lA MISE A JOUR DANS LA BASE EXTERNE******/
                                    pDialog.setMessage("Mise à jour du bon..");
                                    pDialog.setIndeterminate(false);
                                    pDialog.setCancelable(true);
                                    showDialog();
                                    String tag_string_req = "req_register";
                                    StringRequest strReq = new StringRequest(Request.Method.POST,
                                            AppConfig.URL_UPDATE_BON_SORTIE, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d(TAG, "Register Response: " + response.toString());
                                            hideDialog();
                                            try {
                                                JSONObject jObj = new JSONObject(response);
                                                boolean error = jObj.getBoolean("error");
                                                if (!error) {
                                                    dbAdapter.openDataBase();
                                                    ContentValues cv = new ContentValues();                                                    //
                                                    cv.put("status", "1");//
                                                    dbAdapter.updateRecordsInDB("bonsortie", cv, "refsortie= ?", new String[]{String.valueOf(reqid)});
                                                    dbAdapter.close();
                                                    finish();
                                                    //  GetBonLine(refers);
                                                } else {
                                                    String errorMsg = jObj.getString("message");
                                                    Toast.makeText(NotificationLine.this,
                                                            errorMsg, Toast.LENGTH_LONG).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }, new Response.ErrorListener() {

                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e(TAG, "Registration Error: " + error.getMessage());
                                            Toast.makeText(NotificationLine.this,
                                                    error.getMessage(), Toast.LENGTH_LONG).show();
                                            hideDialog();
                                        }
                                    }) {

                                        @Override
                                        protected Map<String, String> getParams() {
                                            // Posting params to register url
                                            Map<String, String> params = new HashMap<String, String>();
                                            params.put("comment", pass);
                                            params.put("reqid", reqid);

                                            return params;
                                        }

                                    };
                                    int socketTimeout = 30000;//30 seconds - change to what you want
                                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                                    strReq.setRetryPolicy(policy);

                                    AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
                                    /****FIN****/

                                } else {
                                    Toast.makeText(
                                            NotificationLine.this,
                                            "vide", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        });
                alertDialog.setNegativeButton("REFUSEZ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final String pass = input.getText().toString();
                                //    int n = Integer.parseInt(pass);
                                if (!pass.equals("")) {
                                    /****DEBUT DE lA MISE A JOUR DANS LA BASE EXTERNE******/
                                    pDialog.setMessage("Mise à jour du bon..");
                                    pDialog.setIndeterminate(false);
                                    pDialog.setCancelable(true);
                                    showDialog();
                                    String tag_string_req = "req_register";
                                    StringRequest strReq = new StringRequest(Request.Method.POST,
                                            AppConfig.URL_REFUSER_BON_SORTIE, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d(TAG, "Register Response: " + response.toString());
                                            hideDialog();
                                            try {
                                                JSONObject jObj = new JSONObject(response);
                                                boolean error = jObj.getBoolean("error");
                                                if (!error) {
                                                    Toast.makeText(NotificationLine.this, "Approvisionnement refusé", Toast.LENGTH_LONG).show();
                                                    finish();
                                                    //  GetBonLine(refers);
                                                } else {
                                                    String errorMsg = jObj.getString("message");
                                                    Toast.makeText(NotificationLine.this,
                                                            errorMsg, Toast.LENGTH_LONG).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }, new Response.ErrorListener() {

                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e(TAG, "Registration Error: " + error.getMessage());
                                            Toast.makeText(NotificationLine.this,
                                                    error.getMessage(), Toast.LENGTH_LONG).show();
                                            hideDialog();
                                        }
                                    }) {

                                        @Override
                                        protected Map<String, String> getParams() {
                                            // Posting params to register url
                                            Map<String, String> params = new HashMap<String, String>();
                                            params.put("comment", pass);
                                            params.put("reqid", reqid);

                                            return params;
                                        }

                                    };
                                    int socketTimeout = 30000;//30 seconds - change to what you want
                                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                                    strReq.setRetryPolicy(policy);

                                    AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
                                    /****FIN****/

                                } else {
                                    Toast.makeText(
                                            NotificationLine.this,
                                            "vide", Toast.LENGTH_SHORT)
                                            .show();
                                }


                            }
                        });
                alertDialog.setNeutralButton("ANNULER",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int whitch){
                                Toast.makeText(NotificationLine.this, "Annuler", Toast.LENGTH_LONG).show();
                            }
                        });
                alertDialog.show();

            }
        });
        /****FIN ****/

    }



    private void GetBonLine(final String idbon) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Recherche...");
        showDialog();
        StringRequest info = new StringRequest(Request.Method.POST,
                AppConfig.URL_LIGNE_BON_SORTIE,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        JSONArray deliver = jObj.getJSONArray("lignebon");
                        for (int i = 0; i < deliver.length(); i++) {
                            JSONObject element = (JSONObject) deliver .get(i);
                            Model_Rqst mats = new Model_Rqst();
                        /*   id = element.getString("id_mat");
                            qt=element.getString("qt");
                                                */
                            mats.setRef(element.getString("id_mat"));
                            mats.setDate(element.getString("qt"));

                            matList.add(mats);

                        }
                        refresh();
                    }    else{

                       Toast.makeText(getApplicationContext(),
                                jObj.getString("message"), Toast.LENGTH_LONG).show();
                        finish();
                    }


                }	catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Get Bon line Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Connexion impossible , reessayer SVP!", Toast.LENGTH_LONG).show();
                hideDialog();
                finish();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("lbn", idbon);
                return params;
            }
        };
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        info.setRetryPolicy(policy);


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(info, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    public void refresh(){
        listView = (ListView) findViewById(R.id.list_commande);
        LAdapter = new Adapter_List(this,matList );
        listView.setAdapter(LAdapter);
        LAdapter.notifyDataSetChanged();
    }




}