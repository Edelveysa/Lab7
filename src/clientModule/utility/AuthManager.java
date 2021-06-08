package clientModule.utility;

import common.utility.Request;
import common.utility.User;

import java.util.Scanner;
/**
 * Класс AuthManager.
 * Работает с авторизацией.
 *
 */
public class AuthManager {
    /** Поле команда для входа в систему */
    private final String loginCommand = "sign_in";
    /** Поле команда для выхода из системы */
    private final String registerCommand = "sign_up";
    /** Поле сканер*/
    private Scanner scanner;

    public AuthManager(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Создание пользователя, вошедшего\зарегестрировшегося в системе.
     * Создание запроса от пользователя на использование команды.
     * @return request
     */
    public Request handle() {
        AuthAsker authAsker = new AuthAsker(scanner);
        String command = authAsker.askQuestion("У вас уже есть учетная запись?") ? loginCommand : registerCommand;
        User user = new User(authAsker.askLogin(), authAsker.askPassword());
        return new Request(command, "", user);
    }
}
