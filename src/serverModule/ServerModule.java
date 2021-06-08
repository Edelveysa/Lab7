package serverModule;

import serverModule.commands.*;
import serverModule.utility.*;

/**
 * Класс ClientModule.
 * Точка входа клиента.
 */
public class ServerModule {
    public static final int PORT = 2222;

    public static void main(String[] args) {
        DatabaseManager databaseManager = new DatabaseManager();
        DatabaseUserManager databaseUserManager = new DatabaseUserManager(databaseManager);
        DatabaseCollectionManager databaseCollectionManager = new DatabaseCollectionManager(databaseManager, databaseUserManager);
        CollectionManager collectionManager = new CollectionManager(databaseCollectionManager);
        CommandManager commandManager = new CommandManager(new HelpCommand(),
                new InfoCommand(collectionManager),
                new ShowCommand(collectionManager),
                new InsertCommand(collectionManager, databaseCollectionManager),
                new UpdateCommand(collectionManager, databaseCollectionManager),
                new RemoveKeyCommand(collectionManager, databaseCollectionManager),
                new ClearCommand(collectionManager, databaseCollectionManager),
                new ExecuteScriptCommand(),
                new ExitCommand(),
                new RemoveGreaterCommand(collectionManager, databaseCollectionManager),
                new HistoryCommand(),
                new RemoveLowerKeyCommand(collectionManager, databaseCollectionManager),
                new RemoveAllByWeaponTypeCommand(collectionManager, databaseCollectionManager),
                new SumOfHealthCommand(collectionManager),
                new AverageOfHeartCountCommand(collectionManager),
                new SignUpCommand(databaseUserManager),
                new SignInCommand(databaseUserManager),
                new LogOutCommand(databaseUserManager));
        RequestManager requestManager = new RequestManager(commandManager);
        Server server = new Server(PORT, requestManager);
        server.run();
        databaseManager.closeConnection();
    }
}
