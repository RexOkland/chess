package server.handlers;

import spark.Request;
import spark.Response;
import spark.Route;

public interface HandlerInterface extends Route {
    @Override
    Object handle(Request request, Response response) throws Exception;
}
