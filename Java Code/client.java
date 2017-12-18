package com.example.maupi.parkking;

/**
 * Created by ahmedsameh19997 on 10/10/2017.
 */

public class client {

    int id;
    String uname , pass , email;

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setUname(String uname){this.uname = uname;}

    public String getUname(){
        return uname;
    }

    public void setPass(String pass){
        this.pass = pass;
    }

    public String getPass(){
        return this.pass;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return this.email;
    }
}

