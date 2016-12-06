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

import info.androidhive.alphagestion.Adapter.Adapter_old_com;
import info.androidhive.alphagestion.R;
import info.androidhive.alphagestion.helper.DBAdapter;
import info.androidhive.alphagestion.helper.SQLiteHandler;
import info.androidhive.alphagestion.helper.SessionManager;

/**
 * Created by Jean Philippe on 01/09/2016.
 */
public class DetailSortieOldActivity extends AppCompatActivity {
    private static final String TAG =DetailActivity.class.getSimpleName();
    private Toolbar toolbar;
    private ListView listView;
    public Adapter_old_com LAdapter;
    public SQLiteHandler db;
    private Bundle req;
    private String reqid;
    private TextView hd;
    public String reference, date,ref;
    public SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailsortieold);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());


        req = getIntent().getExtras();
        reqid = (String) req.get("requ");

        hd = (TextView) findViewById(R.id.headcd);
        hd.setText("Sortie n°" + reqid);
        listView = (ListView) findViewById(R.id.list_commande);

        LAdapter = new Adapter_old_com(DetailSortieOldActivity.this, db.getRequestLineDetails(reqid));

        //Mettre les valeurs de la vue
        listView.setAdapter(LAdapter);
        listView.setOnItemClickListener(new ListClick());


    }

    public class ListClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View rootView, int position,
                                long id) {

        }
    }

}
