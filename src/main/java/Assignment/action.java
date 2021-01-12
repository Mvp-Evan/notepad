package Assignment;

import javax.swing.*;

public class action extends JFrame{

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        notePad note = new notePad();
        note.init();
        note.eventListener();

    }
}
