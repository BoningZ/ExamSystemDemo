package com.company.gui;

import javafx.scene.control.PasswordField;
import sun.rmi.runtime.Log;

import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends BaseFrame implements KeyListener {
    private JPanel pane;
    private JTextField text;
    private JPasswordField pwd;

    public LoginFrame(){init();}
    public LoginFrame(ClientContext context){
        this();
        this.context=context;
    }
    @Override
    protected void init() {

        ImageIcon icon=new ImageIcon("src/com/company/img/login.png" );

        JLabel backGround=new JLabel(icon);
        backGround.setBounds(0,0,400,300);
        JPanel myPanel = (JPanel)this.getContentPane();		//���ҵ��������Ϊ�������
        myPanel.setOpaque(false);					//���ҵ��������Ϊ������
        myPanel.setLayout(null);
        this.getLayeredPane().setLayout(null);
        this.getLayeredPane().add(backGround, new Integer(Integer.MIN_VALUE));



        this.setSize(400,300);
        this.setLocation((WIDTH - 400) / 2, (HEIGHT - 300) / 2);



        this.setTitle("��¼");

        myPanel.add(createPanel());

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                context.exit();
            }
        });
        this.addKeyListener(this);
    }
    private JPanel createPanel() {



        pane=new JPanel();
        pane.setSize(600,300 );
        pane.setOpaque(false);//͸��
        pane.setLayout(null);
        createLogo();
        createName();
        createPwd();
        createButtons();



        return pane;
    }
    private void createLogo() {
        JLabel jl=new JLabel("�û���¼");
        jl.setBounds(164, 10, 100, 20);
        jl.setOpaque(false);
        pane.add(jl);
    }
    private void createButtons() {
        JButton studentLogin=new JButton("ѧ����¼");
        studentLogin.setContentAreaFilled(false);
        studentLogin.setBorder(BorderFactory.createRaisedBevelBorder());
        studentLogin.setBounds(75, 120, 120, 50);
        studentLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                context.login(true);
            }
        });
        studentLogin.addKeyListener(this);
        pane.add(studentLogin);

        JButton teacherLogin=new JButton("��ʦ��¼");
        teacherLogin.setBounds(195, 120, 120, 50);
        teacherLogin.setContentAreaFilled(false);
        teacherLogin.setBorder(BorderFactory.createRaisedBevelBorder());
        teacherLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                context.login(false);
            }
        });
        teacherLogin.addKeyListener(this);
        pane.add(teacherLogin);

        JButton studentReg=new JButton("ѧ��ע��");
        studentReg.setContentAreaFilled(false);
        studentReg.setBorder(BorderFactory.createRaisedBevelBorder());
        studentReg.setBounds(75, 120+60, 120, 30);
        studentReg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                context.showRegister(true);
            }
        });
        studentReg.addKeyListener(this);
        pane.add(studentReg);

        JButton teacherReg=new JButton("��ʦע��");
        teacherReg.setContentAreaFilled(false);
        teacherReg.setBorder(BorderFactory.createRaisedBevelBorder());
        teacherReg.setBounds(195, 120+60, 120, 30);
        teacherReg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                context.showRegister(false);
            }
        });
        teacherReg.addKeyListener(this);
        pane.add(teacherReg);
    }
    private void createPwd() {
        JLabel jl=new JLabel("����:");
        jl.setBounds(44, 80, 200, 20);
        pane.add(jl);
        pwd=new JPasswordField(30);
        pwd.setBorder(new SoftBevelBorder(1));
        pwd.setBounds(120, 80, 150, 20);
        pane.add(pwd);
    }
    private void createName() {
        JLabel jl=new JLabel("ѧ/����:");
        jl.setBounds(44, 50, 200, 20);
        pane.add(jl);
        text=new JTextField(30);
        text.setBounds(120, 50, 150, 20);
        text.setBorder(new SoftBevelBorder(1));
        pane.add(text);
    }
    public String getId(){
        return text.getText();
    }
    public String getPwd(){
        return new String(pwd.getPassword());
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_ESCAPE) {
            context.exit();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
