package clientModule.utility;

import common.data.Chapter;
import common.data.Coordinates;
import common.data.Weapon;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.ScriptRecursionException;
import common.exceptions.WrongAmountOfParametersException;
import common.utility.Request;
import common.utility.ResponseCode;
import common.utility.HumanBeingLite;
import common.utility.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Класс Console.
 * Работает с режимами программы: интерактивный и скрипт.
 */
public class Console {
    /** Поле сканер */
    private Scanner scanner;
    /** Поле коллекция с командами из скрипта */
    private Stack<File> scriptFileNames = new Stack<>();
    /** Поле коллекция со сканнером для возврата из скрипта */
    private Stack<Scanner> scannerStack = new Stack<>();
    /** Поле менеджер авторизации*/
    private AuthManager authManager;

    public Console(Scanner scanner, AuthManager authManager) {
        this.scanner = scanner;
        this.authManager = authManager;
    }

    /**
     * Режим интерактивного пользования.
     * @param serverResponseCode - проверка кода на правильность\статус выхода.
     * @param user - пользователь, запрашивающий выполнение.
     * @return request
     */
    public Request interactiveMode(ResponseCode serverResponseCode, User user) {
        String userInput = "";
        String[] userCommand = {"", ""};
        ProcessCode processCode = null;
        try {
            do {
                try {
                    if (fileMode() && (serverResponseCode == ResponseCode.SERVER_EXIT || serverResponseCode == ResponseCode.ERROR)) {
                        throw new IncorrectInputInScriptException();
                    }
                    while (fileMode() && !scanner.hasNextLine()) {
                        scanner.close();
                        scanner = scannerStack.pop();
                        System.out.println("Возврат из скрипта '" + scriptFileNames.pop().getName() + "'!");
                    }
                    if (fileMode()) {
                        userInput = scanner.nextLine();
                        if (!userInput.isEmpty()) {
                            System.out.print("~ ");
                            System.out.println(userInput);
                        }
                    } else {
                        System.out.print("~  ");
                        if (scanner.hasNext()) {
                            userInput = scanner.nextLine();
                        } else {
                            System.out.println("Клиент завершен!");
                            System.exit(0);
                        }
                    }
                    userCommand = (userInput.trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                } catch (NoSuchElementException | IllegalStateException exception) {
                    System.out.println("Произошла ошибка при вводе команды!");
                    userCommand = new String[]{"", ""};
                }
                processCode = checkCommand(userCommand[0], userCommand[1]);
            } while (processCode == ProcessCode.ERROR && !fileMode() || userCommand[0].isEmpty());
            try {
                switch (processCode) {
                    case OBJECT:
                        HumanBeingLite marineToInsert = generateHumanToInsert();
                        return new Request(userCommand[0], userCommand[1], marineToInsert, user);
                    case UPDATE_OBJECT:
                        HumanBeingLite marineToUpdate = generateHumanToUpdate();
                        return new Request(userCommand[0], userCommand[1], marineToUpdate, user);
                    case SCRIPT:
                        File scriptFile = new File(userCommand[1]);
                        if (!scriptFile.exists()) throw new FileNotFoundException();
                        if (!scriptFileNames.isEmpty() && scriptFileNames.search(scriptFile) != -1) {
                            throw new ScriptRecursionException();
                        }
                        scannerStack.push(scanner);
                        scriptFileNames.push(scriptFile);
                        scanner = new Scanner(scriptFile);
                        System.out.println("Выполнение скрипта '" + scriptFile.getName() + "'!");
                        break;
                    case LOG_IN:
                        return authManager.handle();
                }
            } catch (FileNotFoundException exception) {
                System.out.println("Файл со скриптом не найден!");
            } catch (ScriptRecursionException exception) {
                System.out.println("Скрипты не могут вызываться рекурсивно!");
                throw new IncorrectInputInScriptException();
            }
        } catch (IncorrectInputInScriptException exception) {
            System.out.println("Выполнение скрипта прервано!");
            while (!scannerStack.isEmpty()) {
                scanner.close();
                scanner = scannerStack.pop();
            }
        }
        return new Request(userCommand[0], userCommand[1], null, user);
    }



    /**
     * Проверка и запуск команды.
     * @param command - введенная команда
     * @param argument - аргумент для выполнения команды
     * @return Статус кода.
     */
    private ProcessCode checkCommand(String command, String argument) {
        try {
            switch (command) {
                case "":
                    return ProcessCode.ERROR;
                case "help":
                case "show":
                case "info":
                case "clear":
                case "history":
                case "sum_of_health":
                case "average_of_heart_count":
                case "exit":
                case "log_out":
                    if (!argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.OK;
                case "insert":
                    if (argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.OBJECT;
                case "update":
                    if (argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.UPDATE_OBJECT;
                case "log_in":
                    if (!argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.LOG_IN;
                case "remove_key":
                case "remove_lower_key":
                case "remove_all_by_weapon_type":
                    if (argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.OK;
                case "execute_script":
                    if (argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.SCRIPT;
                case "remove_greater":
                    if (!argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.OBJECT;
                default:
                    System.out.println("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
                    return ProcessCode.ERROR;
            }
        } catch (WrongAmountOfParametersException e) {
            System.out.println("Проверьте правильность ввода аргументов!");
        }
        return ProcessCode.OK;
    }

    /**
     * Генерация человека для вставки.
     * @return humanBeingLite
     */

    private HumanBeingLite generateHumanToInsert() {
        HumanBeingBuilder builder = new HumanBeingBuilder(scanner);
        if (fileMode()) {
            builder.setFileMode();
        } else {
            builder.setUserMode();
        }
        return new HumanBeingLite(
                builder.askName(),
                builder.askCoordinates(),
                builder.askHealth(),
                builder.askHeartCount(),
                builder.askAchievements(),
                builder.askWeapon(),
                builder.askChapter()
        );
    }

    /**
     * Генерация человека для обновления элемента.
     * @return humanBeingLite
     */

    private HumanBeingLite generateHumanToUpdate() {
        HumanBeingBuilder builder = new HumanBeingBuilder(scanner);
        if (fileMode()) {
            builder.setFileMode();
        } else {
            builder.setUserMode();
        }
        String name = builder.askAboutChangingField("Хотите изменить имя героя?") ?
                builder.askName() : null;
        Coordinates coordinates = builder.askAboutChangingField("Хотите изменить координаты героя?") ?
                builder.askCoordinates() : null;
        int health = builder.askAboutChangingField("Хотите изменить здоровье героя?") ?
                builder.askHealth() : -1;
        Integer heartCount = builder.askAboutChangingField("Хотите изменить количество сердец героя?") ?
                builder.askHeartCount() : -1;
        String achievements = builder.askAboutChangingField("Хотите изменить достижения героя?") ?
                builder.askAchievements() : null;
        Weapon weapon = builder.askAboutChangingField("Хотите изменить оружие героя?") ?
                builder.askWeapon() : null;
        Chapter chapter = builder.askAboutChangingField("Хотите изменить группу, к которой принадлежит герой?") ?
                builder.askChapter() : null;
        return new HumanBeingLite(
                name,
                coordinates,
                health,
                heartCount,
                achievements,
                weapon,
                chapter
        );
    }

    /**
     * Проверка режима файла.
     * @return Состояние коллекции сканеров.
     */

    private boolean fileMode() {
        return !scannerStack.isEmpty();
    }
}
