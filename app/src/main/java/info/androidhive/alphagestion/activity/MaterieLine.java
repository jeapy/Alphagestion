package info.androidhive.alphagestion.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import info.androidhive.alphagestion.Adapter.Adapter_old_com;
import info.androidhive.alphagestion.R;
import info.androidhive.alphagestion.helper.SQLiteHandler;

/**
 * Created by Jean Philippe on 21/07/2016.
 */
public class MaterieLine extends AppCompatActivity {
    private Toolbar toolbar;
    public SQLiteHandler db;
    public String reference, user;
    private Bundle req ;
    private String reqid;
    private TextView hd;

    private ListView listView;
    public Adapter_old_com LAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrieline);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new SQLiteHandler(getApplicationContext());

        req = getIntent().getExtras();
        reqid =	(String) req.get("ref");

        listView = (ListView) findViewById(R.id.list_commande);

        LAdapter = new Adapter_old_com(MaterieLine.this, db.getRequestLineDetails(reqid));

        //Mettre les valeurs de la vue
        listView.setAdapter(LAdapter);
/*
        hd =(TextView) findViewById(R.id.headcd);
        hd.setText("Commande n°" + reqid);
       */


    }
}
