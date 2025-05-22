package test;

import GUI.BossGUI;

import java.io.*;
import java.net.Socket;

public class testGUI {
    public static void main(String[] args) {
        try {
            PipedOutputStream pos = new PipedOutputStream();
            PipedInputStream pis = new PipedInputStream(pos);

            Socket dummySocket = new DummySocket(pis, pos);

            new BossGUI("TEST - Boss GUI", dummySocket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
