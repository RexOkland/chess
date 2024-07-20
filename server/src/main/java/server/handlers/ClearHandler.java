package server.handlers;

import dataaccess.DatabaseHolder;
import responses.ClearResponse;
import server.services.ClearService;
import server.services.CreateGameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHandler implements Route {
    ClearService service;
    DatabaseHolder holder;

    public ClearHandler(DatabaseHolder databaseHolder){
        this.service = new ClearService();
        this.holder = databaseHolder;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        ClearResponse clearResponse = service.clear(holder);
        if (clearResponse.message() == null) {
            response.status(200);
        } else {
            response.status(500);
        }

        return "{}";
    }
}
