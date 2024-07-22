package service;

import dataaccess.DatabaseHolder;
import models.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import responses.*;
import server.services.*;
import spark.utils.Assert;

public class CustomServiceTests {
    @Test
    @DisplayName("Good Register Request")
    public void goodRegister(){
        DatabaseHolder dbTest = new DatabaseHolder();
        RegisterService regService = new RegisterService();
        UserData testData = new UserData("theUser", "thePass", "fakeEmail@email.com");

        RegisterResponse expectedResponse = new RegisterResponse(testData.username(), "not null", null);
        RegisterResponse actualResponse = regService.register(testData, dbTest);

        Assertions.assertEquals(expectedResponse.username(), actualResponse.username());
        Assertions.assertNull(actualResponse.message());
    }

    @Test
    @DisplayName("Bad Register Request")
    public void badRegister(){
        DatabaseHolder dbTest = new DatabaseHolder();
        RegisterService regService = new RegisterService();
        UserData badTestData = new UserData("theUser", null, null);

        RegisterResponse expectedResponse = new RegisterResponse(null, null, "error: bad request");
        RegisterResponse actualResponse = regService.register(badTestData, dbTest);

        Assertions.assertEquals(actualResponse.message(), expectedResponse.message());
        Assertions.assertNull(actualResponse.username());
        Assertions.assertNull(actualResponse.authToken());
    }

    @Test
    @DisplayName("Good Login Request")
    public void goodLogin(){
        DatabaseHolder dbTest = new DatabaseHolder();
        RegisterService registerService = new RegisterService();
        LoginService loginService = new LoginService();

        UserData goodTestData = new UserData("theUser", "thePass", "dontMatter@gmail.com");
        registerService.register(goodTestData, dbTest); //will be successful//

        LoginResponse expectedResponse = new LoginResponse(goodTestData.username(), "notNull", null);
        LoginResponse actualResponse = loginService.login(goodTestData, dbTest);

        Assertions.assertNull(actualResponse.message());
        Assertions.assertEquals(expectedResponse.username(), actualResponse.username());
        Assertions.assertNotNull(actualResponse.authToken());
    }

    @Test
    @DisplayName("Bad Login Request")
    public void badLogin(){
        DatabaseHolder dbTest = new DatabaseHolder();
        LoginService loginService = new LoginService();

        UserData badTestData = new UserData("notRegisteredUser", "fakePassword", null);

        LoginResponse expectedResponse = new LoginResponse(null, null, "error: unauthorized");
        LoginResponse actualResponse = loginService.login(badTestData, dbTest);

        Assertions.assertEquals(expectedResponse.message(), actualResponse.message());
        Assertions.assertNull(actualResponse.username());
        Assertions.assertNull(actualResponse.authToken());
    }


    @Test
    @DisplayName("Good Logout Request")
    public void goodLogout(){
        DatabaseHolder dbTest = new DatabaseHolder();
        RegisterService registerService = new RegisterService();
        LogoutService logoutService = new LogoutService();

        UserData goodTestData = new UserData("leUser", "lePassword", "leGmail");
        RegisterResponse goodResponse = registerService.register(goodTestData, dbTest); //will work//
        String validAuthString = goodResponse.authToken();

        LogoutResponse actualResponse = logoutService.logout(validAuthString, dbTest);

        Assertions.assertNull(actualResponse.message()); //that's it...//
    }

    @Test
    @DisplayName("Bad Logout Request")
    public void badLogout(){
        DatabaseHolder dbTest = new DatabaseHolder();
        LogoutService logoutService = new LogoutService();

        UserData badTestData = new UserData("leUser", "lePassword", "leGmail");
        String invalidAuthString = "some string that isn't an auth token";

        LogoutResponse expectedResponse = new LogoutResponse("error: unauthorized");
        LogoutResponse actualResponse = logoutService.logout(invalidAuthString, dbTest);

        Assertions.assertEquals(expectedResponse.message(), actualResponse.message());
    }

