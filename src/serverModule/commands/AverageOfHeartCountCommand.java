package serverModule.commands;

import common.exceptions.EmptyCollectionException;
import common.exceptions.NonAuthorizedUserException;
import common.exceptions.WrongAmountOfParametersException;
import common.utility.User;
import serverModule.utility.CollectionManager;
import serverModule.utility.ResponseOutputer;

/**
 * Класс AverageOfHeartCountCommand.
 * Команда "average_of_heart_count".
 */
public class AverageOfHeartCountCommand extends AbstractCommand{
    /** Поле менеджер коллекции */
    private CollectionManager collectionManager;

    public AverageOfHeartCountCommand(CollectionManager collectionManager) {
        super("average_of_heart_count", "вывести среднее значение поля heartCount для всех элементов коллекции");
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
            if (user == null) throw new NonAuthorizedUserException();
            if (!argument.isEmpty() || objectArgument != null) throw new WrongAmountOfParametersException();
            double average_of_heart_count = collectionManager.averageOfHeartCount();
            if (average_of_heart_count == 0) throw new EmptyCollectionException();
            ResponseOutputer.append("Среднее значение поля количества сердец всех героев: " + average_of_heart_count + "\n");
            return true;
        } catch (WrongAmountOfParametersException exception) {
            ResponseOutputer.append("У этой команды нет параметров!\n");
        } catch (EmptyCollectionException exception) {
            ResponseOutputer.append("Коллекция пуста!\n");
        } catch (NonAuthorizedUserException e) {
            ResponseOutputer.append("Необходимо авторизоваться!\n");
        }
        return false;
    }
}
