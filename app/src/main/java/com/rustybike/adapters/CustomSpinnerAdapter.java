package com.rustybike.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rustybike.R;
import com.rustybike.models.Users;

import java.util.List;

public class CustomSpinnerAdapter extends BaseAdapter {
    Context context;
    List<Users> users;
    String[] usersType;
    LayoutInflater inflter;

    public CustomSpinnerAdapter(Context applicationContext, List<Users> users) {
        this.context = applicationContext;

        this.users = users;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);

        TextView tv =  view.findViewById(R.id.userNameitem);
        if(position == 0){
            // Set the hint text color gray
            tv.setTextColor(Color.GRAY);

        }
        else {
            tv.setTextColor(Color.BLACK);

        }
        return view;
    }
    @Override
    public boolean isEnabled(int position){
        if(position == 0)
        {
            // Disable the first item from Spinner
            // First item will be use for hint
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.users_spinner_item, null);
        TextView names =  view.findViewById(R.id.userNameitem);
        String userString = users.get(i).getFullName();
        if(users.get(i).getUserType().equals("D"))
        {
            userString +="(Driver)";
        }else if(users.get(i).getUserType().equals("R")){
            userString +="(Restaurant)";
        }else {
            userString +="";
        }
        names.setText(userString);

        return view;
    }
}