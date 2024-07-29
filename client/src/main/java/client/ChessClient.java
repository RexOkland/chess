package client;

import models.UserData;
import responses.RegisterResponse;
import server.Server;



public class ChessClient {
    private String visitorName = null;
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
            var tokens = input.split(" ");
            String[] params = new String[tokens.length - 1];
            var command = tokens[0].toLowerCase();
            if(tokens.length > 1){ System.arraycopy(tokens, 1, params, 0, tokens.length - 1);}
            //first token is the command, the rest are parameters//

            NavState currentUI = this.nav; //the nav decides which options are available, and which menu we'll see//
            currentUI = switch(command){
                //first ui / logged out//
                case "login" -> this.login(params);
                case "register"-> this.register(params);
                case "quit" -> this.quit();
                //second ui / logged in//
                //todo: implement this in phase 5!//
                //third ui / gameplay//
                //todo: implement this in phase 6//
                default -> this.help();
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
        /*
        login successful -> move to the POSTLOGIN nav - returns NavState.PRELOGIN
        login unsuccessful -> stay in the PRELOGIN nav - returns NavState.POSTLOGIN
         */
        //todo: implement actual function//
        return null; //will result in an exception until I fix this//
    }

    public NavState register(String... params) throws Exception {
        //initial check to see if we can be running this function//
        if(this.nav != NavState.PRELOGIN){throw new Exception("register attempted while already logged in");}
        /*
        register successful -> move to the POSTLOGIN nav - returns NavState.PRELOGIN
        register unsuccessful -> stay in the PRELOGIN nav - returns NavState.POSTLOGIN
         */
        UserData givenData = new UserData(params[0], params[1], params[2]); //todo: prolly check these params//
        RegisterResponse response = facade.clientRegister(givenData);
        NavState newState = null;
        if(response.authToken().length() > 10){
            System.out.println("Welcome " + response.username() + "! Select a command:");
            newState = NavState.POSTLOGIN;
        }
        else{
            System.out.println("Register unsuccessful: " + response.message());
            newState = NavState.PRELOGIN;
        }
        //todo: implement actual function//
        return newState;
    }

    public NavState help() throws Exception {
        String currentMenu = null;
        if(this.nav == NavState.PRELOGIN){
            currentMenu = """
                         CHESS SERVER: Enter a command:
                         - register
                         - login
                         - help
                         - quit
                    """;
        }
        else if(this.nav == NavState.POSTLOGIN){
            //todo: implement this parte//
            currentMenu = """
                         CHESS SERVER: Enter a command:
                         - play game
                         - create game
                         - list games
                         - observe game
                         - help
                         - logout
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
        return this.nav; //nav state stays the same in all cases on the help function//
    }

    public NavState quit(){
        return NavState.QUIT;
    }

    public void setNav(NavState state){this.nav = state;}
    public NavState getNav(){return this.nav;}

}
