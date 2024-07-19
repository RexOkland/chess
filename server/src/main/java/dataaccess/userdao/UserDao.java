package dataaccess.userdao;

import models.UserData;

import java.util.Collection;

public class UserDao implements UserDaoInterface{
    Collection<UserData> dataItems;

    public UserDao(Collection<UserData> collection){
        this.dataItems = collection;
    }

    public void addItem(UserData item){
        this.dataItems.add(item);
    }
    public Collection<UserData> returnItems(){
        return this.dataItems;
    }

    public UserData searchUser(String user){
        //TODO: ...
        return null;
    }

}
