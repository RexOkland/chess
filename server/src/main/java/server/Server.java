package server;

import dataaccess.DatabaseHolder;
import spark.*;
import server.handlers.*;

public class Server {
    //my Database Manager//
    DatabaseHolder manager;

    //handlers//
    RegisterHandler regHandler;
    LoginHandler logHandler;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        // Register your endpoints and handle exceptions here.
        DatabaseHolder db = new DatabaseHolder(); //make our dbManager//

        Spark.post("/user", new RegisterHandler(db));
        Spark.post("/session", new LoginHandler(db));
        Spark.delete("/session", new LogoutHandler(db));

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
