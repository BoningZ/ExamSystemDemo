package com.company.gui;


import com.company.bean.TestPaper;
import com.company.gui.my_component.MyButton;
import com.company.gui.my_component.MyPanel;
import com.company.gui.my_component.MyScrollPane;
import com.company.utils.Consts;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class TeacherMenuFrame extends BaseFrame{
    private final int W_SPACE=WIDTH/15;
    private final int H_SPACE=HEIGHT/15;

    private MyPanel pane;
    private MyPanel userInfo;
    private MyPanel releasedExam;
    private MyPanel todoExam;

    private MyScrollPane scrollReleased;
    private MyScrollPane scrollTodo;

    Object[][] doneTableData;
    Object[][] todoTableData;

    public TeacherMenuFrame(){init();}
    public TeacherMenuFrame(ClientContext context){
        this();
        this.context=context;
        fillData();
    }
    @Override
    protected void init() {

        ImageIcon icon=new ImageIcon("src/com/company/img/menu.jpg" );

        JLabel backGround=new JLabel(icon);
        backGround.setBounds(0,0,WIDTH,HEIGHT);
        JPanel myPanel = (JPanel)this.getContentPane();		//把我的面板设置为内容面板
        myPanel.setOpaque(false);					//把我的面板设置为不可视
        myPanel.setLayout(null);		//把我的面板设置为流动布局
        this.getLayeredPane().setLayout(null);
        this.getLayeredPane().add(backGround, new Integer(Integer.MIN_VALUE));
        
        this.setSize(WIDTH,HEIGHT);
        this.setLocation(0,0);
        this.setTitle("教师界面");
        this.add(createPanel());
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                context.exit();
            }
        });
    }
    private void fillData(){
        fillInfo();
        fillDone();
        fillTodo();
    }

    private MyPanel createPanel(){
        pane=new MyPanel();
        pane.setSize(WIDTH,HEIGHT );
        pane.setOpaque(false);//透明
        pane.setLayout(null);
        createInfo();
        createDone();
        createTodo();
        return pane;
    }

    private void fillInfo(){
        JLabel name=new JLabel("用户名:"+context.getUser().getUsername());
        name.setBounds(2*W_SPACE,0,2*W_SPACE,H_SPACE);
        userInfo.add(name);
        JLabel id=new JLabel("工号:"+context.getUser().getId());
        id.setBounds(2*W_SPACE,H_SPACE,2*W_SPACE,H_SPACE);
        userInfo.add(id);
        JLabel grade=new JLabel("年级:"+context.getUser().getGrade());
        grade.setBounds(2*W_SPACE,2*H_SPACE,2*W_SPACE,H_SPACE);
        userInfo.add(grade);
    }
    private void createInfo(){
        userInfo=new MyPanel();
        userInfo.setLayout(null);
        userInfo.setOpaque(false);
        userInfo.setSize(6*W_SPACE,3*H_SPACE);
        userInfo.setLocation(W_SPACE,H_SPACE);
        userInfo.setBorder(BorderFactory.createLineBorder(Color.black));

        JLabel logo=new JLabel(new ImageIcon("src/com/company/img/logo.png"));
        logo.setBounds(0,0,2*W_SPACE,3*H_SPACE);
        userInfo.add(logo);

        MyButton faultStatistics=new MyButton("刷新");
        faultStatistics.setBounds(4*W_SPACE,H(0.5),(int)(1.75* W_SPACE),H(0.7));
        faultStatistics.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {context.backToTeacherMenu();}});
        userInfo.add(faultStatistics);

        MyButton questionBase=new MyButton("题库");
        questionBase.setBounds(4*W_SPACE,H(1.5),(int)(1.75* W_SPACE),H(0.7));
        questionBase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                context.showQuestionBase();
            }
        });
        userInfo.add(questionBase);

        pane.add(userInfo);
    }
    private void fillDone(){
        List<TestPaper> list=context.getReleased();
        releasedExam.setPreferredSize(new Dimension(W(5.7),H(1.2*list.size())));
        int y=H(0.1);
        for(TestPaper paper:list){
            DonePane donePane=new DonePane(paper.getId(), paper.getTitle(), paper.getStatus(), y, new ActionListener() {@Override
                public void actionPerformed(ActionEvent e) {
                    context.showCorrect(paper.getId());}}, new ActionListener() {@Override
                public void actionPerformed(ActionEvent e) {
                    context.showQuery(paper.getId());}});
            releasedExam.add(donePane);
            y+=H(1.1);
        }
        pane.remove(scrollReleased);

        scrollReleased=new MyScrollPane(releasedExam);
        scrollReleased.setBounds(W_SPACE,5*H_SPACE,6*W_SPACE,9*H_SPACE);

        pane.add(scrollReleased);
        repaint();
        setVisible(true);
    }
    private void createDone(){
        JLabel label=new JLabel("已发布考试");
        label.setBounds(W_SPACE,(int)(4.5*H_SPACE),W_SPACE,(int)(0.5*H_SPACE));
        pane.add(label);

        releasedExam =new MyPanel();
        releasedExam.setLayout(null);
        releasedExam.setOpaque(false);
        releasedExam.setPreferredSize(new Dimension(W(5.7),9*H_SPACE));

        scrollReleased=new MyScrollPane(releasedExam);
        scrollReleased.setSize(6*W_SPACE,9*H_SPACE);
        scrollReleased.setLocation(W_SPACE,5*H_SPACE);
        scrollReleased.setOpaque(false);

        pane.add(scrollReleased);
    }
    private void fillTodo(){
        List<TestPaper> list=context.getUnreleased();
        todoExam.setPreferredSize(new Dimension(W(5.7),H(1.2*list.size())));
        int y=H(0.1);
        for(TestPaper paper:list){
            TodoPane todoPane=new TodoPane(paper.getId(),paper.getTitle(),y,
                    new ActionListener() {@Override
                        public void actionPerformed(ActionEvent e) {context.showEditPaper(paper.getId());}},
                    new ActionListener() {@Override
                        public void actionPerformed(ActionEvent e) {context.releasePaper(paper.getId());}});
            todoExam.add(todoPane);
            y+=H(1.1);
        }
        pane.remove(scrollTodo);

        scrollTodo=new MyScrollPane(todoExam);
        scrollTodo.setBorder(BorderFactory.createLineBorder(Color.black));
        scrollTodo.setSize(6*W_SPACE,12*H_SPACE);
        scrollTodo.setLocation(8*W_SPACE,H_SPACE);
        scrollTodo.setOpaque(false);

        pane.add(scrollTodo);
        repaint();
        setVisible(true);
    }
    private void createTodo(){
        JLabel label=new JLabel("未发布考试");
        label.setBounds(8*W_SPACE,(int)(0.5*H_SPACE),W_SPACE,(int)(0.5*H_SPACE));
        pane.add(label);

        todoExam=new MyPanel();
        todoExam.setLayout(null);
        todoExam.setOpaque(false);
        todoExam.setPreferredSize(new Dimension(W(5.7),12*H_SPACE));

        scrollTodo=new MyScrollPane(todoExam);
        scrollTodo.setSize(6*W_SPACE,12*H_SPACE);
        scrollTodo.setLocation(8*W_SPACE,H_SPACE);
        scrollTodo.setOpaque(false);

        pane.add(scrollTodo);

        MyButton createPaper=new MyButton("新建试卷");
        createPaper.setBounds(W(8), H(13), W(6), H(1));
        createPaper.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                context.createPaper();
            }
        });
        pane.add(createPaper);
    }

    class DonePane extends MyPanel{
        JLabel id,title;
        MyButton corr,query;
        DonePane(int id,String title,int status,int y,ActionListener corrListener,ActionListener queryListener){
            this.setBorder(BorderFactory.createLineBorder(Color.black));
            this.setLayout(null);
            this.setBounds(W(0.1),y,W(6-0.3),H(1));
            this.id=new JLabel("id："+id);
            this.id.setBounds(W(0.1),0,W(0.5-0.1),H(1));
            this.add(this.id);
            this.title=new JLabel(title);
            this.title.setBounds( W(0.6),0, W(2-0.5), H(1));
            this.add(this.title);

            JLabel left=new JLabel("待批阅："+context.getToCorrect(context.findPaper(id)).size());
            left.setBounds(W(2.1),0,W(1.2),H(1));
            this.add(left);

            query=new MyButton("查看数据");
            corr=new MyButton("阅卷");
            corr.addActionListener(corrListener);
            query.addActionListener(queryListener);
            corr.setBounds( W(4-0.5),H(0.1), W(1), H(1-0.2));
            query.setBounds( W(4-0.5+1.1),H(0.1), W(1), H(1-0.2));
            this.add(corr);
            this.add(query);
        }
    }
    class TodoPane extends MyPanel{
        JLabel id,title;
        MyButton edit,release;
        TodoPane(int id,String title,int y,ActionListener editListener,ActionListener releaseListener){
            this.setBorder(BorderFactory.createLineBorder(Color.black));
            this.setLayout(null);
            this.setBounds(W(0.1),y, W(6-0.3), H(1));
            this.id=new JLabel("id："+id);
            this.id.setBounds(W(0.1),0, W(1-0.1), H(1));
            this.add(this.id);
            this.title=new JLabel(title);
            this.title.setBounds( W(1),0, W(3-0.5), H(1));
            this.add(this.title);
            edit=new MyButton("编辑");
            edit.addActionListener(editListener);
            edit.setBounds( W(4-0.5),H(0.1), W(1), H(1-0.2));
            this.add(edit);
            release=new MyButton("发布");
            release.addActionListener(releaseListener);
            release.setBounds( W(5-0.4),H(0.1), W(1), H(1-0.2));
            this.add(release);
        }
    }
}


