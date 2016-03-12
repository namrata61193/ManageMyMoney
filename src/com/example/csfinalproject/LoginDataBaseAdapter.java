package com.example.csfinalproject;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


import com.androidhive.androidsqlite.*;

public class LoginDataBaseAdapter 
{
	 	static final String DATABASE_NAME = "login.db";
	 	static final int DATABASE_VERSION = 6;
		/**INFO FOR THE EXPENSES**/
	
		static final String TABLE_EXPENSES = "Expenses" ;
		private static final String KEY_ID = "expenseID";
		private static final String AMOUNT = "amount";
		private static final String USER = "user";
		private static final String DESCRIPTION = "description";
		private static final String DATE = "expenseDate";
		private static final String RECURRING = "isRecurring";
		
		static final String CREATE_TABLE_EXPENSES = "CREATE TABLE IF NOT EXISTS " + TABLE_EXPENSES + "( " + KEY_ID + " integer primary key autoincrement, "
		+ AMOUNT + " FLOAT," + USER + " TEXT," + DESCRIPTION + " TEXT," + DATE + " DATETIME," + RECURRING + " BOOLEAN)";
		 
		 
		static final String TABLE_BILLS = "Bills" ;
		private static final String BILL_KEY_ID = "billID";
		private static final String BILL_AMOUNT = "amount";
		private static final String BILL_USER = "user";
		private static final String BILL_DESCRIPTION = "description";
		private static final String BILL_DATE = "dueDate";
		private static final String BILL_RECURRING = "isRecurring";
		
		
		static final String CREATE_TABLE_BILLS = "CREATE TABLE IF NOT EXISTS " + TABLE_BILLS + "( " + BILL_KEY_ID + " integer primary key autoincrement, "
				+ BILL_AMOUNT + " FLOAT," + BILL_USER + " TEXT," + BILL_DESCRIPTION + " TEXT," + BILL_DATE + " DATETIME," + BILL_RECURRING + " BOOLEAN)";
		 
		 
		 
		
		/**INFO FOR THE LOGIN**/
       
        public static final int NAME_COLUMN = 1;
        // TODO: Create public field for each column in your table.
        // SQL Statement to create a new database.
        static final String DATABASE_CREATE = "create table if not exists "+"LOGIN"+
                                     "( " +"ID"+" integer primary key autoincrement,"+ "USERNAME  text,PASSWORD text); ";
        // Variable to hold the database instance
        public  SQLiteDatabase db;
       
 
        // Context of the application using the database.
        private final Context context;
        // Database open/upgrade helper
        private DataBaseHelper dbHelper;
        public  LoginDataBaseAdapter(Context _context) 
        {
            context = _context;
            dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public  LoginDataBaseAdapter open() throws SQLException 
        {
            db = dbHelper.getWritableDatabase();
            return this;
        }
        public void close() 
        {
            db.close();
        }
 
        public  SQLiteDatabase getDatabaseInstance()
        {
            return db;
        }
 
        public void insertEntry(String userName,String password)
        {
           ContentValues newValues = new ContentValues();
            // Assign values for each row.
            newValues.put("USERNAME", userName);
            newValues.put("PASSWORD",password);
            
            // Insert the row into your table
            db.insert("LOGIN", null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
        }
        public void deleteEntry(String UserName)
        {
            //String id=String.valueOf(ID);
        	List<Expense> expenseList = new ArrayList<Expense>();
        	expenseList = getExpenses(UserName);
        	for(int i = 0 ; i < expenseList.size() ; i++)
        	{
        		deleteExpense(expenseList.get(i));
        	}
        	List<Bill> billList = new ArrayList<Bill>();
        	billList = getBills(UserName);
        	for(int i = 0 ; i < billList.size() ; i++)
        	{
        		deleteBill(billList.get(i));
        	}
            String where="USERNAME=?";
            int numberOFEntriesDeleted= db.delete("LOGIN", where, new String[]{UserName}) ;
           // Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
            return ;
        }    
        public String getSingleEntry(String userName)
        {
            Cursor cursor=db.query("LOGIN", null, " USERNAME=?", new String[]{userName}, null, null, null);
            if(cursor.getCount()<1) // UserName Not Exist
            {
                cursor.close();
                return "NOT EXIST";
            }
            cursor.moveToFirst();
            String password= cursor.getString(cursor.getColumnIndex("PASSWORD"));
            cursor.close();
            return password;                
        }
        
     
        
        public boolean usernameExists(String username)
        {
        	 Cursor cursor=db.query("LOGIN", null, " USERNAME=?", new String[]{username}, null, null, null);
             if(cursor.getCount()>=1) // UserName Not Exist
             {
                 cursor.close();
                 return true;
             }
             
             return false;
        }
        
        public void  updateEntry(String userName,String password)
        {
            // Define the updated row content.
            ContentValues updatedValues = new ContentValues();
            // Assign values for each row.
            updatedValues.put("USERNAME", userName);
            updatedValues.put("PASSWORD",password);
            
 
            String where="USERNAME = ?";
            db.update("LOGIN",updatedValues, where, new String[]{userName});               
        }        
        
        /**********EXPENSES TABLE METHODS ***********************************/
        
        /**Create an expense **/
        public void createExpense(Expense expense)
        {
        	
        	ContentValues values = new ContentValues();
        	values.put(AMOUNT, expense.getAmount());
        	values.put(USER , expense.getUser());
        	values.put(DESCRIPTION , expense.getDescription());
        	values.put(DATE, expense.getDate().toString());
        	values.put(RECURRING, expense.isRecurring());
        	
        	db.insert(TABLE_EXPENSES, null, values);
        }
        
        /**GET all EXPENSES **/
        public List<Expense> getExpenses(String user)
        {
        	String selectQuery = "SELECT * FROM " + TABLE_EXPENSES;
        	List<Expense> expenseList = new ArrayList<Expense>();
        	
        	Cursor cursor = db.rawQuery(selectQuery, null);
        	
        	
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                	if(cursor.getString(2).equals(user))
                	{
                		Expense expense = new Expense();
                		expense.setExpenseID(Integer.parseInt(cursor.getString(0)));
                		expense.setAmount(Float.parseFloat(cursor.getString(1)));
                		expense.setUser(user);
                		expense.setDescription(cursor.getString(3));
                		Date newDate = Date.valueOf(cursor.getString(4));
                		expense.setDate(newDate);
                		expense.setRecurring(Boolean.parseBoolean(cursor.getString(5)));
                		expenseList.add(expense);
                	}
                }while(cursor.moveToNext());
                
            }
                
           return expenseList;
           
        }
        
