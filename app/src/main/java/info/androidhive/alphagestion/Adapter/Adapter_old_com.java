package info.androidhive.alphagestion.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import info.androidhive.alphagestion.Model.Model_Rqst;
import info.androidhive.alphagestion.R;

/**
 * Created by Jean Philippe on 21/07/2016.
 */
public class Adapter_old_com  extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Model_Rqst> requestList;
    public  Model_Rqst m;


    public TextView title,comment,id;



    public Adapter_old_com(Activity activity, List<Model_Rqst> requestList) {
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
            convertView = inflater.inflate(R.layout.list_old_commande, null);

        title = (TextView) convertView.findViewById(R.id.title);
        comment = (TextView) convertView.findViewById(R.id.year);
       id = (TextView) convertView.findViewById(R.id.idmat);

        // getting movie data for the row
        m = requestList.get(position);

        title.setText(m.getRef());
        comment.setText(m.getDate());
       id.setText(m.getId());


        return convertView;
    }



}
