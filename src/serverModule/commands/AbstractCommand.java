package serverModule.commands;

import common.utility.User;

import java.util.Objects;

/**
 * Класс AbstractCommand.
 * Шаблон для всех команд.
 */
public abstract class AbstractCommand {
    /** Поле имя команды */
    private String name;
    /** Поле описание команды */
    private String description;

    public AbstractCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Абстрактный метод исполнения команды.
     *
     * @param argument
     * @param objectArgument
     * @param user
     * @return статус исполнения команды.
     */
    public abstract boolean execute(String argument, Object objectArgument, User user);

    /**
     * Запрос имени команды.
     * @return name - имя команды.
     */
    public String getName() {
        return name;
    }

    /**
     * Запрос описания команды.
     * @return description
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "AbstractCommand{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractCommand that = (AbstractCommand) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }
}
