package info.androidhive.alphagestion.fragments;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;

import android.content.Context;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


import info.androidhive.alphagestion.Adapter.Adapter_bon;
import info.androidhive.alphagestion.R;
import info.androidhive.alphagestion.activity.MainActivity;
import info.androidhive.alphagestion.activity.NotificationLine;
import info.androidhive.alphagestion.app.AppConfig;
import info.androidhive.alphagestion.app.AppController;
import info.androidhive.alphagestion.helper.DBAdapter;
import info.androidhive.alphagestion.helper.SQLiteHandler;


/**
 * Created by Jean Philippe on 24/05/2016.
 */
public class NotificationFragment extends Fragment {

    public NotificationFragment() {
        // Required empty public constructor
    }
    private static final String TAG ="NotificationFragment";
    private ListView listView;
    public Adapter_bon LAdapter;
    public SQLiteHandler db;
    private  String bon;
    private  Bonsortie bons;
    private  String  idbon  ,refbon , nomliv  ,  numliv, dtbon,idartstock,qtartstock;
    public DBAdapter dbAdapter;


    private NetworkInfo netInfo;
    private ConnectivityManager connect;
    private ProgressDialog  pDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =   inflater.inflate(R.layout.fragment_notification, container, false);

        db = new SQLiteHandler(getActivity());
        listView = (ListView) rootView.findViewById(R.id.notif_bon);


       //Refreshfragment();

        LAdapter = new Adapter_bon(getActivity(), db.getBonDetails());
        listView.setAdapter(LAdapter);
        listView.setOnItemClickListener(new ListClick());

        LAdapter.notifyDataSetChanged();
        /****RECUPERATION DES REFERENCES DE REQUETTE DANS LA BASE INTERENE ****/
        dbAdapter = DBAdapter.getDBAdapterInstance(getActivity());
        try {
            dbAdapter.createDataBase();
        } catch (IOException e) {
            Log.i("*** select ", e.getMessage());
        }

        dbAdapter.openDataBase();
        String query = "SELECT * FROM requete WHERE status =1";
        ArrayList<ArrayList<String>> stringList = dbAdapter.selectRecordsFromDBList(query, null);
        dbAdapter.close();
        for (int i = 0; i < stringList.size(); i++) {
            ArrayList<String> list = stringList.get(i);
            bon = list.get(1);

        }
        /****FIN  ****/

        /****VERIFICATION DE LA PRESENCE ET DE LA CONNEXION A INTERNET ET
         *  EXECUTION DE LA REQUETTE DE RECUPERATION DES EVENTUELLES BON DE SORTIE ****/
        connect = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = connect.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isAvailable() && netInfo.isConnected()){
            callAsynchronousTask();
            refresh();
        }else{

        }
                            /****FIN ****/
        return rootView;
    }
            /*ACTION AU CLICK SUR UN ELEMENT DE LA LISTE*/
    public class ListClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View rootView, int position,
                                long id) {
            TextView tv = (TextView) rootView.findViewById(R.id.reference);
            String refers = tv.getText().toString();
            // TODO Auto-generated method stub
            Intent intent = new Intent(getActivity(), NotificationLine.class);
            intent.putExtra("lignebon",refers);
            startActivity(intent);
        }
    }
                        /****FIN ****/

                    /****RAFFRAICHIR LA LISTE ****/
    public void refresh() {
        LAdapter = new Adapter_bon(getActivity(), db.getBonDetails());
        listView.setAdapter(LAdapter);
        LAdapter.notifyDataSetChanged();

    }
    /****FIN ****/
/*
    private final void createNotification(){
       // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(getActivity(), MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(getActivity(), (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(getActivity())
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_desc)).setSmallIcon(R.drawable.icon)
                .setContentIntent(pIntent).build();
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);

    }*/

    /****RECUPERATION DU BON DE SORTIE DANS LA BASE EXTERNE****/
    private void GetBonInfo(final String fam) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        StringRequest infoconnect = new StringRequest(Request.Method.POST,
                AppConfig.URL_BON_SORTIE,  new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        db.DeleteAllBon();
                        JSONArray deliver = jObj.getJSONArray("bon");

                        for (int i = 0; i < deliver.length(); i++) {
                            JSONObject element = (JSONObject) deliver .get(i);

                             idbon  =  element.getString("ref_req");
                             refbon =  element.getString("reference_bon");
                             nomliv =  element.getString("nom_livreur");
                             numliv =  element.getString("numero_livreur");
                             dtbon  =  element.getString("date_bon");

                            db.addbonsortie(idbon, refbon, nomliv, numliv, "0", dtbon);

                        }
                        Toast.makeText(getActivity(),"Bon à valider!", Toast.LENGTH_LONG).show();
                        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        v.vibrate(5000);
                     //   createNotification();
                        refresh();
                    }    else{
                       /* Toast.makeText(getActivity(),  jObj.getString("message"), Toast.LENGTH_LONG).show();*/
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
               /* Toast.makeText(getActivity(),
                        "Connexion impossible,reessayer SVP", Toast.LENGTH_LONG).show();*/
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                // params.put("nom", "password");
                params.put("reqcom", fam);
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
    /****FIN ****/

                 /****REQUETTE DE RECUPERATION DES BONS DE SORTIE****/
    private class Bonsortie extends AsyncTask<Void, Integer, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // Toast.makeText(getActivity(), "Début du traitement asynchrone", Toast.LENGTH_LONG).show();
        }
        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
        }
        @Override
        protected Void doInBackground(Void... arg0) {

             GetBonInfo(bon);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
         //  Toast.makeText(getActivity(), "Le traitement asynchrone est terminé", Toast.LENGTH_LONG).show();
        }
    }
                                /****FIN ****/
        public void callAsynchronousTask() {
            final Handler handler = new Handler();
            Timer timer = new Timer();
            TimerTask doAsynchronousTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            try {
                                bons=new Bonsortie();
                                bons.execute();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                            }
                        }
                    });
                }
            };
            timer.schedule(doAsynchronousTask, 0, 50000); //execute in every 50000 ms
        }


public void Refreshfragment(){
    Fragment frg = null;
    frg = getFragmentManager().findFragmentByTag(TAG);
    final android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
    ft.detach(frg);
    ft.attach(frg);
    ft.commit();

}
}
