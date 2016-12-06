package info.androidhive.alphagestion.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import info.androidhive.alphagestion.Model.Model_interne;
import info.androidhive.alphagestion.R;

/**
 * Created by Jean Philippe on 15/08/2016.
 */
public class Adapter_interne extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Model_interne> requestList;
    public  Model_interne m;

    // public Button action;
    public TextView reference,nom_livreur,date_emission;


    public Adapter_interne(Activity activity, List<Model_interne> requestList) {
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
            convertView = inflater.inflate(R.layout.list_interne, null);


        reference = (TextView) convertView.findViewById(R.id.reference);
        nom_livreur = (TextView) convertView.findViewById(R.id.nomouvrier);
        date_emission = (TextView) convertView.findViewById(R.id.date);

        // getting movie data for the row
        m = requestList.get(position);


        reference.setText(m.getRefsrt());
        nom_livreur.setText(m.getNomouvrier());
        date_emission.setText(m.getDate());

        return convertView;
    }

}
