package client;

import chess.*;
import client.websocket.WebSocketFacade;
import models.AuthData;
import models.GameData;
import models.UserData;
import requests.JoinGameRequest;
import responses.*;
import ui.EscapeSequences;
import websocket.commands.UserGameCommand;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.util.ArrayList;
import java.util.List;

import static ui.EscapeSequences.*;


public class ChessClient {
    private String visitorName;
    private String visitorAuthToken;
    private final ServerFacade facade;

    private final String serverUrl = null;
    //private final NotificationHandler notificationHandler;
    private WebSocketFacade webSocketFacade;
    private WebSocketContainer container;
    private NavState nav = NavState.PRELOGIN;
    private ArrayList<GameData> gameDataList;
    private Integer currentGameId;

    ChessClient(){
        facade = new ServerFacade(8080);
        webSocketFacade = new WebSocketFacade(8080);
    }

    public NavState eval(String input) throws Exception {
        //input is the whole line that we'll have to make sense of here//
        try {
            //making sense of the input string//
            String[] tokens = input.split(" ");
            String[] params = null;
            if(tokens.length > 1){
                params = new String[tokens.length - 1];
                System.arraycopy(tokens, 1, params, 0, params.length);
            }
            var command = tokens[0].toLowerCase();

            //first token is the command, the rest are parameters//

            NavState currentUI = this.nav; //the nav decides which options are available, and which menu we'll see//
            currentUI = switch(command){
                //can be ran from ANY nav state//
                case "help" -> this.help();
                case "quit" -> this.quit();
                //first ui / logged out//
                case "login" -> this.login(params);
                case "register" -> this.register(params);
                //second ui / logged in//
                case "logout" -> this.logout();
                case "list" -> this.list();
                case "join" -> this.join(params);
                case "create" -> this.create(params);
                case "observe" -> this.observe(params);
                //third ui / gameplay - WEBSOCKET COMMANDS//
                case "redraw" -> this.redraw();
                case "leave" -> this.leave();//todo: fix//
                case "highlight" -> this.hightlight(params);

                //todo: implement this in phase 6//

                default -> this.unrecognized();
            };
            setNav(currentUI);
            return currentUI;
        }
        catch(Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    public NavState login(String... params) throws Exception {
        //initial check to see if we can be running this function//
        if(this.nav != NavState.PRELOGIN){throw new Exception("login attempted while already logged in");}

        UserData givenData;
        //makes sure we have username AND password//
        if(params.length < 2){throw new Exception("We're short parameters for this operation");}
        else{ givenData = new UserData(params[0], params[1], null); }

        LoginResponse response = null;
        try {response = facade.clientLogin(givenData);}
        catch (Exception ex){
            throw new Exception("user or password is incorrect, try again please: ");
        }

        NavState newState;
        if(response.authToken().length() > 10){
            /*SUCCESSFUL*/ newState = NavState.POSTLOGIN;
            System.out.println("Welcome back " + response.username() + "! Select a command:");
            setVisitorInfo(response.username(), response.authToken());
        }
        else{
            /*UNSUCCESSFUL*/ newState = NavState.PRELOGIN;
            System.out.println("Login unsuccessful: " + response.message());
        }
        setNav(newState);
        this.options(); //list out new options depending on result//
        /*
        login successful -> move to the POSTLOGIN nav - returns NavState.PRELOGIN
        login unsuccessful -> stay in the PRELOGIN nav - returns NavState.POSTLOGIN
         */
        return newState;
    }

    public NavState register(String... params) throws Exception {
        //initial check to see if we can be running this function//
        if(this.nav != NavState.PRELOGIN){throw new Exception("register attempted while already logged in");}

        UserData givenData;
        //makes sure we have username/password... email is optional which is why I have the else{} statement//
        if(params.length < 2){throw new Exception("We're short parameters for this operation");}
        else if(params.length == 2){ givenData = new UserData(params[0], params[1], null); }
        else{givenData = new UserData(params[0], params[1], params[2]);}

        RegisterResponse response = null;
        try{
            response = facade.clientRegister(givenData); //run it//
        }
        catch(Exception ex){
            switch (ex.getMessage()){
                case "400" -> throw new Exception("Invalid data provided, please try again: ");
                case "403" -> throw new Exception("Username already taken, please try again: ");
                default -> throw new Exception("Invalid data, please try again");
            }
        }

        NavState newState = null;
        if(response.authToken().length() > 10){
            /*SUCCESSFUL*/ newState = NavState.POSTLOGIN;
            System.out.println("Welcome " + response.username() + "! Select a command:");
            setVisitorInfo(response.username(), response.authToken());
        }
        else{
            /*UNSUCCESSFUL*/ newState = NavState.PRELOGIN;
            System.out.println("Register unsuccessful: " + response.message());
        }
        setNav(newState);
        this.options(); //list out new options depending on result//
        /*
        RETURN LOGIC
        register successful -> move to the POSTLOGIN nav - returns NavState.PRELOGIN
        register unsuccessful -> stay in the PRELOGIN nav - returns NavState.POSTLOGIN
         */
        return newState;
    }

    public NavState logout() throws Exception {
        //initial check to see if we can be running this function//
        if(this.nav == NavState.PRELOGIN){throw new Exception("logout attempted while not logged in");}
        NavState newState = getNav();
        try{
            String activeToken = this.getClientAuthToken();
            LogoutResponse response = facade.clientLogout(activeToken);
            //if we make it here, nothing was thrown... which is a W//
            if(response.message() == null){
                System.out.println("See you later " + getVisitorName() + "!");
                newState = NavState.PRELOGIN;//you've logged out//
                setNav(newState);
                this.options();
            }

        }catch(Exception ex){
            throw new Exception(ex.getMessage());
            //something went wrong with the logout//
        }
        return newState;
    }

    public NavState list(String... params) throws Exception {
        //initial check to see if we can be running this function//
        if(this.nav != NavState.POSTLOGIN){throw new Exception("must be logged in to access game list");}
        try{
            String activeToken = this.getClientAuthToken();
            ListGamesResponse response = facade.clientListGame(activeToken);
            System.out.println("GAMES: ");
            gameDataList = new ArrayList<GameData>(response.games());
            for (int i = 0; i < gameDataList.size(); i++) {
                GameData g = gameDataList.get(i);
                System.out.println((i+1) + ": " + g.gameName());
                System.out.println(" - white: " + g.whiteUsername());
                System.out.println(" - black: " + g.blackUsername());
            }
        }catch(Exception ex){
            throw new Exception(ex.getMessage());
            //something went wrong with creating the game//
        }
        return getNav(); //NavState does not change in this function//
    }

    public NavState join(String...params) throws Exception {
        NavState state = getNav();
        //initial check to see if we can be running this function//
        if(this.nav != NavState.POSTLOGIN){throw new Exception("must be logged in to join game");}
        if(params.length < 2){throw new Exception("We're short parameters for this operation");}

        int gameNumber = Integer.parseInt(params[0]) - 1;
        if(gameNumber > this.gameDataList.size()){throw new Exception("game doesn't exist, please select another: ");}
        int requestedGameID = this.gameDataList.get(Integer.parseInt(params[0]) - 1).gameID();

        String requestedTeam = params[1].toUpperCase();
        if((!requestedTeam.equals("WHITE")) && (!requestedTeam.equals("BLACK"))){throw new Exception("select a valid color please: ");}

        try{
            String activeToken = this.getClientAuthToken();

            JoinGameRequest request = new JoinGameRequest(requestedTeam, requestedGameID);
            JoinGameResponse response = facade.clientJoinGame(activeToken, request);
            if(response.message() == null){
                System.out.println("Game Joined!");
            }

            //joining the gameplay repl//
            UserGameCommand.CommandType typeToSend = UserGameCommand.CommandType.CONNECT;
            String authToSend = this.visitorAuthToken;

            UserGameCommand commandToSend = new UserGameCommand(typeToSend, authToSend, requestedGameID);
            this.webSocketFacade.sendCommand(commandToSend);
            state = NavState.GAMEPLAY;
            this.currentGameId = requestedGameID;
        }
        catch(Exception ex){
            switch(ex.getMessage()){
                case "400" -> throw new Exception("bad input provided, please try again: ");
                case "403" -> throw new Exception("spot is already taken, choose another game or color please: ");
                default -> throw new Exception("Something went wrong, please try again: ");
            }
            //something went wrong with joining the game//
        }
        return state;
    }

    public NavState create(String... params) throws Exception {
        if(this.nav != NavState.POSTLOGIN){throw new Exception("must be logged in to create a game");}
        if(params.length < 1){throw new Exception("we need a name for the new game");}

        try{
            String activeToken = this.getClientAuthToken();
            GameData gameData = new GameData(0, null, null, params[0], new ChessGame());
            CreateGameResponse response = facade.clientCreateGame(activeToken, gameData);
            if(response.message() == null){
                System.out.println("Game Created Successfully!");
                System.out.println("ID: " + response.gameID());
                System.out.println("Select another option to continue: ");
                options();
            }
        }
        catch(Exception ex){
            throw new Exception(ex.getMessage());
            //something went wrong with creating the game//
        }
        return getNav(); //DOES NOT CHANGE NAV STATE//
    }

    public NavState observe(String... params) throws Exception { //so this all works, I just need to format//
        NavState state = getNav();
        if(this.nav != NavState.POSTLOGIN){throw new Exception("must be logged in to observe a game");}
        if(params.length < 2){throw new Exception("we're short parameters here'");}

        int selectedGame = Integer.parseInt(params[0]);
        if(selectedGame > this.gameDataList.size()){throw new Exception("game does not exist");}

        String selectedTeam = params[1].toUpperCase();
        if((!selectedTeam.equals("WHITE")) && (!selectedTeam.equals("BLACK"))){throw new Exception("select a valid color please: ");}

        //todo: this is going to become a CONNECT-WS command//
        UserGameCommand.CommandType typeToSend = UserGameCommand.CommandType.CONNECT;
        String authToSend = this.visitorAuthToken;
        int gameToSend = this.gameDataList.get(selectedGame - 1).gameID();

        UserGameCommand commandToSend = new UserGameCommand(typeToSend, authToSend, gameToSend);
        this.webSocketFacade.sendCommand(commandToSend);
        state = NavState.GAMEPLAY;
        this.currentGameId = gameToSend;

        return state; //DOES NOT CHANGE NAV STATE//
    }

    public NavState redraw(){
        //already in the gameplay mode, already has their session in the set//
        webSocketFacade.redrawBoard();
        return getNav(); //DOES NOT CHANGE NAV STATE//
    }

    public NavState leave(){
        NavState state = getNav();
        UserGameCommand.CommandType typeToSend = UserGameCommand.CommandType.LEAVE;
        String authToSend = this.visitorAuthToken;

        UserGameCommand leaveCommand = new UserGameCommand(typeToSend, authToSend, this.currentGameId);
        webSocketFacade.sendCommand(leaveCommand);

        state = NavState.POSTLOGIN; //exiting game play mode//
        return state;
    }

    public NavState hightlight(String... params) throws Exception {
        if(params.length == 0){throw new Exception("please enter a position on the board with the command");}
        if(params[0].length() != 2){throw new Exception("Chess Positions should be two characters long");}

        char first = params[0].charAt(0);
        int firstInt = charToInt(first);
        char second = params[0].charAt(1);
        int secondInt = charToInt(second);

        ChessPosition theSpot = new ChessPosition(secondInt, firstInt);
        webSocketFacade.highlightBoard(theSpot);

        return getNav(); //DOES NOT CHANGE NAV STATE//
    }

    public int charToInt(char c) {
        return switch (c) {
            case 'a' -> 1; case '1' -> 1;
            case 'b' -> 2; case '2' -> 2;
            case 'c' -> 3; case '3' -> 3;
            case 'd' -> 4; case '4' -> 4;
            case 'e' -> 5; case '5' -> 5;
            case 'f' -> 6; case '6' -> 6;
            case 'g' -> 7; case '7' -> 7;
            case 'h' -> 8; case '8' -> 8;
            default -> throw new IllegalArgumentException("Unexpected value: " + c);
        };
    }




    public NavState help() throws Exception {
        System.out.println("Here are your current options: ");
        this.options(); //uses current nav//
        return getNav(); //DOES NOT CHANGE NAV STATE//
    }

    public NavState unrecognized() throws Exception {
        System.out.println("Request not recognized, please try again: ");
        this.options(); //uses current nav//
        return getNav(); //DOES NOT CHANGE NAV STATE//
    }


    public NavState quit(){
        return NavState.QUIT;
    }

    //HELPER FUNCTIONS//
    public void options() throws Exception {
        String currentMenu = null;
        if(this.nav == NavState.PRELOGIN){
            currentMenu = """
                         - register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                         - login <USERNAME> <PASSWORD> - to access your account
                         - help - to display current command options
                         - quit - to exit 
                    """;
        }
        else if(this.nav == NavState.POSTLOGIN){
            currentMenu = """
                         - join <GAMEID> <BLACK|WHITE> - to join a game as black or white
                         - create <GAMENAME> - to create a new game
                         - list - to see existing games
                         - observe <GAMEID> - to observe an ongoing game
                         - help - to display current command options
                         - logout - to sign out of your account
                    """;
        }
        else if(this.nav == NavState.GAMEPLAY){
            //todo: implement this parte in PHASE 6//
            currentMenu = """
                         - redraw - to display the board again
                         - move <start position> <end position> - to move a piece
                         - resign - to forfeit the game
                         - highlight <position> - to show legal moves for indicated piece
                         - help - to display current command options
                         - leave - to exit the game
                    """;
        }
        else{
            throw new Exception("nav state not found");
        }
        System.out.println(currentMenu);
    }


    //GETTERS Y SETTERS//

    public void setNav(NavState state){this.nav = state;}
    public NavState getNav(){return this.nav;}

    public void setClientAuthToken(String authToken){this.visitorAuthToken = authToken;}
    public String getClientAuthToken(){return this.visitorAuthToken;}

    public void setVisitorName(String name){ this.visitorName = name;}
    public String getVisitorName(){return this.visitorName;}

    public void setVisitorInfo(String name, String auth){ //2 for 1//
        this.setVisitorName(name);
        this.setClientAuthToken(auth);

        this.webSocketFacade.setUserInfo(name, auth);// when we set data for this guy, the webSocket gets it too//
    }

}
