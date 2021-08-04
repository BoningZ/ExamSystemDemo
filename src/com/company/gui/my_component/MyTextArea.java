package com.company.gui.my_component;

import javax.swing.*;

public class MyTextArea extends JTextArea {
    public MyTextArea(){
        super();
        this.setOpaque(false);
    }
    public MyTextArea(String st){
        super(st);
        this.setOpaque(false);
    }
}
