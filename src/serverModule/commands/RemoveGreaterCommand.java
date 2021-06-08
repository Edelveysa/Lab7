package serverModule.commands;

import common.data.HumanBeing;
import common.exceptions.*;
import common.utility.HumanBeingLite;
import common.utility.User;
import serverModule.utility.CollectionManager;
import serverModule.utility.DatabaseCollectionManager;
import serverModule.utility.ResponseOutputer;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс RemoveGreaterCommand.
 * Команда "remove_greater {element}".
 */
public class RemoveGreaterCommand extends AbstractCommand{
    /** Поле менеджер коллекции */
    private CollectionManager collectionManager;
    /** Поле менеджер бд коллекции */
    private DatabaseCollectionManager databaseCollectionManager;

    public RemoveGreaterCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("remove_greater {element}", "удалить из коллекции все элементы, превышающие заданный");
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
            if (!argument.isEmpty() || objectArgument == null) throw new WrongAmountOfParametersException();
            if (collectionManager.collectionSize() == 0) throw new EmptyCollectionException();
            HumanBeingLite humanBeingLite = (HumanBeingLite) objectArgument;
            HumanBeing humanBeing = new HumanBeing(
                    collectionManager.generateId(),
                    humanBeingLite.getName(),
                    humanBeingLite.getCoordinates(),
                    LocalDateTime.now(),
                    humanBeingLite.getHealth(),
                    humanBeingLite.getHeartCount(),
                    humanBeingLite.getAchievements(),
                    humanBeingLite.getWeaponType(),
                    humanBeingLite.getChapter(),
                    user
            );
            List<HumanBeing> humanBeings = collectionManager.getGreater(humanBeing);
            for (HumanBeing humanBeing1 : humanBeings) {
                if (!humanBeing1.getOwner().equals(user)) continue;
                if (!databaseCollectionManager.checkHeroByIdAndUserId(humanBeing1.getId(), user)) throw new IllegalDatabaseEditException();
                databaseCollectionManager.deleteHeroById(humanBeing1.getId());
                collectionManager.removeByValue(humanBeing1);
            }
            ResponseOutputer.append("Герои успешно удалены!\n");
            return true;
        } catch (WrongAmountOfParametersException exception) {
            ResponseOutputer.append("У этой команды нет параметров!\n");
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
