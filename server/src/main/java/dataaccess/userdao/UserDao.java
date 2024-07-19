package dataaccess.userdao;

import models.UserData;

import java.util.Collection;
import java.util.HashSet;

public class UserDao implements UserDaoInterface{
    Collection<UserData> dataItems;

    //CONSTRUCTORS//
    public UserDao(){
        this.dataItems = new HashSet<UserData>();
        //adding myself into every database//
        this.dataItems.add(new UserData("RexOkland", "XalapaMexico", "rex.okland@gmail.com"));
    }
    public UserDao(Collection<UserData> collection){
        this.dataItems = collection;
        //adding myself into every database//
        this.dataItems.add(new UserData("RexOkland", "XalapaMexico", "rex.okland@gmail.com"));
    }

    public void addItem(UserData item){
        this.dataItems.add(item);
    }
    public Collection<UserData> returnItems(){
        return this.dataItems;
    }

    public UserData searchUser(String user){
        for(UserData u : dataItems){
            if(u.username().equals(user)){
                return u;
            }
        }
        return null;
    }

}
