package info.androidhive.alphagestion.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import info.androidhive.alphagestion.Model.Model_Bon;
import info.androidhive.alphagestion.R;

/**
 * Created by Jean Philippe on 30/07/2016.
 */
    public class Adapter_bon extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Model_Bon> requestList;
    public  Model_Bon m;

    // public Button action;
    public TextView id,reference,nom_livreur,numero_livreur,date_emission;


    public Adapter_bon(Activity activity, List<Model_Bon> requestList) {
        this.activity = activity;
        this.requestList= requestList;
    }

    @Override
    public int getCount() {
        return requestList.size();
    }

    @Override
    public Object getItem(int location) {
        return requestList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_bon, null);

         id = (TextView) convertView.findViewById(R.id.req);
        reference = (TextView) convertView.findViewById(R.id.reference);
        nom_livreur = (TextView) convertView.findViewById(R.id.nomlivreur);
        numero_livreur = (TextView) convertView.findViewById(R.id.numerolivreur);
        date_emission = (TextView) convertView.findViewById(R.id.date);

        // getting movie data for the row
        m = requestList.get(position);

        id.setText(m.getIdbon());
      //  reference.setText("Bon de sortie N°"+m.getRefbon());
        reference.setText(m.getRefbon());
        nom_livreur.setText(m.getNomlivreur());
        numero_livreur.setText(m.getNumlivreur());
        date_emission.setText(m.getDate());

        return convertView;
    }

}
