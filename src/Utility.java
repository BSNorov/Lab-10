import java.util.Arrays;

public class Utility {
    public static <T> void swapElements(T[] array, int index1, int index2) {
        if (index1 < 0 || index2 < 0 || index1 >= array.length || index2 >= array.length) {
            throw new IndexOutOfBoundsException("Индексы находятся за пределами массива.");
        }
        T temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }
}