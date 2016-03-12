package com.androidhive.androidsqlite;

import java.util.*;

public class Expense {
	
	private int expenseID;
	private float amount;
	private String user;
	private String description;
	private Date expenseDate;
	private boolean isRecurring;
	
	public Expense()
	{
		
	}
	
	public Expense(int expenseID , float amount , String user , String description , Date expenseDate , boolean isRecurring)
	{
		this.expenseID = expenseID;
		this.amount = amount;
		this.user = user;
		this.description = description;
		this.expenseDate = expenseDate;
		this.isRecurring = isRecurring;
	}
	
	public int getExpenseID()
	{
		return this.expenseID;
	}
	
	public float getAmount()
	{
		return this.amount;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	public Date getDate()
	{
		return this.expenseDate;
	}
	
	public String getUser()
	{
		return this.user;
	}

	public boolean isRecurring()
	{
		return this.isRecurring;
	}
	
	public void setExpenseID(int expenseID)
	{
		this.expenseID = expenseID;
	}
	
	public void setAmount(float amount)
	{
		this.amount = amount;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public void setDate(Date date)
	{
		this.expenseDate = date;
	}
	
	public void setRecurring(boolean recurring)
	{
		this.isRecurring = recurring;
	}
	
	public void setUser(String username)
	{
		this.user = username;
	}
	
	
}
