package info.androidhive.alphagestion.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import info.androidhive.alphagestion.Adapter.Adapter_old_com;
import info.androidhive.alphagestion.R;
import info.androidhive.alphagestion.helper.SQLiteHandler;

/**
 * Created by Jean Philippe on 03/08/2016.
 */
public class HistcomActivity extends AppCompatActivity {
    private ListView listView;
    public Adapter_old_com LAdapter;
    public SQLiteHandler db;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histcom);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        db = new SQLiteHandler(HistcomActivity.this);
        listView = (ListView) findViewById(R.id.list_commande);

        LAdapter = new Adapter_old_com(HistcomActivity.this, db.getOldRequestDetails());
        //Mettre les valeurs de la vue
        listView.setAdapter( LAdapter);
        listView.setOnItemClickListener(new ListClick());

        refresh();

}
    public class ListClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View rootView, int position,
                                long id) {
            // TODO Auto-generated method stub
            TextView listText = (TextView)rootView.findViewById(R.id.title);
            String text = listText.getText().toString();

            // create intent to start another activity
            Intent intent = new Intent(HistcomActivity.this,MaterieLine.class);
            // add the selected text item to our intent.
            intent.putExtra("ref", text);
            startActivity(intent);
        }
    }

    public void refresh() {

        LAdapter = new Adapter_old_com(HistcomActivity.this, db.getOldRequestDetails());
        //Mettre les valeurs de la vue
        listView.setAdapter(LAdapter);
        LAdapter.notifyDataSetChanged();
    }
}