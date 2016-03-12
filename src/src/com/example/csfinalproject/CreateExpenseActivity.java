package com.example.csfinalproject;

import com.androidhive.androidsqlite.Expense;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateExpenseActivity extends Activity {
	
	EditText editTextAmount , editTextDescription , editTextRecurring ;


	TextView editTextDate;
	
	
	LoginDataBaseAdapter loginDataBaseAdapters;
	List<Expense> allExpenses ;
	
	private DatePicker date_picker;
	private int year;
	private int month ;
	private int day;
	
	static final int DATE_DIALOG_ID = 0;
    
    ImageButton btnCreateExpense;
    
    Button btnChangeDate;
    
    String username;
    
    Boolean isUpdate;
    
    int updateID;
    
    Spinner spinnerDescription ;
	
	@SuppressLint("NewApi")
	@SuppressWarnings("unchecked")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.createnewexpense);
		
		android.app.ActionBar actionBar = getActionBar();
        actionBar.show();
		
		username = getIntent().getStringExtra("USERNAME");
		isUpdate = getIntent().getBooleanExtra("UPDATE", false);
		
		loginDataBaseAdapters=new LoginDataBaseAdapter(this);
        loginDataBaseAdapters=loginDataBaseAdapters.open();
        
        setCurrentDateOnView();
		addListenerOnButton();
        
        editTextAmount=(EditText)findViewById(R.id.editTextAmount);
        
         //Date date = Date.valueOf(year+"-"+month+"-" + day);    
         editTextDate=(TextView)findViewById(R.id.editTextDate);
        //editTextRecurring = (EditText)findViewById(R.id.editTextRecurring);
        
        spinnerDescription = (Spinner)findViewById(R.id.SpinnerDescription);
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
        
       // final CheckBox checkRecurring = (CheckBox) findViewById(R.id.checkBoxRecurring);
        
        if(isUpdate)
        {
        	updateID = Integer.parseInt(getIntent().getStringExtra("ID"));
        	
        	
        	
        	editTextAmount.setText(getIntent().getStringExtra("AMOUNT"));
        	//editTextDescription.setText(getIntent().getStringExtra("DESCRIPTION"));
        	spinnerDescription.setSelection(0);
        	
        	
        	editTextDate.setText(getIntent().getStringExtra("DATE"));
        	//checkRecurring.setChecked(recurring);
        	
        }
        
        btnCreateExpense=(ImageButton)findViewById(R.id.buttonCreateNewExpense);
        
        btnCreateExpense.setOnClickListener(new View.OnClickListener() {
        	 
            public void onClick(View v) {
                // TODO Auto-generated method stub
     
                String amount=editTextAmount.getText().toString();
              //  String description=editTextDescription.getText().toString();
                String date=editTextDate.getText().toString();
                
                //String recurring=editTextRecurring.getText().toString();
                
                String category = String.valueOf(spinnerDescription.getSelectedItem());
                
               /* Calendar cal = Calendar.getInstance();
				java.util.Date utilDate = cal.getTime();
				java.sql.Date todayDate = new java.sql.Date(utilDate.getTime());*/
				
                if(amount.equals("")||date.equals(""))
                {
                        Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
                        return;
                }
                
                float getAmount =  Float.parseFloat(amount);
                if(getAmount < 0)
                {
                		Toast.makeText(getApplicationContext(), "Amount cannot be negative", Toast.LENGTH_LONG).show();
                		return;
                }
                
               
                Date expenseDate = Date.valueOf(date);
                float expenseAmount = Float.parseFloat(amount);
                
                if(!isUpdate)
                {
                	Expense newExpense = new Expense(0 , expenseAmount , username , category , expenseDate , false);
                	loginDataBaseAdapters.createExpense(newExpense);
                }
                else
                {
                	Expense updateExpense = new Expense(updateID ,expenseAmount , username , category , expenseDate , false );
                	loginDataBaseAdapters.updateExpense(updateExpense);
                }
                
                Intent expensePage=new Intent(getApplicationContext(),ViewExpenseActivity.class);
            	expensePage.putExtra("USERNAME", username);
            	expensePage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(expensePage);
                
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
    
    
 // display current date
 	public void setCurrentDateOnView() {
  
 		editTextDate = (TextView) findViewById(R.id.editTextDate);
 		date_picker = (DatePicker) findViewById(R.id.dpResult);
  
 		final Calendar c = Calendar.getInstance();
 		year = c.get(Calendar.YEAR);
 		month = c.get(Calendar.MONTH);
 		day = c.get(Calendar.DAY_OF_MONTH);
  
 		editTextDate.setText(new StringBuilder().append(year).append("-").append(month+1).append("-").append(day));
  
 		// set current date into datepicker
 		date_picker.init(year, month, day, null);
  
 	}
  
 	public void addListenerOnButton() {
  
 		btnChangeDate = (Button) findViewById(R.id.btnChangeDate);
  
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
 		   return new DatePickerDialog(this, datePickerListener, 
                          year, month,day);
 		}
 		return null;
 	}
 	
 	
 	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

 		// when dialog box is closed, below method will be called.
 		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
 			year = selectedYear;
 			month = selectedMonth;
 			day = selectedDay;

 			// set selected date into textview
 			editTextDate.setText(new StringBuilder().append(year).append("-").append(month+1).append("-").append(day));
 			Toast.makeText(getApplicationContext(), editTextDate.getText(), Toast.LENGTH_LONG).show();
 			// set selected date into datepicker also
 			date_picker.init(year, month, day, null);

 		}
 	};
    
	
	

}
