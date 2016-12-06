/**
 * Created by Jean Philippe on 12/08/2016.
 */
package info.androidhive.alphagestion.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import info.androidhive.alphagestion.Adapter.Adapter_interne;
import info.androidhive.alphagestion.helper.SQLiteHandler;


import info.androidhive.alphagestion.Adapter.Adapter_interne;
import info.androidhive.alphagestion.R;
import info.androidhive.alphagestion.helper.DBAdapter;
import info.androidhive.alphagestion.helper.SQLiteHandler;

public class RetourInterneActivity extends AppCompatActivity {
    private Toolbar toolbar;

    public SQLiteHandler db;
    private ListView listView;
    public Adapter_interne LAdapter;
    public DBAdapter dbAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_retourinterne);


            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new SQLiteHandler(getApplicationContext());
        listView = (ListView) findViewById(R.id.list_commande);

        LAdapter = new Adapter_interne(this, db.getValideSortieInterne());
        listView.setAdapter(LAdapter);
        refresh();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                TextView tv = (TextView) arg1.findViewById(R.id.reference);
             final   String refer = tv.getText().toString();


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(RetourInterneActivity.this);
                alertDialog.setTitle("RETOUR INTERNE");
                alertDialog.setMessage("Entrez un commentaire");

                final EditText input = new EditText(RetourInterneActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.ic_logo);

                alertDialog.setPositiveButton("VALIDEZ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                final String pass = input.getText().toString();

                                if (!pass.equals("")) {

                                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
                                    Calendar cal = Calendar.getInstance();
                                   String date = dateFormat.format(cal.getTime());
                                    dbAdapter = DBAdapter.getDBAdapterInstance(RetourInterneActivity.this);
                                    try {
                                        dbAdapter.createDataBase();
                                    } catch (IOException e) {
                                        Log.i("*** select ", e.getMessage());
                                    }

                                    dbAdapter.openDataBase();
                                    ContentValues cv = new ContentValues();                                                    //
                                    cv.put("retour", "1");//
                                    cv.put("date_ri", date);//
                                    cv.put("commentaire",pass);//
                                    dbAdapter.updateRecordsInDB("sortieinterne", cv, "ref_si= ?", new String[]{String.valueOf(refer)});
                                    dbAdapter.close();

                                    refresh();
                                    Toast.makeText(RetourInterneActivity.this, pass, Toast.LENGTH_LONG).show();

                                }
                            }
                        });
                                alertDialog.setNegativeButton("ANNULER",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                Toast.makeText(RetourInterneActivity.this, "Annulé", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                alertDialog.show();


                            }
        });


    }


    public void refresh() {

        LAdapter = new Adapter_interne(this, db.getValideSortieInterne());
        listView.setAdapter(LAdapter);
        LAdapter.notifyDataSetChanged();
    }


}
