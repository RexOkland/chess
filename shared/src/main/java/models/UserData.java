package models;

public record UserData (String u, String p, String e){
    static String userName;
    static String password;
    static String email;

    public String getUserName(){
        return userName;
    }
    public String getPassword(){
        return password;
    }
    public String getEmail(){
        return email;
    }


}
