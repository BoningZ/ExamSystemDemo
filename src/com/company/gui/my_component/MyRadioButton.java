package com.company.gui.my_component;

import javax.swing.*;

public class MyRadioButton extends JRadioButton {
    public MyRadioButton(){
        super();
        this.setOpaque(false);
    }
    public MyRadioButton(String st,Boolean b){
        super(st,b);
        this.setOpaque(false);
    }
    public MyRadioButton(String st){
        super(st);
        this.setOpaque(false);
    }
}
