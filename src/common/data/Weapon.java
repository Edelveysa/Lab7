package common.data;

import java.io.Serializable;

/**
 * Enum Weapon.
 * Тип оружия.
 */
public enum Weapon implements Serializable {
    HEAVY_BOLTGUN,
    FLAMER,
    GRENADE_LAUNCHER;

    public static String list() {
        String list = "";
        for (Weapon weapon : values()) {
            list += weapon.name() + ", ";
        }
        return list.substring(0, list.length() - 2);
    }
}
