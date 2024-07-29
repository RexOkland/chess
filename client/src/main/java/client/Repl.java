package client;

import java.util.Scanner;

public class Repl {
    private ChessClient client;

    public Repl(String serverURL){
        client = new ChessClient();
        String helpMenu = client.help(); //ui changes depending on being logged in or not//

        System.out.println("Chess Server! Enter a command to get started: \n");
        System.out.println(helpMenu);

        Scanner scanner = new Scanner(System.in); //user console input//
        var result = "";
        while(result != "quit"){ //this guy will keep running until the user enters in 'quit'//
            String line = scanner.nextLine();
            //result = client.eval(line); //todo: implement the eval function within the client
        }

        System.out.println("bye!");
    }

    public void run(){
        //todo: implement
    }
}
