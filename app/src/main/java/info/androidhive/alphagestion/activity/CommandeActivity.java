package info.androidhive.alphagestion.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;

import java.util.Random;

import info.androidhive.alphagestion.Adapter.Adapter_List;
import info.androidhive.alphagestion.R;

import info.androidhive.alphagestion.helper.SQLiteHandler;
import info.androidhive.alphagestion.helper.SessionManager;

/**
 * Created by Jean Philippe on 07/06/2016.
 */
public class CommandeActivity extends AppCompatActivity {
    private static final String TAG =CommandeActivity.class.getSimpleName();


    private Toolbar toolbar;
    private FloatingActionButton commande;
    private ListView listView;
    public Adapter_List LAdapter;
    public SQLiteHandler db;
    public String reference, date, user,val  ,refers;
    public SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());



        listView = (ListView) findViewById(R.id.list_commande);


        LAdapter = new Adapter_List(this, db.getRequestDetails());
        listView.setAdapter(LAdapter);
        refresh();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                TextView tv = (TextView) arg1.findViewById(R.id.title);
                String refer = tv.getText().toString();
                Intent intent = new Intent(CommandeActivity.this, DetailActivity.class);
                intent.putExtra("requ", refer);
                startActivity(intent);
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
                        TextView tv = (TextView) view.findViewById(R.id.title);
                       refers = tv.getText().toString();

                        Intent intent = new Intent(CommandeActivity.this, FamilleActivity.class);
                             intent.putExtra("request",refers);
                        startActivity(intent);
                        }
        });


        commande = (FloatingActionButton) findViewById(R.id.com);
        commande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CommandeActivity.this);
                alertDialog.setTitle("COMMANDE");
                alertDialog.setMessage("Voulez vous créer un nouvelle commande?");
                alertDialog.setIcon(R.drawable.ic_logo);

                alertDialog.setPositiveButton("VALIDEZ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                addrequestinfo();
                                refresh();
                                Toast.makeText(getApplicationContext(), "Creation OK", Toast.LENGTH_LONG).show();

                            }
                        });

                alertDialog.setNegativeButton("ANNULER",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                refresh();
                                Toast.makeText(getApplicationContext(), "Creation Annulé", Toast.LENGTH_LONG).show();
                            }
                        });

                alertDialog.show();

            }
        });


        }


public void addrequestinfo() {
        //generate activity_commande request
        int min = 4444444;
        int max = 8888888;
        Random rand = new Random();
        int nombreAleatoire = rand.nextInt(max - min + 1) + min;
       // reference = "REQM_" + nombreAleatoire;
    reference =  String.valueOf(nombreAleatoire);
        //get current date
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        date = dateFormat.format(cal.getTime());
        //get user id
        user = session.getId();
        val="0";
        //Add inormation into database
        db.addRequest(reference, date, user,val);


        }

public void refresh() {

        LAdapter = new Adapter_List(this, db.getRequestDetails());
        listView.setAdapter(LAdapter);
        LAdapter.notifyDataSetChanged();
        }

public void delect() {
        db.DeleteRequest();
        }

        }


 /*
        LAdapter = new Adapter_List(CommandeActivity.this,db.getRequestDetails(), new Adapter_List.BtnClickListener() {
            @Override
            public void onBtnClick(int position) {

                            TextView tv = (TextView) findViewById(R.id.title);
                            final String refer = tv.getText().toString();
                            // TODO Auto-generated method stub
                            // Call your function which creates and shows the dialog here
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CommandeActivity.this);
                            alertDialog.setTitle("COMMANDE"+ position);
                            alertDialog.setMessage("Voulez vous enregistrer cette commande " + refer +" ?");
                            alertDialog.setIcon(R.drawable.ic_logo);

                            alertDialog.setPositiveButton("VALIDEZ",
                            new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
                          /*  Calendar cal = Calendar.getInstance();
                            date = dateFormat.format(cal.getTime());
                            //get user id
                            user = session.getId();

                       dbAdapter= DBAdapter.getDBAdapterInstance(CommandeActivity.this);
                        try {
                            dbAdapter.createDataBase();
                        } catch (IOException e) {
                            Log.i("*** select ",e.getMessage());
                        }

                        dbAdapter.openDataBase();
                        String query="SELECT * FROM requete WHERE ref_reqst =?";

                        ArrayList<ArrayList<String>> stringList = dbAdapter.selectRecordsFromDBList(query,  new String[] { String.valueOf(refer) });
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

                            });*/