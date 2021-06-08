package common.data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс Chapter.
 * Объекты дополнительных глав героя: его прошлой группы и нынешней.
 */
public class Chapter implements Serializable {
    /** Поле имя группы */
    private String name;
    /** Поле начальной группы */
    private String parentLegion;

    public Chapter(String name, String parentLegion) {
        this.name = name;
        this.parentLegion = parentLegion;
    }

    /**
     * Запрос имени текущей группы.
     * @return Имя группы.
     */
    public String getName() {
        return name;
    }

    /**
     * Запрос начальной группы героя.
     * @return Начальная группа.
     */
    public String getParentLegion() {
        return parentLegion;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "name='" + name + '\'' +
                ", parentLegion='" + parentLegion + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chapter chapter = (Chapter) o;
        return Objects.equals(name, chapter.name) &&
                Objects.equals(parentLegion, chapter.parentLegion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parentLegion);
    }
}
