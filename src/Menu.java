import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Menu {
    private Scanner sc;
    private Validator validator;

    // Для истории расчётов мы выбрали ArrayList, потому что:
    // - Мы добавляем новые записи только в конец списка.
    // - Доступ к элементам происходит по порядку (просмотр истории).
    private List<Box<Shape>> history = new ArrayList<>();

    // Для хранения уникальных фигур мы используем HashSet, потому что:
    // - Он автоматически убирает дублирующие фигуры.
    // - Проверка и добавление в HashSet работают быстро.
    private Set<Shape> uniqueShapes = new HashSet<>();

    // Для поиска фигуры по имени мы используем HashMap, потому что:
    // - Это позволяет быстро найти фигуру по её имени.
    // - Доступ к объекту в HashMap очень быстрый.
    private Map<String, Shape> shapesByName = new HashMap<>();

    private static final String HISTORY_FILE = "history.json";

    public Menu(Scanner scanner) {
        this.sc = scanner;
        this.validator = new Validator(scanner);

        loadHistory();
    }

    public void showMainMenu() {
        boolean exit = false;

        while (!exit) {
            printMainMenu();
            int choice = validator.getValidChoice(1, 7);

            switch (choice) {
                case 1 -> {
                    printShapesMenu();
                    int shapeMenuChoice = validator.getValidChoice(1, 2);
                    switch (shapeMenuChoice) {
                        case 1 -> performRectangleCalculation();
                        case 2 -> performCircleCalculation();
                    }
                }
                case 2 -> printProgramInfo();
                case 3 -> printDeveloperInfo();
                case 4 -> showShapesHistory();
                case 5 -> showUniqueShapes();
                case 6 -> findShapeByName();
                case 7 -> {
                    saveHistory();
                    exit = true;
                    System.out.println("Выход из программы...");
                }
                default -> System.out.println("Неверный выбор. Пожалуйста, выберите один из пунктов меню.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n======= Главное меню =======");
        System.out.println("1. Выполнить расчет для фигуры");
        System.out.println("2. Информация о программе");
        System.out.println("3. Информация о разработчике");
        System.out.println("4. Показать историю расчётов");
        System.out.println("5. Показать уникальные фигуры");
        System.out.println("6. Найти фигуру по имени");
        System.out.println("7. Выход");
        System.out.print("Выберите пункт меню: ");
    }

    private void printShapesMenu() {
        System.out.println("\n======= Выбор фигуры =======");
        System.out.println("1. Площадь прямоугольника");
        System.out.println("2. Площадь круга");
        System.out.print("Выберите пункт меню: ");
    }

    private void performRectangleCalculation() {
        try {
            double length = validator.getPositiveDouble("Введите длину прямоугольника (положительное число): ");
            double width = validator.getPositiveDouble("Введите ширину прямоугольника (положительное число): ");
            Rectangle rectangle = new Rectangle(length, width);
            history.add(new Box<>(rectangle));
            uniqueShapes.add(rectangle);
            shapesByName.put(rectangle.getName(), rectangle);
            System.out.println("Результат: " + rectangle.calculateArea());
        } catch (InvalidShapeParameterException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void performCircleCalculation() {
        try {
            double radius = validator.getPositiveDouble("Введите радиус круга (положительное число): ");
            Circle circle = new Circle(radius);
            history.add(new Box<>(circle));
            uniqueShapes.add(circle);
            shapesByName.put(circle.getName(), circle);
            System.out.println("Результат: " + circle.calculateArea());
        } catch (InvalidShapeParameterException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void printProgramInfo() {
        System.out.println("\nИнформация о программе:");
        System.out.println("Эта программа предназначена для работы с различными геометрическими фигурами и расчета их площади.");
    }

    private void printDeveloperInfo() {
        System.out.println("\nИнформация о разработчике:");
        System.out.println("Разработчик: Бехруз, студент магистратуры по направлению 'Разработка и управление в программных проектах'.");
    }

    private void showShapesHistory() {
        System.out.println("\n======= История расчетов =======");
        if (history.isEmpty()) {
            System.out.println("История пуста.");
        } else {
            for (Box<Shape> box : history) {
                Shape shape = box.getContent();
                System.out.println(shape.toString());
                System.out.println("Результат: " + shape.calculateArea());
                System.out.println();
            }
        }
    }

    private void showUniqueShapes() {
        System.out.println("\n======= Уникальные фигуры =======");
        if (uniqueShapes.isEmpty()) {
            System.out.println("Список уникальных фигур пуст.");
        } else {
            for (Shape shape : uniqueShapes) {
                System.out.println(shape);
            }
        }
    }

    private void findShapeByName() {
        System.out.print("Введите имя фигуры для поиска: ");
        String name = sc.next();
        Shape shape = shapesByName.get(name);
        if (shape != null) {
            System.out.println("Найдена фигура: " + shape);
        } else {
            System.out.println("Фигура с таким именем не найдена.");
        }
    }

    private void saveHistory() {
        File file = new File(HISTORY_FILE);
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(history);
            FileUtils.writeStringToFile(file, json, "UTF-8");
            System.out.println("История расчетов успешно сохранена.");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении истории: " + e.getMessage());
        }
    }

    private void loadHistory() {
        File file = new File(HISTORY_FILE);
        if (!file.exists()) {
            System.out.println("Файл истории не найден, начнем с пустой истории.");
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = FileUtils.readFileToString(file, "UTF-8");
            history = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, Box.class));
            System.out.println("История расчетов успешно загружена.");
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке истории: " + e.getMessage());
        }
    }
}
