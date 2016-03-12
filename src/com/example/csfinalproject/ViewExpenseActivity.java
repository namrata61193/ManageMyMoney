package com.example.csfinalproject;

import com.androidhive.androidsqlite.Expense;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
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



@SuppressLint("DefaultLocale")
public class ViewExpenseActivity extends Activity{

	
	LoginDataBaseAdapter loginDataBaseAdapter;
	List<Expense> allExpenses = new ArrayList<Expense> ();

    ImageButton btnCreate;
    
    EditText expenseSearch;
    
    private ListView expenseList;
    
    DisplayAdapter disadpt;
    
    private ArrayList<String> expenseId = new ArrayList<String>();
    private ArrayList<String> expenseAmount = new ArrayList<String>();
    private ArrayList<String> expenseDescription = new ArrayList<String>();
    private ArrayList<String> expenseDate= new ArrayList<String>();
    private ArrayList<String> expenseRecurring = new ArrayList<String>();
    
    private AlertDialog.Builder build;
    
    String username;
    
    float totalAmount = 0;
	
	@SuppressLint({ "NewApi", "DefaultLocale" })
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.viewallexpenses);
		
		android.app.ActionBar actionBar = getActionBar();
        actionBar.show();
		
		expenseList = (ListView) findViewById(R.id.listExpenses);
		
		username = getIntent().getStringExtra("USERNAME");
		
		loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();
        
        allExpenses = loginDataBaseAdapter.getExpenses(username);
        
        btnCreate = (ImageButton)findViewById(R.id.buttonCreateExpense);
        
        expenseSearch = (EditText) findViewById(R.id.inputSearchExpense);
        
        displayData();
        
        expenseSearch.addTextChangedListener(new TextWatcher() {
            
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
            	ArrayList<String> tempExpenseId = new ArrayList<String>();
                ArrayList<String> tempExpenseAmount = new ArrayList<String>();
                ArrayList<String> tempExpenseDescription = new ArrayList<String>();
                ArrayList<String> tempExpenseDate= new ArrayList<String>();
                ArrayList<String> tempExpenseRecurring = new ArrayList<String>();
                
            	for(int i = 0 ; i < allExpenses.size() ; i++)
            	{
            		if(textLength <= allExpenses.get(i).getDescription().length())
            		{
            			if (allExpenses.get(i).getDescription().toLowerCase().contains(cs.toString().toLowerCase())) {
                            tempExpenseId.add(String.valueOf(allExpenses.get(i).getExpenseID()));
                            tempExpenseAmount.add(String.valueOf(allExpenses.get(i).getAmount()));
                            tempExpenseDescription.add(String.valueOf(allExpenses.get(i).getDescription()));
                            tempExpenseDate.add(String.valueOf(allExpenses.get(i).getDate()));
                            tempExpenseRecurring.add(String.valueOf(allExpenses.get(i).isRecurring()));
                         }
            		}
            	}
            	
            	DisplayAdapter searchDisplay = new DisplayAdapter(ViewExpenseActivity.this,tempExpenseId , tempExpenseAmount , tempExpenseDescription , tempExpenseDate , tempExpenseRecurring);
                ((ListView) expenseList).setAdapter(searchDisplay);
            	
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
     
            	Intent createExpense=new Intent(getApplicationContext(),CreateExpenseActivity.class);
            	createExpense.putExtra("USERNAME", username);
            	createExpense.putExtra("UPDATE" , false);
            	createExpense.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(createExpense);
                }
            });
        
        

   
       	expenseList.setOnItemClickListener(new OnItemClickListener() {
		@Override
			public void onItemClick(AdapterView<?> parent, View view, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
				Intent updateExpense = new Intent(getApplicationContext(), CreateExpenseActivity.class);
				updateExpense.putExtra("AMOUNT", expenseAmount.get(arg2));
				updateExpense.putExtra("DESCRIPTION", expenseDescription.get(arg2));
				updateExpense.putExtra("ID", expenseId.get(arg2));
				updateExpense.putExtra("RECURRING", expenseRecurring.get(arg2));
				updateExpense.putExtra("DATE", expenseDate.get(arg2));
				updateExpense.putExtra("USERNAME", username);
				updateExpense.putExtra("UPDATE" , true);
            	startActivity(updateExpense);
			}
       	});
       	
       	
       	expenseList.setOnItemLongClickListener(new OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {

                build = new AlertDialog.Builder(ViewExpenseActivity.this);
                build.setTitle("Delete " + expenseDescription.get(arg2) + "-" + expenseDate.get(arg2));
                build.setMessage("Do you want to delete ?");
                build.setPositiveButton("Yes",new DialogInterface.OnClickListener() {

                            @SuppressLint("ShowToast")
							public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText( getApplicationContext(),
                                        expenseDescription.get(arg2) + "-"
                                                + expenseDate.get(arg2)
                                                + " is deleted.", 3000).show();

                                Expense deleteExpense = new Expense();
                                deleteExpense.setExpenseID(Integer.parseInt(expenseId.get(arg2)));
                                deleteExpense.setAmount(Float.parseFloat(expenseAmount.get(arg2)));
                                deleteExpense.setDescription(expenseDescription.get(arg2));
                                deleteExpense.setRecurring(Boolean.parseBoolean(expenseRecurring.get(arg2)));
                                deleteExpense.setUser(username);
                                deleteExpense.setDate(Date.valueOf(expenseDate.get(arg2)));
                                loginDataBaseAdapter.deleteExpense(deleteExpense);
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
        
    	String selectQuery = "SELECT * FROM " + loginDataBaseAdapter.TABLE_EXPENSES + " ORDER BY " + loginDataBaseAdapter.getDate() + " DESC";
    
    	Cursor cursor = loginDataBaseAdapter.db.rawQuery(selectQuery, null);
    	
    	expenseId.clear();
    	expenseAmount.clear();
    	expenseDescription.clear();
    	expenseDate.clear();
    	expenseRecurring.clear();
    	
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            		if(cursor.getString(2).equals(username))
            		{
            			expenseId.add(cursor.getString(0));
            			expenseAmount.add(cursor.getString(1));
            			expenseDescription.add(cursor.getString(3));
            			expenseDate.add(cursor.getString(4));
            			expenseRecurring.add(cursor.getString(5));
            			
            		}
            }while (cursor.moveToNext());
        }
       disadpt = new DisplayAdapter(ViewExpenseActivity.this,expenseId , expenseAmount , expenseDescription , expenseDate , expenseRecurring);
        ((ListView) expenseList).setAdapter(disadpt);
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
