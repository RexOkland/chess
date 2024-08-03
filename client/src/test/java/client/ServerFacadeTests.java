package client;

import chess.ChessGame;
import models.AuthData;
import models.GameData;
import models.UserData;
import org.junit.jupiter.api.*;
import requests.JoinGameRequest;
import responses.*;
import server.Server;
import org.junit.jupiter.api.Assertions;

import javax.crypto.SecretKey;
import java.util.UUID;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void goodFacadeRegister() throws Exception {
        String randomNewUser = UUID.randomUUID().toString();
        var authData = facade.clientRegister(new UserData(randomNewUser, "password", "tacos@email.com"));
        Assertions.assertNull(authData.message());
        Assertions.assertNotNull(authData.authToken());
    }

    @Test
    void badFacadeRegister(){
        String errorMessagee = null;
        RegisterResponse response = null;
        try {
            response = facade.clientRegister(new UserData(null, "password", "tacos@email.com"));
        }
        catch(Exception ex){
            errorMessagee = ex.getMessage();
        }
        Assertions.assertNotNull(errorMessagee);
        Assertions.assertNull(response);
    }

    @Test
    void goodFacadeLogin() throws Exception {
        this.cleanup();
        //using a user that DOES exist//
        UserData validUser = new UserData("wsrtdyutfivfgcrxdfo;y/lhvclur", "abc123", "EMAIL");
        RegisterResponse goodRegister = facade.clientRegister(validUser);
        var response = facade.clientLogin(new UserData(validUser.username(), validUser.password(), null));

        Assertions.assertNull(response.message());
        Assertions.assertNotNull(response.authToken());
    }

    @Test
    void badFacadeLogin(){
        String errorMessage = null;
        LoginResponse response = null;
        try{
            response = facade.clientLogin(new UserData("qwertyuiop", "abc1234", null));
        }
        catch (Exception ex){
            errorMessage = ex.getMessage();
        }
        Assertions.assertNotNull(errorMessage);
        Assertions.assertNull(response);
    }

    @Test
    void goodFacadeLogout() throws Exception {
        String validToken = getValidToken();
        LogoutResponse response = facade.clientLogout(validToken);
        Assertions.assertNull(response.message());
    }

    @Test
    void badFacadeLogout(){
        String errorMessage = null;
        LogoutResponse response = null;
        try{
            response = facade.clientLogout("notAgoodString");
        }
        catch (Exception ex){
            errorMessage = ex.getMessage();
        }
        Assertions.assertNotNull(errorMessage);
        Assertions.assertNull(response);
    }

    @Test
    void goodFacadeList() throws Exception{
        String validToken = getValidToken();
        ListGamesResponse response = facade.clientListGame(validToken);
        Assertions.assertNull(response.message());
    }

    @Test
    void badFacadeList(){
        String errorMessage = null;
        ListGamesResponse response = null;
        try{
            response = facade.clientListGame("totallyNotGoodToken");
        }
        catch (Exception ex) {
            errorMessage = ex.getMessage();
        }
        Assertions.assertNotNull(errorMessage);
        Assertions.assertNull(response);
    }

    @Test
    void goodFacadeCreate() throws Exception {
        String validToken = getValidToken();
        GameData randomGameData = new GameData(0, null, null, UUID.randomUUID().toString(), new ChessGame());
        CreateGameResponse response = facade.clientCreateGame(validToken, randomGameData);

        Assertions.assertNull(response.message());
        Assertions.assertNotNull(response.gameID());
    }

    @Test
    void badFacadeCreate() {
        String errorMessage = null;
        CreateGameResponse response = null;
        //has invalid auth//
        try{
            response = facade.clientCreateGame("bad token", null);
        }
        catch (Exception ex){
            errorMessage = ex.getMessage();
        }
        Assertions.assertNotNull(errorMessage);
        Assertions.assertNull(response);
    }

    @Test
    void goodFacadeJoin() throws Exception {
        //empty server, then create a new game//
        this.cleanup();
        String validToken = getValidToken();//create a user//
        GameData randomGameData = new GameData(0, null, null, UUID.randomUUID().toString(), new ChessGame());
        CreateGameResponse createGameResponse = facade.clientCreateGame(validToken, randomGameData);

        Assertions.assertNull(createGameResponse.message());
        JoinGameRequest request = new JoinGameRequest("WHITE", createGameResponse.gameID());

        JoinGameResponse joinGameResponse = facade.clientJoinGame(validToken, request);

        Assertions.assertNull(joinGameResponse.message());
        Assertions.assertNotNull(joinGameResponse);
    }

    @Test
    void badFacadeJoin(){
        String errorMessage = null;
        JoinGameResponse response = null;
        try{
            response = facade.clientJoinGame("notValidToken", null);
        }
        catch (Exception ex){
            errorMessage = ex.getMessage();
        }

        Assertions.assertNotNull(errorMessage);
        Assertions.assertNull(response);
    }



    //helper functions//
    private String getValidToken() throws Exception {
        //gets me a valid user//
        String randomNewUser = UUID.randomUUID().toString();
        var authData = facade.clientRegister(new UserData(randomNewUser, "password", "validUser@email.com"));
        return authData.authToken();
    }

    private void cleanup(){
        try{
            ClearResponse response = facade.clearDatabase();
        }
        catch (Exception ex){
            if(ex.getMessage() != null){System.out.println(ex.getMessage());}
        }
    }



}
