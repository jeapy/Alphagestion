package info.androidhive.alphagestion.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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

import info.androidhive.alphagestion.Adapter.Adapter_famille;
import info.androidhive.alphagestion.R;
import info.androidhive.alphagestion.app.AppConfig;
import info.androidhive.alphagestion.app.AppController;
import info.androidhive.alphagestion.helper.SQLiteHandler;
import info.androidhive.alphagestion.helper.SessionManager;

/**
 * Created by Jean Philippe on 28/06/2016.
 */
public class FamilleActivity extends AppCompatActivity {

    private static final String TAG =FamilleActivity.class.getSimpleName();


    private Toolbar toolbar;
    private FloatingActionButton update;
    private ListView listView;
    public Adapter_famille LAdapter;

    public SQLiteHandler db;
    private ProgressDialog pDialog;
    public String reference, user;
    public SessionManager session;
    private Bundle req ;
    private String reqid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_famille);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        req = getIntent().getExtras();
        reqid =	(String) req.get("request");



        listView = (ListView) findViewById(R.id.list_commande);
        LAdapter = new Adapter_famille(this, db.getFamilleDetails());
        listView.setAdapter(LAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
              //  String selectedSweet = listView.getItemAtPosition(pos).toString();
              TextView textView = (TextView) arg1.findViewById(R.id.year);
                    String text = textView.getText().toString();
                Toast.makeText(getApplicationContext(), text + "longclick is selected!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,  int position, long id) {
              TextView tv = (TextView) view.findViewById(R.id.title);
                String refers = tv.getText().toString();

                Intent intent = new Intent(FamilleActivity.this, MaterielsActivity.class);
                intent.putExtra("famille",refers);
                intent.putExtra("reqst",reqid);
                startActivity(intent);
            }
        });

        update = (FloatingActionButton) findViewById(R.id.com);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(FamilleActivity.this);
                alertDialog.setTitle("Famille de materiels");
                alertDialog.setMessage("Voulez vous faire une mise à jour?");
                alertDialog.setIcon(R.drawable.ic_logo);
                alertDialog.setPositiveButton("VALIDEZ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                GetFamilleMateriel();
                                refresh();                            }
                        });
                alertDialog.setNegativeButton("ANNULER",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Mise à jour Annulé", Toast.LENGTH_LONG).show();
                            }
                        });
                alertDialog.show();
            }
        });
    }


    private void GetFamilleMateriel() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Recherche...");
        showDialog();
        StringRequest info = new StringRequest(Request.Method.GET,
                AppConfig.URL_FAMILLE_MATERIEL,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        db.DeleteAllFamille();
                        JSONArray deliver = jObj.getJSONArray("famille");
                        for (int i = 0; i < deliver.length(); i++) {
                            JSONObject element = (JSONObject) deliver .get(i);

                            reference = element.getString("id_fam");
                            user=element.getString("lib_fam");
                            db.addFamille(reference, user);
                            Toast.makeText(getApplicationContext(), "Mise à jour OK", Toast.LENGTH_LONG).show();

                        }
                        refresh();
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
                Log.e(TAG, "Get materiel famille Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Connexion impossible, reessayer SVP!", Toast.LENGTH_LONG).show();
                hideDialog();
                        finish();
            }
        }) ;
       int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        info.setRetryPolicy(policy);


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(info, tag_string_req);
    }
    public void refresh() {

        LAdapter = new Adapter_famille(this, db.getFamilleDetails());
        listView.setAdapter(LAdapter);
        LAdapter.notifyDataSetChanged();
    }



    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}