    @Test
    @DisplayName("Good List Games Request")
    public void goodListGames(){
        DatabaseHolder dbTest = new DatabaseHolder();
        RegisterService registerService = new RegisterService();
        ListGamesService listService = new ListGamesService();

        UserData unimportantData = new UserData("user", "pw", "email");
        RegisterResponse goodResponse = registerService.register(unimportantData, dbTest); //will work//
        String validAuthString = goodResponse.authToken();

        ListGamesResponse expectedResponse = new ListGamesResponse(null, null); //games won't be null//
        ListGamesResponse actualResponse = listService.getGames(validAuthString, dbTest);

        Assertions.assertEquals(expectedResponse.message(), actualResponse.message());
        Assertions.assertNotNull(actualResponse.games());
    }

    @Test
    @DisplayName("BAD List Games Request")
    public void badListGames() {
        DatabaseHolder dbTest = new DatabaseHolder();
        ListGamesService listService = new ListGamesService();

        String invalidAuthString = "not a valid auth token";

        ListGamesResponse expectedResponse = new ListGamesResponse(null, "error: unauthorized"); //games won't be null//
        ListGamesResponse actualResponse = listService.getGames(invalidAuthString, dbTest);

        Assertions.assertEquals(expectedResponse.message(), actualResponse.message());
        Assertions.assertNull(actualResponse.games());
    }

    @Test
    @DisplayName("Good Create Game Request")
    public void goodCreateGame(){
        DatabaseHolder dbTest = new DatabaseHolder();
        RegisterService registerService = new RegisterService();
        CreateGameService createService = new CreateGameService();

        UserData goodData = new UserData("me", "myPassword", "goodEmail@gmail.com");
        RegisterResponse validResponse = registerService.register(goodData, dbTest);
        String validAuthString = validResponse.authToken();

        CreateGameResponse actualResponse = createService.createGame(validAuthString, "myGame", dbTest);

        Assertions.assertNotNull(actualResponse.gameID());
        Assertions.assertNull(actualResponse.message());
    }

    @Test
    @DisplayName("Bad Create Game Request")
    public void badCreateGame(){
        DatabaseHolder dbTest = new DatabaseHolder();
        CreateGameService createService = new CreateGameService();

        String invalidAuthString = "not a valid auth token";

        CreateGameResponse expectedResponse = new CreateGameResponse(null, "error: unauthorized");
        CreateGameResponse actualResponse = createService.createGame(invalidAuthString, "gameName", dbTest);

        Assertions.assertEquals(expectedResponse.message(), actualResponse.message());
        Assertions.assertNull(actualResponse.gameID());
    }

    @Test
    @DisplayName("Good Join Game Request")
    public void goodJoinGame(){
        DatabaseHolder dbTest = new DatabaseHolder();
        RegisterService registerService = new RegisterService();
        CreateGameService createService = new CreateGameService();
        JoinGameService joinService = new JoinGameService();

        UserData goodData = new UserData("goodUser", "goodPassword", "goodEmail");
        RegisterResponse rResponse = registerService.register(goodData, dbTest);

        String goodAuth = rResponse.authToken();;
        CreateGameResponse cResponse = createService.createGame(goodAuth, "a", dbTest);
        Integer goodID = cResponse.gameID();

        JoinGameResponse expectedResponse = new JoinGameResponse(null);
        JoinGameResponse actualResponse = joinService.joinGame(goodAuth, "WHITE", goodID, dbTest);

        Assertions.assertEquals(actualResponse.message(), expectedResponse.message());
        Assertions.assertNull(actualResponse.message());
    }

    @Test
    @DisplayName("Bad Join Game Request")
    public void badJoinGame(){
        DatabaseHolder dbTest = new DatabaseHolder();
        JoinGameService joinService = new JoinGameService();

        String invalidAuthString = "not a valid auth token";
        Integer invalidGameID = 99;

        JoinGameResponse expectedResponse = new JoinGameResponse("error: unauthorized");
        JoinGameResponse actualResponse = joinService.joinGame(invalidAuthString, "WHITE", invalidGameID, dbTest);

        Assertions.assertEquals(actualResponse.message(), expectedResponse.message());
        Assertions.assertNotNull(actualResponse.message());
    }

    @Test
    @DisplayName("Good Clear Request")
    public void clearGame(){
        DatabaseHolder dbTest = new DatabaseHolder();
        ClearService clearService = new ClearService();

        ClearResponse actualResponse = clearService.clear(dbTest);
        Assertions.assertNull(actualResponse.message());
    }
}
