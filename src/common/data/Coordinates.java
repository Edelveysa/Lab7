package common.data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс Coordinates.
 * Отвечает за координаты героя.
 */
public class Coordinates implements Serializable {
    /** Поле координата Х */
    private double x;
    /** Поле координата У */
    private Float y;

    public Coordinates(double x, Float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Запрос координаты Х
     * @return X
     */
    public double getX() {
        return x;
    }

    /**
     * Запрос координаты Y
     * @return Y
     */
    public Float getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Double.compare(that.x, x) == 0 &&
                Objects.equals(y, that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
