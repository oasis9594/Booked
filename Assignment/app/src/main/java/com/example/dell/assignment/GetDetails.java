package com.example.dell.assignment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GetDetails extends AppCompatActivity {

    Toolbar toolbar;
    TextView t_id, t_name, t_entity, t_region, t_phone, t_available;
    Button b;
    String mode;
    UserDetails user;
    Activity activity;
    ImageView imageView;
    MyDBHandler dbHandler;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_details);
        toolbar=(Toolbar)findViewById(R.id.det_toolbar);
        dbHandler=MyDBHandler.getInstance(this);
        Bundle bundle=getIntent().getExtras();
        if(bundle==null) {
            Log.w(MyConstants.TAG, "Bundle null in getDetails");
            finish();
        }
        activity=this;
        assert bundle!=null;
        id=bundle.getInt(MyConstants.ID_KEY);
        mode=bundle.getString(MyConstants.MODE_KEY);
        user=new UserDetails();
        user=dbHandler.getUser(id);
        if(user==null)
        {
            Log.w(MyConstants.TAG, "User null in getDetails");
            finish();
        }
        Log.w(MyConstants.TAG, "onCreate1");
        try{
            toolbar.setTitle(user.getName());
            toolbar.setTitleTextColor(Color.WHITE);
        }catch (Exception e)
        {
            Log.w(MyConstants.TAG, e.getMessage());
        }

        Log.w(MyConstants.TAG, "onCreate1");
        t_id=(TextView)findViewById(R.id.det_editID);
        t_id.setText(String.valueOf(user.getId()));


        Log.w(MyConstants.TAG, "onCreate2");
        t_name=(TextView)findViewById(R.id.det_editName);
        t_name.setText(user.getName());

        t_entity=(TextView)findViewById(R.id.det_editEntity);
        t_entity.setText(user.getEntity());

        t_region=(TextView)findViewById(R.id.det_reg);
        t_region.setText(getResources().getStringArray(R.array.regions)[user.getRegionOfWork()]);

        t_phone=(TextView)findViewById(R.id.det_editphone);
        t_phone.setText(user.getPhone());

        imageView=(ImageView)findViewById(R.id.imageView);
        t_available=(TextView)findViewById(R.id.det_available);
        b=(Button)findViewById(R.id.det_button);

        Log.w(MyConstants.TAG, "onCreate3");
        if(user.isAvailable())
        {
            imageView.setImageResource(R.drawable.ic_action_label_green);
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
            t_available.setText("Available");
            t_available.setTextColor(ContextCompat.getColor(this, R.color.green));
            b.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        }
        else {

            imageView.setImageResource(R.drawable.ic_action_label_red);
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            t_available.setText("Not Available");
            t_available.setTextColor(ContextCompat.getColor(this, R.color.red));
            b.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
        }

        Log.w(MyConstants.TAG, "onCreate4");
        if(mode.equals(MyConstants.REMOVE_KEY))
        {
            toolbar.setBackgroundColor(ContextCompat.getColor(this, MyConstants.getColor(user.getRegionOfWork())));
            b.setBackgroundColor(ContextCompat.getColor(this, MyConstants.getColor(user.getRegionOfWork())));
            b.setText("Delete");
        }
        else {
            if(user.isAvailable())
                b.setText("Not Available");
            else b.setText("Available");
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode.equals(MyConstants.SEARCH_KEY)) {
                    user.setAvailable(!user.isAvailable());
                    if(user.isAvailable()) {
                        imageView.setImageResource(R.drawable.ic_action_label_green);
                        toolbar.setBackgroundColor(ContextCompat.getColor(activity, R.color.green));
                        t_available.setText("Available");
                        t_available.setTextColor(ContextCompat.getColor(activity, R.color.green));
                        b.setBackgroundColor(ContextCompat.getColor(activity, R.color.green));
                        b.setText("Not Available");
                    }
                    else {

                        imageView.setImageResource(R.drawable.ic_action_label_red);
                        toolbar.setBackgroundColor(ContextCompat.getColor(activity, R.color.red));
                        t_available.setText("Not Available");
                        t_available.setTextColor(ContextCompat.getColor(activity, R.color.red));
                        b.setBackgroundColor(ContextCompat.getColor(activity, R.color.red));
                        b.setText("Available");
                    }
                    dbHandler.updateAvailable(user.getId(), user.isAvailable());
                }
                else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                    dialog.setTitle("Delete");
                    dialog.setMessage("Are you sure you want to delete this entry?");
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbHandler.removeUser(user.getId());
                            Intent intent=new Intent(activity, MainActivity.class);
                            intent.putExtra(MyConstants.MODE_KEY, mode);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
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
        });

        setSupportActionBar(toolbar);
        Log.w(MyConstants.TAG, "onCreate5");
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(activity, MainActivity.class);
        intent.putExtra(MyConstants.MODE_KEY, mode);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onBackPressed();
    }
}
