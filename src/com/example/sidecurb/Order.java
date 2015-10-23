package com.example.sidecurb;


public class Order {
	private String purchase_no;
    private String dateString;
 
    public Order ( String purchase_no, String dateString) {
        super();
        this.purchase_no 	= purchase_no;
        this.dateString 	= dateString;
    }
    
    public CharSequence getPurchase_no() {
		// TODO Auto-generated method stub
		return this.purchase_no;
	}

	public CharSequence getDateString() {
		// TODO Auto-generated method stub
		return this.dateString;
	}
	
	public void setPurchase_no(String purchase_no) {
		// TODO Auto-generated method stub
		this.purchase_no = purchase_no;
	}

	public void setDateString(String dateString) {
		// TODO Auto-generated method stub
		this.dateString	= dateString;
	}

}
