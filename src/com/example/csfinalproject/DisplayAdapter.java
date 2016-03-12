package com.example.csfinalproject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class DisplayAdapter extends BaseAdapter{
	
	private Context mContext;
	private ArrayList<String> expenseId = new ArrayList<String>();
	private ArrayList<String> expenseAmount = new ArrayList<String>();
	private ArrayList<String> expenseDescription = new ArrayList<String>();
	private ArrayList<String> expenseDate= new ArrayList<String>();
	private ArrayList<String> expenseRecurring = new ArrayList<String>();
	    

	public DisplayAdapter(Context c, ArrayList<String> id,ArrayList<String> amount, ArrayList<String> description , ArrayList<String> date , ArrayList<String> recurring) {
        this.mContext = c;

        this.expenseId = id;
        this.expenseAmount = amount;
        this.expenseDate = date;
        this.expenseDescription = description;
        this.expenseRecurring = recurring;
    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return expenseId.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View child, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder mHolder;
        LayoutInflater layoutInflater;
        if (child == null) {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.listeachexpense, null);
            mHolder = new Holder();
            mHolder.txt_expenseId = (TextView) child.findViewById(R.id.txt_expenseId);
            mHolder.txt_expenseAmount = (TextView) child.findViewById(R.id.txt_expenseAmount);
            mHolder.txt_expenseDescription = (TextView) child.findViewById(R.id.txt_expenseDescription);
            mHolder.txt_expenseRecurring = (TextView) child.findViewById(R.id.txt_expenseRecurring);
            mHolder.txt_expenseDate = (TextView) child.findViewById(R.id.txt_expenseDate);
            child.setTag(mHolder);
        } else {
            mHolder = (Holder) child.getTag();
        }
        mHolder.txt_expenseId.setText(expenseId.get(position));
        String amount = expenseAmount.get(position);
        amount = "$" + amount;
        mHolder.txt_expenseAmount.setText(amount);
        mHolder.txt_expenseAmount.setTextColor(Color.WHITE);
        mHolder.txt_expenseDescription.setText(expenseDescription.get(position));
        mHolder.txt_expenseDescription.setTextColor(Color.CYAN);
        Date date = Date.valueOf(expenseDate.get(position));
        String month = new SimpleDateFormat("MMM").format(date);
        String day = new SimpleDateFormat("d").format(date);
        String newDate = month + " " + day ;
        mHolder.txt_expenseDate.setText(newDate);
        mHolder.txt_expenseDate.setTextColor(Color.CYAN);
        mHolder.txt_expenseRecurring.setText(expenseRecurring.get(position));

        return child;
	}
	
	public class Holder {
        TextView txt_expenseId;
        TextView txt_expenseAmount;
        TextView txt_expenseDescription;
        TextView txt_expenseDate;
        TextView txt_expenseRecurring;
    }

}
