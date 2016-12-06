package info.androidhive.alphagestion.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import info.androidhive.alphagestion.Adapter.Adapter_old_com;
import info.androidhive.alphagestion.R;
import info.androidhive.alphagestion.helper.DBAdapter;
import info.androidhive.alphagestion.helper.SQLiteHandler;
import info.androidhive.alphagestion.helper.SessionManager;

/**
 * Created by Jean Philippe on 31/08/2016.
 */
public class DetailSortieActivity extends AppCompatActivity {
    private static final String TAG =DetailActivity.class.getSimpleName();
    private Toolbar toolbar;
    private ListView listView;
    public Adapter_old_com LAdapter;
    public SQLiteHandler db;
    private Bundle req;
    private String reqid;
    private TextView hd;
    private Button valider;
    private ProgressDialog pDialog;
    public DBAdapter dbAdapter;
    public String reference, date, user,val ,ref,dt,usr ,refers,matr,reqr,qtm;
    public SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailsortie);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        valider= (Button) findViewById(R.id.Button);
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());


        req = getIntent().getExtras();
        reqid = (String) req.get("requ");

        hd = (TextView) findViewById(R.id.headcd);
        hd.setText("Sortie n°" + reqid);
        listView = (ListView) findViewById(R.id.list_commande);

        LAdapter = new Adapter_old_com(DetailSortieActivity.this, db.getRequestLineDetails(reqid));

        //Mettre les valeurs de la vue
        listView.setAdapter(LAdapter);
        listView.setOnItemClickListener(new ListClick());

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetailSortieActivity.this);
                alertDialog.setTitle("SORTIE");
                alertDialog.setMessage("Voulez vous valider cette sortie?");
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
                                dbAdapter = DBAdapter.getDBAdapterInstance(DetailSortieActivity.this);
                                try {
                                    dbAdapter.createDataBase();
                                } catch (IOException e) {
                                    Log.i("*** select ", e.getMessage());
                                }

                                dbAdapter.openDataBase();
                                ContentValues cv = new ContentValues();                                                    //
                                cv.put("status", "1");//
                                dbAdapter.updateRecordsInDB("sortieinterne", cv, "ref_si= ?", new String[]{String.valueOf(reqid)});
                                dbAdapter.close();

                                        finish();
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

    public class ListClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View rootView, int position,
                                long id) {

        }
    }






}
