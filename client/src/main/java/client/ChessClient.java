package client;

import server.Server;



public class ChessClient {
    private String visitorName = null;
    private final ServerFacade facade;

    private final String serverUrl = null;
    //private final NotificationHandler notificationHandler;
    //private WebSocketFacade ws;
    private LoginState state = LoginState.SIGNEDOUT;

    ChessClient(){
        facade = new ServerFacade();
    };

    public String eval(String input){ //input is the whole line that we'll have to make sense of here//
        try {
            //making sense of the input string//
            var tokens = input.toLowerCase().split(" ");
            String[] params = new String[tokens.length - 1];
            var command = tokens[0];
            if(tokens.length > 1){ System.arraycopy(tokens, 1, params, 0, tokens.length - 1);}
            //first token is the command, the rest are parameters//

            String result = switch(command){
                //first ui / logged out//
                case "login" -> this.login(params);
                case "register"-> this.register(params);
                case "quit" -> this.quit();
                //second ui / logged in//
                //todo: implement//
                //third ui / gameplay//
                //this is for phase 6//
                default -> this.help();
            };

        }
        catch(Exception ex){
            return "some sort of invalid input identified: " + ex.getMessage();
        }
        return null;
    }

    public String login(String... params){ //not sure if these parameters are valid//
        //todo: implement
        System.out.println("login operator reached");
        for(int i = 0 ; i < params.length; ++i){
            System.out.println("param " + i + ": " + params[i]);
        }
        return null;
    }

    public String register(String... params){
        //todo: implement
        System.out.println("register operator reached");
        return null;
    }

    public String help(){
        String returnString = null;
        if(this.state == LoginState.SIGNEDOUT){
            returnString =  "     - register" + "\n" +
                            "     - login" + "\n" +
                            "     - help" + "\n" +
                            "     - quit" + "\n";
        }
        else{
            //todo: implement this parte//
            returnString = "not there yet";
        }
        return returnString;
    }

    public String quit(){
        return "quit";
    }

}
