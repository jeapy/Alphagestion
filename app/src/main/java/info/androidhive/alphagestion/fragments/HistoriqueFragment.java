package info.androidhive.alphagestion.fragments;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;

import info.androidhive.alphagestion.R;
import info.androidhive.alphagestion.activity.HistcomActivity;

import info.androidhive.alphagestion.activity.OldBonSortieActivity;

import info.androidhive.alphagestion.activity.RetourInterneOldActivity;
import info.androidhive.alphagestion.activity.SortieInterneOldActivity;


/**
 * Created by Jean Philippe on 24/05/2016.
 */
public class HistoriqueFragment extends Fragment {

    public HistoriqueFragment() {
        // Required empty public constructor
    }
    private FloatingActionButton btn1,btn2,btn3,btn4;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_historique, container, false);

        btn1= (FloatingActionButton) rootView.findViewById(R.id.menu_item);
        btn2= (FloatingActionButton)rootView.findViewById(R.id.menu_item1);
        btn3= (FloatingActionButton) rootView.findViewById(R.id.menu_item3);
        btn4= (FloatingActionButton) rootView.findViewById(R.id.menu_item4);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), HistcomActivity.class));
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), OldBonSortieActivity.class));
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SortieInterneOldActivity.class));
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RetourInterneOldActivity.class));
            }
        });



        return rootView;
    }




}
