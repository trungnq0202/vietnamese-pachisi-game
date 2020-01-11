import controllers.ClientController;

import java.io.IOException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner userinput = new Scanner(System.in);
        String name = userinput.nextLine();
        ClientController client = new ClientController(name);
        client.start();
        try {
            client.ready();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
