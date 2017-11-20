package com.creativeminds.omkar.bookshelf.models;


public class User {

    private String u_id;
    private String u_name;
    private String u_pass;
    private String u_email;
    private String u_phno;
    private String u_city;
    private String u_state;

    public User(){

    }

    public User (String _u_id,String _u_name,String _u_pass,String _u_email,String _u_phno,String _u_city,String _u_state){
        this.u_id = _u_id;
        this.u_name = _u_name;
        this.u_pass = _u_pass;
        this.u_email = _u_email;
        this.u_phno = _u_phno;
        this.u_city = _u_city;
        this.u_state = _u_state;
    }

    public void setId(String _id){
        this.u_id = _id;
    }

    public String getId(){
        return this.u_id;
    }

    public void setName(String _name){
        this.u_name = _name;
    }

    public String getName(){
        return this.u_name;
    }

    public void setPass(String _pass){
        this.u_pass = _pass;
    }

    public String getPassword(){
        return this.u_pass;
    }

    public void setEmail(String _email){
        this.u_email = _email;
    }

    public String getEmail(){
        return this.u_email;
    }

    public void setPhoneNo(String _phno){
        this.u_phno = _phno;
    }

    public String getPhoneNo(){
        return this.u_phno;
    }

    public void setCity(String _city){
        this.u_city = _city;
    }

    public String getCity(){
        return this.u_city;
    }

    public void setState(String _state){
        this.u_state = _state;
    }

    public String getState(){
        return this.u_state;
    }
}
