package my.homework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import org.apache.commons.math3.*;

/**
 * Класс игры
 */
public class Game {
    private int totalDoors;
    int selectedDoor;

    private ArrayList<Door> doors;
    private Random random;
    private Scanner scanner;
    private HashMap<Integer, Integer> log;

    public Game(int numberOfDoors) {
        this.totalDoors = numberOfDoors;

        init();
        start();
    }

    /**
     * Инициализируем игру
     */
    private void init() {
        this.random = new Random();
        this.doors = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.log = new HashMap<>();

        int positionCar = random.nextInt(totalDoors) + 1;
        for (int i=1; i<=totalDoors; i++) {
            if (i == positionCar) {
                Car car = new Car();
                doors.add(new Door<>(i, car));
            } else {
               Goat goat = new Goat();
               doors.add(new Door<>(i, goat));
            }
        }
    }

    /**
     * Запрашиваем ввод пользователя
     * @param placeholder Сообщение, подсказка
     * @return номер двери
     */
    private int inputDoor(String placeholder) {
        int door;

        System.out.printf(placeholder);
        while (true) {
            try {
                door = scanner.nextInt() - 1;
                if (door >= 0 && door < doors.size()) {
                    return door;
                }
            } catch (Exception e) {
                System.out.println("Ошибка ввода! Введите корректное значение.");
                scanner.nextLine();
            }
        }
    }

    /**
     * Запускает игру
     */
    private void start() {
        while (doors.size() > 2) {
            selectedDoor = inputDoor(String.format("Выберите дверь от 1 до %d: ", doors.size()));
            writeStep();
            System.out.printf("За дверью %d находится коза.\n", openLosingDoor());
            System.out.println();
            System.out.printf("Вы выбрали дверь %d.\n", getUserDoor());
        }
        finish();
    }

    /**
     * Открывает проигрышную дверь
     * @return порядковый номер проигрышной двери (начиная с 1)
     */
    private int openLosingDoor() {
        int openTheDoor;
        while (true) {
            openTheDoor = new Random().nextInt(doors.size());
            if (openTheDoor != selectedDoor && (doors.get(openTheDoor).getContent() instanceof Goat)) {
                removeDoor(openTheDoor);
                return openTheDoor + 1;
            }
        }
    }

    /**
     * Удаляет дверь из списка
     * @param index индекс двери для удаления из списка
     */
    private void removeDoor(int index) {
        if (selectedDoor > index) {
            selectedDoor--;
        }
        doors.remove(index);
    }

    /**
     * Выбранная пользователем дверь
     * @return номер выбранной двери
     */
    private int getUserDoor() {
        return selectedDoor + 1;
    }

    /**
     * Проверяет является ли выбранная дверь выигрышной
     * @return
     */
    private boolean isRightChoice() {
        return doors.get(selectedDoor).getContent() instanceof Car;
    }

    /**
     * Окончание игры
     */
    private void finish() {
        selectedDoor = inputDoor(String.format("Выберите дверь от 1 до %d: ", doors.size()));
        if (!isRightChoice()) {
            System.out.printf("За дверью %d находится коза.\n", getUserDoor());
            System.out.println("Вы проиграли!");
        } else {
            System.out.printf("За дверью %d находится автомобиль.\n", getUserDoor());
            System.out.println("Вы выиграли!");
        }
        scanner.close();
        statistics();
    }

    /**
     * Записывает в лог статус выбора на каждом шаге
     */
    private void writeStep() {
        int step = log.size() + 1;
        log.put(step, isRightChoice() ? 1 : 0);
    }

    public void statistics() {
        DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
        for (int v : log.values()) {
            descriptiveStatistics.addValue(v);
        }
        System.out.printf("Коэффициент угадывания: %d\n", descriptiveStatistics.getMean());
    }
}
