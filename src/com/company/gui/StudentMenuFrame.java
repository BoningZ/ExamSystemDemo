package com.company.gui;


import com.company.bean.AnswerSheet;
import com.company.bean.TestPaper;
import com.company.gui.my_component.MyButton;
import com.company.utils.Consts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudentMenuFrame extends BaseFrame{
    private final int W_SPACE=WIDTH/15;
    private final int H_SPACE=HEIGHT/15;

    private JPanel pane;
    private JPanel userInfo;
    private JPanel doneExam;
    private JPanel todoExam;

    private JScrollPane scrollDone;
    private JScrollPane scrollTodo;

    Object[][] doneTableData;
    Object[][] todoTableData;

    public StudentMenuFrame(){init();}
    public StudentMenuFrame(ClientContext context){
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
        this.setTitle("学生界面");
        myPanel.add(createPanel());
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

    private JPanel createPanel(){
        pane=new JPanel();
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
        JLabel id=new JLabel("学号:"+context.getUser().getId());
        id.setBounds(2*W_SPACE,H_SPACE,2*W_SPACE,H_SPACE);
        userInfo.add(id);
        JLabel grade=new JLabel("年级:"+context.getUser().getGrade());
        grade.setBounds(2*W_SPACE,2*H_SPACE,2*W_SPACE,H_SPACE);
        userInfo.add(grade);



    }
    private void createInfo(){
        userInfo=new JPanel();
        userInfo.setLayout(null);
        userInfo.setOpaque(false);
        userInfo.setSize(6*W_SPACE,3*H_SPACE);
        userInfo.setLocation(W_SPACE,H_SPACE);
        userInfo.setBorder(BorderFactory.createLineBorder(Color.black));

        JLabel logo=new JLabel(new ImageIcon("src/com/company/img/logo.png"));
        logo.setBounds(0,0,2*W_SPACE,3*H_SPACE);
        userInfo.add(logo);
        MyButton faultStatistics=new MyButton("刷新");
        faultStatistics.setBounds(4*W_SPACE,H_SPACE,(int)(1.75* W_SPACE),H_SPACE);
        faultStatistics.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                context.backToStudentMenu();
            }
        });
        userInfo.add(faultStatistics);

        pane.add(userInfo);
    }
    private void fillDone(){
        doneTableData=context.getDoneExamInfo();
        doneExam.setPreferredSize(new Dimension(W(5.7),H(1.2*doneTableData.length)));
        int y=H(0.1);
        for(int i=0;i<doneTableData.length;i++){
            int finalI = i;
            DonePane donePane=new DonePane((int)doneTableData[i][0],(String)doneTableData[i][1],(double)doneTableData[i][2],(int)doneTableData[i][3],y,
                    new ActionListener() {@Override
                    public void actionPerformed(ActionEvent e) {context.showFault((int)doneTableData[finalI][0]);}});
            doneExam.add(donePane);
            y+=H(1.1);
        }
        pane.remove(scrollDone);

        scrollDone=new JScrollPane(doneExam);
        scrollDone.setBounds(W_SPACE,5*H_SPACE,6*W_SPACE,9*H_SPACE);
        scrollDone.setOpaque(false);
        scrollDone.getViewport().setOpaque(false);

        pane.add(scrollDone);
        repaint();
        setVisible(true);
    }
    private void createDone(){
        JLabel label=new JLabel("已批阅考试");
        label.setBounds(W_SPACE,(int)(4.5*H_SPACE),W_SPACE,(int)(0.5*H_SPACE));
        pane.add(label);

        doneExam =new JPanel();
        doneExam.setLayout(null);
        doneExam.setOpaque(false);
        doneExam.setPreferredSize(new Dimension(W(5.7),9*H_SPACE));

        scrollDone=new JScrollPane(doneExam);
        scrollDone.setOpaque(false);
        scrollDone.getViewport().setOpaque(false);
        scrollDone.setSize(6*W_SPACE,9*H_SPACE);
        scrollDone.setLocation(W_SPACE,5*H_SPACE);

        pane.add(scrollDone);
    }
    private void fillTodo(){
        todoTableData=context.getTodoExamInfo();
        todoExam.setPreferredSize(new Dimension(W(5.7),H(1.2*todoTableData.length)));
        int y=H(0.1);
        for(int i=0;i<todoTableData.length;i++){
            int finalI = i;
            TodoPane todoPane=new TodoPane((int)todoTableData[i][0],(String)todoTableData[i][1],(String)todoTableData[i][2],y,
                    new ActionListener() {@Override
                    public void actionPerformed(ActionEvent e) {context.startExam((int)todoTableData[finalI][0]);}});
            todoExam.add(todoPane);
            y+=H(1.1);
        }
        pane.remove(scrollTodo);

        scrollTodo=new JScrollPane(todoExam);
        scrollTodo.setBorder(BorderFactory.createLineBorder(Color.black));
        scrollTodo.setBounds(8*W_SPACE,H_SPACE,6*W_SPACE,13*H_SPACE);
        scrollTodo.setOpaque(false);
        scrollTodo.getViewport().setOpaque(false);

        pane.add(scrollTodo);
        repaint();
        setVisible(true);
    }
    private void createTodo(){
        JLabel label=new JLabel("待完成考试");
        label.setBounds(8*W_SPACE,(int)(0.5*H_SPACE),W_SPACE,(int)(0.5*H_SPACE));
        pane.add(label);

        todoExam=new JPanel();
        todoExam.setLayout(null);
        todoExam.setOpaque(false);
        todoExam.setPreferredSize(new Dimension(W(5.7),13*H_SPACE));

        scrollTodo=new JScrollPane(todoExam);
        scrollTodo.setSize(6*W_SPACE,13*H_SPACE);
        scrollTodo.setLocation(8*W_SPACE,H_SPACE);
        scrollTodo.getViewport().setOpaque(false);
        scrollTodo.setOpaque(false);

        pane.add(scrollTodo);
    }

    class DonePane extends JPanel{
        JLabel id,title,score,rank;
        MyButton query;
        DonePane(int id,String title,double score,int rank,int y,ActionListener listener){
            this.setOpaque(false);
            this.setBorder(BorderFactory.createLineBorder(Color.black));
            this.setLayout(null);
            this.setBounds(W(0.1),y, W(6-0.3), H(1));

            this.id=new JLabel("id："+id);
            this.id.setBounds(W(0.1),0, W(1-0.1), H(1));
            this.add(this.id);

            this.title=new JLabel(title);
            this.title.setBounds( W(1),0, W(2-0.5), H(1));
            this.add(this.title);

            this.score=new JLabel("分数："+String.valueOf(score)+"/"+context.getScoreOfType(context.findPaper(id),Consts.TYPE_ALL));
            this.score.setBounds( W(2.5),0, W(1.3), H(1));
            this.add(this.score);

            this.rank=new JLabel("排名："+String.valueOf(rank));
            this.rank.setBounds( W(3.8),0, W(0.5), H(1));
            this.add(this.rank);

            query=new MyButton("查看");
            query.addActionListener(listener);
            query.setBounds( W(4.5),H(0.1), W(1-0.2), H(1-0.2));
            this.add(query);
        }
    }
    class TodoPane extends JPanel{
        JLabel id,title,timeLimit;
        MyButton start;
        TodoPane(int id,String title,String timeLimit,int y,ActionListener listener){
            this.setBorder(BorderFactory.createLineBorder(Color.black));
            this.setOpaque(false);
            this.setLayout(null);
            this.setBounds(W(0.1),y, W(6-0.3), H(1));

            this.id=new JLabel("id："+id);
            this.id.setBounds(W(0.1),0, W(1-0.1), H(1));
            this.add(this.id);
            
            this.title=new JLabel(title);
            this.title.setBounds( W(1),0, W(3-0.5), H(1));
            this.add(this.title);

            AnswerSheet sheet=context.findSheet(id);
            if(sheet.getStatus()==Consts.STATE_WAITING_ANS) {
                start = new MyButton("考试");
                start.addActionListener(listener);
                start.setBounds(W(4.5), H(0.1), W(1 - 0.2), H(1 - 0.2));
                this.add(start);

                this.timeLimit=new JLabel("时限："+timeLimit);
                this.timeLimit.setBounds(W(2.7),0,W(1.5),H(1));
                this.add(this.timeLimit);
            }else{
                JLabel noAccess=new JLabel("考试完毕");
                noAccess.setBounds(W(4.5), H(0.1), W(1 - 0.2), H(1 - 0.2));
                this.add(noAccess);

                TestPaper paper=context.findPaper(sheet.getTestPaperId());
                JLabel objScore=new JLabel("客观题得分："+context.autoCorrect(id,sheet.getId())+"/"+
                        (context.getScoreOfType(paper,Consts.TYPE_MULTI_CHOICE)+context.getScoreOfType(paper,Consts.TYPE_SINGLE_CHOICE)));
                objScore.setBounds(W(2.7),0,W(1.5),H(1));
                this.add(objScore);
            }
        }
    }
}
