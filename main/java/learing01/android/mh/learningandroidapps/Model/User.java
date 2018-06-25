package learing01.android.mh.learningandroidapps.Model;

public class User {

    private String Name;
    private String Password;

    public User() {
    }

    public User(String name, String password) {
        Name = name;
        Password = password;
    }

    public String getName() {
        return Name;    //don not use this.   pointer
    }

    public void setName(String name) {
        Name = name;        //don not use this.   pointer
    }
    public String getPassword() {
        return Password;    //don not use this.   pointer
    }

    public void setPassword(String password) {
        Password = password;    //don not use this.   pointer
    }
}
