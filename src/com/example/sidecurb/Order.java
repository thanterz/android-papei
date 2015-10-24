package com.example.sidecurb;


public class Order {
	private String purchase_no;
    private String dateString;
    private String latString;
    private String lonString;
 
    public Order ( String purchase_no, String dateString, String latString, String lonString) {
        super();
        this.purchase_no 	= purchase_no;
        this.dateString 	= dateString;
        this.latString		= latString;
        this.lonString		= lonString;
    }
    
    public CharSequence getPurchase_no() {
		// TODO Auto-generated method stub
		return this.purchase_no;
	}

	public CharSequence getDateString() {
		// TODO Auto-generated method stub
		return this.dateString;
	}
	
	public CharSequence getLatString() {
		// TODO Auto-generated method stub
		return this.latString;
	}
	
	public CharSequence getLonString() {
		// TODO Auto-generated method stub
		return this.lonString;
	}
	
	public void setPurchase_no(String purchase_no) {
		// TODO Auto-generated method stub
		this.purchase_no = purchase_no;
	}

	public void setDateString(String dateString) {
		// TODO Auto-generated method stub
		this.dateString	= dateString;
	}
	
	public void setLatString(String latString) {
		// TODO Auto-generated method stub
		this.latString	= latString;
	}
	
	public void setLonString(String lonString) {
		// TODO Auto-generated method stub
		this.lonString	= lonString;
	}

}
