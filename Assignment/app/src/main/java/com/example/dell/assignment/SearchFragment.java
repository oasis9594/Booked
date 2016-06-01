package com.example.dell.assignment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

    FloatingActionButton FAB;
    Toolbar toolbar;
    RecyclerView recyclerView;
    String mode;
    Integer[] data;
    ArrayList <UserDetails> users;
    ArrayList<Integer> listIds;
    AutoCompleteTextView id_text;
    MyDBHandler dbHandler;
    CoordinatorLayout coordinatorLayout;
    MyCustomAdapter myCustomAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        toolbar=(Toolbar) view.findViewById(R.id.sdtoolbar);
        Bundle bundle=this.getArguments();
        if(bundle==null)
        {
            Log.w(MyConstants.TAG, "bundle null in SearchFragment");
        }

        coordinatorLayout=(CoordinatorLayout)view.findViewById(R.id.coord_content);
        users=new ArrayList<>();
        mode=bundle.getString(MyConstants.MODE_KEY);
        FAB=(FloatingActionButton)view.findViewById(R.id.mfab);
        dbHandler=MyDBHandler.getInstance(getActivity());
        users=dbHandler.getAllUsers();
        Log.w(MyConstants.TAG, "got users from database");
        listIds=new ArrayList<>();
        for(UserDetails u:users)
        {
            listIds.add(u.getId());
            Log.w(MyConstants.TAG, " "+u.getId());
        }
        data=listIds.toArray(new Integer[listIds.size()]);
        Log.w(MyConstants.TAG, data.length+"");
        for(int i=0;i<data.length;i++)
        {
            Log.w(MyConstants.TAG, data[i]+" ");
        }
        Log.w(MyConstants.TAG, "autocomplete data feeded");
        final ArrayAdapter<?> idAdapter = new ArrayAdapter<Object>(getActivity(), android.R.layout.simple_dropdown_item_1line, data);
        Log.w(MyConstants.TAG, "autocomplete adapter created");

        idAdapter.notifyDataSetChanged();
        if(mode==null)
        {
            Log.w(MyConstants.TAG , "mode null");
        }
        assert mode != null;
        if(mode.equals(MyConstants.SEARCH_KEY))
        {
            FAB.setImageResource(R.drawable.ic_search2);
            toolbar.setTitle("Search");
        }
        else
        {
            FAB.setImageResource(R.drawable.ic_content_remove);
            toolbar.setTitle("Remove");
        }

        Log.w(MyConstants.TAG, "onCreateView");
        recyclerView=(RecyclerView)view.findViewById(R.id.myListView);
        // use a linear layout manager
        mLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        Log.w(MyConstants.TAG, "onCreateView");

        for(UserDetails u:users)
        {
            Log.w(MyConstants.TAG, " "+u.getId());
        }
        myCustomAdapter = new MyCustomAdapter(users, getActivity(), mode);
        Log.w(MyConstants.TAG, "onCreateView");
        recyclerView.setAdapter(myCustomAdapter);
        Log.w(MyConstants.TAG, "onCreateView");
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View DialogView=inflater.inflate(R.layout.enter_id, null);
                id_text=(AutoCompleteTextView)DialogView.findViewById(R.id.idDialogEnter);
                id_text.setAdapter(idAdapter);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(DialogView);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            int m_id = Integer.parseInt(id_text.getText().toString());
                            if (!dbHandler.userExists(m_id)) {
                                Toast.makeText(getActivity().getApplicationContext(), "Entry with this id doesn't exists", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(getActivity(), GetDetails.class);
                                intent.putExtra(MyConstants.ID_KEY, m_id);
                                intent.putExtra(MyConstants.MODE_KEY, mode);
                                startActivity(intent);
                            }
                        }catch (NumberFormatException e)
                        {
                            Log.w(MyConstants.TAG, e.getMessage());
                            Toast.makeText(getActivity(), "Enter valid id", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

        //InputMethodManager im = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //im.showSoftInput(recyclerView, InputMethodManager.SHOW_FORCED);
        //myCustomAdapter.notify();
        //Toast.makeText
        // (getActivity().getApplicationContext(), "", Toast.LENGTH_SHORT).show();
        return view;
    }

}
