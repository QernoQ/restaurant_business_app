package test;

import java.io.*;
import java.net.Socket;

public class DummySocket extends Socket {
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public DummySocket(InputStream in, OutputStream out) {
        this.inputStream = in;
        this.outputStream = out;
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }
}
