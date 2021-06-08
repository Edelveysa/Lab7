package serverModule.utility;

import common.data.Chapter;
import common.data.Coordinates;
import common.data.HumanBeing;
import common.data.Weapon;
import common.exceptions.DatabaseManagerException;
import common.utility.HumanBeingLite;
import common.utility.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.TreeMap;
/**
 * Класс DatabaseCollectionManager.
 * Работа с таблицами(элементами) бд.
 */
public class DatabaseCollectionManager {
    /** Поля для работы с бд */
    private final String SELECT_ALL_HERO = "SELECT * FROM " + DatabaseManager.HERO_TABLE;
    private final String SELECT_HERO_BY_ID = SELECT_ALL_HERO + " WHERE " +
            DatabaseManager.HERO_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_HERO_BY_ID_AND_USER_ID = SELECT_HERO_BY_ID + " AND " +
            DatabaseManager.HERO_TABLE_USER_ID_COLUMN + " = ?";
    private final String INSERT_HERO = "INSERT INTO " +
            DatabaseManager.HERO_TABLE + " (" +
            DatabaseManager.HERO_TABLE_KEY_COLUMN + ", " +
            DatabaseManager.HERO_TABLE_NAME_COLUMN + ", " +
            DatabaseManager.HERO_TABLE_COORDINATES_ID_COLUMN + ", " +
            DatabaseManager.HERO_TABLE_CREATION_DATE_COLUMN + ", " +
            DatabaseManager.HERO_TABLE_HEALTH_COLUMN + ", " +
            DatabaseManager.HERO_TABLE_HEART_COUNT_COLUMN + ", " +
            DatabaseManager.HERO_TABLE_ACHIEVEMENTS_COLUMN + ", " +
            DatabaseManager.HERO_TABLE_WEAPON_TYPE_COLUMN + ", " +
            DatabaseManager.HERO_TABLE_CHAPTER_ID_COLUMN + ", " +
            DatabaseManager.HERO_TABLE_USER_ID_COLUMN + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String DELETE_HERO_BY_ID = "DELETE FROM " + DatabaseManager.HERO_TABLE +
            " WHERE " + DatabaseManager.HERO_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_HERO_NAME_BY_ID = "UPDATE " + DatabaseManager.HERO_TABLE + " SET " +
            DatabaseManager.HERO_TABLE_NAME_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.HERO_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_HERO_HEALTH_BY_ID = "UPDATE " + DatabaseManager.HERO_TABLE + " SET " +
            DatabaseManager.HERO_TABLE_HEALTH_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.HERO_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_HERO_HEART_COUNT_BY_ID = "UPDATE " + DatabaseManager.HERO_TABLE + " SET " +
            DatabaseManager.HERO_TABLE_HEART_COUNT_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.HERO_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_HERO_ACHIEVEMENTS_BY_ID = "UPDATE " + DatabaseManager.HERO_TABLE + " SET " +
            DatabaseManager.HERO_TABLE_ACHIEVEMENTS_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.HERO_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_HERO_WEAPON_TYPE_BY_ID = "UPDATE " + DatabaseManager.HERO_TABLE + " SET " +
            DatabaseManager.HERO_TABLE_WEAPON_TYPE_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.HERO_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_ALL_COORDINATES = "SELECT * FROM " + DatabaseManager.COORDINATES_TABLE;
    private final String SELECT_COORDINATES_BY_ID = SELECT_ALL_COORDINATES + " WHERE " + DatabaseManager.COORDINATES_TABLE_ID_COLUMN + " =?";
    private final String DELETE_COORDINATES_BY_ID = "DELETE FROM " + DatabaseManager.COORDINATES_TABLE +
            " WHERE " + DatabaseManager.COORDINATES_TABLE_ID_COLUMN + " = ?";
    private final String INSERT_COORDINATES = "INSERT INTO " +
            DatabaseManager.COORDINATES_TABLE + " (" +
            DatabaseManager.COORDINATES_TABLE_X_COLUMN + ", " +
            DatabaseManager.COORDINATES_TABLE_Y_COLUMN + ") VALUES (?, ?)";
    private final String UPDATE_COORDINATES_BY_ID = "UPDATE " + DatabaseManager.COORDINATES_TABLE + " SET " +
            DatabaseManager.COORDINATES_TABLE_X_COLUMN + " = ?, " +
            DatabaseManager.COORDINATES_TABLE_Y_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.COORDINATES_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_ALL_CHAPTERS = "SELECT * FROM " + DatabaseManager.CHAPTER_TABLE;
    private final String SELECT_CHAPTER_BY_ID = SELECT_ALL_CHAPTERS + " WHERE " + DatabaseManager.CHAPTER_TABLE_ID_COLUMN + " =?";
    private final String DELETE_CHAPTER_BY_ID = "DELETE FROM " + DatabaseManager.CHAPTER_TABLE +
            " WHERE " + DatabaseManager.CHAPTER_TABLE_ID_COLUMN + " = ?";
    private final String INSERT_CHAPTER = "INSERT INTO " +
            DatabaseManager.CHAPTER_TABLE + " (" +
            DatabaseManager.CHAPTER_TABLE_NAME_COLUMN + ", " +
            DatabaseManager.CHAPTER_TABLE_PARENT_LEGION_COLUMN + ") VALUES (?, ?)";
    private final String UPDATE_CHAPTER_BY_ID = "UPDATE " + DatabaseManager.CHAPTER_TABLE + " SET " +
            DatabaseManager.CHAPTER_TABLE_NAME_COLUMN + " = ?, " +
            DatabaseManager.CHAPTER_TABLE_PARENT_LEGION_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.CHAPTER_TABLE_ID_COLUMN + " = ?";
    private DatabaseManager databaseManager;
    private DatabaseUserManager databaseUserManager;

