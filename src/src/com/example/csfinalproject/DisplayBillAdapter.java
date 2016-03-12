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


@SuppressLint("SimpleDateFormat")
public class DisplayBillAdapter extends BaseAdapter{
	
	private Context mContext;
	private ArrayList<String> billId = new ArrayList<String>();
	private ArrayList<String> billAmount = new ArrayList<String>();
	private ArrayList<String> billDescription = new ArrayList<String>();
	private ArrayList<String> billDate= new ArrayList<String>();
	private ArrayList<String> billRecurring = new ArrayList<String>();
	    

	public DisplayBillAdapter(Context c, ArrayList<String> id,ArrayList<String> amount, ArrayList<String> description , ArrayList<String> date , ArrayList<String> recurring) {
        this.mContext = c;

        this.billId = id;
        this.billAmount = amount;
        this.billDate = date;
        this.billDescription = description;
        this.billRecurring = recurring;
    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return billId.size();
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
            child = layoutInflater.inflate(R.layout.listeachbill, null);
            mHolder = new Holder();
            mHolder.txt_billId = (TextView) child.findViewById(R.id.txt_billId);
            mHolder.txt_billAmount = (TextView) child.findViewById(R.id.txt_billAmount);
            mHolder.txt_billDescription = (TextView) child.findViewById(R.id.txt_billDescription);
            mHolder.txt_billRecurring = (TextView) child.findViewById(R.id.txt_billRecurring);
            mHolder.txt_billDate = (TextView) child.findViewById(R.id.txt_billDate);
            child.setTag(mHolder);
        } else {
            mHolder = (Holder) child.getTag();
        }
        mHolder.txt_billId.setText(billId.get(position));
        String amount = billAmount.get(position);
        amount = "$" + amount;
        mHolder.txt_billAmount.setText(amount);
        mHolder.txt_billAmount.setTextColor(Color.WHITE);
        mHolder.txt_billDescription.setText(billDescription.get(position));
        mHolder.txt_billDescription.setTextColor(Color.CYAN);
        Date date = Date.valueOf(billDate.get(position));
        String month = new SimpleDateFormat("MMM").format(date);
        String day = new SimpleDateFormat("d").format(date);
        String newDate = month + " " + day ;
        mHolder.txt_billDate.setText(newDate);
        mHolder.txt_billDate.setTextColor(Color.CYAN);
        mHolder.txt_billRecurring.setText(billRecurring.get(position));

        return child;
	}
	
	public class Holder {
        TextView txt_billId;
        TextView txt_billAmount;
        TextView txt_billDescription;
        TextView txt_billDate;
        TextView txt_billRecurring;
    }

	
}