        public float getTotalAmountSpent(String user)
        {
        	List<Expense> expenseList = new ArrayList<Expense>();
        	expenseList = getExpenses(user);
        	float total = 0;
        	for(int i = 0 ; i < expenseList.size() ; i++)
        	{
        		total = total + expenseList.get(i).getAmount();
        	}
        	
        	return total;
       
        }
            
       public int getExpensesCount() 
       {
                String countQuery = "SELECT  * FROM " + TABLE_EXPENSES;
                Cursor cursor = db.rawQuery(countQuery, null);
                cursor.close();
         
                // return count
                return cursor.getCount();
       }
       
       
       // Deleting single expense
       public void deleteExpense(Expense expense) 
       {
       
    	   db.delete(TABLE_EXPENSES, KEY_ID + " = ?",
           new String[] { String.valueOf(expense.getExpenseID()) });
    	   
       }
       
       
       // Updating single expense
       public int updateExpense(Expense expense) 
       {
    	   ContentValues values = new ContentValues();
    	   values.put(AMOUNT, expense.getAmount());
       	   values.put(USER , expense.getUser());
       	   values.put(DESCRIPTION , expense.getDescription());
       	   values.put(DATE, expense.getDate().toString());
       	   values.put(RECURRING, expense.isRecurring());
       	   
           // updating row
    	   return db.update(TABLE_EXPENSES, values, KEY_ID + " = ?",
           new String[] { String.valueOf(expense.getExpenseID()) });
       }
       
       
       
       /****BILLS TABLE METHODS ****/
       
       /**Create an expense **/
       public void createBill(Bill bill)
       {
       	
       	ContentValues values = new ContentValues();
       	values.put(BILL_AMOUNT, bill.getAmount());
       	values.put(BILL_USER , bill.getUser());
       	values.put(BILL_DESCRIPTION , bill.getDescription());
       	values.put(BILL_DATE, bill.getDate().toString());
       	values.put(BILL_RECURRING, bill.isRecurring());
       	
       	db.insert(TABLE_BILLS, null, values);
       }
       
       /**GET all EXPENSES **/
       public List<Bill> getBills(String user)
       {
       	String selectQuery = "SELECT * FROM " + TABLE_BILLS;
       	List<Bill> billList = new ArrayList<Bill>();
       	
       	Cursor cursor = db.rawQuery(selectQuery, null);
       	
       	
           // looping through all rows and adding to list
           if (cursor.moveToFirst()) {
               do {
            	   if(cursor.getString(2).equals(user))
               	{
            		   Bill bill = new Bill();
            		   bill.setBillID(Integer.parseInt(cursor.getString(0)));
            		   bill.setAmount(Float.parseFloat(cursor.getString(1)));
            		   bill.setUser(cursor.getString(2));
            		   bill.setDescription(cursor.getString(3));
            		   Date newDate = Date.valueOf(cursor.getString(4));
            		   bill.setDate(newDate);
            		   bill.setRecurring(Boolean.parseBoolean(cursor.getString(5)));
            		   billList.add(bill);
               	}
               }while(cursor.moveToNext());
               
           }
               
          return billList;
          
       }
           
      public int getBillsCount() 
      {
               String countQuery = "SELECT  * FROM " + TABLE_BILLS;
               Cursor cursor = db.rawQuery(countQuery, null);
               cursor.close();
        
               return cursor.getCount();
      }
      
      
      public float getTotalAmountOwed(String user)
      {
      	List<Bill> billList = new ArrayList<Bill>();
      	billList = getBills(user);
      	float total = 0;
      	for(int i = 0 ; i < billList.size() ; i++)
      	{
      		total = total + billList.get(i).getAmount();
      	}
      	
      	return total;
     
      }
      
      
      // Deleting single bill
      public void deleteBill(Bill bill) 
      {
      
   	   		db.delete(TABLE_BILLS, BILL_KEY_ID + " = ?",
   	   		new String[] { String.valueOf(bill.getBillID()) });
   	   		
      }
      
      
      // Updating single bill
      public int updateBill(Bill bill) 
      {
   	   	   ContentValues values = new ContentValues();
   	       values.put(BILL_AMOUNT, bill.getAmount());
      	   values.put(BILL_USER , bill.getUser());
      	   values.put(BILL_DESCRIPTION , bill.getDescription());
      	   values.put(BILL_DATE, bill.getDate().toString());
      	   values.put(BILL_RECURRING, bill.isRecurring());
      	   
          // updating row
   	      return db.update(TABLE_BILLS, values, BILL_KEY_ID + " = ?",
          new String[] { String.valueOf(bill.getBillID()) });
      }
      
      @SuppressWarnings("static-access")
	public String getDate()
      {
    	  return this.DATE;
      }
      
      @SuppressWarnings("static-access")
	public String getBillDate()
      {
    	  return this.BILL_DATE;
      }
}
