package serverModule.commands;

import common.data.HumanBeing;
import common.exceptions.*;
import common.utility.User;
import serverModule.utility.CollectionManager;
import serverModule.utility.DatabaseCollectionManager;
import serverModule.utility.ResponseOutputer;

import java.util.List;

/**
 * Класс RemoveLowerKeyCommand.
 * Команда "remove_lower_key {key}".
 */
public class RemoveLowerKeyCommand extends AbstractCommand{
    /** Поле менеджер коллекции */
    private CollectionManager collectionManager;
    /** Поле менеджер бд коллекции */
    private DatabaseCollectionManager databaseCollectionManager;

    public RemoveLowerKeyCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("remove_lower_key {key}", "удалить из коллекции все элементы, ключ которых меньше, чем заданный");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
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
            if (user == null) throw new NonAuthorizedUserException();
            if (argument.isEmpty() || objectArgument != null) throw new WrongAmountOfParametersException();
            if (collectionManager.collectionSize() == 0) throw new EmptyCollectionException();
            int key = Integer.parseInt(argument);
            List<HumanBeing> humans = collectionManager.getLowerKey(key);
            for (HumanBeing humanBeing : humans) {
                if (!humanBeing.getOwner().equals(user)) continue;
                if (!databaseCollectionManager.checkHeroByIdAndUserId(humanBeing.getId(), user)) throw new IllegalDatabaseEditException();
                databaseCollectionManager.deleteHeroById(humanBeing.getId());
                collectionManager.removeByValue(humanBeing);
            }
            ResponseOutputer.append("Герои успешно удалены!\n");
            return true;
        } catch (WrongAmountOfParametersException exception) {
            ResponseOutputer.append("Вместе с этой командой должен быть передан параметр! Наберит 'help' для справки\n");
        } catch (EmptyCollectionException exception) {
            ResponseOutputer.append("Коллекция пуста!\n");
        } catch (DatabaseManagerException e) {
            ResponseOutputer.append("Произошла ошибка при обращении к базе данных!\n");
        } catch (IllegalDatabaseEditException exception) {
            ResponseOutputer.append("Произошло нелегальное изменение объекта в базе данных!\n");
            ResponseOutputer.append("Перезапустите клиент для избежания ошибок!\n");
        } catch (NonAuthorizedUserException e) {
            ResponseOutputer.append("Необходимо авторизоваться!\n");
        }
        return false;
    }
}
