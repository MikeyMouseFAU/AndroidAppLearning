package learing01.android.mh.learningandroidapps.Model;

import android.support.annotation.NonNull;

public class User {

    private String Name;
    private String Phone;
    private String Email;

    public User() {
    }

    public User(String name, String phone, String email) {
        Name = name;
        Phone = phone;
        Email = email;
    }


    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @NonNull
    public String getName() {
         return Name;    //don not use this.   pointer
    }

    public void setName(String name) {
        Name = name;        //don not use this.   pointer
    }
    public String getPhone() {
        return Phone;    //don not use this.   pointer
    }

    public void setPhone(String phone) {
        Phone = phone;    //don not use this.   pointer
    }
}
