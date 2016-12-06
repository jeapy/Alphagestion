package info.androidhive.alphagestion.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import info.androidhive.alphagestion.Model.Model_Rqst;
import info.androidhive.alphagestion.R;

/**
 * Created by Jean Philippe on 28/06/2016.
 */
public class Adapter_famille extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Model_Rqst> requestList;


    public Adapter_famille(Activity activity, List<Model_Rqst> requestList) {
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_famille, null);



        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView comment = (TextView) convertView.findViewById(R.id.year);


        // getting movie data for the row
        Model_Rqst m = requestList.get(position);
        // title
        title.setText(m.getRef());
        // rating
        comment.setText(m.getDate());
        // release year
        title.setVisibility(View.INVISIBLE);
        return convertView;
    }

}
