package client;

import java.util.Objects;
import java.util.Scanner;

public class Repl {
    private ChessClient client;

    public Repl(String serverURL){
        client = new ChessClient();

    }

    public void run(){
        try {
            client.help(); //ui changes depending on being logged in or not//

            Scanner scanner = new Scanner(System.in); //user console input//
            NavState result = null;
            while (!Objects.equals(client.getNav(), NavState.QUIT)) { //this guy will keep running until the user enters in 'quit'//
                String line = scanner.nextLine();
                //System.out.println("line read: " + line);
                result = client.eval(line);

                //todo: implement the eval function within the client
            }

            System.out.println("bye!");
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
