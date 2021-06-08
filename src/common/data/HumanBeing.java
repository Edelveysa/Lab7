package common.data;

import common.utility.User;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс HumanBeing.
 * Объекты, хранимые в коллекции.
 */
public class HumanBeing implements Comparable<HumanBeing>{
    /** Поле id*/
    private int id;
    /** Поле имя */
    private String name;
    /** Объект координаты*/
    private Coordinates coordinates;
    /** Объект дата создания*/
    private LocalDateTime creationDate;
    /** Поле здоровье */
    private int health;
    /** Поле копичество сердец */
    private Integer heartCount;
    /** Поле достижение */
    private String achievements;
    /** Поле тип оружия */
    private Weapon weaponType;
    /** Поле глава */
    private Chapter chapter;
    /** Поле пользователь */
    private User owner;

    public HumanBeing(int id, String name, Coordinates coordinates, LocalDateTime creationDate, int health, Integer heartCount, String achievements, Weapon weaponType, Chapter chapter, User owner) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.health = health;
        this.heartCount = heartCount;
        this.achievements = achievements;
        this.weaponType = weaponType;
        this.chapter = chapter;
        this.owner = owner;
    }

    /**
     * Запрос id героя.
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Запрос имени героя.
     * @return name - имя героя.
     */
    public String getName() {
        return name;
    }

    /**
     * Запрос координат героя.
     * @return coordinates - координаты.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Запрос даты создания коллекции.
     * @return creationDate - дата создания.
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Запрос здоровья героя.
     * @return health - здоровье героя.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Запрос количества сердец героя.
     * @return heartCount - количество сердец.
     */
    public Integer getHeartCount() {
        return heartCount;
    }

    /**
     * Запрос достижений героя.
     * @return achievements - достижения героев.
     */
    public String getAchievements() {
        return achievements;
    }

    /**
     * Запрос типа оружия героя.
     * @return weaponType - тип оружия.
     */
    public Weapon getWeaponType() {
        return weaponType;
    }

    /**
     * Запрос главы героя.
     * @return chapter - глава героя.
     */
    public Chapter getChapter() {
        return chapter;
    }

    /**
     * Запрос пользователя.
     * @return owner - пользователь
     */
    public User getOwner() {
        return owner;
    }

    @Override
    public int compareTo(HumanBeing o) {
        if (this.health == o.health) {
            return Integer.compare(this.heartCount, o.heartCount);
        }
        return Integer.compare(this.health, o.health);
    }

    @Override
    public String toString() {
        String info = "";
        info += "Герой №" + id;
        info += " (добавлен " + creationDate.toLocalDate() + " " + creationDate.toLocalTime() + ")";
        info += "\n Имя: " + name;
        info += "\n Местоположение: " + coordinates;
        info += "\n Здоровье: " + health;
        info += "\n Число сердец: " + heartCount;
        info += "\n Дальнее оружие: " + weaponType;
        info += "\n Достжения: " + achievements;
        info += "\n Орден: " + chapter;
        return info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HumanBeing that = (HumanBeing) o;
        return id == that.id && health == that.health && Objects.equals(name, that.name) && Objects.equals(coordinates, that.coordinates) && Objects.equals(creationDate, that.creationDate) && Objects.equals(heartCount, that.heartCount) && Objects.equals(achievements, that.achievements) && weaponType == that.weaponType && Objects.equals(chapter, that.chapter) && Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, health, heartCount, achievements, weaponType, chapter, owner);
    }
}
