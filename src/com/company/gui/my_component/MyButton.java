package com.company.gui.my_component;

import javax.swing.*;

public class MyButton extends JButton {
    public MyButton(String st){
        super(st);
        this.setContentAreaFilled(false);
        this.setBorder(BorderFactory.createRaisedBevelBorder());
    }
}
