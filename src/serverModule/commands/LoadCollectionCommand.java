package serverModule.commands;

import common.exceptions.WrongAmountOfParametersException;
import serverModule.utility.CollectionManager;
import serverModule.utility.ResponseOutputer;
import common.utility.User;
/**
 * Класс LoadCollectionCommand.
 * Команда "loadCollection".
 */
public class LoadCollectionCommand extends AbstractCommand{
    /** Поле менеджер коллекции */
    private CollectionManager collectionManager;

    public LoadCollectionCommand(CollectionManager collectionManager) {
        super("loadCollection", "загружает коллекцию (эта команда недоступна пользователю)");
        this.collectionManager = collectionManager;
    }

    /**
     * Исполнение команды.
     *
     * @param argument
     * @param objectArgument
     * @param user
     * @return статус исполнения команды.
     */
    @Override
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (argument.isEmpty() || objectArgument != null) throw new WrongAmountOfParametersException();
            collectionManager.loadCollection(argument);
            return true;
        } catch (WrongAmountOfParametersException exception) {
            ResponseOutputer.append("Должно быть передано название файла!\n");
        }
        return false;
    }
}
