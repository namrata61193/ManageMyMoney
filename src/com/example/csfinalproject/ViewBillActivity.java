package com.example.csfinalproject;

import com.androidhive.androidsqlite.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

public class ViewBillActivity extends Activity{
	
	LoginDataBaseAdapter loginDataBaseAdapter;
	List<Bill> allBills = new ArrayList<Bill>();

    ImageButton btnCreate;
    
    EditText billSearch;
    
    private ListView billList;
    
    private ArrayList<String> billId = new ArrayList<String>();
    private ArrayList<String> billAmount = new ArrayList<String>();
    private ArrayList<String> billDescription = new ArrayList<String>();
    private ArrayList<String> billDate= new ArrayList<String>();
    private ArrayList<String> billRecurring = new ArrayList<String>();
    
    private AlertDialog.Builder build;
    
    String username;
    
    float totalAmount = 0;
	
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.viewallbills);
		
		android.app.ActionBar actionBar = getActionBar();
        actionBar.show();
		
		billList = (ListView) findViewById(R.id.listBills);
		
		username = getIntent().getStringExtra("USERNAME");
		
		loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();
        
        btnCreate = (ImageButton)findViewById(R.id.buttonCreateBill);
        
        billSearch = (EditText) findViewById(R.id.inputSearchBill);
        
        allBills = loginDataBaseAdapter.getBills(username);
        
        displayData();
        
        billSearch.addTextChangedListener(new TextWatcher() {
	
            @SuppressLint("DefaultLocale")
			@Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                // ViewExpenseActivity.this.disadpt).getFilter().filter(cs);   
            	
            	int textLength = cs.length();
            	if(textLength == 0)
            	{
            		displayData();
            	}
            	ArrayList<String> tempBillId = new ArrayList<String>();
                ArrayList<String> tempBillAmount = new ArrayList<String>();
                ArrayList<String> tempBillDescription = new ArrayList<String>();
                ArrayList<String> tempBillDate= new ArrayList<String>();
                ArrayList<String> tempBillRecurring = new ArrayList<String>();
                
            	for(int i = 0 ; i < allBills.size() ; i++)
            	{
            		if(textLength <= allBills.get(i).getDescription().length())
            		{
            			if (allBills.get(i).getDescription().toLowerCase().contains(cs.toString().toLowerCase())) {
                            tempBillId.add(String.valueOf(allBills.get(i).getBillID()));
                            tempBillAmount.add(String.valueOf(allBills.get(i).getAmount()));
                            tempBillDescription.add(String.valueOf(allBills.get(i).getDescription()));
                            tempBillDate.add(String.valueOf(allBills.get(i).getDate()));
                            tempBillRecurring.add(String.valueOf(allBills.get(i).isRecurring()));
                         }
            		}
            	}
            	
            	DisplayBillAdapter searchBillDisplay = new DisplayBillAdapter(ViewBillActivity.this,tempBillId , tempBillAmount , tempBillDescription , tempBillDate , tempBillRecurring);
                ((ListView) billList).setAdapter(searchBillDisplay);
            	
            }
            
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
                // TODO Auto-generated method stub
                 
            }
             
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
        });
        
        
        
        
        
        btnCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
     
            	Intent createBill=new Intent(getApplicationContext(),CreateBillActivity.class);
            	createBill.putExtra("USERNAME", username);
            	createBill.putExtra("UPDATE" , false);
            	createBill.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(createBill);
                }
            });
        
        
       	billList.setOnItemClickListener(new OnItemClickListener() {
		@Override
			public void onItemClick(AdapterView<?> parent, View view, final int arg2, long arg3) {
				
				build = new AlertDialog.Builder(ViewBillActivity.this);
				build.setTitle(billDescription.get(arg2) + "-" + billDate.get(arg2));
				build.setMessage("What would you like to do ?");
				build.setPositiveButton("Update the Bill" , new DialogInterface.OnClickListener(){
					
					@SuppressLint("ShowToast")
					public void onClick(DialogInterface dialog, int which) {
						
						// TODO Auto-generated method stub
						Intent updateBill = new Intent(getApplicationContext(), CreateBillActivity.class);
						updateBill.putExtra("AMOUNT", billAmount.get(arg2));
						updateBill.putExtra("DESCRIPTION", billDescription.get(arg2));
						updateBill.putExtra("ID", billId.get(arg2));
						updateBill.putExtra("RECURRING", billRecurring.get(arg2));
						updateBill.putExtra("DATE", billDate.get(arg2));
						updateBill.putExtra("USERNAME", username);
						updateBill.putExtra("UPDATE" , true);
		            	startActivity(updateBill);
						}
			
       	});
				
				build.setNegativeButton("Bill Has been payed" , new DialogInterface.OnClickListener(){
					
					@SuppressLint({ "ShowToast", "SimpleDateFormat" })
					public void onClick(DialogInterface dialog, int which){
						
						float amount = Float.parseFloat(billAmount.get(arg2));
						String description = billDescription.get(arg2);
						java.util.Date date = Calendar.getInstance().getTime();
						Date today = Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(date));
						Expense newExpense = new Expense(0 , amount , username , description , today , false);
						loginDataBaseAdapter.createExpense(newExpense);
						
						boolean recurring = Boolean.parseBoolean(billRecurring.get(arg2));
						int billID = Integer.parseInt(billId.get(arg2));
						
						Bill deleteBill = new Bill(billID , amount , username , description , date , recurring);
						loginDataBaseAdapter.deleteBill(deleteBill);
						
						Intent expensePage=new Intent(getApplicationContext(),ViewExpenseActivity.class);
		            	expensePage.putExtra("USERNAME", username);
		            	expensePage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		                startActivity(expensePage);
					}
				});
				
				AlertDialog alert = build.create();
                alert.show();

                return ;
				
		}
		
       	});
       	
       	
       	
       	billList.setOnItemLongClickListener(new OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {

                build = new AlertDialog.Builder(ViewBillActivity.this);
                build.setTitle("Delete " + billDescription.get(arg2) + "-" + billDate.get(arg2));
                build.setMessage("Do you want to delete ?");
                build.setPositiveButton("Yes",new DialogInterface.OnClickListener() {

                            @SuppressLint("ShowToast")
							public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText( getApplicationContext(),
                                        billDescription.get(arg2) + "-"
                                                + billDate.get(arg2)
                                                + " is deleted.", 3000).show();

                                Bill deleteBill = new Bill();
                                deleteBill.setBillID(Integer.parseInt(billId.get(arg2)));
                                deleteBill.setAmount(Float.parseFloat(billAmount.get(arg2)));
                                deleteBill.setDescription(billDescription.get(arg2));
                                deleteBill.setRecurring(Boolean.parseBoolean(billRecurring.get(arg2)));
                                deleteBill.setUser(username);
                                deleteBill.setDate(Date.valueOf(billDate.get(arg2)));
                                loginDataBaseAdapter.deleteBill(deleteBill);
                                displayData();
                                dialog.cancel();
                            }
                        });

                build.setNegativeButton("No", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                            }
                        });
                AlertDialog alert = build.create();
                alert.show();

                return true;
            }
        });
       
	
	}
	
	@Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        loginDataBaseAdapter.close();
    }
	
	
	/**
     * displays data from SQLite
     */
    @SuppressWarnings("static-access")
	private void displayData() {
        
    	String selectQuery = "SELECT * FROM " + loginDataBaseAdapter.TABLE_BILLS + " ORDER BY " + loginDataBaseAdapter.getBillDate() ;
    
    	Cursor cursor = loginDataBaseAdapter.db.rawQuery(selectQuery, null);
    	
    	billId.clear();
    	billAmount.clear();
    	billDescription.clear();
    	billDate.clear();
    	billRecurring.clear();
    	
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            		if(cursor.getString(2).equals(username))
            		{
            			billId.add(cursor.getString(0));
            			billAmount.add(cursor.getString(1));
            			billDescription.add(cursor.getString(3));
            			billDate.add(cursor.getString(4));
            			billRecurring.add(cursor.getString(5));
            			
            		}
            }while (cursor.moveToNext());
        }
        DisplayBillAdapter disadpt = new DisplayBillAdapter(ViewBillActivity.this,billId , billAmount , billDescription , billDate , billRecurring);
        ((ListView) billList).setAdapter(disadpt);
       cursor.close();
    	
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

