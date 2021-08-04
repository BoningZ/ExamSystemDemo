package com.company.gui.my_component;

import javax.swing.*;

public class MyCheckBox extends JCheckBox {
    public MyCheckBox(){
        super();
        this.setOpaque(false);
    }
    public MyCheckBox(String st,Boolean b){
        super(st,b);
        this.setOpaque(false);
    }
    public MyCheckBox(String st){
        super(st);
        this.setOpaque(false);
    }
}
