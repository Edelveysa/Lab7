package serverModule.commands;

import common.data.Chapter;
import common.data.Coordinates;
import common.data.HumanBeing;
import common.data.Weapon;
import common.exceptions.*;
import common.utility.HumanBeingLite;
import common.utility.User;
import serverModule.utility.CollectionManager;
import serverModule.utility.DatabaseCollectionManager;
import serverModule.utility.ResponseOutputer;

import java.time.LocalDateTime;

/**
 * Класс UpdateCommand.
 * Команда "update id {element}".
 */
public class UpdateCommand extends AbstractCommand{
    /** Поле менеджер коллекции */
    private CollectionManager collectionManager;
    /** Поле менеджер бд коллекции */
    private DatabaseCollectionManager databaseCollectionManager;

    public UpdateCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("update id {element}", "обновить значение элемента коллекции по id");
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
            if (argument.isEmpty() || objectArgument == null) throw new WrongAmountOfParametersException();
            if (collectionManager.collectionSize() == 0) throw new EmptyCollectionException();
            int id = Integer.parseInt(argument);
            int key = collectionManager.getKeyById(id);
            HumanBeing oldHuman = collectionManager.getFromCollection(key);
            if (oldHuman == null) throw new NotFoundMarineException();
            if (!oldHuman.getOwner().equals(user)) throw new PermissionDeniedException();
            if (!databaseCollectionManager.checkHeroByIdAndUserId(oldHuman.getId(), user)) throw new IllegalDatabaseEditException();
            HumanBeingLite humanBeingLite = (HumanBeingLite) objectArgument;
            databaseCollectionManager.updateHeroByID(id, humanBeingLite);
            String name = humanBeingLite.getName() == null ? oldHuman.getName() : humanBeingLite.getName();
            Coordinates coordinates = humanBeingLite.getCoordinates() == null ? oldHuman.getCoordinates() : humanBeingLite.getCoordinates();
            LocalDateTime creationDate = oldHuman.getCreationDate();
            int health = humanBeingLite.getHealth() == -1 ? oldHuman.getHealth() : humanBeingLite.getHealth();
            Integer heartCount = humanBeingLite.getHeartCount() == -1 ? oldHuman.getHeartCount() : humanBeingLite.getHeartCount();
            String achievements = humanBeingLite.getAchievements() == null ? oldHuman.getAchievements() : humanBeingLite.getAchievements();
            Weapon weapon = humanBeingLite.getWeaponType() == null ? oldHuman.getWeaponType() : humanBeingLite.getWeaponType();
            Chapter chapter = humanBeingLite.getChapter() == null ? oldHuman.getChapter() : humanBeingLite.getChapter();
            collectionManager.removeFromCollection(key);
            collectionManager.addToCollection(key, new HumanBeing(
                    id,
                    name,
                    coordinates,
                    creationDate,
                    health,
                    heartCount,
                    achievements,
                    weapon,
                    chapter,
                    user
            ));
            ResponseOutputer.append("Успешно изменено!\n");
            return true;
        } catch (WrongAmountOfParametersException exception) {
            ResponseOutputer.append("Вместе с этой командой должен быть передан параметр! Наберит 'help' для справки!\n");
        } catch (EmptyCollectionException exception) {
            ResponseOutputer.append("Коллекция пуста!\n");
        } catch (NotFoundMarineException e) {
            ResponseOutputer.append("HumanBeing с таким id в коллекции нет!\n");
        } catch (PermissionDeniedException e) {
            ResponseOutputer.append("Принадлежащие другим пользователям объекты доступны только для чтения!\n");
        } catch (DatabaseManagerException e) {
            ResponseOutputer.append("Произошла ошибка при обращении к базе данных!\n");
        } catch (IllegalDatabaseEditException e) {
            ResponseOutputer.append("Произошло нелегальное изменение объекта в базе данных!\n");
            ResponseOutputer.append("Перезапустите клиент для избежания ошибок!\n");
        } catch (NonAuthorizedUserException e) {
            ResponseOutputer.append("Необходимо авторизоваться!\n");
        }
        return false;
    }
}
