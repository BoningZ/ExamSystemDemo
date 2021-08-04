package com.company.gui;

import com.company.gui.my_component.MyButton;
import com.company.utils.Consts;

import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentRegisterFrame extends BaseFrame{
    private JPanel pane;
    private JTextField username;
    private JTextField id;
    private JPasswordField pwd;
    private JPasswordField repPwd;
    private JComboBox<Integer> grade;

    public StudentRegisterFrame(){init();}
    public StudentRegisterFrame(ClientContext context){
        this();
        this.context=context;
    }
    @Override
    protected void init() {

        ImageIcon icon=new ImageIcon("src/com/company/img/register.jpg" );

        JLabel backGround=new JLabel(icon);
        backGround.setBounds(0,0,380,400);
        JPanel myPanel = (JPanel)this.getContentPane();		//把我的面板设置为内容面板
        myPanel.setOpaque(false);					//把我的面板设置为不可视
        myPanel.setLayout(null);		//把我的面板设置为流动布局
        this.getLayeredPane().setLayout(null);
        this.getLayeredPane().add(backGround, new Integer(Integer.MIN_VALUE));

        this.setSize(380,400 );
        this.setLocation((WIDTH - 380) / 2, (HEIGHT - 400) / 2);
        this.setTitle("学生注册");
        myPanel.add(createPanel());
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
    private JPanel createPanel() {
        pane=new JPanel();
        pane.setSize(600,300 );
        pane.setOpaque(false);//透明
        pane.setLayout(null);
        createLogo();
        createName();
        createId();
        createPwd();
        createRepPwd();
        createGrade();
        createButtons();
        return pane;
    }

    private void createLogo() {
        JLabel jl=new JLabel("学生注册");
        jl.setBounds(134, 10, 100, 20);
        pane.add(jl);
    }
    private void createName(){
        JLabel jl=new JLabel("姓名:");
        jl.setBounds(44, 50, 200, 20);
        pane.add(jl);
        username=new JTextField(30);
        username.setBounds(120, 50, 150, 20);
        username.setBorder(new SoftBevelBorder(1));
        pane.add(username);
    }
    private void createId(){
        JLabel jl=new JLabel("学号:");
        jl.setBounds(44, 80, 200, 20);
        pane.add(jl);
        id=new JTextField(30);
        id.setBounds(120, 80, 150, 20);
        id.setBorder(new SoftBevelBorder(1));
        pane.add(id);
    }
    private void createPwd(){
        JLabel jl=new JLabel("密码:");
        jl.setBounds(44, 130, 200, 20);
        pane.add(jl);
        pwd=new JPasswordField(30);
        pwd.setBounds(120, 130, 150, 20);
        pwd.setBorder(new SoftBevelBorder(1));
        pane.add(pwd);
    }
    private void createRepPwd(){
        JLabel jl=new JLabel("重复密码:");
        jl.setBounds(44, 160, 200, 20);
        pane.add(jl);
        repPwd=new JPasswordField(30);
        repPwd.setBounds(120, 160, 150, 20);
        repPwd.setBorder(new SoftBevelBorder(1));
        pane.add(repPwd);
    }
    private void createGrade(){
        JLabel jl=new JLabel("年级:");
        jl.setBounds(44, 190, 200, 20);
        pane.add(jl);
        grade=new JComboBox<Integer>();
        for(int i=1;i<= Consts.NUM_OF_GRADES;i++)grade.addItem(i);
        grade.setBounds(120, 190, 150, 20);
        grade.setBorder(new SoftBevelBorder(1));
        pane.add(grade);
    }
    private void createButtons(){
        MyButton register=new MyButton("注册");
        register.setBounds(75, 230, 100, 30);
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                context.studentRegister();
            }
        });
        pane.add(register);

        MyButton back=new MyButton("返回");
        back.setBounds(175, 230, 100, 30);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                context.backToLogin();
            }
        });
        pane.add(back);
    }
    public String getUsername(){return username.getText();}
    public String getId(){return id.getText();}
    public String getPwd(){return new String(pwd.getPassword());}
    public String getResPwd(){return new String(repPwd.getPassword());}
    public int getGrade(){return grade.getItemAt(grade.getSelectedIndex());}
}
