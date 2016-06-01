package com.example.dell.assignment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;


public class StoreFragment extends Fragment implements AdapterView.OnItemSelectedListener{


    Toolbar storeToolbar;
    EditText name, entity, phone;
    Button save;
    UserDetails item;
    Spinner region;
    RadioButton r1, r2;


    String pn;
    int reg;
    Boolean a;

    MyDBHandler dbHandler;
    public StoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        storeToolbar=(Toolbar)view.findViewById(R.id.store_toolbar);
        storeToolbar.setTitle("Add User");
        storeToolbar.setTitleTextColor(Color.WHITE);

        a=true;
        reg=0;
        dbHandler=MyDBHandler.getInstance(getActivity());
        name=(EditText) view.findViewById(R.id.store_editName);
        phone=(EditText) view.findViewById(R.id.store_editphone);
        entity=(EditText) view.findViewById(R.id.store_editEntity);
        region=(Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.regions, android.R.layout.simple_spinner_item);
    // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
        region.setAdapter(adapter);
        region.setOnItemSelectedListener(this);

        r1=(RadioButton) view.findViewById(R.id.radio_yes);
        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a=true;
            }
        });
        r2=(RadioButton) view.findViewById(R.id.radio_no);
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a=false;
            }
        });
        save=(Button)view.findViewById(R.id.store_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item=new UserDetails();
                if(name.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity(), "Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                item.setName(name.getText().toString());
                Log.w(MyConstants.TAG, "Name set");
                pn=phone.getText().toString();
                for(int i=0;i<pn.length();i++)
                {
                    if(pn.charAt(i)>'9'||pn.charAt(i)<'0') {
                        Toast.makeText(getActivity(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                item.setPhone(pn);
                Log.w(MyConstants.TAG, "Phone set");
                item.setEntity(entity.getText().toString());
                Log.w(MyConstants.TAG, "Entity set");
                item.setRegionOfWork(reg);
                Log.w(MyConstants.TAG, "Region of work set");
                if(item.getPhone().equals(""))
                {
                    Toast.makeText(getActivity(), "Enter Phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(item.getEntity().equals(""))
                {
                    Toast.makeText(getActivity(), "Enter Entity", Toast.LENGTH_SHORT).show();
                    return;
                }
                item.setAvailable(a);
                Log.w(MyConstants.TAG, "available set");
                dbHandler.addUser(item);
                Toast.makeText(getActivity(), "Entry successfully saved", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        reg=position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
