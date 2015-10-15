package com.example.sidecurb;

import android.graphics.Bitmap;

public class Shop {
	private String name;
    private String id;
    private String logo;
    private Bitmap image;
    private String distance;
    private String address; 
    private String shop;
 
    public Shop ( String id, String name,String logo, Bitmap image, String distance, String address, String shop) {
        super();
        this.name 	= name;
        this.id 	= id;
        this.logo	= logo;
        this.image  = image;	
		this.distance = distance;
        this.address  = address;
        this.shop	= shop;
    }

	public CharSequence getId() {
		// TODO Auto-generated method stub
		return this.id;
	}

	public CharSequence getName() {
		// TODO Auto-generated method stub
		return this.name;
	}
	
	public String getLogo() {
		// TODO Auto-generated method stub
		return this.logo;
	}
	
	public Bitmap getImage(){
		return this.image;
	}
	
	public String getDistance() {
		// TODO Auto-generated method stub
		return this.distance;
	}
	
	public String getAddress() {
		// TODO Auto-generated method stub
		return this.address;
	}
	
	public String getShop() {
		// TODO Auto-generated method stub
		return this.shop;
	}
	
	public void setId(String id) {
		// TODO Auto-generated method stub
		this.id		= id;
	}

	public void setName(String name) {
		// TODO Auto-generated method stub
		this.name	= name;
	}
	
	public void setLogo(String logo) {
		// TODO Auto-generated method stub
		this.logo	= logo;
	}
	
	public void setImage(Bitmap image){
		this.image	= image;
	}
	
	public void setDistance(String distance) {
		// TODO Auto-generated method stub
		this.distance	= distance;
	}
	
	public void setAddress(String address) {
		// TODO Auto-generated method stub
		this.address	= address;
	}
	
	public void setShop(String shop) {
		// TODO Auto-generated method stub
		this.shop	= shop;
	}
}
