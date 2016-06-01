package com.example.dell.assignment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.ViewHolder>{

    ArrayList<UserDetails> myUsers;
    Activity mContext;
    String mode;
    String[] regions;
    MyDBHandler dbHandler;
    public MyCustomAdapter(ArrayList<UserDetails> myDataset, Activity context, String mode) {
        myUsers=new ArrayList<>();
        myUsers = myDataset;
        this.mContext=context;
        this.mode=mode;
        regions=mContext.getResources().getStringArray(R.array.regions);
        dbHandler=MyDBHandler.getInstance(context);
        for(UserDetails u: myUsers)
        {
            Log.w(MyConstants.TAG, u.getId()+" ");
        }
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView myImage, phoneImage;
        TextView phone_text, name_text, available_text, region, user_id;
        Button button;
        public IMyViewHolderClicks mListener;

        public ViewHolder(View customView, IMyViewHolderClicks listener) {
            super(customView);


            mListener = listener;
            myImage=(ImageView) customView.findViewById(R.id.myImage);
            phone_text=(TextView)customView.findViewById(R.id.text_phone);
            name_text=(TextView)customView.findViewById(R.id.myName);
            available_text=(TextView)customView.findViewById(R.id.text_available);
            region=(TextView)customView.findViewById(R.id.text_region);
            user_id=(TextView)customView.findViewById(R.id.user_id);
            button=(Button)customView.findViewById(R.id.button);
            phoneImage=(ImageView)customView.findViewById(R.id.phone_image);

            button.setOnClickListener(this);
            customView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if(v instanceof Button) {
                mListener.reUser((Button)v, getAdapterPosition());
            }
            else {
                mListener.showUser(v, getAdapterPosition());
            }

        }
        public interface IMyViewHolderClicks
        {
            void reUser(Button b, int pos);
            void showUser(View v, int pos);
        }
    }
    @Override
    public MyCustomAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent,final int viewType) {

        Log.w(MyConstants.TAG, "onCreateViewHolder1");
        View customView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_row, parent, false);
        Log.w(MyConstants.TAG, "onCreateViewHolder2");
        ViewHolder vh=new ViewHolder(customView, new ViewHolder.IMyViewHolderClicks() {
            @Override
            public void reUser(Button b, final int pos) {
                UserDetails user=myUsers.get(pos);
                if(mode.equals(MyConstants.SEARCH_KEY))
                {
                    user.setAvailable(!user.isAvailable());
                    dbHandler.updateAvailable(user.getId(), user.isAvailable());
                    Log.w(MyConstants.TAG, user.isAvailable()+"");
                    notifyItemChanged(pos);
                }
                else
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                    dialog.setTitle("Delete");
                    dialog.setMessage("Are you sure you want to delete this entry?");
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteItem(pos);
                        }
                    });
                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }

            @Override
            public void showUser(View v, int pos) {
                Log.w(MyConstants.TAG, "showUser");
                Intent intent=new Intent(mContext, GetDetails.class);
                intent.putExtra(MyConstants.MODE_KEY, mode);
                intent.putExtra(MyConstants.ID_KEY, myUsers.get(pos).getId());
                Log.w(MyConstants.TAG, "showUser");
                mContext.startActivity(intent);
            }
        });

        Log.w(MyConstants.TAG, "onCreateViewHolder3");
        return vh;
    }

    private void deleteItem(int pos) {
        dbHandler.removeUser(myUsers.get(pos).getId());//remove from database
        myUsers.remove(pos);//remove from list
        notifyItemRemoved(pos);
    }

    @Override
    public void onBindViewHolder(MyCustomAdapter.ViewHolder holder, int position) {
        if(holder==null)
            return;
        Log.w(MyConstants.TAG, "onBindViewHolder1");
        UserDetails item=myUsers.get(position);
        holder.user_id.setText("ID: "+ item.getId());
        Log.w(MyConstants.TAG, "onBindViewHolder2");
        holder.name_text.setText(item.getName());
        holder.region.setText(regions[item.getRegionOfWork()]);
        holder.phone_text.setText(item.getPhone());
        if(item.isAvailable())
        {
            Log.w(MyConstants.TAG, "onBindViewHolder: available");
            holder.myImage.setImageResource(R.drawable.ic_action_label_green);
            holder.available_text.setText("Available");
            holder.available_text.setTextColor(ContextCompat.getColor(mContext, R.color.green));
            if(mode.equals(MyConstants.SEARCH_KEY))
                holder.button.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            holder.name_text.setPaintFlags(holder.name_text.getPaintFlags()&(~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        else
        {
            Log.w(MyConstants.TAG, "onBindViewHolder: not available");
            holder.myImage.setImageResource(R.drawable.ic_action_label_red);
            holder.available_text.setText("Not Available");
            holder.available_text.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            if(mode.equals(MyConstants.SEARCH_KEY))
                holder.button.setTextColor(ContextCompat.getColor(mContext, R.color.green));
            holder.name_text.setPaintFlags(holder.name_text.getPaintFlags()|(Paint.STRIKE_THRU_TEXT_FLAG));
        }
        Log.w(MyConstants.TAG, "onBindViewHolder3");
        if(mode.equals(MyConstants.REMOVE_KEY))
        {
            holder.button.setText("Delete Entry");
            holder.button.setTextColor(ContextCompat.getColor(mContext, MyConstants.getColor(item.getRegionOfWork())));
        }
        else
        {
            if(item.isAvailable())
                holder.button.setText("Not Available");
            else
                holder.button.setText("Available");
        }
        holder.region.setTextColor(ContextCompat.getColor(mContext, MyConstants.getColor(item.getRegionOfWork())));
        holder.phone_text.setTextColor(ContextCompat.getColor(mContext, MyConstants.getColor(item.getRegionOfWork())));
        Log.w(MyConstants.TAG, "onBindViewHolder4");
    }

    @Override
    public int getItemCount() {
        Log.w(MyConstants.TAG, "getItemCount "+myUsers.size());
        return myUsers.size();
    }
}
