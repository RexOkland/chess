package dataaccess.authdao;

import models.AuthData;
import models.UserData;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class AuthDao implements AuthDaoInterface {

    //going to code functions in AuthDaoInterface and implement here//
    private Collection<AuthData> dataItems;

    //CONSTRUCTORS//
    public AuthDao(){
        this.dataItems = new HashSet<AuthData>();
        //adding myself into every database//
        this.dataItems.add(new AuthData(UUID.randomUUID().toString(), "RexOkland"));
    }
    public AuthDao(Collection<AuthData> collection){
        this.dataItems = collection;
        //adding myself into every database//
        this.dataItems.add(new AuthData(UUID.randomUUID().toString(), "RexOkland"));
    }

    public void addItem(AuthData item){
        dataItems.add(item);
    }

    public Collection<AuthData> returnItems(){
        return this.dataItems;
    }

    public void removeItem(AuthData item){
        dataItems.remove(item);
    }


}
