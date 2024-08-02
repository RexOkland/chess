package client;

import chess.ChessGame;
import models.AuthData;
import models.GameData;
import models.UserData;
import requests.JoinGameRequest;
import responses.*;


public class ChessClient {
    private String visitorName;
    private String visitorAuthToken;
    private final ServerFacade facade;

    private final String serverUrl = null;
    //private final NotificationHandler notificationHandler;
    //private WebSocketFacade ws;
    private NavState nav = NavState.PRELOGIN;

    ChessClient(){
        facade = new ServerFacade("http://localhost:8080");
    };
    ChessClient(String url){
        facade = new ServerFacade(url);
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
                //third ui / gameplay//
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

        LoginResponse response = facade.clientLogin(givenData);

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

        RegisterResponse response = facade.clientRegister(givenData); //run it//

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
            for(GameData g : response.games()){
                System.out.println("Name: " + g.gameName());
                System.out.println("ID: " + g.gameID());
                System.out.println(" - white: " + g.whiteUsername() );
                System.out.println(" - black: " + g.blackUsername() );
            }
        }catch(Exception ex){
            throw new Exception(ex.getMessage());
            //something went wrong with creating the game//
        }
        return getNav(); //NavState does not change in this function//

    }

    public NavState join(String...params) throws Exception {
        //initial check to see if we can be running this function//
        if(this.nav != NavState.POSTLOGIN){throw new Exception("must be logged in to join game");}
        if(params.length < 2){throw new Exception("We're short parameters for this operation");}

        try{
            String activeToken = this.getClientAuthToken();
            JoinGameRequest request = new JoinGameRequest(params[1], Integer.parseInt(params[0]) );
            JoinGameResponse response = facade.clientJoinGame(activeToken, request);
            if(response.message() == null){
                System.out.println("Game Joined!");

            }
        }
        catch(Exception ex){
            throw new Exception(ex.getMessage());
            //something went wrong with joining the game//
        }
        return getNav();
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
                         - help - for more info
                         - quit - to exit 
                    """;
        }
        else if(this.nav == NavState.POSTLOGIN){
            currentMenu = """
                         - join <GAMEID> <BLACK|WHITE> - to join a game as black or white
                         - create <GAMENAME> - to create a new game
                         - list - to see existing games
                         - observe <GAMEID> - to observe an ongoing game
                         - help - for more info
                         - logout - to sign out of your account
                    """;
        }
        else if(this.nav == NavState.GAMEPLAY){
            //todo: implement this parte in PHASE 6//
            currentMenu = "not there yet";
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
    }

    public AuthData getVisitorInfo(){
        return new AuthData(getClientAuthToken(), getVisitorName());
    }

}
