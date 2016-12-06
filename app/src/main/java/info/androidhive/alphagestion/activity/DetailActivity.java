package info.androidhive.alphagestion.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.alphagestion.Adapter.Adapter_old_com;
import info.androidhive.alphagestion.R;
import info.androidhive.alphagestion.app.AppConfig;
import info.androidhive.alphagestion.app.AppController;
import info.androidhive.alphagestion.helper.DBAdapter;
import info.androidhive.alphagestion.helper.SQLiteHandler;
import info.androidhive.alphagestion.helper.SessionManager;

/**
 * Created by Jean Philippe on 21/07/2016.
 */
public class DetailActivity extends AppCompatActivity {
    private static final String TAG =DetailActivity.class.getSimpleName();
    private Toolbar toolbar;
    private ListView listView;
    public Adapter_old_com LAdapter;
    public SQLiteHandler db;
    private  Bundle req;
    private String reqid;
    private TextView hd;
    private Button valider;
    private ProgressDialog  pDialog;
    public DBAdapter dbAdapter;
    public String reference, date, user,val ,ref,dt,usr ,refers,matr,reqr,qtm;
    public SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        dbAdapter = DBAdapter.getDBAdapterInstance(DetailActivity.this);
        valider = (Button) findViewById(R.id.childButton);
        req = getIntent().getExtras();
        reqid = (String) req.get("requ");

        hd = (TextView) findViewById(R.id.headcd);
        hd.setText("Commande n°" + reqid);
        listView = (ListView) findViewById(R.id.list_commande);

        LAdapter = new Adapter_old_com(DetailActivity.this, db.getRequestLineDetails(reqid));

        //Mettre les valeurs de la vue
        listView.setAdapter(LAdapter);

        listView.setOnItemClickListener(new
                AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView < ? > parent, View view,int position, long id){

                TextView tv = (TextView) view.findViewById(R.id.idmat);
                refers = tv.getText().toString();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetailActivity.this);
                alertDialog.setTitle("ACTION");
                alertDialog.setMessage("Effectuez une action sur ce materiel ");
                alertDialog.setIcon(R.drawable.ic_logo);
                final EditText input = new EditText(DetailActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("Modif",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String pass = input.getText().toString();

                              try {
                                    dbAdapter.createDataBase();
                                } catch (IOException e) {
                                    Log.i("*** select ", e.getMessage());
                                }

                                if (pass.length()!=0) {
                                    int n = Integer.parseInt(pass);
                                    dbAdapter.openDataBase();
                                    ContentValues cv = new ContentValues();                                                    //
                                    cv.put("qt", n);//
                                    dbAdapter.updateRecordsInDB("lignecom", cv, "ref_com= ? and ref_art=?", new String[]{String.valueOf(reqid),String.valueOf( refers )});

                                    Toast.makeText(getApplicationContext(), "modification OK", Toast.LENGTH_SHORT).show();
                                    refresh();

                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "vide", Toast.LENGTH_SHORT)
                                            .show();
                                } /*
                                dbAdapter.openDataBase();
                                String query = "SELECT * FROM requete WHERE ref_reqst =?";

                                ArrayList<ArrayList<String>> stringList = dbAdapter.selectRecordsFromDBList(query, new String[]{String.valueOf(reqid)});
                                dbAdapter.close();
                                for (int i = 0; i < stringList.size(); i++) {
                                    ArrayList<String> list = stringList.get(i);
                                    ref = list.get(1);
                                    usr = list.get(2);
                                    CreateNewRequest(ref, usr);
                                }*/


                            }
                        });

                alertDialog.setNegativeButton("Suppprimer",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                try {
                                    dbAdapter.createDataBase();
                                } catch (IOException e) {
                                    Log.i("*** select ", e.getMessage());
                                }
                                dbAdapter.openDataBase();

                                dbAdapter.deleteRecordInDB("lignecom","ref_com= ? and ref_art=?",new String[]{String.valueOf(reqid),String.valueOf( refers )});
                                    Toast.makeText(getApplicationContext(), "suppression OK", Toast.LENGTH_SHORT).show();
                                    refresh();
                            }
                        });
                alertDialog.setNeutralButton("Annul",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });

                alertDialog.show();

            }
        });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetailActivity.this);
                alertDialog.setTitle("COMMANDE");
                alertDialog.setMessage("Voulez vous enregistrer cette commande " +  reqid +" ?");
                alertDialog.setIcon(R.drawable.ic_logo);

                alertDialog.setPositiveButton("VALIDEZ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                          /*      DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
                            Calendar cal = Calendar.getInstance();
                            date = dateFormat.format(cal.getTime());
                            //get user id
                            user = session.getId();
                                                                        */
                       dbAdapter= DBAdapter.getDBAdapterInstance(DetailActivity.this);
                        try {
                            dbAdapter.createDataBase();
                        } catch (IOException e) {
                            Log.i("*** select ",e.getMessage());
                        }

                        dbAdapter.openDataBase();
                        String query="SELECT * FROM requete WHERE ref_reqst =?";

                        ArrayList<ArrayList<String>> stringList = dbAdapter.selectRecordsFromDBList(query,  new String[] { String.valueOf(reqid) });
                        dbAdapter.close();
                        for (int i = 0; i < stringList.size(); i++) {
                            ArrayList<String> list = stringList.get(i);
                                ref=list.get(1);
                            usr =list.get(2);


                            CreateNewRequest(ref,usr);
                        }


                    }
                            });

                            alertDialog.setNegativeButton("ANNULER",
                            new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                            Toast.makeText(getApplicationContext(), "Commande non enregistré", Toast.LENGTH_LONG).show();
                            }
                            });

                            alertDialog.show();

            }
        });


    }


public void refresh(){
    listView = (ListView) findViewById(R.id.list_commande);
    LAdapter = new Adapter_old_com(DetailActivity.this, db.getRequestLineDetails(reqid));
    //Mettre les valeurs de la vue
    listView.setAdapter(LAdapter);
   LAdapter.notifyDataSetChanged();
}

    public void  CreateNewRequest(final String ref_request, final String user){

        pDialog = new ProgressDialog(DetailActivity.this);
        pDialog.setMessage("Transmission commande..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
        String tag_string_req = "req_register";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADD_REQUEST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                pDialog.hide();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        dbAdapter.openDataBase();
                        String query="SELECT * FROM lignecom WHERE ref_com =?";

                        ArrayList<ArrayList<String>> stringList = dbAdapter.selectRecordsFromDBList(query,  new String[] { String.valueOf(reqid) });
                        dbAdapter.close();
                        for (int i = 0; i < stringList.size(); i++) {
                            ArrayList<String> list = stringList.get(i);
                            reqr=list.get(1);
                            matr =list.get(2);
                            qtm=list.get(4);

                            CreateNewRequestLine(matr,reqr,qtm);
                        }
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
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
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("ref", ref_request);
                params.put("usr", user);

                return params;
            }

        };
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }




    public void  CreateNewRequestLine(final String ref_materiel, final String ref_request, final String quantite){
        String tag_string_req = "req_register";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADD_REQUESTLINE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        db.updateRequest(reqid, "1");
                        Intent intent = new Intent( DetailActivity.this,CommandeActivity.class);
                        startActivity(intent);
                        SendSms();
                        Toast.makeText(getApplicationContext(),"Votre commande à bien été transmise" , Toast.LENGTH_LONG).show();
                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
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
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("mat", ref_materiel);
                params.put("req", ref_request);
                params.put("qt", quantite);

                return params;
            }

        };
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }




    public void  SendSms(){
        String tag_string_req = "sms";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SMS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {


                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        })
                ;
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
