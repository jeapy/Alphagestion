package info.androidhive.alphagestion.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import info.androidhive.alphagestion.R;
import info.androidhive.alphagestion.activity.RetourInterneActivity;
import info.androidhive.alphagestion.activity.SortieInterneActivity;

/**
 * Created by Jean Philippe on 24/05/2016.
 */
public class TransactionFragment extends Fragment {

    public FloatingActionButton entree,sortie;

    public TransactionFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_transaction, container, false);

      entree  = (FloatingActionButton) rootView.findViewById(R.id.btnEntree);
       sortie = (FloatingActionButton) rootView.findViewById(R.id.btnSortie);

       entree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Toast.makeText(getActivity(), "entree", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), RetourInterneActivity.class);
                startActivity(intent);
            }
        });
        sortie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    Toast.makeText(getActivity(), "sortie", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), SortieInterneActivity.class);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

}
