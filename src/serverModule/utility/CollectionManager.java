package serverModule.utility;

import common.data.HumanBeing;
import common.data.Weapon;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс CollectionManager.
 * Работа с коллекцией.
 */
public class CollectionManager {
    /** Поле коллекция */
    private TreeMap<Integer, HumanBeing> humans = new TreeMap<>();
    /** Поле дата инициализации */
    private LocalDateTime lastInitTime;
    /** Поле менеджер бд коллекции */
    private DatabaseCollectionManager databaseCollectionManager;

    public CollectionManager(DatabaseCollectionManager databaseCollectionManager) {
        this.databaseCollectionManager = databaseCollectionManager;
        loadCollection(null);
    }

    /**
     * Запрос коллекции.
     * @return humans - коллекция.
     */
    public TreeMap<Integer, HumanBeing> getCollection() {
        return humans;
    }

    /**
     * Добавить элемент в коллекцию по ключу.
     * @param key
     * @param human
     */
    public void addToCollection(int key, HumanBeing human) {
        humans.put(key, human);
    }

    /**
     * Генерация нового ID.
     * @return id
     */
    public int generateId() {
        if (humans.isEmpty()) return 1;
        int lastId = 0;
        for (HumanBeing human : humans.values()) {
            lastId = Math.max(lastId, human.getId());
        }
        return lastId + 1;
    }


    /**
     * Запрос типа коллекции.
     * @return тип коллекции
     */
    public String collectionType() {
        return humans.getClass().getName();
    }

    /**
     * Очистка коллекции.
     */
    public void clearCollection() {
        humans.clear();
    }

    /**
     * Получение элементов, выше заданного.
     * @param humanToCompare - элемент для сравнения.
     * @return list - список элементов.
     */
    public List<HumanBeing> getGreater(HumanBeing humanToCompare) {
        return humans.values().stream().filter(human -> human.compareTo(humanToCompare) > 0).collect(Collectors.toList());
    }

    /**
     * Получение ключей, которые меньше заданного.
     * @param keyToCompare - ключ для сравнения.
     * @return list - списко ключей
     */
    public List<HumanBeing> getLowerKey(int keyToCompare) {
        return humans.entrySet().stream().filter(entry -> entry.getKey() < keyToCompare).map(Map.Entry::getValue).collect(Collectors.toList());
    }

    /**
     * Получить все элементы с данным типом оружия.
     * @param weapon - тип оружия
     * @return list - список оружия
     */
    public List<HumanBeing> getAllByWeaponType(Weapon weapon) {
        return humans.values().stream().filter(humanBeing -> humanBeing.getWeaponType().equals(weapon)).collect(Collectors.toList());
    }

    /**
     * Удалить данные элементы из коллекции
     * @param humanBeing
     */
    public void removeByValue(HumanBeing humanBeing) {
        humans.entrySet().removeIf(entry -> entry.getValue().equals(humanBeing));
    }

    /**
     * Удаление элемента по ключу из коллекции.
     * @param key
     */
    public void removeFromCollection(int key) {
        humans.remove(key);
    }

    /**
     * Запрос о размере коллекции.
     * @return size - размер
     */
    public int collectionSize() {
        return humans.size();
    }

    /**
     * Получить элемент из коллекции по ключу.
     * @param key - ключ.
     * @return HumanBeing - элемент из коллекции.
     */
    public HumanBeing getFromCollection(int key) {
        return humans.get(key);
    }

    /**
     * Получить ключ по id
     * @param id
     * @return ключ героя
     */
    public Integer getKeyById(int id) {
        return humans.entrySet().stream().filter(entry -> entry.getValue().getId() == id).map(Map.Entry::getKey).findFirst().get();
    }

    /**
     * Получить сумму всего здоровья у героев.
     * @return Сумму здоровья героев.
     */
    public Integer getSumOfHealth() {
        return humans.values().stream().reduce(0, (sum, p) -> sum += p.getHealth(), Integer::sum);
    }

    /**
     * Получить среднее значение количества сердец.
     * @return среднее количество сердец
     */
    public double averageOfHeartCount() {
        double average = (double) humans.values().stream().reduce(0, (sum, p) -> sum += p.getHeartCount(), Integer::sum);
        if (average == 0) return 0;
        return average / humans.size();
    }

    /**
     * Получить последние дату и время инициализации коллекции.
     * @return время и дата инициализации
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * Получить все элементы из коллекции.
     */
    public String showCollection() {
        if (humans.isEmpty()) return "Коллекция пуста!";
        return humans.values().stream().reduce("", (sum, m) -> sum += m + "\n\n", (sum1, sum2) -> sum1 + sum2).trim();
    }

    /**
     * Загрузить коллекцию.
     * @param arg
     */
    public void loadCollection(String arg) {
        humans = databaseCollectionManager.getCollection();
        lastInitTime = LocalDateTime.now();
        System.out.println("Коллекция загружена.");
    }

    public void saveCollection() { }
}
