package service;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import passoff.model.*;
import passoff.server.TestServerFacade;
import server.Server;
import server.services.LoginService;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomAPITests {

    private static TestUser existingUser;
    private static TestServerFacade serverFacade;
    private static Server server;

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        serverFacade = new TestServerFacade("localhost", Integer.toString(port));

        existingUser = new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");

    }

    @BeforeEach
    public void setup() {
        serverFacade.clear();
        //one user already logged in
        TestAuthResult regResult = serverFacade.register(existingUser);
    }

    @Test
    @Order(1)
    @DisplayName("Bad Login Request")
    public void badLogin() {
        //username left blank!//
        TestUser existingUserA = new TestUser("", "existingUserPassword", "eu@mail.com");
        TestAuthResult loginResultA = serverFacade.login(existingUserA);
        Assertions.assertNull(loginResultA.getUsername(), "Response Username should be left null"); //should be left null//

        //password left blank!//
        TestUser existingUserB = new TestUser("anotherUser", "", "qwertyuiop@gmail.com");
        TestAuthResult loginResultB = serverFacade.login(existingUserB);
        Assertions.assertNull(loginResultB.getUsername(), "Response Username should be left null");


        LoginService testService = new LoginService();
    }

    @Test
    @Order(2)
    @DisplayName("Good Login Request")
    public void usernameTaken(){
        TestUser existingUserA = new TestUser("TakenUser", "pw", "qwertyuiop@gmail.com");
        TestAuthResult loginResultA = serverFacade.login(existingUserA);
        //should be a successful login//

        TestUser existingUserB = new TestUser("TakenUser", "pw2", "qwertyuiop@gmail.com2");
        TestAuthResult loginResultB = serverFacade.login(existingUserA);
        Assertions.assertNull(loginResultB.getUsername(), "Response Username should be left null");
    }


    @Test
    @Order(3)
    @DisplayName("Bad Register Request")
    public void badRegisters() {
        //username left blank!//
        TestUser existingUserA = new TestUser("", "existingUserPassword", "eu@mail.com");
        TestAuthResult registerResultA = serverFacade.register(existingUserA);
        Assertions.assertNull(registerResultA.getUsername(),  "Response Username should be left null"); //should be left null//


        TestUser existingUserB = new TestUser("qwertyuiop", "", "eu@mail.com");
        TestAuthResult registerResultB = serverFacade.register(existingUserB);
        Assertions.assertNull(registerResultB.getUsername(),  "Response Username should be left null"); //should be left null//
    }

    @Test
    @Order(4)
    @DisplayName("Good Register Request")
    public void goodRegister(){
        TestUser existingUserA = new TestUser("existingUser", "pw", "qwertyuiop@gmail.com");
        TestAuthResult registerResultA = serverFacade.register(existingUserA);
        Assertions.assertEquals(registerResultA.getUsername(), existingUserA.getUsername());
        //should be a successful register//

        TestUser existingUserB = new TestUser("AnotherExistingUser", "pw2", "qwertyuiop@gmail.com2");
        TestAuthResult registerResultB = serverFacade.register(existingUserB);
        Assertions.assertEquals(registerResultB.getUsername(), existingUserB.getUsername());
        //should also be a successful register//
    }

    @Test
    @Order(5)
    @DisplayName("Good Logout Request")
    public void goodLogout(){
        TestUser existingUserA = new TestUser("existingUser", "pw", "qwertyuiop@gmail.com");
        TestAuthResult registerResultA = serverFacade.register(existingUserA);
        Assertions.assertEquals(registerResultA.getUsername(), existingUserA.getUsername());
        //should be a successful register//

        TestResult logoutResult = serverFacade.logout(registerResultA.getAuthToken());
        Assertions.assertNull(logoutResult.getMessage()); //on successful run, message is null//
    }
    @Test
    @Order(6)
    @DisplayName("Bad Logout Request")
    public void badLogout(){
        TestResult logoutResult = serverFacade.logout("someRandomString");
        Assertions.assertNotNull(logoutResult.getMessage()); //should be some sort of error message//
    }


    @Test
    @Order(7)
    @DisplayName("Good List Games Request")
    public void goodListGames(){
        TestUser existingUserA = new TestUser("existingUser", "pw", "qwertyuiop@gmail.com");
        TestAuthResult registerResultA = serverFacade.register(existingUserA);
        Assertions.assertEquals(registerResultA.getUsername(), existingUserA.getUsername());
        //should be a successful register//

        TestResult listGamesResult = serverFacade.listGames(registerResultA.getAuthToken());
        Assertions.assertNull(listGamesResult.getMessage()); //on successful run, message is null//
    }
    @Test
    @Order(8)
    @DisplayName("Bad List Games Request")
    public void listGamesWithoutAuthentication(){
        TestResult listGamesResult = serverFacade.listGames("someRandomString");
        Assertions.assertNotNull(listGamesResult.getMessage()); //should be some sort of error message//
    }


    @Test
    @Order(9)
    @DisplayName("Good Create Game Request")
    public void goodCreateGame(){
        TestUser existingUserA = new TestUser("existingUser", "pw", "qwertyuiop@gmail.com");
        TestAuthResult registerResultA = serverFacade.register(existingUserA);
        Assertions.assertEquals(registerResultA.getUsername(), existingUserA.getUsername());
        //should be a successful register//

        TestCreateRequest testCreateRequest = new TestCreateRequest(registerResultA.getAuthToken());
        TestCreateResult createGameResult = serverFacade.createGame(testCreateRequest, registerResultA.getAuthToken());
        Assertions.assertNotNull(createGameResult.getGameID());
    }
    @Test
    @Order(10)
    @DisplayName("Bad Create Game Request")
    public void badCreateGame(){
        TestUser existingUserA = new TestUser("existingUser", "pw", "qwertyuiop@gmail.com");
        TestAuthResult registerResultA = serverFacade.register(existingUserA);
        Assertions.assertEquals(registerResultA.getUsername(), existingUserA.getUsername());
        //should be a successful register//

        TestCreateRequest testCreateRequest = new TestCreateRequest(registerResultA.getAuthToken());
        TestCreateResult createGameResult = serverFacade.createGame(testCreateRequest, "notAnAuthenticationToken");
        Assertions.assertNull(createGameResult.getGameID());
    }

    @Test
    @Order(11)
    @DisplayName("Good Join Game Request")
    public void goodJoinGame(){
        TestUser existingUserA = new TestUser("existingUser", "pw", "qwertyuiop@gmail.com");
        TestAuthResult registerResultA = serverFacade.register(existingUserA);
        Assertions.assertEquals(registerResultA.getUsername(), existingUserA.getUsername());
        //should be a successful register//

        TestCreateRequest testCreateRequest = new TestCreateRequest(registerResultA.getAuthToken());
        TestCreateResult createGameResult = serverFacade.createGame(testCreateRequest, registerResultA.getAuthToken());
        Assertions.assertNotNull(createGameResult.getGameID());
        //should be a successful create game//

        TestJoinRequest joinRequest = new TestJoinRequest(ChessGame.TeamColor.WHITE, createGameResult.getGameID());
        TestResult joinGameResult = serverFacade.joinPlayer(joinRequest, registerResultA.getAuthToken());
        Assertions.assertNull(joinGameResult.getMessage());
    }
    @Test
    @Order(12)
    @DisplayName("Bad Join Game Request - Color Already Taken")
    public void colorAlreadyTaken(){
        TestUser existingUserA = new TestUser("existingUser", "pw", "qwertyuiop@gmail.com");
        TestAuthResult registerResultA = serverFacade.register(existingUserA);
        Assertions.assertEquals(registerResultA.getUsername(), existingUserA.getUsername());
        //should be a successful register//

        TestCreateRequest testCreateRequest = new TestCreateRequest(registerResultA.getAuthToken());
        TestCreateResult createGameResult = serverFacade.createGame(testCreateRequest, registerResultA.getAuthToken());
        Assertions.assertNotNull(createGameResult.getGameID());
        //should be a successful create game//

        TestJoinRequest joinRequest = new TestJoinRequest(ChessGame.TeamColor.WHITE, createGameResult.getGameID());
        TestResult joinGameResult = serverFacade.joinPlayer(joinRequest, registerResultA.getAuthToken());
        Assertions.assertNull(joinGameResult.getMessage());
        //allowed the first time//

        TestJoinRequest joinRequest2 = new TestJoinRequest(ChessGame.TeamColor.WHITE, createGameResult.getGameID());
        TestResult joinGameResult2 = serverFacade.joinPlayer(joinRequest2, registerResultA.getAuthToken());
        Assertions.assertNotNull(joinGameResult2.getMessage());
        //but spot is taken for the second//
    }

    @Test
    @Order(13)
    @DisplayName("Clear Successful!")
    public void clearTest(){
        TestResult clearResult = serverFacade.clear();
        Assertions.assertNull(clearResult.getMessage());
    }


}
