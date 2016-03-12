package com.example.csfinalproject;

import com.androidhive.androidsqlite.Bill;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateBillActivity extends Activity{
	
EditText editTextAmountBill , editTextRecurring ;

	TextView editTextDateBill;
	
	private DatePicker date_picker;
	private int year;
	private int month ;
	private int day;

	static final int DATE_DIALOG_ID = 0;
   
	LoginDataBaseAdapter loginDataBaseAdapters;
	List<Bill> allBills ;

    ImageButton btnCreateBill;
    
    String username;
    
    Boolean isUpdate;
    
    Button btnChangeDate;
    
    private AlertDialog.Builder build;
    
    int updateID;
    
    Spinner spinnerDescription;
	
	@SuppressWarnings("unchecked")
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.createnewbill);
		
		android.app.ActionBar actionBar = getActionBar();
        actionBar.show();
		
		username = getIntent().getStringExtra("USERNAME");
		isUpdate = getIntent().getBooleanExtra("UPDATE", false);
		
		loginDataBaseAdapters=new LoginDataBaseAdapter(this);
        loginDataBaseAdapters=loginDataBaseAdapters.open();
        
        setCurrentDateOnView();
		addListenerOnButton();
        
        editTextAmountBill=(EditText)findViewById(R.id.editTextAmountBill);
        editTextDateBill=(TextView)findViewById(R.id.editTextDateBill);
        
        spinnerDescription = (Spinner)findViewById(R.id.SpinnerDescriptionBill);
        List<String> categories = new ArrayList<String>();
        categories.add("Rent");
        categories.add("Electric Bill");
        categories.add("Water Bill");
        categories.add("Food");
        categories.add("Clothes");
        categories.add("Education");
        categories.add("Other");
        
        @SuppressWarnings("rawtypes")
		ArrayAdapter dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDescription.setAdapter(dataAdapter);
        
        final CheckBox checkRecurring = (CheckBox) findViewById(R.id.checkBoxRecurring);
        
        
        if(isUpdate)
        {
        	updateID = Integer.parseInt(getIntent().getStringExtra("ID"));
        	
        	boolean recurring = Boolean.parseBoolean(getIntent().getStringExtra("RECURRING"));
        	spinnerDescription.setSelection(0);
        	
        	editTextAmountBill.setText(getIntent().getStringExtra("AMOUNT"));
        	editTextDateBill.setText(getIntent().getStringExtra("DATE"));
        	checkRecurring.setChecked(recurring);
        	
        }
        
        btnCreateBill=(ImageButton)findViewById(R.id.buttonCreateNewBill);
        
        btnCreateBill.setOnClickListener(new View.OnClickListener() {
        	 
            public void onClick(View v) {
                // TODO Auto-generated method stub
     
                String amount=editTextAmountBill.getText().toString();
                
                String date=editTextDateBill.getText().toString();
          
                final String category = String.valueOf(spinnerDescription.getSelectedItem());
                Calendar cal = Calendar.getInstance();
				java.util.Date utilDate = cal.getTime();
				java.sql.Date todayDate = new java.sql.Date(utilDate.getTime());
				
                
                if(amount.equals("")||date.equals(""))
                {
                        Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
                        return;
                }
                if(date.compareTo(String.valueOf(todayDate))< 0)
                {
                	Toast.makeText(getApplicationContext(), "Invalid Date", Toast.LENGTH_LONG).show();
                    return;
                }
                float getAmount =  Float.parseFloat(amount);
                if(getAmount < 0)
                {
                		Toast.makeText(getApplicationContext(), "Amount cannot be negative", Toast.LENGTH_LONG).show();
                		return;
                }
                
                
                final Date billDate = Date.valueOf(date);
                final float billAmount = Float.parseFloat(amount);
                boolean billRecurring;
                if(checkRecurring.isChecked())
                {
                	final CharSequence[] choice = {"Every 2 weeks" , "Every month" };
                	billRecurring = true;
                	if(!isUpdate)
                    {
                    	Bill newBill = new Bill(0 , billAmount , username , category , billDate , billRecurring);
                    	loginDataBaseAdapters.createBill(newBill);
                    }
                    else
                    {
                    	Bill updateBill = new Bill(updateID ,billAmount , username , category , billDate , billRecurring );
                    	loginDataBaseAdapters.updateBill(updateBill);
                    }
                	build = new AlertDialog.Builder(CreateBillActivity.this);
                	build.setTitle("How often should the bill be payed?");
                	build.setSingleChoiceItems(choice, -1, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if(choice[which] == "Every 2 weeks")
							{
								Calendar cal = Calendar.getInstance();
								cal.setTime(billDate);
								cal.add(Calendar.DATE , 14);
								java.util.Date utilDate = cal.getTime();
								java.sql.Date newDate = new java.sql.Date(utilDate.getTime());
								Bill recurringBill = new Bill(0 , billAmount , username , category , newDate , false);
			                	loginDataBaseAdapters.createBill(recurringBill);
			                	dialog.cancel();
							}
							else if(choice[which] == "Every month")
							{
								Calendar cal = Calendar.getInstance();
								cal.setTime(billDate);
								cal.add(Calendar.MONTH , 1);
								java.util.Date utilDate = cal.getTime();
								java.sql.Date newDate = new java.sql.Date(utilDate.getTime());
								Bill recurringBill = new Bill(0 , billAmount , username , category , newDate , false);
			                	loginDataBaseAdapters.createBill(recurringBill);
			                	dialog.cancel();
							}
							Intent billPage=new Intent(getApplicationContext(),ViewBillActivity.class);
			            	billPage.putExtra("USERNAME", username);
			            	billPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			                startActivity(billPage);
						}
					});
                	
                	build.setPositiveButton("Cancel ",new DialogInterface.OnClickListener() {

                        @SuppressLint("ShowToast")
                        @Override
						public void onClick(DialogInterface dialog, int which) {
                        	
                        	dialog.cancel();
                        }
                	});
                	
                	AlertDialog alert = build.create();
                    alert.show();

                    return ;	
                }
                else
                {
                	billRecurring = false;
                }
                if(!isUpdate)
                {
                	Bill newBill = new Bill(0 , billAmount , username , category , billDate , billRecurring);
                	loginDataBaseAdapters.createBill(newBill);	
                }
                else
                {
                	Bill updateBill = new Bill(updateID ,billAmount , username , category , billDate , billRecurring );
                	loginDataBaseAdapters.updateBill(updateBill);
                }
                
                Intent billPage=new Intent(getApplicationContext(),ViewBillActivity.class);
            	billPage.putExtra("USERNAME", username);
            	billPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(billPage);
                
            }
		
	});
        
	}
	
	@Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        loginDataBaseAdapters.close();
    }
	
	
	@Override
	   
    public boolean onCreateOptionsMenu(Menu menu) {

        // use an inflater to populate the ActionBar with items
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main, menu);

        return true;

    }



