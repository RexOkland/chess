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
        //todo: implement
        return null;
    }

    public String login(String... params){ //not sure if these parameters are valid//
        //todo: implement
        System.out.println("login operator reached");
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
