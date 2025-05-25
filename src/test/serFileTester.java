package test;

import java.io.*;
import java.util.ArrayList;

public class serFileTester {
    public static void main(String[] args) {
        String fileName = "bills.ser";
        ArrayList<Object> list = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            while (true) {
                try {
                    Object obj = ois.readObject();
                    list.add(obj);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (Object o : list) {
            System.out.println(o + "\n");
        }
    }
}
