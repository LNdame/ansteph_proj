package ansteph.com.beecab.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


import ansteph.com.beecab.R;
import ansteph.com.beecab.model.Referral;

/**
 * Created by loicStephan on 29/09/16.
 */
public class ReferralListAdapter extends ArrayAdapter<Referral> {

    Context context;
    int layoutResourceId;

    ArrayList<Referral> referrals  = null;

    public ReferralListAdapter(Context context, int layoutResourceId, ArrayList<Referral> referrals) {
        super(context, layoutResourceId, referrals);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.referrals = referrals;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View row = convertView;
        ReferralHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context ).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent,false);

            holder = new ReferralHolder();
            holder.txtContact = (TextView) row.findViewById(R.id.txtContact);
            holder.txtDate = (TextView) row.findViewById(R.id.txtDateSent);
            holder.imgbtnStatus = (ImageView) row.findViewById(R.id.imgbtnStatus);

         row.setTag(holder);
        }else
        {
        holder = (ReferralHolder) row.getTag();
        }

        final Referral  ref = referrals.get(position);

        holder.txtContact.setText(ref.getProvidedContact());
        holder.txtDate.setText(ref.getDateSent());

        switch (ref.getStatus())
        {
            case 0:holder.imgbtnStatus.setImageResource(R.mipmap.ic_pending); break;
            case 1:holder.imgbtnStatus.setImageResource(R.mipmap.ic_valid);break;
            case 2: holder.imgbtnStatus.setImageResource(R.mipmap.ic_close);break;
            default:holder.imgbtnStatus.setImageResource(R.mipmap.ic_pending) ; break;
        }

        if(ref.getStatus() == 0){

        }else if (ref.getStatus() == 1){

        }


    return row;

    }

    static class ReferralHolder
    {
        TextView txtContact, txtDate;

        ImageView imgbtnStatus;
    }
}