    public DatabaseCollectionManager(DatabaseManager databaseManager, DatabaseUserManager databaseUserManager) {
        this.databaseManager = databaseManager;
        this.databaseUserManager = databaseUserManager;
    }

    private HumanBeing returnHero(ResultSet resultSet, int id) throws SQLException{
        String name = resultSet.getString(DatabaseManager.HERO_TABLE_NAME_COLUMN);
        Coordinates coordinates = getCoordinatesByID(resultSet.getInt(DatabaseManager.HERO_TABLE_COORDINATES_ID_COLUMN));
        LocalDateTime creationDate = resultSet.getTimestamp(DatabaseManager.HERO_TABLE_CREATION_DATE_COLUMN).toLocalDateTime();
        int health = resultSet.getInt(DatabaseManager.HERO_TABLE_HEALTH_COLUMN);
        Integer heartCount = resultSet.getInt(DatabaseManager.HERO_TABLE_HEART_COUNT_COLUMN);
        String achieve = resultSet.getString(DatabaseManager.HERO_TABLE_ACHIEVEMENTS_COLUMN);
        Weapon weaponType = Weapon.valueOf(resultSet.getString(DatabaseManager.HERO_TABLE_WEAPON_TYPE_COLUMN));
        Chapter chapter = getChapterByID(resultSet.getInt(DatabaseManager.HERO_TABLE_CHAPTER_ID_COLUMN));
        User owner = databaseUserManager.getUserById(resultSet.getInt(DatabaseManager.HERO_TABLE_USER_ID_COLUMN));
        return new HumanBeing(id, name, coordinates, creationDate, health, heartCount, achieve, weaponType, chapter, owner );
    }

