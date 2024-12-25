import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Menu menu = new Menu(scanner);

        Box<String> stringBox = new Box<>("Привет, Java!");
        Box<Integer> integerBox = new Box<>(42);

        System.out.println("Что в stringBox? " + stringBox.getContent());
        System.out.println("Что в integerBox? " + integerBox.getContent());

        stringBox.setContent("Изменённое значение");
        System.out.println("Новое содержимое stringBox: " + stringBox.getContent());

        Integer[] numbers = {1, 2, 3, 4, 5};
        System.out.println("Числа до перестановки: " + Arrays.toString(numbers));
        Utility.swapElements(numbers, 1, 3);
        System.out.println("Числа после перестановки: " + Arrays.toString(numbers));

        menu.showMainMenu();
    }
}