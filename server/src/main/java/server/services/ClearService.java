package server.services;

import dataaccess.DatabaseHolder;
import responses.ClearResponse;

public class ClearService {
    String responseMessage = null;
    public ClearResponse clear(DatabaseHolder db){
        db.GamesDAO().clearDAO();
        db.AuthDAO().clearDAO();
        db.UserDAO().clearDAO();
        return new ClearResponse(null);
    }
}
