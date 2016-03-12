package com.androidhive.androidsqlite;

import java.util.Date;

public class Bill {
	
	private int billID;
	private float amount;
	private String user;
	private String description;
	private Date dueDate;
	private boolean isRecurring;
	
	public Bill()
	{
		
	}
	
	public Bill(int billID , float amount , String user , String description , Date dueDate , boolean isRecurring)
	{
		this.billID = billID;
		this.amount = amount;
		this.user = user;
		this.description = description;
		this.dueDate = dueDate;
		this.isRecurring = isRecurring;
	}
	
	public int getBillID()
	{
		return this.billID;
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
		return this.dueDate;
	}
	
	public String getUser()
	{
		return this.user;
	}

	public boolean isRecurring()
	{
		return this.isRecurring;
	}
	
	public void setBillID(int billID)
	{
		this.billID = billID;
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
		this.dueDate = date;
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
