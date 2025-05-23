package test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class serFileTester {
    public static void main(String[] args) {
        String fileName = "Workers.ser";

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            Object obj = ois.readObject();
            System.out.println("Odczytany obiekt:");
            System.out.println(obj);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}