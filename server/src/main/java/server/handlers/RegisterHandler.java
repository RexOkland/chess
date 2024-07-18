package server.handlers;

import spark.Request;
import spark.Response;
import server.services.*;

public class RegisterHandler implements HandlerInterface{
    Request req;
    Response res;
    RegisterService service;

    public RegisterHandler(Request req, Response res){
        this.req = req;
        this.res = res;
        this.service = new RegisterService();
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
        //TODO: code//
        return null;
    }
}
