package server.handlers;

import com.google.gson.Gson;
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
        if(request == null){
            throw new Exception("empty request!");
        }
        else{
            service.
        }
        //TODO: code//
        return null;
    }

    public Object register(Request request){
        service.
        //TODO: code//
        return null;
    }
}
