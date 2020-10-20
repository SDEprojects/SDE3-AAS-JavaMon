package com.capstone;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

/**
 * This class extends from OutputStream to redirect output to a JTextArrea
 * @author www.codejava.net
 * Took this from stackoverflow/codejava
 */
public class CustomOutputStream extends OutputStream {
    private JTextArea textArea;

    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    // Writes the specified byte to this output stream
    @Override
    public void write(int b) throws IOException {
        // redirects data to the text area
        textArea.append(String.valueOf((char)b));
        // scrolls the text area to the end of data
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    // Flushes this output stream and forces any buffered output bytes to be written out.
    @Override
    public void flush() throws IOException {
        textArea.setText("");
    }
}