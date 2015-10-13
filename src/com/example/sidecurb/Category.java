package com.example.sidecurb;

public class Category {
	private String name;
    private String id;
    private String url;
 
    public Category ( String id, String name ,String url) {
        super();
        this.name 	= name;
        this.id 	= id;
        this.url	= url;
    }

	public CharSequence getId() {
		// TODO Auto-generated method stub
		return this.id;
	}

	public CharSequence getName() {
		// TODO Auto-generated method stub
		return this.name;
	}
	
	public String getUrl() {
		// TODO Auto-generated method stub
		return this.url;
	}
	
	public void setId(String id) {
		// TODO Auto-generated method stub
		this.id		= id;
	}

	public void setName(String name) {
		// TODO Auto-generated method stub
		this.name	= name;
	}
	
	public void setUrl(String url) {
		// TODO Auto-generated method stub
		this.url	= url;
	}
}
