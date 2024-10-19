package my.homework;

import lombok.Getter;

/**
 * Класс двери
 * @param <T>
 */
@Getter
public class Door <T> {
    private int number;
    private T content;

    public Door(int number, T content) {
        this.number = number;
        this.content = content;
    }
}
