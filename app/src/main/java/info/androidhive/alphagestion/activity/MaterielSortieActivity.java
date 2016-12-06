package info.androidhive.alphagestion.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.alphagestion.Adapter.Adapter_Materiels;
import info.androidhive.alphagestion.Model.Model_Materiels;
import info.androidhive.alphagestion.R;
import info.androidhive.alphagestion.helper.SQLiteHandler;

/**
 * Created by Jean Philippe on 15/08/2016.
 */
public class MaterielSortieActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private Button back;
    private ListView listView;
    public Adapter_Materiels MAdapter;

    public SQLiteHandler db;
    private Bundle req ;
    private String idreq;

    //public String reference, libelle, idreq, id_famille, lib_famille;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materiels);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new SQLiteHandler(getApplicationContext());
        back= (Button) findViewById(R.id.back);
        req = getIntent().getExtras();
        idreq =	(String) req.get("reqst");

        listView = (ListView) findViewById(R.id.lvExp);
        MAdapter = new Adapter_Materiels(this, db.getMateriels());
        listView.setAdapter(MAdapter);




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.mat_name);
                final String refers = tv.getText().toString();

                TextView refart = (TextView) view.findViewById(R.id.mat_ref);
                final String refarts = refart.getText().toString();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MaterielSortieActivity.this);
                alertDialog.setTitle("QUANTITE");
                alertDialog.setMessage("Entrez la quantité( " + refers + ")");

                final EditText input = new EditText(MaterielSortieActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.ic_logo);

                alertDialog.setPositiveButton("VALIDEZ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String pass = input.getText().toString();
                              //  int n = Integer.parseInt(pass);
                                if (pass.length()!=0) {
                                    int n = Integer.parseInt(pass);
                                    db.addLigncommande(idreq, refarts, refers, n);
                                    Toast.makeText(getApplicationContext(), pass, Toast.LENGTH_SHORT).show();


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
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }

        });


    }

}