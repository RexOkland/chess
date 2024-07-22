package dataaccess.authdao;

import models.AuthData;
import models.UserData;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.function.Predicate;

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

    public void removeItem(AuthData item){
        dataItems.remove(item);
    }

    public AuthData findAuth(String token){
        //return this.dataItems.stream().filter(authData -> authData.authToken().equals(token)).findAny().orElse(null);
        for(AuthData a : this.dataItems){
            if(a.authToken().equals(token)){
                return a;
            }
        }
        return null;
    }

    public void clearDAO(){
        this.dataItems.clear();
    }


}
