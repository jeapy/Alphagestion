package info.androidhive.alphagestion.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import info.androidhive.alphagestion.Adapter.Adapter_bon;
import info.androidhive.alphagestion.R;
import info.androidhive.alphagestion.helper.SQLiteHandler;

/**
 * Created by Jean Philippe on 04/08/2016.
 */
public class OldBonSortieActivity extends AppCompatActivity {

    private ListView listView;
    public Adapter_bon LAdapter;
    public SQLiteHandler db;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oldbonsortie);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new SQLiteHandler(OldBonSortieActivity.this);

        listView = (ListView) findViewById(R.id.notif_bon);

        LAdapter = new Adapter_bon(OldBonSortieActivity.this, db.getOldBonDetails());
        listView.setAdapter(LAdapter);
        LAdapter.notifyDataSetChanged();


    }
    public void refresh() {
        LAdapter = new Adapter_bon(OldBonSortieActivity.this, db.getOldBonDetails());
        listView.setAdapter(LAdapter);
        LAdapter.notifyDataSetChanged();
    }
}
