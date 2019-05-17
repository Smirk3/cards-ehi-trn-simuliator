package ehi.message.service;

import java.io.OutputStream;

public class StringOutputStream extends OutputStream {

    private StringBuilder string = new StringBuilder();

    @Override
    public void write(int b) {
        this.string.append((char) b);
    }

    @Override
    public String toString() {
        return this.string.toString();
    }

}
