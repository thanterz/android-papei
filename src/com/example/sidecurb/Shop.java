package com.example.sidecurb;

import android.graphics.Bitmap;

public class Shop {
	private String name;
    private String id;
    private String logo;
    private Bitmap image;
 
    public Shop (String name, String id, String logo, Bitmap image) {
        super();
        this.name 	= name;
        this.id 	= id;
        this.logo	= logo;
        this.image  = image;
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
}
