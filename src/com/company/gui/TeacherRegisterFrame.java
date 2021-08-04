package com.company.gui;

import com.company.utils.Consts;

import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TeacherRegisterFrame extends BaseFrame{
    private JPanel pane;
    private JTextField username;
    private JTextField id;
    private JPasswordField pwd;
    private JPasswordField repPwd;
    private JComboBox<Integer> grade;
    private JTextField adminPwd;

    public TeacherRegisterFrame(){init();}
    public TeacherRegisterFrame(ClientContext context){
        this();
        this.context=context;
    }
    @Override
    protected void init() {

        ImageIcon icon=new ImageIcon("src/com/company/img/register.jpg" );

        JLabel backGround=new JLabel(icon);
        backGround.setBounds(0,0,350,400);
        JPanel myPanel = (JPanel)this.getContentPane();		//���ҵ��������Ϊ�������
        myPanel.setOpaque(false);					//���ҵ��������Ϊ������
        myPanel.setLayout(null);		//���ҵ��������Ϊ��������
        this.getLayeredPane().setLayout(null);
        this.getLayeredPane().add(backGround, new Integer(Integer.MIN_VALUE));

        this.setSize(350,400 );
        this.setLocation((WIDTH - 350) / 2, (HEIGHT - 400) / 2);
        this.setTitle("��ʦע��");
        myPanel.add(createPanel());
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
    private JPanel createPanel() {
        pane=new JPanel();
        pane.setSize(600,300 );
        pane.setOpaque(false);//͸��
        pane.setLayout(null);
        createLogo();
        createName();
        createId();
        createPwd();
        createRepPwd();
        createGrade();
        createAdminPwd();
        createButtons();
        return pane;
    }

    private void createLogo() {
        JLabel jl=new JLabel("��ʦע��");
        jl.setOpaque(false);
        jl.setBounds(134, 10, 100, 20);
        pane.add(jl);
    }
    private void createName(){
        JLabel jl=new JLabel("����:");
        jl.setBounds(44, 50, 200, 20);
        pane.add(jl);
        username=new JTextField(30);
        username.setBounds(130, 50, 150, 20);
        username.setBorder(new SoftBevelBorder(1));
        pane.add(username);
    }
    private void createId(){
        JLabel jl=new JLabel("����:");
        jl.setBounds(44, 80, 200, 20);
        pane.add(jl);
        id=new JTextField(30);
        id.setBounds(130, 80, 150, 20);
        id.setBorder(new SoftBevelBorder(1));
        pane.add(id);
    }
    private void createPwd(){
        JLabel jl=new JLabel("����:");
        jl.setBounds(44, 130, 200, 20);
        pane.add(jl);
        pwd=new JPasswordField(30);
        pwd.setBounds(130, 130, 150, 20);
        pwd.setBorder(new SoftBevelBorder(1));
        pane.add(pwd);
    }
    private void createRepPwd(){
        JLabel jl=new JLabel("�ظ�����:");
        jl.setBounds(44, 160, 200, 20);
        pane.add(jl);
        repPwd=new JPasswordField(30);
        repPwd.setBounds(130, 160, 150, 20);
        repPwd.setBorder(new SoftBevelBorder(1));
        pane.add(repPwd);
    }
    private void createGrade(){
        JLabel jl=new JLabel("�꼶:");
        jl.setBounds(44, 190, 200, 20);
        pane.add(jl);
        grade=new JComboBox<Integer>();
        for(int i=1;i<= Consts.NUM_OF_GRADES;i++)grade.addItem(i);
        grade.setBounds(130, 190, 150, 20);
        grade.setBorder(new SoftBevelBorder(1));
        pane.add(grade);
    }
    private void createAdminPwd(){
        JLabel jl=new JLabel("����Ա����:");
        jl.setBounds(44, 220, 200, 20);
        pane.add(jl);
        adminPwd=new JPasswordField(30);
        adminPwd.setBounds(130, 220, 150, 20);
        adminPwd.setBorder(new SoftBevelBorder(1));
        pane.add(adminPwd);
    }
    private void createButtons(){
        JButton register=new JButton("ע��");
        register.setContentAreaFilled(false);
        register.setBorder(BorderFactory.createRaisedBevelBorder());
        register.setBounds(75, 260, 100, 30);
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                context.teacherRegister();
            }
        });
        pane.add(register);

        JButton back=new JButton("����");
        back.setContentAreaFilled(false);
        back.setBorder(BorderFactory.createRaisedBevelBorder());
        back.setBounds(175, 260, 100, 30);
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
    public String getAdminPwd(){return adminPwd.getText();}
}

