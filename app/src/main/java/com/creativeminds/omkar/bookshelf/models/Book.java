package com.creativeminds.omkar.bookshelf.models;

/**
 * Created by Omkar on 9/23/2017.
 */

public class Book {
    private String u_id;
    private String b_id;
    private String b_name;
    private String b_author;
    private String b_edition;
    private String b_isbn;
    private String b_category;
    private String b_mrp;
    private String b_sp;
    private String b_condition;
    private String b_desc;
    private String b_date_created;
    private String u_city;
    private String image_url;

    public Book(String _u_id,String _b_id,String _b_name,String _b_author,String _b_edition,String _b_isbn,String _b_category,String _b_mrp,String _b_sp,String _b_condition,String _b_desc,String _b_date_created,String _u_city,String _image_url){
        this.u_id = _u_id;
        this.b_id = _b_id;
        this.b_name = _b_name;
        this.b_author = _b_author;
        this.b_edition = _b_edition;
        this.b_isbn = _b_isbn;
        this.b_category = _b_category;
        this.b_mrp = _b_mrp;
        this.b_sp = _b_sp;
        this.b_condition = _b_condition;
        this.b_desc = _b_desc;
        this.b_date_created = _b_date_created;
        this.u_city = _u_city;
        this.image_url = _image_url;
    }

    public void setUID(String _u_id){
        this.u_id = _u_id;
    }

    public String getUID(){
        return this.u_id;
    }

    public void setBID(String _b_id){
        this.b_id = _b_id;
    }

    public String getBID(){
        return this.b_id;
    }

    public void setName(String _name){
        this.b_name = _name;
    }

    public String getName(){
        return this.b_name;
    }

    public void setAuthor(String _author){
        this.b_author = _author;
    }

    public String getAuthor(){
        return this.b_author;
    }

    public void setEdition(String _edition){
        this.b_edition = _edition;
    }

    public String getEdition(){
        return this.b_edition;
    }

    public void setIsbn(String _isbn){
        this.b_isbn = _isbn;
    }

    public String getIsbn(){
        return this.b_isbn;
    }

    public void setCategory(String _cat){
        this.b_category = _cat;
    }

    public String getCategory(){
        return this.b_category;
    }

    public void setMRP(String _mrp){
        this.b_mrp = _mrp;
    }

    public String getMRP(){
        return this.b_mrp;
    }

    public void setSellingPrice(String _sp){
        this.b_sp = _sp;
    }

    public String getSellingPrice(){
        return this.b_sp;
    }

    public void setCondition(String _cond){
        this.b_condition = _cond;
    }

    public String getCondition(){
        return this.b_condition;
    }

    public void setDescription(String _desc){
        this.b_desc = _desc;
    }

    public String getDescription(){
        return this.b_desc;
    }

    public void setCreatedDate(String _date){
        this.b_date_created = _date;
    }

    public String getCreatedDate(){
        return this.b_date_created;
    }

    public void setCity(String _city){
        this.u_city = _city;
    }

    public String getCity(){
        return this.u_city;
    }

    public void setImageUrl(String _url){
        this.image_url = _url;
    }

    public String getImageUrl(){
        return this.image_url;
    }
}
