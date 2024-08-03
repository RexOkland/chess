package client;

import com.google.gson.Gson;
import models.AuthData;
import models.GameData;
import models.UserData;
import requests.JoinGameRequest;
import responses.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public ServerFacade(int port){
        serverUrl = "http://localhost:" + port;
    }

    public RegisterResponse clientRegister(UserData user) throws Exception {
        var path = "/user";
        return makeRequest("POST", path, null, user, RegisterResponse.class);
    }

    public LoginResponse clientLogin(UserData user) throws Exception {
        var path = "/session";
        return makeRequest("POST", path, null, user, LoginResponse.class);
    }

    public LogoutResponse clientLogout(String token) throws Exception {
        var path = "/session";
        return makeRequest("DELETE", path, token, null, LogoutResponse.class);
    }

    public ListGamesResponse clientListGame(String token) throws Exception{
        var path = "/game";
        return makeRequest("GET", path, token, null, ListGamesResponse.class);
    }

    public JoinGameResponse clientJoinGame(String token, JoinGameRequest request) throws Exception {
        var path = "/game";
        return makeRequest("PUT", path, token, request, JoinGameResponse.class);
    }

    public CreateGameResponse clientCreateGame(String token, GameData data) throws Exception {
        var path = "/game";
        return makeRequest("POST", path, token, data,  CreateGameResponse.class);
    }

    public ClearResponse clearDatabase() throws Exception {
        var path = "/db";
        return makeRequest("DELETE", path, null, null, ClearResponse.class);
    }

    private <T> T makeRequest(String method, String path, String header, Object request, Class<T> responseClass) throws Exception {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeHeader(header, http);
            writeBody(request, http);

            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new Exception("error code:  " + ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws Exception {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static void writeHeader(String header, HttpURLConnection http) throws Exception {
        if (header != null) {
            http.addRequestProperty("authorization", header);
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws Exception {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new Exception(String.valueOf(status));
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws Exception {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
