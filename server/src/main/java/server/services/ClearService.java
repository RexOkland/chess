package server.services;

import dataaccess.DatabaseHolder;
import responses.ClearResponse;

public class ClearService {
    String responseMessage = null;
    public ClearResponse clear(DatabaseHolder db){
        db.gamesDAO().clearDAO();
        db.authDAO().clearDAO();
        db.userDAO().clearDAO();
        return new ClearResponse(null);
    }
}
