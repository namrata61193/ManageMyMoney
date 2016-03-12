package com.example.csfinalproject;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.androidhive.androidsqlite.Expense;

public class DeleteAccountActivity extends Activity {
	
	LoginDataBaseAdapter loginDataBaseAdapters;

	String username;
	
	TextView userName ;
	
	private AlertDialog.Builder build;
	
	Button delete;
    
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.deleteaccount);
        
        android.app.ActionBar actionBar = getActionBar();
        actionBar.show();
        
        username = getIntent().getStringExtra("USERNAME");
        
		loginDataBaseAdapters=new LoginDataBaseAdapter(this);
        loginDataBaseAdapters=loginDataBaseAdapters.open();
        
        userName = (TextView) findViewById(R.id.username);
        delete = (Button)findViewById(R.id.deleteAccount);
        
        userName.setText(username);
        userName.setTextColor(Color.WHITE);
        
        //delete.getBackground().setColorFilter(Color.RED, null);
        
    	delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				build = new AlertDialog.Builder(DeleteAccountActivity.this);
				build.setTitle("Delete Your Account");
                build.setMessage("Do you want to delete your Account?");
                build.setPositiveButton("Yes",new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						loginDataBaseAdapters.deleteEntry(username);
						Intent logout = new Intent(getApplicationContext() , MainActivity.class);
			        	startActivity(logout);
						
					}
                
			});
                
                build.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                    }
                });
                	AlertDialog alert = build.create();
                	alert.show();

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
    
}