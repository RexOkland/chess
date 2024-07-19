package dataaccess.authdao;

import models.AuthData;
import models.UserData;

import java.util.Collection;

public class AuthDao implements AuthDaoInterface {

    //going to code functions in AuthDaoInterface and implement here//
    private Collection<AuthData> dataItems;

    public AuthDao(Collection<AuthData> collection){
        this.dataItems = collection;
    }
    public void addItem(AuthData item){
        dataItems.add(item);
    }


}
