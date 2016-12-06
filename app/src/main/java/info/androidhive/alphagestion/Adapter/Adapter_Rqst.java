package info.androidhive.alphagestion.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import info.androidhive.alphagestion.Model.Model_Rqst;
import info.androidhive.alphagestion.R;

/**
 * Created by Jean Philippe on 14/06/2016.
 */
public class Adapter_Rqst extends RecyclerView.Adapter<Adapter_Rqst.MyViewHolder> {

private List<Model_Rqst> requestList;
    private Activity activity;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView reference,date;

    public MyViewHolder(View view) {
        super(view);
       reference = (TextView) view.findViewById(R.id.title);
        date = (TextView) view.findViewById(R.id.year);

    }
}

    public Adapter_Rqst(Activity activity, List<Model_Rqst> requestList) {
        this.activity = activity;
        this.requestList = requestList;
    }

    public Adapter_Rqst(List<Model_Rqst> requestList) {
        this.requestList = requestList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_commande, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Model_Rqst request = requestList.get(position);
        holder.reference.setText(request.getRef());
        holder.date.setText(request.getDate());

    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }
}
