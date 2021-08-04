package com.company.gui;

import com.company.bean.Question;
import com.company.gui.my_component.*;
import com.company.utils.Consts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class QuestionBaseFrame extends BaseFrame{
    private MyPanel pane;
    private MyPanel screenPane;
    private MyPanel screen;
    private MyPanel questionPane;
    private QuestionPanel questionPanel;

    private MyScrollPane scrollScreen;

    private MyTextArea lowDiff=new MyTextArea("0");
    private MyTextArea uppDiff=new MyTextArea("1");
    private JLabel numOfScreened;
    private ButtonGroup sortKey=new ButtonGroup();
    private MyRadioButton sortByDiff=new MyRadioButton("按难度排序",true);
    private MyRadioButton sortByType=new MyRadioButton("按题型排序");
    private MyRadioButton sortById=new MyRadioButton("按编号排序");
    private MyRadioButton sortByTitle=new MyRadioButton("按标题排序");
    private MyCheckBox singleChoice=new MyCheckBox("单选",true);
    private MyCheckBox multiChoice=new MyCheckBox("多选",true);
    private MyCheckBox fillBlank=new MyCheckBox("填空",true);
    private MyCheckBox shortAns=new MyCheckBox("简答",true);

    public QuestionBaseFrame(){init();}
    public QuestionBaseFrame(ClientContext context){
        this();
        this.context=context;
        fillData();
    }
    private void fillData(){reScreen();}
    protected void init() {

        ImageIcon icon=new ImageIcon("src/com/company/img/questionBase.jpg" );

        JLabel backGround=new JLabel(icon);
        backGround.setBounds(0,0,WIDTH,HEIGHT);
        JPanel myPanel = (JPanel)this.getContentPane();		//把我的面板设置为内容面板
        myPanel.setOpaque(false);					//把我的面板设置为不可视
        myPanel.setLayout(null);		//把我的面板设置为流动布局
        this.getLayeredPane().setLayout(null);
        this.getLayeredPane().add(backGround, new Integer(Integer.MIN_VALUE));
        
        this.setSize(WIDTH,HEIGHT);
        this.setLocation(0,0);
        this.setTitle("题库");
        this.add(createPanel());
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                context.exit();
            }
        });
    }
    private MyPanel createPanel(){
        pane=new MyPanel();
        pane.setSize(WIDTH,HEIGHT);
        pane.setOpaque(false);//透明
        pane.setLayout(null);
        createScreen();
        createQuestion();

        MyButton back=new MyButton("返回");
        back.setBounds(0,0,WW(1),HH(1));
        back.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {context.backToTeacherMenu();}});
        pane.add(back);

        return pane;
    }
    private void createScreen(){
        screenPane=new MyPanel();
        screenPane.setSize(WW(8),HH(28));
        screenPane.setLocation(WW(21),HH(1));
        screenPane.setOpaque(false);//透明
        screenPane.setLayout(null);

        MyPanel screenKeys=new MyPanel();
        screenKeys.setBorder(BorderFactory.createLineBorder(Color.black));
        screenKeys.setLocation(0,0);
        screenKeys.setSize(WW(8),HH(2));
        screenKeys.setOpaque(false);
        screenKeys.setLayout(null);

        singleChoice.setLocation(WW(0.5),HH(0.1));
        singleChoice.setSize(WW(1),HH(0.5));
        screenKeys.add(singleChoice);
        multiChoice.setLocation(WW(1.5),HH(0.1));
        multiChoice.setSize(WW(1),HH(0.5));
        screenKeys.add(multiChoice);
        fillBlank.setLocation(WW(0.5),HH(1.1));
        fillBlank.setSize(WW(1),HH(0.5));
        screenKeys.add(fillBlank);
        shortAns.setLocation(WW(1.5),HH(1.1));
        shortAns.setSize(WW(1),HH(0.5));
        screenKeys.add(shortAns);
        JLabel diffLabel=new JLabel("难度系数");
        diffLabel.setBounds(WW(3),0,WW(5),HH(1));
        screenKeys.add(diffLabel);

        lowDiff.setBounds(WW(3),HH(1),WW(1.5),HH(0.7));
        lowDiff.setBorder(BorderFactory.createLineBorder(Color.black));
        screenKeys.add(lowDiff);
        JLabel separate=new JLabel("~");
        separate.setBounds(WW(4.5),HH(1),WW(1.5),HH(1));
        screenKeys.add(separate);
        uppDiff.setBounds(WW(5),HH(1),WW(1.5),HH(0.7));
        uppDiff.setBorder(BorderFactory.createLineBorder(Color.black));
        screenKeys.add(uppDiff);
        screenPane.add(screenKeys);

        MyPanel screenSort=new MyPanel();
        screenSort.setBorder(BorderFactory.createLineBorder(Color.black));
        screenSort.setLocation(0,HH(2));
        screenSort.setSize((WW(8)),HH(2));
        screenSort.setOpaque(false);
        screenSort.setLayout(null);
        sortKey.add(sortByDiff); sortKey.add(sortById); sortKey.add(sortByTitle); sortKey.add(sortByType);
        sortByDiff.setLocation(WW(0.5),HH(0.1));
        sortByDiff.setSize(WW(1.7),HH(0.5));
        screenSort.add(sortByDiff);
        sortByType.setLocation(WW(2.5),HH(0.1));
        sortByType.setSize(WW(1.7),HH(0.5));
        screenSort.add(sortByType);
        sortByTitle.setLocation(WW(0.5),HH(1.1));
        sortByTitle.setSize(WW(1.7),HH(0.5));
        screenSort.add(sortByTitle);
        sortById.setLocation(WW(2.5),HH(1.1));
        sortById.setSize(WW(1.7),HH(0.5));
        screenSort.add(sortById);
        MyButton screenButt=new MyButton("筛选");
        screenButt.setBounds(WW(5),HH(0.5),WW(1.5),HH(1));
        screenButt.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {reScreen();}});
        screenSort.add(screenButt);
        numOfScreened=new JLabel("共0题");
        numOfScreened.setBounds(WW(6.5),0,WW(3),HH(2));
        screenSort.add(numOfScreened);
        screenPane.add(screenSort);

        scrollScreen=new MyScrollPane();
        scrollScreen.setBounds(0,HH(4),WW(8),HH(22));
        scrollScreen.setLayout(null);
        scrollScreen.setOpaque(false);
        screenPane.add(scrollScreen);

        screen=new MyPanel();
        screen.setBounds(0,0,WW(8),HH(22));
        screen.setOpaque(false);
        screen.setLayout(null);
        scrollScreen.add(screen);

        MyButton createQuestion=new MyButton("新建题目");
        createQuestion.setBounds(0,HH(26),WW(8),HH(1));
        createQuestion.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {context.createQuestion();}});
        screenPane.add(createQuestion);

        pane.add(screenPane);
    }
    protected void createQuestion(){
        if(questionPane!=null)pane.remove(questionPane);
        questionPane=new MyPanel();
        questionPane.setSize(WW(19),HH(28));
        questionPane.setLocation(WW(1),HH(1));
        questionPane.setOpaque(false);
        questionPane.setLayout(null);

        questionPanel=new QuestionPanel(0,Consts.TYPE_SHORT_ANS,this,context);
        questionPane.add(questionPanel);

        pane.add(questionPane);
        setVisible(true);
    }
    private void reScreen(){
        try {
            screenPane.remove(scrollScreen);


            screen = new MyPanel();
            //screen.setBounds(0,0,WW(8),HH(22));
            //screen.setPreferredSize(new Dimension(WW(7),HH(22)));
            screen.setOpaque(false);
            screen.setLayout(null);

            int key = 0;
            if (sortById.isSelected()) key = Consts.KEY_ID;
            if (sortByTitle.isSelected()) key = Consts.KEY_TITLE;
            if (sortByType.isSelected()) key = Consts.KEY_TYPE;
            if (sortByDiff.isSelected()) key = Consts.KEY_DIFF;
            double low = Double.parseDouble(lowDiff.getText());
            double upp = Double.parseDouble(uppDiff.getText());
            if (low < 0 || upp > 1 || upp < low) throw new Exception();
            List<Question> list = context.screenQuestion(singleChoice.isSelected(), multiChoice.isSelected(),
                    fillBlank.isSelected(), shortAns.isSelected(),
                    low, upp, key);
            int y = 0;
            screen.setPreferredSize(new Dimension(WW(7), HH(2 * list.size() + 1)));
            for (Question q : list) {
                ScreenedQuestion question = new ScreenedQuestion(q.getId(), q.getTitle(), q.getDifficulty(), q.getScore(), q.getQuestionType(), y,
                        new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                showQuestion(q.getId());
                            }
                        });
                screen.add(question);
                y += HH(2);
            }
            numOfScreened.setText("共" + list.size() + "题");
            //scrollScreen.add(screen);
            scrollScreen = new MyScrollPane(screen);
            scrollScreen.setBounds(0, HH(4), WW(8), HH(22));
            //scrollScreen.setLayout(null);
            scrollScreen.setOpaque(false);
            screenPane.add(scrollScreen);
            screenPane.setVisible(true);
            repaint();
            setVisible(true);
        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"请输入合法的答案系数\n难度应为为0~1的实数\n且下限须小于上限");
        }
    }
    public void showQuestion(int id){
        if(questionPane!=null)pane.remove(questionPane);
        questionPane=new MyPanel();
        questionPane.setSize(WW(19),HH(28));
        questionPane.setLocation(WW(1),HH(1));
        questionPane.setOpaque(false);
        questionPane.setLayout(null);

        questionPanel=new QuestionPanel(context.findQuestion(id),this,context);
        questionPane.add(questionPanel);
        pane.add(questionPane);
        repaint();
        setVisible(true);
    }
    class QuestionPanel extends MyPanel{
        private MyTextArea title,description,difficulty,score;
        private AnswerPanel answerPanel;
        private ClientContext context;
        private JFrame thisFrame;
        MyButton delete=new MyButton("删除");
        MyButton save=new MyButton("保存");
        int type,id;
        Question q;
        QuestionPanel(int id,int type,JFrame thisFrame,ClientContext context){
            this.id=id; this.type=type; this.context=context;
            this.thisFrame=thisFrame;
            this.setSize(WW(19),HH(28));
            this.setLocation(0,0);
            this.setLayout(null);
            this.setOpaque(false);

            JLabel titleLabel=new JLabel("标题：");
            titleLabel.setBounds(0,0,WW(3),HH(1));
            this.add(titleLabel);

            title=new MyTextArea();
            title.setBorder(BorderFactory.createLineBorder(Color.black));
            title.setBounds(WW(3),0,WW(16),HH(1));
            title.setEditable(false);
            this.add(title);



            JLabel descriptionLabel=new JLabel("描述：");
            descriptionLabel.setBounds(0,HH(1),WW(3),HH(1));
            this.add(descriptionLabel);

            description=new MyTextArea();
            description.setLineWrap(true);
            //description.setBounds(WW(0),HH(0),WW(16),HH(10));
            description.setPreferredSize(new Dimension(WW(15),HH(100)));
            description.setEditable(false);
            MyScrollPane descriptionPane=new MyScrollPane(description);
            //descriptionPane.setLayout(null);
            descriptionPane.setBounds(WW(3),HH(1),WW(16),HH(10));
            this.add(descriptionPane);

            JLabel answerLabel=new JLabel("答案：");
            answerLabel.setBounds(0,HH(11),WW(3),HH(1));
            this.add(answerLabel);

            answerPanel=new AnswerPanel(type,thisFrame);
            answerPanel.setLocation(WW(3),HH(11));
            answerPanel.setEditable(false);
            //answerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            this.add(answerPanel);

            JLabel diffLabel=new JLabel("难度：");
            diffLabel.setBounds(0,HH(22),WW(3),HH(1));
            this.add(diffLabel);

            difficulty=new MyTextArea();
            difficulty.setBorder(BorderFactory.createLineBorder(Color.black));
            difficulty.setBounds(WW(3),HH(22),WW(3),HH(1));
            difficulty.setEditable(false);
            this.add(difficulty);

            JLabel scoreLabel=new JLabel("分值：");
            scoreLabel.setBounds(0,HH(23),WW(3),HH(1));
            this.add(scoreLabel);

            score=new MyTextArea();
            score.setBorder(BorderFactory.createLineBorder(Color.black));
            score.setBounds(WW(3),HH(23),WW(3),HH(1));
            score.setEditable(false);
            this.add(score);

            delete.setBounds(WW(14),HH(23),WW(5),HH(1.5));
            delete.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {context.deleteQuestion(id);}});
            delete.setEnabled(false);
            this.add(delete);


            save.setBounds(WW(14),HH(25),WW(5),HH(1.5));
            save.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {updateQuestion();}});
            save.setEnabled(false);
            this.add(save);
        }
        QuestionPanel(Question q,JFrame thisFrame,ClientContext context){
            this(q.getId(),q.getQuestionType(),thisFrame,context);
            this.q=q;
            title.setEditable(true); title.setText(q.getTitle());
            description.setEditable(true); description.setText(q.getDescription());
            answerPanel.setEditable(true);

            this.remove(answerPanel);
            if(type==Consts.TYPE_SHORT_ANS||type==Consts.TYPE_FILL_BLANK)
                    answerPanel=new AnswerPanel(q.getQuestionType(),thisFrame,q.getSubAnswer());
            else answerPanel=new AnswerPanel(q.getQuestionType(),thisFrame,q.getObjAnswer(),q.getNumOfOptions());
            answerPanel.setLocation(WW(3),HH(11));
            answerPanel.setEditable(true);
            //answerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            this.add(answerPanel);

            difficulty.setEditable(true); difficulty.setText(String.valueOf(q.getDifficulty()));
            score.setEditable(true); score.setText(String.valueOf(q.getScore()));
            delete.setEnabled(true); save.setEnabled(true);
            thisFrame.setVisible(true);
        }
        void updateQuestion(){
            try{
                double diff=Double.parseDouble(difficulty.getText());
                double sco=Double.parseDouble(score.getText());
                if(diff<0||diff>1||sco<0||sco>1000)throw new Exception();
                q.setDifficulty(diff); q.setScore(sco);
                if(type==Consts.TYPE_SHORT_ANS||type==Consts.TYPE_FILL_BLANK)
                    q.setSubAnswer(answerPanel.getSubAns());
                else {
                    if(answerPanel.getObjAns()==0)throw new Exception();
                    else q.setObjAnswer(answerPanel.getObjAns());
                }
                q.setNumOfOptions(answerPanel.numOfOptions);
                q.setTitle(title.getText());
                q.setDescription(description.getText());
                context.updateQuestion(id,q);
                JOptionPane.showMessageDialog(thisFrame, "保存成功");
            }catch (Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(thisFrame, "格式错误！\n难度为0~1的实数\n分数为0~1000的实数\n客观题至少选择一个选项");
            }
        }
        class AnswerPanel extends MyPanel{
            private JFrame thisFrame;
            private int type,numOfOptions;
            private MyCheckBox[] multiOptions=new MyCheckBox[26];
            private MyRadioButton[] singleOptions=new MyRadioButton[26];
            private ButtonGroup group=new ButtonGroup();
            private MyTextArea numOfOption;
            private MyTextArea subAnswer;
            private MyPanel objAnswer;
            private MyScrollPane subPane;
            AnswerPanel(int type,JFrame thisFrame){
                this.setSize(WW(16),HH(11));
                this.setLayout(null);
                this.setOpaque(false);
                this.type=type; this.thisFrame=thisFrame;
                switch (type){
                    case Consts.TYPE_FILL_BLANK:
                    case Consts.TYPE_SHORT_ANS:


                        subAnswer=new MyTextArea();
                        subAnswer.setLineWrap(true);
                        subAnswer.setBorder(BorderFactory.createLineBorder(Color.black));
                        //subAnswer.setBounds(0,0,WW(16),HH(11));
                        subAnswer.setPreferredSize(new Dimension(WW(15),HH(100)));

                        subPane=new MyScrollPane(subAnswer);
                        subPane.setBounds(0,0,WW(16),HH(11));
                        //subPane.setLayout(null);
                        this.add(subPane);
                        break;
                    case Consts.TYPE_SINGLE_CHOICE:
                    case Consts.TYPE_MULTI_CHOICE:
                        JLabel numLabel=new JLabel("答案数量：");
                        numLabel.setBounds(0,0,WW(3),HH(1));
                        this.add(numLabel);

                        numOfOption=new MyTextArea();
                        numOfOption.setBorder(BorderFactory.createLineBorder(Color.black));
                        numOfOption.setBounds(WW(3),0,WW(3),HH(1));
                        this.add(numOfOption);

                        MyButton refreshNum=new MyButton("确认");
                        refreshNum.setBounds(WW(6),0,WW(2),HH(1));
                        refreshNum.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {refresh();}});
                        this.add(refreshNum);

                        objAnswer=new MyPanel();
                        objAnswer.setLayout(new FlowLayout(FlowLayout.LEFT,WW(1),HH(1)));
                        objAnswer.setSize(WW(16),HH(10));
                        objAnswer.setLocation(0,HH(1));
                        this.add(objAnswer);

                        break;
                }
            }
            AnswerPanel(int type,JFrame thisFrame,int ans,int numOfOptions){
                this(type,thisFrame);
                for(int i=0;i<this.numOfOptions;i++){
                    if(multiOptions[i]!=null)objAnswer.remove(multiOptions[i]);
                    if(singleOptions[i]!=null)objAnswer.remove(singleOptions[i]);
                }
                numOfOption.setText(""+numOfOptions);
                int tmp=ans;
                this.numOfOptions=numOfOptions;
                for(int i=0;i<numOfOptions;i++){
                        switch (type){
                            case Consts.TYPE_MULTI_CHOICE:
                                multiOptions[i]=new MyCheckBox(""+(char)(i+'A'),tmp%2==1);
                                multiOptions[i].setSize(WW(2),HH(1));
                                objAnswer.add(multiOptions[i]);
                                break;
                            case Consts.TYPE_SINGLE_CHOICE:
                                singleOptions[i]=new MyRadioButton(""+(char)(i+'A'),tmp%2==1);
                                singleOptions[i].setSize(WW(2),HH(1));
                                group.add(singleOptions[i]);
                                objAnswer.add(singleOptions[i]);
                                break;
                        }
                    tmp/=2;
                }
                //this.thisFrame.setVisible(true);
            }
            AnswerPanel(int type,JFrame thisFrame,String ans){
                this(type,thisFrame);
                subAnswer.setText(ans);
            }
            public void setEditable(boolean b){if(subAnswer!=null)subAnswer.setEditable(b);}
            public void refresh(){
                if(numOfOption.getText().equals(""))return;
                try {
                    for(int i=0;i<numOfOptions;i++){
                        if(multiOptions[i]!=null)objAnswer.remove(multiOptions[i]);
                        if(singleOptions[i]!=null)objAnswer.remove(singleOptions[i]);
                    }
                    if(Integer.parseInt(numOfOption.getText())<=0||Integer.parseInt(numOfOption.getText())>26)throw new Exception();
                    numOfOptions = Integer.parseInt(numOfOption.getText());
                    for(int i=0;i<numOfOptions;i++)
                        switch (type){
                            case Consts.TYPE_MULTI_CHOICE:
                                multiOptions[i]=new MyCheckBox(""+(char)(i+'A'));
                                multiOptions[i].setSize(WW(2),HH(1));
                                objAnswer.add(multiOptions[i]);
                                break;
                            case Consts.TYPE_SINGLE_CHOICE:
                                singleOptions[i]=new MyRadioButton(""+(char)(i+'A'));
                                singleOptions[i].setSize(WW(2),HH(1));
                                group.add(singleOptions[i]);
                                objAnswer.add(singleOptions[i]);
                                break;
                        }
                    thisFrame.repaint();
                    thisFrame.setVisible(true);
                }catch (Exception e){
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(thisFrame, "请输入一个26以内的正整数！");
                }
            }
            public int getObjAns(){
                int ans=0;
                for(int i=0;i<numOfOptions;i++)
                    switch (type){
                        case Consts.TYPE_MULTI_CHOICE:
                            if(multiOptions[i].isSelected())ans+=(1<<i);
                            break;
                        case Consts.TYPE_SINGLE_CHOICE:
                            if(singleOptions[i].isSelected())ans+=(1<<i);
                            break;
                    }
                return ans;
            }
            public String getSubAns(){ return subAnswer.getText(); }
        }
    }
    class ScreenedQuestion extends MyPanel{
        JLabel id,title,diff,type,score;
        MyButton edit;
        ScreenedQuestion(int id,String title,double diff,double score,int type,int y,ActionListener listener){
            this.setLayout(null);
            this.setBounds(0,y,WW(7.6),HH(2));
            this.setLocation(WW(0.2),y);
            //this.setPreferredSize(new Dimension(WW(7.6),HH(2)));
            this.setBorder(BorderFactory.createLineBorder(Color.black));

            this.id=new JLabel(""+id);
            this.id.setBounds(WW(0.1),0,WW(2),HH(1));
            this.add(this.id);

            this.title=new JLabel(title);
            this.title.setBounds(WW(2),0,WW(6),HH(1));
            this.add(this.title);

            this.diff=new JLabel("难度："+diff);
            this.diff.setBounds(WW(0.5),HH(1),WW(1.5),HH(1));
            this.add(this.diff);

            this.score=new JLabel("分数："+score);
            this.score.setBounds(WW(2.25),HH(1),WW(1.5),HH(1));
            this.add(this.score);

            String tpe="";
            switch (type){
                case Consts.TYPE_FILL_BLANK:
                    tpe="填空";
                    break;
                case Consts.TYPE_MULTI_CHOICE:
                    tpe="多选";
                    break;
                case Consts.TYPE_SHORT_ANS:
                    tpe="简答";
                    break;
                case Consts.TYPE_SINGLE_CHOICE:
                    tpe="单选";
                    break;
            }
            this.type=new JLabel(tpe);
            this.type.setBounds(WW(4),HH(1),WW(2),HH(1));
            this.add(this.type);

            this.edit=new MyButton("编辑");
            this.edit.setBounds(WW(6),HH(1),WW(1.5),HH(0.7));
            this.edit.addActionListener(listener);
            this.add(this.edit);
        }
    }
}