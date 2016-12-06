package info.androidhive.alphagestion.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import info.androidhive.alphagestion.Model.Model_Materiels;
import info.androidhive.alphagestion.R;
import info.androidhive.alphagestion.app.AppController;

/**
 * Created by Jean Philippe on 16/06/2016.
 */
public class Adapter_Materiels extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Model_Materiels> Materielinfo;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public TextView lib,ref;
    public ImageView thumbNail;

   // private int[] imageArray;

    public Adapter_Materiels(Activity activity, List<Model_Materiels> Matinfo) {
        this.activity = activity;
        this.Materielinfo = Matinfo;
    }
    public Adapter_Materiels( List<Model_Materiels> requestList) {
        this.Materielinfo = requestList;
    }
    @Override
    public int getCount() {
        return Materielinfo.size();
    }

    @Override
    public Object getItem(int location) {
        return Materielinfo.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_materiel, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

     //   thumbNail = (NetworkImageView) convertView.findViewById(R.id.flag);
        lib = (TextView) convertView.findViewById(R.id.mat_name);
        ref=(TextView)convertView.findViewById(R.id.mat_ref);

        Model_Materiels m = Materielinfo.get(position);

  //    thumbNail.setImageResource(m.getImage());
      //  thumbNail.setImage(m.getImage(), imageLoader);
        // titre
        lib.setText(m.getLibelle());
        ref.setText(m.getRef());

        ref.setVisibility(View.INVISIBLE);

        return convertView;
    }
}