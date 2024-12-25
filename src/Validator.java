import java.util.InputMismatchException;
import java.util.Scanner;

public class Validator {
    private Scanner sc;

    public Validator(Scanner scanner) {
        this.sc = scanner;
    }

    public double getPositiveDouble(String prompt) throws InvalidShapeParameterException {
        double value;
        while (true) {
            try {
                System.out.print(prompt);
                while (!sc.hasNextDouble()) {
                    System.out.print("Ошибка: Введите корректное число: ");
                    sc.next();
                }
                value = sc.nextDouble();
                if (value > 0) {
                    return value;
                } else {
                    throw new InvalidShapeParameterException("Ошибка: число должно быть положительным.");
                }
            } catch (InvalidShapeParameterException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public int getValidChoice(int min, int max) {
        int choice;
        while (true) {
            try {
                if (!sc.hasNextInt()) {
                    System.out.print("Ошибка: Введите число: ");
                    sc.next();
                    continue;
                }
                choice = sc.nextInt();
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.println("Ошибка: выберите вариант от " + min + " до " + max);
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: Введите корректное число.");
                sc.next();
            }
        }
    }
}