package client;

import java.util.Scanner;

public class Repl {
    private ChessClient client;

    public Repl(String serverURL){
        client = new ChessClient();

    }

    public void run(){
        String helpMenu = client.help(); //ui changes depending on being logged in or not//

        System.out.println("Chess Server! Enter a command to get started:");
        System.out.println(helpMenu);

        Scanner scanner = new Scanner(System.in); //user console input//
        var result = "";
        while(result != "quit"){ //this guy will keep running until the user enters in 'quit'//
            String line = scanner.nextLine();
            System.out.println("line read: " + line);
            result = client.eval(line); //todo: implement the eval function within the client
            System.out.println("result we got: " + result);
            break;
        }

        System.out.println("bye!");
    }
}
