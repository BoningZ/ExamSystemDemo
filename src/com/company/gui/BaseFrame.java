package com.company.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class BaseFrame extends JFrame {
    public final int HEIGHT=Toolkit.getDefaultToolkit().getScreenSize().height;
    public final int WIDTH=Toolkit.getDefaultToolkit().getScreenSize().width;
    ClientContext context;
    public BaseFrame(){
        this.setLayout(null);
        String lookAndFeel=UIManager.getSystemLookAndFeelClassName();
        try{
            UIManager.setLookAndFeel(lookAndFeel);
        }catch (Exception e){
            e.printStackTrace();
        }
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("x=" + e.getX() + ";y=" + e.getY());
            }
        });
        if (this.getFocusableWindowState()) {
            repaint();
        }
    }

    protected abstract void init();

    public void showView(){
        this.setResizable(false);
        this.setVisible(true);
        this.repaint();
    }
    public int H(double r){return (int)(r*(HEIGHT/15));}
    public int W(double r){return (int)(r*(WIDTH/15));}
    public int HH(double r){return (int)(r*(HEIGHT/30));}
    public int WW(double r){return (int)(r*(WIDTH/30));}

}
