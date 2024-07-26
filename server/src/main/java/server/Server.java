package server;

import dataaccess.*;
import spark.*;
import server.handlers.*;

import javax.xml.crypto.Data;

public class Server {
    //my Database Holder//
    //DatabaseHolder db = new DatabaseHolder();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        // Register your endpoints and handle exceptions here.
        DatabaseAccess db = null;

        try{
            DatabaseManager.createDatabase();
            DatabaseManager.createChessTables();
            db = new DatabaseHolderSQL(); //make our dbManager//

        }catch (DataAccessException exception){
            System.out.print("error");
        }



        //Spark.delete("/db", ((request, response) -> new ClearHandler(db).handle(request,response)));//why//
        Spark.post("/user", new RegisterHandler(db));
        Spark.post("/session", new LoginHandler(db));
        Spark.delete("/session", new LogoutHandler(db));
        Spark.get("/game", new ListGamesHandler(db));
        Spark.post("/game", new CreateGameHandler(db));
        Spark.put("/game", new JoinGameHandler(db));
        Spark.delete("/db", new ClearHandler(db));


        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


}
