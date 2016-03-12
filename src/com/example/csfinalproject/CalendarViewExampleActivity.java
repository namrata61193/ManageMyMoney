package com.example.csfinalproject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;

import com.androidhive.androidsqlite.*;

public class CalendarViewExampleActivity extends Activity{
	
	CalendarView calendar;
	LoginDataBaseAdapter loginDataBaseAdapter;
	String username;
	
	List <Bill> bills;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.calendarview);
	calendar = (CalendarView)findViewById(R.id.calendar);
	
	username = getIntent().getStringExtra("USERNAME");
	
	loginDataBaseAdapter=new LoginDataBaseAdapter(this);
    loginDataBaseAdapter=loginDataBaseAdapter.open();
    
    bills = loginDataBaseAdapter.getBills(username);
	
	calendar.setOnDateChangeListener(new OnDateChangeListener(){

		@SuppressLint("SimpleDateFormat")
		@Override
		public void onSelectedDayChange(CalendarView view,int year, int month, int dayOfMonth) {
			
			List <Bill> today = new ArrayList<Bill>();
			
			for(int i = 0 ; i < bills.size() ; i++)
			{
				String dayBill = new SimpleDateFormat("d").format(bills.get(i).getDate());
				int dayInt = Integer.parseInt(dayBill);
				
				String monthBill = new SimpleDateFormat("M").format(bills.get(i).getDate());
				int monthInt = Integer.parseInt(monthBill);
				
				String yearBill = new SimpleDateFormat("y").format(bills.get(i).getDate());
				int yearInt = Integer.parseInt(yearBill);
				
				if(dayInt == dayOfMonth && (month+1) == monthInt && yearInt == year)
				{
					today.add(bills.get(i));
				}
				
			}
			
			for(int i = 0 ; i < today.size() ; i++)
			{
				String amount = String.valueOf(today.get(i).getAmount());
				Toast.makeText(getApplicationContext(),today.get(i).getDescription()+ " - " + amount,Toast.LENGTH_LONG).show();
	
			}
		
			
		}
		
	});
	
	}
	

}
