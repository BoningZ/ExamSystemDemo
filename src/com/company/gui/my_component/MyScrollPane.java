package com.company.gui.my_component;

import javax.swing.*;

public class MyScrollPane extends JScrollPane {
    public MyScrollPane(JPanel panel){
        super(panel);
        this.setOpaque(false);
        this.getViewport().setOpaque(false);
    }
    public MyScrollPane(JTextArea textArea){
        super(textArea);
        this.setOpaque(false);
        this.getViewport().setOpaque(false);
    }
    public MyScrollPane(JTable table){
        super(table);
        this.setOpaque(false);
        this.getViewport().setOpaque(false);
    }
    public MyScrollPane(){
        super();
        this.setOpaque(false);
        this.getViewport().setOpaque(false);
    }
}
