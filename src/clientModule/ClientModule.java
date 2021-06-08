package clientModule;

import clientModule.utility.AuthManager;
import clientModule.utility.Console;

import java.util.Scanner;


/**
 * Класс ClientModule.
 * Точка входа клиента.
 */
public class ClientModule {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthManager authManager = new AuthManager(scanner);
        Console console = new Console(scanner, authManager);
        Client client = new Client("localhost", 2222, console, authManager);
        client.run();
        scanner.close();
    }


}

