package clientModule.utility;

import common.data.Chapter;
import common.data.Coordinates;
import common.data.Weapon;
import common.exceptions.NotDeclaredValueException;

import java.util.Scanner;

/**
 * Класс HumanBeingBuilder.
 * Класс-строитель объектов типа HumanBeing.
 */
public class HumanBeingBuilder {
    /** Поле сканер */
    private Scanner scanner;
    /** Поле режим файла */
    private boolean fileMode;

    public HumanBeingBuilder(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Установка сканера для вывода.
     * @param scanner - сканер для вывода.
     */
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Запрос сканера.
     * @return установленный сканер.
     */
    public Scanner getScanner() {
        return scanner;
    }

    /**
     * Установка режима работы для файла.
     */
    public void setFileMode() {
        this.fileMode = true;
    }

    /**
     * Установка режима работы для пользователя.
     * */
    public void setUserMode() {
        this.fileMode = false;
    }

    /**
     * Запрос имени героя.
     * @return name - имя героя.
     */
    public String askName() {
        String name;
        while (true) {
            try {
                System.out.println("Введите имя:");
                System.out.print(">");
                name = scanner.nextLine().trim();
                if (fileMode) System.out.println(name);
                if (name.equals("")) throw new NotDeclaredValueException();
                break;
            } catch (NotDeclaredValueException exception) {
                System.out.println("Значение поля не может быть пустым!");
            }
        }
        return name;
    }

    /**
     * Запрос координаты X.
     * @return x
     */
    public int askX() {
        String strX;
        int x;
        while (true) {
            try {
                System.out.println("Введите координату X > -666:");
                System.out.print(">");
                strX = scanner.nextLine().trim();
                x = Integer.parseInt(strX);
                if (fileMode) System.out.println(strX);
                if (x <= -666) throw new NotDeclaredValueException();
                break;
            } catch (NotDeclaredValueException exception) {
                System.out.println("Значение должно быть больше -666!");
            } catch (NumberFormatException exception) {
                System.out.println("Значение 'X' должно быть числом!");
            }
        }
        return x;
    }

    /**
     * Запрос координаты Y.
     * @return y
     */
    public Float askY() {
        String strY;
        float y;
        while (true) {
            try {
                System.out.println("Введите координату Y > -603:");
                System.out.print(">");
                strY = scanner.nextLine().trim();
                y = Float.parseFloat(strY);
                if (fileMode) System.out.println(strY);
                if (y <= -603) throw new NotDeclaredValueException();
                break;
            } catch (NotDeclaredValueException exception) {
                System.out.println("Значение должно быть больше -666!");
            } catch (NumberFormatException exception) {
                System.out.println("Значение 'У' должно быть числом!");
            }
        }
        return y;
    }

    /**
     * Запрос координат героя.
     * @return coordinates(х,у) - координат героя.
     */
    public Coordinates askCoordinates() {
        int x;
        float y;
        x = askX();
        y = askY();
        return new Coordinates(x, y);
    }

    /**
     * Запрос здоровья героя.
     * @return health - здоровье героя.
     */
    public int askHealth() {
        String strHealth;
        int health;
        while (true) {
            try {
                System.out.println("Введите здоровье:");
                System.out.print(">");
                strHealth = scanner.nextLine().trim();
                health = Integer.parseInt(strHealth);
                if (fileMode) System.out.println(strHealth);
                if (health <= 0) throw new NotDeclaredValueException();
                break;
            } catch (NotDeclaredValueException exception) {
                System.out.println("Здоровье должно быть больше нуля!");
            } catch (NumberFormatException exception) {
                System.out.println("Значение должно быть целым числом!");
            }
        }
        return health;
    }

    /**
     * Запрос количество сердец героя.
     * @return heartCount - количество сердец.
     */
    public Integer askHeartCount() {
        String strHeartCount;
        int heartCount;
        while (true) {
            try {
                System.out.println("Введите кол-во сердец:");
                System.out.print(">");
                strHeartCount = scanner.nextLine().trim();
                heartCount = Integer.parseInt(strHeartCount);
                if (fileMode) System.out.println(strHeartCount);
                if (heartCount < 0 || heartCount > 3) throw new NotDeclaredValueException();
                break;
            } catch (NotDeclaredValueException exception) {
                System.out.println("Количество сердец может быть от 1 до 3");
            } catch (NumberFormatException exception) {
                System.out.println("Значение должно быть целым числом!");
            }
        }
        return heartCount;
    }

    /**
     * Запрос достижений героя.
     * @return achieve - достижение героя.
     */
    public String askAchievements() {
        String achieve;
        while (true) {
            try {
                System.out.println("Введите достижение:");
                System.out.print(">");
                achieve = scanner.nextLine().trim();
                if (fileMode) System.out.println(achieve);
                if (achieve.equals("")) throw new NotDeclaredValueException();
                break;
            } catch (NotDeclaredValueException exception) {
                System.out.println("Значение поля не может быть пустым!");
            }
        }
        return achieve;
    }

    /**
     * Запрос тип оружия героя.
     * @return weapon - оружие героя.
     */
    public Weapon askWeapon() {
        String strWeapon;
        Weapon weapon;
        while (true) {
            try {
                System.out.println("Список доступного оружия: " + Weapon.list());
                System.out.println("Введите оружие");
                System.out.print(">");
                strWeapon = scanner.nextLine().trim();
                if (fileMode) System.out.println(strWeapon);
                weapon = Weapon.valueOf(strWeapon.toUpperCase());
                break;
            } catch (IllegalArgumentException exception) {
                System.out.println("Такого оружия нет!");
            }
        }
        return weapon;
    }

    /**
     * Запрос информации о герое.
     * @return chapter - глава героя.
     */
    public Chapter askChapter() {
        return new Chapter(askChapterName(), askChapterParentLegion());
    }

    /**
     * Запрос имени главы.
     * @return chapter - имя главы.
     */

    public String askChapterName() {
        String name;
        while (true) {
            try {
                System.out.println("Введите имя главы:");
                System.out.print(">");
                name = scanner.nextLine().trim();
                if (fileMode) System.out.println(name);
                if (name.equals("")) throw new NotDeclaredValueException();
                break;
            } catch (NotDeclaredValueException exception) {
                System.out.println("Значение поля не может быть пустым!");
            }
        }
        return name;
    }

    /**
     * Запрос начальной группы героя.
     * @return parentLegion - начальная группа.
     */
    public String askChapterParentLegion() {
        String parentLegion;
        while (true) {
            System.out.println("Введите имя начальной группы:");
            System.out.print(">");
            parentLegion = scanner.nextLine().trim();
            if (fileMode) System.out.println(parentLegion);
            if (parentLegion.equals("")) parentLegion = null;
            break;
        }
        return parentLegion;
    }

    /**
     * Запрос на изменения полей.
     * @param ask - заданный вопрос
     * @return подтверждение\отказ.
     */
    public boolean askAboutChangingField(String ask) {
        String res = ask + " (+/-):";
        String answer;
        while (true) {
            try {
                System.out.println(res);
                System.out.print(">");
                answer = scanner.nextLine().trim();
                if (fileMode) System.out.println(answer);
                if (!answer.equals("+") && !answer.equals("-")) throw new NotDeclaredValueException();
                break;
            } catch (NotDeclaredValueException exception) {
                System.out.println("Ответ должен быть представлен знаками '+' или '-'!");
            }
        }
        return answer.equals("+");
    }
}
