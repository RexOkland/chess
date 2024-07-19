package server;

import dataaccess.DatabaseManager;
import org.eclipse.jetty.util.log.Log;
import spark.*;
import server.handlers.*;
import server.services.*;

public class Server {
    //my Database Manager//
    DatabaseManager manager;

    //handlers//
    RegisterHandler regHandler;
    LoginHandler logHandler;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        // Register your endpoints and handle exceptions here.
        DatabaseManager manager = new DatabaseManager(); //make our dbManager//
        Spark.post("/user", this::registerUser);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public Object registerUser(Request req, Response res){
        RegisterHandler registerHandler = new RegisterHandler(req, res);
        return null;
    }

}
