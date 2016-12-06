package info.androidhive.alphagestion.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.TextView;


import info.androidhive.alphagestion.Adapter.Adapter_interne;
import info.androidhive.alphagestion.R;
import info.androidhive.alphagestion.helper.SQLiteHandler;

/**
 * Created by Jean Philippe on 31/08/2016.
 */
public class SortieInterneOldActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton sortie;
    public SQLiteHandler db;
    private ListView listView;
    public Adapter_interne LAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sortieinterneold);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new SQLiteHandler(getApplicationContext());



        listView = (ListView) findViewById(R.id.list_commande);

        LAdapter = new Adapter_interne(this, db.getOldSortieInterne());
        listView.setAdapter(LAdapter);
        refresh();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                TextView tv = (TextView) arg1.findViewById(R.id.reference);
                String refer = tv.getText().toString();
                Intent intent = new Intent(SortieInterneOldActivity.this, DetailSortieOldActivity.class);
                intent.putExtra("requ", refer);
                startActivity(intent);

            }
        });


    }


    public void refresh() {

        LAdapter = new Adapter_interne(this, db.getOldSortieInterne());
        listView.setAdapter(LAdapter);
        LAdapter.notifyDataSetChanged();
    }


}
