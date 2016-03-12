package com.example.csfinalproject;

import java.util.List;

import com.androidhive.androidsqlite.Expense;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

public class ActivityOverview extends Activity {
	
	LoginDataBaseAdapter loginDataBaseAdapter;
	List<Expense> allExpenses ;
	
	String username;
    
    float totalExpenseAmount = 0;
    float totalBillAmount = 0;

    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activityoverview);
        
        android.app.ActionBar actionBar = getActionBar();
        actionBar.show();
        
        username = getIntent().getStringExtra("USERNAME");
		
		loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();
        
        totalExpenseAmount = loginDataBaseAdapter.getTotalAmountSpent(username);
        totalBillAmount = loginDataBaseAdapter.getTotalAmountOwed(username);
      
        TextView totalExpense = (TextView)findViewById(R.id.totalexpenseamount);
        totalExpense.setText("Total money spent = " + String.valueOf(totalExpenseAmount));
        
        TextView totalBill = (TextView)findViewById(R.id.totalbillamount);
        totalBill.setText("Total money owed = " + String.valueOf(totalBillAmount));
        
        Button viewExpenses = (Button)findViewById(R.id.viewExpenseDetais);
        viewExpenses.setOnClickListener(new View.OnClickListener() {
        	 
            public void onClick(View v) {
            	
            	Intent expensePage=new Intent(getApplicationContext(),ViewExpenseActivity.class);
            	expensePage.putExtra("USERNAME", username);
            	expensePage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(expensePage);
            	
            }
        });
        
        Button viewCalendar = (Button)findViewById(R.id.viewCalendarDetails);
        viewCalendar.setOnClickListener(new View.OnClickListener() {
        	
        	public void onClick(View v)
        	{
        		Intent calendar = new Intent(getApplicationContext() , CalendarViewExampleActivity.class);
        		calendar.putExtra("USERNAME" , username);
        		calendar.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        		startActivity(calendar);
        	}
        });
        
        
        
        Button viewBills = (Button)findViewById(R.id.viewBillDetais);
        viewBills.setOnClickListener(new View.OnClickListener() {
        	 
            public void onClick(View v) {
            	
            	Intent billPage=new Intent(getApplicationContext(),ViewBillActivity.class);
            	billPage.putExtra("USERNAME", username);
            	billPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(billPage);
            	
            }
        });
        
           
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
    
        
}
