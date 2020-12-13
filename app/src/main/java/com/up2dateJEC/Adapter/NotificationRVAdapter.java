package com.up2dateJEC.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.up2dateJEC.R;
import com.up2dateJEC.Category_Detail;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by User on 2/12/2018.
 */

public class NotificationRVAdapter extends RecyclerView.Adapter<NotificationRVAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> Title = new ArrayList<>();
    private ArrayList<String> Disc = new ArrayList<>();
    private ArrayList<String> Department = new ArrayList<>();
    private ArrayList<String> NoticeTime = new ArrayList<>();
    private Context mContext;

    public NotificationRVAdapter(Context context, ArrayList<String> names, ArrayList<String> imageUrls,ArrayList<String> mDepartment,ArrayList<String> ntime) {
        Title = names;
        Disc = imageUrls;
        Department = mDepartment;
        NoticeTime = ntime;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificationrvlyt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.title.setText(Title.get(position));
        holder.disc.setText(Disc.get(position));
        holder.dep.setText(Department.get(position));
        holder.ntime.setText(NoticeTime.get(position));


    }

    @Override
    public int getItemCount() {
        return Title.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView disc;
        TextView dep;
        TextView ntime;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notititle);
            disc = itemView.findViewById(R.id.notimsg);
            dep = itemView.findViewById(R.id.notidepartment);
            ntime = itemView.findViewById(R.id.notitime);
        }
    }
}
