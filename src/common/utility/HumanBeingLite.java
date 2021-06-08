package common.utility;

import common.data.Chapter;
import common.data.Coordinates;
import common.data.Weapon;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс HumanBeingLite.
 * Класс-клон для HumanBeing.
 */

public class HumanBeingLite implements Serializable {
    /** Поле имя */
    private String name;
    /** Объект координаты*/
    private Coordinates coordinates;
    /** Поле здоровье */
    private int health;
    /** Поле копичество сердец */
    private Integer heartCount;
    /** Поле достижение */
    private String achievements;
    /** Поле тип оружия */
    private Weapon weaponType;
    /** Поле пользователь */
    private Chapter chapter;

    public HumanBeingLite(String name, Coordinates coordinates, int health, Integer heartCount, String achievements, Weapon weaponType, Chapter chapter) {
        this.name = name;
        this.coordinates = coordinates;
        this.health = health;
        this.heartCount = heartCount;
        this.achievements = achievements;
        this.weaponType = weaponType;
        this.chapter = chapter;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HumanBeingLite that = (HumanBeingLite) o;
        return health == that.health && Objects.equals(name, that.name) && Objects.equals(coordinates, that.coordinates) && Objects.equals(heartCount, that.heartCount) && Objects.equals(achievements, that.achievements) && weaponType == that.weaponType && Objects.equals(chapter, that.chapter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, coordinates, health, heartCount, achievements, weaponType, chapter);
    }

    @Override
    public String toString() {
        return "HumanBeingLite{" +
                "name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", health=" + health +
                ", heartCount=" + heartCount +
                ", achievements='" + achievements + '\'' +
                ", weaponType=" + weaponType +
                ", chapter=" + chapter +
                '}';
    }
}