/**
 * On selecting action bar icons
 * */
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    // Take appropriate action for each action item click
    switch (item.getItemId()) {
    case R.id.viewexpenses:
        // search action
    	Intent expensePage=new Intent(getApplicationContext(),ViewExpenseActivity.class);
    	expensePage.putExtra("USERNAME", username);
    	expensePage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(expensePage);
        return true;
        
    case R.id.viewbills:
    	Intent billPage=new Intent(getApplicationContext(),ViewBillActivity.class);
    	billPage.putExtra("USERNAME", username);
    	billPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(billPage);
        return true;
        
    case R.id.logout:
    	Intent logout = new Intent(getApplicationContext() , MainActivity.class);
    	startActivity(logout);
    	return true;
    	
    case R.id.overview:
    	Intent overview = new Intent(getApplicationContext(),ActivityOverview.class);
    	overview.putExtra("USERNAME", username);
    	overview.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(overview);
        return true;
        
    case R.id.deleteAccount:
    	Intent deleteAccount = new Intent(getApplicationContext(),DeleteAccountActivity.class);
    	deleteAccount.putExtra("USERNAME", username);
    	deleteAccount.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(deleteAccount);
        return true;
    	
    default:
        return super.onOptionsItemSelected(item);
    }
    
}

	public void setCurrentDateOnView() {

		editTextDateBill = (TextView) findViewById(R.id.editTextDateBill);
		date_picker = (DatePicker) findViewById(R.id.dpResultBill);

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		editTextDateBill.setText(new StringBuilder().append(year).append("-").append(month+1).append("-").append(day));

		// set current date into datepicker
		date_picker.init(year, month, day, null);

	}

	public void addListenerOnButton() {

		btnChangeDate = (Button) findViewById(R.id.btnChangeDateBill);

		btnChangeDate.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID);

			}

		});

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
		   // set date picker as current date
		   return new DatePickerDialog(this, datePickerListenerBill, 
                      year, month,day);
		}
		return null;
	}
	
	
	private DatePickerDialog.OnDateSetListener datePickerListenerBill = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// set selected date into textview
			editTextDateBill.setText(new StringBuilder().append(year).append("-").append(month+1).append("-").append(day));
			Toast.makeText(getApplicationContext(), editTextDateBill.getText(), Toast.LENGTH_LONG).show();
			// set selected date into datepicker also
			date_picker.init(year, month, day, null);

		}
	};



                
        
	

}
