package serverModule.commands;

import common.data.HumanBeing;
import common.exceptions.*;
import common.utility.User;
import serverModule.utility.CollectionManager;
import serverModule.utility.DatabaseCollectionManager;
import serverModule.utility.ResponseOutputer;

/**
 * Класс ClearCommand.
 * Команда "clear".
 */
public class ClearCommand extends AbstractCommand{
    /** Поле менеджер коллекции */
    private CollectionManager collectionManager;
    /** Поле менеджер бд коллекции */
    private DatabaseCollectionManager databaseCollectionManager;

    public ClearCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("clear", "очистить коллекцию");
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
            if(!argument.isEmpty() || objectArgument != null) throw new WrongAmountOfParametersException();
            for (HumanBeing human : collectionManager.getCollection().values()) {
                if (!human.getOwner().equals(user)) ResponseOutputer.append("Мы нашли чужой. Мы удаляем лишь ваши объекты.\n");
                if (databaseCollectionManager.checkHeroByIdAndUserId(human.getId(), user)) {
                    databaseCollectionManager.deleteHeroById(human.getId());
                    collectionManager.removeByValue(human);
                }
            }
//            databaseCollectionManager.clearCollection();
//            collectionManager.clearCollection();
            ResponseOutputer.append("Коллекция успешно очищена!\n");
            return true;
        } catch (WrongAmountOfParametersException exception) {
            ResponseOutputer.append("У этой команды нет параметров!\n");
        }  catch (DatabaseManagerException exception) {
            ResponseOutputer.append("Произошла ошибка при обращении к базе данных!\n");
        } catch (NonAuthorizedUserException e) {
            ResponseOutputer.append("Необходимо авторизоваться!\n");
        }
        return false;
    }
}
