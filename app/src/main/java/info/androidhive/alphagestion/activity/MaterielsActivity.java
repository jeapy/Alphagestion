package info.androidhive.alphagestion.activity;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.alphagestion.Adapter.Adapter_Materiels;

import info.androidhive.alphagestion.Model.Model_Materiels;

import info.androidhive.alphagestion.R;
import info.androidhive.alphagestion.app.AppConfig;
import info.androidhive.alphagestion.app.AppController;
import info.androidhive.alphagestion.helper.SQLiteHandler;
import info.androidhive.alphagestion.helper.SessionManager;

/**
 * Created by Jean Philippe on 16/06/2016.
 */
public class MaterielsActivity extends AppCompatActivity {
    private static final String TAG =MaterielsActivity.class.getSimpleName();

    private Toolbar toolbar;
    private Button back;
    private ListView listView;
    public  Adapter_Materiels MAdapter;

    private List<Model_Materiels> matList = new ArrayList<>();
    public  SQLiteHandler db;

    public String reference, libelle, idreq ,id_famille,lib_famille;

    public SessionManager session;
    private ProgressDialog pDialog;
    private NetworkInfo netInfo;
    private ConnectivityManager connect;
    private Bundle familleid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materiels);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        back= (Button) findViewById(R.id.back);
        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

                    familleid = getIntent().getExtras();
                 id_famille =	(String) familleid.get("famille");
                     idreq =	(String) familleid.get("reqst");

            connect = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            netInfo = connect.getActiveNetworkInfo();
            if(netInfo != null && netInfo.isAvailable() && netInfo.isConnected()){

                GetMaterielInfo(id_famille);

                listView = (ListView) findViewById(R.id.lvExp);
                MAdapter = new Adapter_Materiels(this,matList );
                listView.setAdapter(MAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,  int position, long id) {
                        TextView tv = (TextView) view.findViewById(R.id.mat_name);
                        final   String refers = tv.getText().toString();

                        TextView refart = (TextView) view.findViewById(R.id.mat_ref);
                        final   String refarts = refart.getText().toString();

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MaterielsActivity.this);
                        alertDialog.setTitle("QUANTITE");
                        alertDialog.setMessage("Entrez la quantité( "+ refers+")");

                        final EditText input = new EditText(MaterielsActivity.this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        input.setLayoutParams(lp);
                        alertDialog.setView(input);
                        alertDialog.setIcon(R.drawable.ic_logo);

                        alertDialog.setPositiveButton("VALIDEZ",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        String pass = input.getText().toString();

                                        if (pass.length()!=0) {
                                            int n = Integer.parseInt(pass);
                                            db.addLigncommande(idreq, refarts,refers, n);
                                            Toast.makeText(getApplicationContext(), pass, Toast.LENGTH_SHORT).show();


                                        } else {
                                            Toast.makeText(
                                                    getApplicationContext(),
                                                    "vide", Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    }
                                });
                        alertDialog.setNegativeButton("ANNULER",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        Toast.makeText(getApplicationContext(), "Annulé", Toast.LENGTH_LONG).show();
                                    }
                                });
                        alertDialog.show();
                    }
                });

            }else{
                Toast.makeText(getApplicationContext(),
                        "Aucune connexion INTERNET", Toast.LENGTH_LONG)
                        .show();
                Intent intent = new Intent(MaterielsActivity.this,
                        FamilleActivity.class);
                startActivity(intent);
                finish();
            }




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }


        });
    }


   /* function to get livraison details in mysql db
    * */



    private void GetMaterielInfo(final String fam) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Connexion en cours ...");
        showDialog();


        StringRequest infoconnect = new StringRequest(Request.Method.POST,
                AppConfig.URL_MATERIEL,  new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        JSONArray deliver = jObj.getJSONArray("materiel");

                        for (int i = 0; i < deliver.length(); i++) {
                            JSONObject element = (JSONObject) deliver .get(i);
                            Model_Materiels mats = new Model_Materiels();
                            reference = element.getString("idmat");
                             libelle = element.getString("libmat");
                           // photo = element.getString("phtmat");

                            mats.setLibelle(element.getString("libmat"));
                            mats.setRef(element.getString("idmat"));

                            matList.add(mats);
                                                    }


                    }    else{

                        Toast.makeText(getApplicationContext(),
                                jObj.getString("message"), Toast.LENGTH_LONG).show();
                        finish();

                    }


                }	catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
                MAdapter.notifyDataSetChanged();            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Connexion impossible,reessayer SVP", Toast.LENGTH_LONG).show();
                hideDialog();
                finish();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                // params.put("nom", "password");
                params.put("id", fam);
                return params;
            }

        };
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        infoconnect.setRetryPolicy(policy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(infoconnect, tag_string_req);


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