package info.androidhive.alphagestion.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import info.androidhive.alphagestion.Adapter.Adapter_List;
import info.androidhive.alphagestion.Adapter.Adapter_interne;
import info.androidhive.alphagestion.R;
import info.androidhive.alphagestion.helper.SQLiteHandler;

/**
 * Created by Jean Philippe on 12/08/2016.
 */
public class SortieInterneActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton sortie;
    public SQLiteHandler db;
    private ListView listView;
    public Adapter_interne LAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sortieinterne);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new SQLiteHandler(getApplicationContext());

        sortie = (FloatingActionButton) findViewById(R.id.sortie);


        listView = (ListView) findViewById(R.id.list_commande);

        LAdapter = new Adapter_interne(this, db.getSortieInterne());
        listView.setAdapter(LAdapter);
        refresh();

        sortie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addrequestinfo();

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                TextView tv = (TextView) arg1.findViewById(R.id.reference);
                String refer = tv.getText().toString();
                Intent intent = new Intent(SortieInterneActivity.this, DetailSortieActivity.class);
                intent.putExtra("requ", refer);
                startActivity(intent);
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,  int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.reference);
                String refers = tv.getText().toString();

                Intent intent = new Intent(SortieInterneActivity.this,MaterielSortieActivity.class);
                intent.putExtra("reqst",refers);
                startActivity(intent);
            }
        });
    }


    public void refresh() {

        LAdapter = new Adapter_interne(this, db.getSortieInterne());
        listView.setAdapter(LAdapter);
        LAdapter.notifyDataSetChanged();
    }

    public void addrequestinfo() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SortieInterneActivity.this);
        alertDialog.setTitle("SORTIE DE MATERIEL");
        alertDialog.setMessage("Entrez le nom de l\'ouvrier");

        final EditText input = new EditText(SortieInterneActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.ic_logo);

        alertDialog.setPositiveButton("VALIDEZ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String pass = input.getText().toString();

                        if (!pass.equals("")) {
                            //Reference de la sortie
                            int min = 25000;
                            int max = 50000;
                            Random rand = new Random();
                            int nombreAleatoire = rand.nextInt(max - min + 1) + min;
                            String reference = "SRT_" + nombreAleatoire;

                            //Date
                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
                            Calendar cal = Calendar.getInstance();
                            String  date = dateFormat.format(cal.getTime());

                           String val="0";
                            //Add inormation into database
                            db.addsortieinterne(reference,date,pass,val,"0","","");
                            refresh();
                       Toast.makeText(
                                    getApplicationContext(),
                                    "Sortie enregistrée", Toast.LENGTH_SHORT)
                                    .show();

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
}