    public TreeMap<Integer, HumanBeing> getCollection() {
        TreeMap<Integer, HumanBeing> humans = new TreeMap<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_ALL_HERO, false);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(DatabaseManager.HERO_TABLE_ID_COLUMN);
                int key = resultSet.getInt(DatabaseManager.HERO_TABLE_KEY_COLUMN);
                humans.put(key, returnHero(resultSet, id));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return humans;
    }

    private Coordinates getCoordinatesByID(int id) throws SQLException {
        Coordinates coordinates;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_COORDINATES_BY_ID, false);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                coordinates = new Coordinates(
                        resultSet.getDouble(DatabaseManager.COORDINATES_TABLE_X_COLUMN),
                        resultSet.getFloat(DatabaseManager.COORDINATES_TABLE_Y_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_COORDINATES_BY_ID!");
            throw new SQLException(e);
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return coordinates;
    }

    private Chapter getChapterByID(int id) throws SQLException {
        Chapter chapter;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_CHAPTER_BY_ID, false);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                chapter = new Chapter(
                        resultSet.getString(DatabaseManager.CHAPTER_TABLE_NAME_COLUMN),
                        resultSet.getString(DatabaseManager.CHAPTER_TABLE_PARENT_LEGION_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_CHAPTER_BY_ID!");
            throw new SQLException(e);
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return chapter;
    }

    private int getChapterIdByHeroID(int heroID) throws SQLException {
        int chapterID;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_HERO_BY_ID, false);
            preparedStatement.setInt(1, heroID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                chapterID = resultSet.getInt(DatabaseManager.HERO_TABLE_CHAPTER_ID_COLUMN);
            } else throw new SQLException();
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_BY_ID!");
            throw new SQLException(e);
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return chapterID;
    }

    private int getCoordinatesIdByHeroID(int heroID) throws SQLException {
        int coordinatesID;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_HERO_BY_ID, false);
            preparedStatement.setInt(1, heroID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                coordinatesID = resultSet.getInt(DatabaseManager.HERO_TABLE_COORDINATES_ID_COLUMN);
            } else throw new SQLException();
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_BY_ID!");
            throw new SQLException(e);
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return coordinatesID;
    }

    public HumanBeing insertHero(HumanBeingLite humanBeingLite, User user, int key) throws DatabaseManagerException {
        HumanBeing humanBeing;
        PreparedStatement insertHuman = null;
        PreparedStatement insertCoordinates = null;
        PreparedStatement insertChapter = null;
        try {
            databaseManager.setCommit();
            databaseManager.setSavepoint();

            LocalDateTime localDateTime = LocalDateTime.now();

            insertChapter = databaseManager.doPreparedStatement(INSERT_CHAPTER, true);
            insertChapter.setString(1, humanBeingLite.getChapter().getName());
            insertChapter.setString(2, humanBeingLite.getChapter().getParentLegion());
            if (insertChapter.executeUpdate() == 0) throw new SQLException();
            ResultSet resultSetChapter = insertChapter.getGeneratedKeys();
            int chapterID;
            if (resultSetChapter.next()) chapterID = resultSetChapter.getInt(1);
            else throw new SQLException();

            insertCoordinates = databaseManager.doPreparedStatement(INSERT_COORDINATES, true);
            insertCoordinates.setDouble(1, humanBeingLite.getCoordinates().getX());
            insertCoordinates.setFloat(2, humanBeingLite.getCoordinates().getY());
            if (insertCoordinates.executeUpdate() == 0) throw new SQLException();
            ResultSet resultSetCoordinates = insertCoordinates.getGeneratedKeys();
            int coordinatesID;
            if (resultSetCoordinates.next()) coordinatesID = resultSetCoordinates.getInt(1);
            else throw new SQLException();

            insertHuman = databaseManager.doPreparedStatement(INSERT_HERO, true);
            insertHuman.setInt(1, key);
            insertHuman.setString(2, humanBeingLite.getName());
            insertHuman.setInt(3, coordinatesID);
            insertHuman.setTimestamp(4, Timestamp.valueOf(localDateTime));
            insertHuman.setInt(5, humanBeingLite.getHealth());
            insertHuman.setInt(6, humanBeingLite.getHeartCount());
            insertHuman.setString(7, humanBeingLite.getAchievements());
            insertHuman.setString(8, humanBeingLite.getWeaponType().toString());
            insertHuman.setInt(9, chapterID);
            insertHuman.setInt(10, databaseUserManager.getUserIdByUsername(user));
            if (insertHuman.executeUpdate() == 0) throw new SQLException();
            ResultSet resultSet = insertHuman.getGeneratedKeys();
            int heroID;
            if (resultSet.next()) heroID = resultSet.getInt(1);
            else throw new SQLException();
            humanBeing = new HumanBeing(
                    heroID,
                    humanBeingLite.getName(),
                    humanBeingLite.getCoordinates(),
                    localDateTime,
                    humanBeingLite.getHealth(),
                    humanBeingLite.getHeartCount(),
                    humanBeingLite.getAchievements(),
                    humanBeingLite.getWeaponType(),
                    humanBeingLite.getChapter(),
                    user
            );
            databaseManager.commit();
            return humanBeing;
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при добавлении нового объекта в БД!");
            exception.printStackTrace();
            databaseManager.rollback();
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(insertChapter);
            databaseManager.closePreparedStatement(insertCoordinates);
            databaseManager.closePreparedStatement(insertHuman);
            databaseManager.setAutoCommit();
        }
    }

    public void updateHeroByID(int heroID, HumanBeingLite beingLite) throws DatabaseManagerException {
        PreparedStatement updateName = null;
        PreparedStatement updateCoordinates = null;
        PreparedStatement updateHealth = null;
        PreparedStatement updateHeartCount = null;
        PreparedStatement updateAchievements = null;
        PreparedStatement updateWeaponType = null;
        PreparedStatement updateChapter = null;
        try {
            databaseManager.setCommit();
            databaseManager.setSavepoint();

            updateName = databaseManager.doPreparedStatement(UPDATE_HERO_NAME_BY_ID, false);
            updateCoordinates = databaseManager.doPreparedStatement(UPDATE_COORDINATES_BY_ID, false);
            updateHealth = databaseManager.doPreparedStatement(UPDATE_HERO_HEALTH_BY_ID, false);
            updateHeartCount = databaseManager.doPreparedStatement(UPDATE_HERO_HEART_COUNT_BY_ID, false);
            updateAchievements = databaseManager.doPreparedStatement(UPDATE_HERO_ACHIEVEMENTS_BY_ID, false);
            updateWeaponType = databaseManager.doPreparedStatement(UPDATE_HERO_WEAPON_TYPE_BY_ID, false);
            updateChapter = databaseManager.doPreparedStatement(UPDATE_CHAPTER_BY_ID, false);

            if (beingLite.getName() != null) {
                updateName.setString(1, beingLite.getName());
                updateName.setInt(2, heroID);
                if (updateName.executeUpdate() == 0) throw new SQLException();
            }
            if (beingLite.getCoordinates() != null) {
                updateCoordinates.setDouble(1, beingLite.getCoordinates().getX());
                updateCoordinates.setFloat(2, beingLite.getCoordinates().getY());
                updateCoordinates.setInt(3, getCoordinatesIdByHeroID(heroID));
                if (updateCoordinates.executeUpdate() == 0) throw new SQLException();
            }
            if (beingLite.getHealth() != -1) {
                updateHealth.setInt(1, beingLite.getHealth());
                updateHealth.setInt(2, heroID);
                if (updateHealth.executeUpdate() == 0) throw new SQLException();
            }
            if (beingLite.getHeartCount() != -1) {
                updateHeartCount.setInt(1, beingLite.getHeartCount());
                updateHeartCount.setInt(2, heroID);
                if (updateHeartCount.executeUpdate() == 0) throw new SQLException();
            }
            if (beingLite.getAchievements() != null) {
                updateAchievements.setString(1, beingLite.getAchievements());
                updateAchievements.setInt(2, heroID);
                if (updateAchievements.executeUpdate() == 0) throw new SQLException();
            }
            if (beingLite.getWeaponType() != null) {
                updateWeaponType.setString(1, beingLite.getWeaponType().toString());
                updateWeaponType.setInt(2, heroID);
                if (updateWeaponType.executeUpdate() == 0) throw new SQLException();
            }
            if (beingLite.getChapter() != null) {
                updateChapter.setString(1, beingLite.getChapter().getName());
                updateChapter.setString(2, beingLite.getChapter().getParentLegion());
                updateChapter.setInt(3, getChapterIdByHeroID(heroID));
                if (updateChapter.executeUpdate() == 0) throw new SQLException();
            }
            databaseManager.commit();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при выполнении группы запросов на обновление объекта!");
            databaseManager.rollback();
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(updateName);
            databaseManager.closePreparedStatement(updateCoordinates);
            databaseManager.closePreparedStatement(updateHealth);
            databaseManager.closePreparedStatement(updateHeartCount);
            databaseManager.closePreparedStatement(updateAchievements);
            databaseManager.closePreparedStatement(updateWeaponType);
            databaseManager.closePreparedStatement(updateChapter);
            databaseManager.setAutoCommit();
        }
    }

    public boolean checkHeroByIdAndUserId(int heroID, User user) throws DatabaseManagerException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_HERO_BY_ID_AND_USER_ID, false);
            preparedStatement.setInt(1, heroID);
            preparedStatement.setInt(2, databaseUserManager.getUserIdByUsername(user));
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_BY_ID_AND_USER_ID!");
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
    }

    public void deleteHeroById(int heroID) throws DatabaseManagerException {
        PreparedStatement deleteHero = null;
        PreparedStatement deleteCoordinates = null;
        PreparedStatement deleteChapter = null;
        try {
            int coordinatesID = getCoordinatesIdByHeroID(heroID);
            int chapterID = getChapterIdByHeroID(heroID);
            deleteHero = databaseManager.doPreparedStatement(DELETE_HERO_BY_ID, false);
            deleteHero.setInt(1, heroID);
            if (deleteHero.executeUpdate() == 0) throw new SQLException();
            deleteCoordinates = databaseManager.doPreparedStatement(DELETE_COORDINATES_BY_ID, false);
            deleteCoordinates.setInt(1, coordinatesID);
            if (deleteCoordinates.executeUpdate() == 0) throw new SQLException();
            deleteChapter = databaseManager.doPreparedStatement(DELETE_CHAPTER_BY_ID, false);
            deleteChapter.setInt(1, chapterID);
            if (deleteChapter.executeUpdate() == 0) throw new SQLException();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при выполнении запроса DELETE_BY_ID!");
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(deleteHero);
            databaseManager.closePreparedStatement(deleteCoordinates);
            databaseManager.closePreparedStatement(deleteChapter);
        }
    }

    public void clearCollection() throws DatabaseManagerException{
        TreeMap<Integer, HumanBeing> humans = getCollection();
        for (HumanBeing being : humans.values()) {
            deleteHeroById(being.getId());
        }
    }

}
