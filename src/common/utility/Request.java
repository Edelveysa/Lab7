package common.utility;

import java.io.Serializable;

/**
 * Класс Request.
 * Класс-запрос, работа с запросами.
 */
public class Request implements Serializable {
    /** Поле имя команды */
    private String commandName;
    /** Поле аргумент команды */
    private String argument;
    /** Поле аргумент команды - объект */
    private Serializable objectArgument;
    /** Поле пользователь */
    private User user;

    public Request(String commandName, String argument, Serializable objectArgument, User user) {
        this.commandName = commandName;
        this.argument = argument;
        this.objectArgument = objectArgument;
        this.user = user;
    }

    public Request(String commandName, String argument, User user) {
        this.commandName = commandName;
        this.argument = argument;
        this.objectArgument = null;
        this.user = user;
    }

    public Request(String commandName, String argument) {
        this.commandName = commandName;
        this.argument = argument;
        this.objectArgument = null;
        this.user = null;
    }

    public Request(User user) {
        this.commandName = "";
        this.argument = "";
        this.objectArgument = null;
        this.user = user;
    }

    /**
     * Запрос об имени команды.
     * @return commandName
     */

    public String getCommandName() {
        return commandName;
    }

    /**
     * Запрос об аргументе команды.
     * @return argument
     */
    public String getArgument() {
        return argument;
    }

    /**
     * Запрос об аргументе-объекте команды.
     * @return objectArgument
     */
    public Object getObjectArgument() {
        return objectArgument;
    }

    /**
     * Запрос о пользователе команды.
     * @return user
     */
    public User getUser() {
        return user;
    }

    public boolean isEmpty() {
        return commandName.isEmpty() && argument.isEmpty() && objectArgument == null;
    }
}
