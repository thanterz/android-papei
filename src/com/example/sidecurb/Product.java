package com.example.sidecurb;

public class Product {
	private String id;
    private String sku;
    private String name;
	private String url;
    private String price;
    private String description;
	private String photo;
    private String categ;
    private String sid;
    private String qnt;
 
    public Product ( String id, String sku ,String name, String url, String price ,String description, String photo, String categ, String sid, String qnt) {
        super();
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.url = url;
        this.price = price;
        this.description = description;
        this.photo = photo;
        this.categ = categ;
        this.sid = sid;
        this.qnt = qnt;
    }

	public CharSequence getId() {
		// TODO Auto-generated method stub
		return this.id;
	}
	
	public CharSequence getSku() {
		// TODO Auto-generated method stub
		return this.sku;
	}
	
	public CharSequence getName() {
		// TODO Auto-generated method stub
		return this.name;
	}
	
	public String getUrl() {
		// TODO Auto-generated method stub
		return this.url;
	}
	
	public String getPrice() {
		// TODO Auto-generated method stub
		return this.price;
	}
	
	public String getDescription() {
		// TODO Auto-generated method stub
		return this.description;
	}
	
	public String getPhoto() {
		// TODO Auto-generated method stub
		return this.photo;
	}
	
	public String getCateg() {
		// TODO Auto-generated method stub
		return this.categ;
	}
	
	public String getSid() {
		// TODO Auto-generated method stub
		return this.sid;
	}
	
	public String getQnt() {
		// TODO Auto-generated method stub
		return this.qnt;
	}
	
	public void setId(String id) {
		// TODO Auto-generated method stub
		this.id		= id;
	}

	public void setSku(String sku) {
		// TODO Auto-generated method stub
		this.sku	= sku;
	}
	
	public void setUrl(String url) {
		// TODO Auto-generated method stub
		this.url	= url;
	}

	public void setName(String name) {
		// TODO Auto-generated method stub
		this.name	= name;
	}
	
	public void setPrice(String price) {
		// TODO Auto-generated method stub
		this.price	= price;
	}
	
	public void setDescription(String description) {
		// TODO Auto-generated method stub
		this.description		= description;
	}

	public void setPhoto(String photo) {
		// TODO Auto-generated method stub
		this.photo	= photo;
	}

	public void setCateg(String categ) {
		// TODO Auto-generated method stub
		this.categ	= categ;
	}
	
	public void setSid(String sid) {
		// TODO Auto-generated method stub
		this.sid	= sid;
	}
	
	public void setQnt(String qnt) {
		// TODO Auto-generated method stub
		this.qnt	= qnt;
	}
}
