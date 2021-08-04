package com.company.gui;

import com.company.bean.Question;
import com.company.bean.TestPaper;
import com.company.gui.my_component.*;
import com.company.utils.Consts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class ArrangePaperFrame extends BaseFrame{
    TestPaper paper;

    private MyPanel pane;
    private MyPanel screenPane;
    private MyPanel screen;
    private MyPanel questionPane;
    private QuestionPanel questionPanel;
    private MyPanel testPaperPane;
    private PaperInfoPanel paperInfoPanel;
    private PaperQuestionPanel paperQuestionPanel;

    private MyScrollPane scrollScreen;
    private MyScrollPane scrollPaper;

    //<editor-fold desc="Pieces">

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

    //</editor-fold>

    public ArrangePaperFrame(){init();}
    public ArrangePaperFrame(ClientContext context,TestPaper paper){
        this();
        this.context=context;
        setPaper(paper);
        fillData();
    }
    private void fillData(){reScreen();refreshPaper();}
    protected void init() {

        ImageIcon icon=new ImageIcon("src/com/company/img/arrange.jpg" );

        JLabel backGround=new JLabel(icon);
        backGround.setBounds(0,0,WIDTH,HEIGHT);
        JPanel myPanel = (JPanel)this.getContentPane();		//把我的面板设置为内容面板
        myPanel.setOpaque(false);					//把我的面板设置为不可视
        myPanel.setLayout(null);		//把我的面板设置为流动布局
        this.getLayeredPane().setLayout(null);
        this.getLayeredPane().add(backGround, new Integer(Integer.MIN_VALUE));
        
        this.setSize(WIDTH,HEIGHT);
        this.setLocation(0,0);
        this.setTitle("编辑试卷");
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
        createTestPaper();

        MyButton back=new MyButton("返回");
        back.setBounds(0,0,WW(1),HH(1));
        back.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {context.backToTeacherMenu();}});
        pane.add(back);

        MyButton save=new MyButton("保存并返回");
        save.setBounds(WW(1),0,WW(2),HH(1));
        save.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent ee) {
            try {
                context.updatePaper(getPaper());
            }catch (Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,"请正确输入时间");
                return;
            }
            context.backToTeacherMenu();
        }});
        pane.add(save);

        return pane;
    }
    private void createScreen(){
        JLabel baseLabel=new JLabel("手动从题库中添加题目：");
        baseLabel.setBounds(WW(21),HH(1),WW(8),HH(0.7));
        pane.add(baseLabel);

        screenPane=new MyPanel();
        screenPane.setSize(WW(8),HH(28));
        screenPane.setLocation(WW(21),HH(2));
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

        /*MyButton createQuestion=new MyButton("新建题目");
        createQuestion.setBounds(0,HH(26),WW(8),HH(1));
        createQuestion.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {context.createQuestion();}});
        screenPane.add(createQuestion);*/

        pane.add(screenPane);
    }
    protected void createQuestion(){
        JLabel questionLabel=new JLabel("题目预览：");
        questionLabel.setBounds(WW(1),HH(1),WW(2),HH(1));
        pane.add(questionLabel);

        if(questionPane!=null)pane.remove(questionPane);
        questionPane=new MyPanel();
        questionPane.setSize(WW(10),HH(28));
        questionPane.setLocation(WW(1),HH(2));
        questionPane.setOpaque(false);
        questionPane.setLayout(null);

        questionPanel=new QuestionPanel(0, Consts.TYPE_SHORT_ANS,this,context);
        questionPane.add(questionPanel);

        pane.add(questionPane);
        setVisible(true);
    }
    private void createTestPaper(){
        testPaperPane=new MyPanel();
        testPaperPane.setLocation(WW(12),HH(1));
        testPaperPane.setLayout(null);
        testPaperPane.setSize(WW(8),HH(28));

        paperInfoPanel=new PaperInfoPanel(this,context);
        testPaperPane.add(paperInfoPanel);


        paperQuestionPanel=new PaperQuestionPanel(this,context);
        //testPaperPane.add(paperQuestionPanel);

        scrollPaper=new MyScrollPane(paperQuestionPanel);
        scrollPaper.setBounds(0,HH(9),WW(8),HH(18));
        testPaperPane.add(scrollPaper);

        pane.add(testPaperPane);
    }
    private void reScreen(){
        screenPane.remove(scrollScreen);


        screen=new MyPanel();
        //screen.setBounds(0,0,WW(8),HH(22));
        //screen.setPreferredSize(new Dimension(WW(7),HH(22)));
        screen.setOpaque(false);
        screen.setLayout(null);

        int key=0;
        if(sortById.isSelected())key=Consts.KEY_ID;
        if(sortByTitle.isSelected())key=Consts.KEY_TITLE;
        if(sortByType.isSelected())key=Consts.KEY_TYPE;
        if(sortByDiff.isSelected())key=Consts.KEY_DIFF;
        List<Question> list=context.screenQuestion(singleChoice.isSelected(),multiChoice.isSelected(),
                fillBlank.isSelected(),shortAns.isSelected(),
                Double.parseDouble(lowDiff.getText()),
                Double.parseDouble(uppDiff.getText()),key);
        int y=0;
        screen.setPreferredSize(new Dimension(WW(7),HH(2*list.size()+1)));
        for(Question q:list){
            ScreenedQuestion question=new ScreenedQuestion(q.getId(), q.getTitle(), q.getDifficulty(), q.getScore(), q.getQuestionType(), y,
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            showQuestion(q.getId());
                        }
                    },
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            addQuestion(q.getId());
                        }
                    });
            screen.add(question);
            y += HH(2);
        }
        numOfScreened.setText("共"+list.size()+"题");
        //scrollScreen.add(screen);
        scrollScreen=new MyScrollPane(screen);
        scrollScreen.setBounds(0,HH(4),WW(8),HH(22));
        //scrollScreen.setLayout(null);
        scrollScreen.setOpaque(false);
        screenPane.add(scrollScreen);
        screenPane.setVisible(true);
        repaint();
        setVisible(true);
    }
    public void showQuestion(int id){
        if(questionPane!=null)pane.remove(questionPane);
        questionPane=new MyPanel();
        questionPane.setSize(WW(19),HH(28));
        questionPane.setLocation(WW(1),HH(2));
        questionPane.setOpaque(false);
        questionPane.setLayout(null);

        questionPanel=new QuestionPanel(context.findQuestion(id),this,context);
        questionPane.add(questionPanel);
        pane.add(questionPane);
        repaint();
        setVisible(true);
    }
    public void addQuestion(int id){paper.addQuestion(id);refreshPaper();}
    public void removeQuestion(int id){paper.removeQuestion(id);refreshPaper();}
    public TestPaper getPaper(){
        try {
            paper.setTitle(paperInfoPanel.getTitle());
            paper.setTimeLimit(paperInfoPanel.getTimeLimit());
            return paper;
        }catch (Exception e){
            e.printStackTrace();
            //JOptionPane.showMessageDialog(this,"请正确输入时间");
            throw e;
        }
    }
    protected void refreshPaper(){
        try{
            int tmp=Integer.parseInt(paperInfoPanel.hour.getText()) * 3600 +
                    Integer.parseInt(paperInfoPanel.min.getText()) * 60 +
                    Integer.parseInt(paperInfoPanel.sec.getText());//避免生成时调用
            paper=getPaper();//防止标题时限未保存
        }catch (Exception e){
            e.printStackTrace();
        }

        testPaperPane.remove(paperInfoPanel);
        paperInfoPanel=new PaperInfoPanel(this,context,paper);
        testPaperPane.add(paperInfoPanel);

        testPaperPane.remove(scrollPaper);
        paperQuestionPanel=new PaperQuestionPanel(this,context,paper);
        //testPaperPane.add(paperQuestionPanel);

        scrollPaper=new MyScrollPane(paperQuestionPanel);
        scrollPaper.setBounds(0,HH(9),WW(8),HH(18));
        testPaperPane.add(scrollPaper);

        repaint();
        setVisible(true);
    }
    public void setPaper(TestPaper paper){this.paper=paper;}
    class QuestionPanel extends MyPanel{
        private MyTextArea title,description,difficulty,score;
        private QuestionPanel.AnswerPanel answerPanel;
        private ClientContext context;
        private JFrame thisFrame;
        /*MyButton delete=new MyButton("删除");
        MyButton save=new MyButton("保存");*/
        int type,id;
        Question q;
        QuestionPanel(int id,int type,JFrame thisFrame,ClientContext context){
            this.id=id; this.type=type; this.context=context;
            this.thisFrame=thisFrame;
            this.setSize(WW(10),HH(28));
            this.setLocation(0,0);
            this.setLayout(null);
            this.setOpaque(false);

            JLabel titleLabel=new JLabel("标题：");
            titleLabel.setBounds(0,0,WW(1),HH(1));
            this.add(titleLabel);

            title=new MyTextArea();
            title.setBorder(BorderFactory.createLineBorder(Color.black));
            title.setBounds(WW(1),0,WW(9),HH(1));
            title.setEditable(false);
            title.setBackground(new Color(238,238,238));
            this.add(title);



            JLabel descriptionLabel=new JLabel("描述：");
            descriptionLabel.setBounds(0,HH(1),WW(1),HH(1));
            this.add(descriptionLabel);

            description=new MyTextArea();
            description.setLineWrap(true);
            description.setBackground(new Color(238,238,238));
            //description.setBounds(WW(0),HH(0),WW(16),HH(10));
            description.setPreferredSize(new Dimension(WW(8),HH(100)));
            description.setEditable(false);
            MyScrollPane descriptionPane=new MyScrollPane(description);
            //descriptionPane.setLayout(null);
            descriptionPane.setBounds(WW(1),HH(1),WW(9),HH(10));
            this.add(descriptionPane);

            JLabel answerLabel=new JLabel("答案：");
            answerLabel.setBounds(0,HH(11),WW(1),HH(1));
            this.add(answerLabel);

            answerPanel=new AnswerPanel(type,thisFrame);
            answerPanel.setLocation(WW(1),HH(11));
            answerPanel.setEditable(false);
            //answerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            this.add(answerPanel);

            JLabel diffLabel=new JLabel("难度：");
            diffLabel.setBounds(0,HH(22),WW(1),HH(1));
            this.add(diffLabel);

            difficulty=new MyTextArea();
            difficulty.setBackground(new Color(238,238,238));
            difficulty.setBorder(BorderFactory.createLineBorder(Color.black));
            difficulty.setBounds(WW(1),HH(22),WW(3),HH(1));
            difficulty.setEditable(false);
            this.add(difficulty);

            JLabel scoreLabel=new JLabel("分值：");
            scoreLabel.setBounds(0,HH(23),WW(1),HH(1));
            this.add(scoreLabel);

            score=new MyTextArea();
            score.setBackground(new Color(238,238,238));
            score.setBorder(BorderFactory.createLineBorder(Color.black));
            score.setBounds(WW(1),HH(23),WW(3),HH(1));
            score.setEditable(false);
            this.add(score);

            /*delete.setBounds(WW(14),HH(23),WW(5),HH(1.5));
            delete.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {context.deleteQuestion(id);}});
            delete.setEnabled(false);
            this.add(delete);


            save.setBounds(WW(14),HH(25),WW(5),HH(1.5));
            save.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {updateQuestion();}});
            save.setEnabled(false);
            this.add(save);*/
        }
        QuestionPanel(Question q,JFrame thisFrame,ClientContext context){
            this(q.getId(),q.getQuestionType(),thisFrame,context);
            this.q=q;

            title.setText(q.getTitle());//title.setEditable(true);
             description.setText(q.getDescription());//description.setEditable(true);

            this.remove(answerPanel);
            if(type==Consts.TYPE_SHORT_ANS||type==Consts.TYPE_FILL_BLANK)
                answerPanel=new AnswerPanel(q.getQuestionType(),thisFrame,q.getSubAnswer());
            else answerPanel=new AnswerPanel(q.getQuestionType(),thisFrame,q.getObjAnswer(),q.getNumOfOptions());
            answerPanel.setLocation(WW(1),HH(11));
            answerPanel.setEditable(false);
            //answerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            this.add(answerPanel);

            difficulty.setText(String.valueOf(q.getDifficulty()));//difficulty.setEditable(true);
            score.setText(String.valueOf(q.getScore()));//score.setEditable(true);
            //delete.setEnabled(true); save.setEnabled(true);
            thisFrame.setVisible(true);
        }
        /*void updateQuestion(){
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
        }*/
        class AnswerPanel extends MyPanel{
            private JFrame thisFrame;
            private int type,numOfOptions;
            private MyCheckBox[] multiOptions=new MyCheckBox[26];
            private MyRadioButton[] singleOptions=new MyRadioButton[26];
            private ButtonGroup group=new ButtonGroup();
            //private MyTextArea numOfOption;
            private MyTextArea subAnswer;
            private MyPanel objAnswer;
            private MyScrollPane subPane;
            AnswerPanel(int type,JFrame thisFrame){
                this.setSize(WW(9),HH(11));
                this.setLayout(null);
                this.setOpaque(false);
                this.type=type; this.thisFrame=thisFrame;
                switch (type){
                    case Consts.TYPE_FILL_BLANK:
                    case Consts.TYPE_SHORT_ANS:


                        subAnswer=new MyTextArea();
                        subAnswer.setBackground(new Color(238,238,238));
                        subAnswer.setLineWrap(true);
                        subAnswer.setBorder(BorderFactory.createLineBorder(Color.black));
                        //subAnswer.setBounds(0,0,WW(16),HH(11));
                        subAnswer.setPreferredSize(new Dimension(WW(8),HH(100)));

                        subPane=new MyScrollPane(subAnswer);
                        subPane.setBounds(0,0,WW(9),HH(11));
                        //subPane.setLayout(null);
                        this.add(subPane);
                        break;
                    case Consts.TYPE_SINGLE_CHOICE:
                    case Consts.TYPE_MULTI_CHOICE:
                        /*JLabel numLabel=new JLabel("答案数量：");
                        numLabel.setBounds(0,0,WW(3),HH(1));
                        this.add(numLabel);

                        numOfOption=new MyTextArea();
                        numOfOption.setBorder(BorderFactory.createLineBorder(Color.black));
                        numOfOption.setBounds(WW(3),0,WW(3),HH(1));
                        this.add(numOfOption);

                        MyButton refreshNum=new MyButton("确认");
                        refreshNum.setBounds(WW(6),0,WW(2),HH(1));
                        refreshNum.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {refresh();}});
                        this.add(refreshNum);*/

                        objAnswer=new MyPanel();
                        objAnswer.setLayout(new FlowLayout(FlowLayout.LEFT,WW(1),HH(1)));
                        objAnswer.setSize(WW(9),HH(10));
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
                //numOfOption.setText(""+numOfOptions);
                int tmp=ans;
                this.numOfOptions=numOfOptions;
                for(int i=0;i<numOfOptions;i++){
                    switch (type){
                        case Consts.TYPE_MULTI_CHOICE:
                            multiOptions[i]=new MyCheckBox(""+(char)(i+'A'),tmp%2==1);
                            multiOptions[i].setSize(WW(2),HH(1));
                            multiOptions[i].setEnabled(false);
                            objAnswer.add(multiOptions[i]);
                            break;
                        case Consts.TYPE_SINGLE_CHOICE:
                            singleOptions[i]=new MyRadioButton(""+(char)(i+'A'),tmp%2==1);
                            singleOptions[i].setSize(WW(2),HH(1));
                            singleOptions[i].setEnabled(false);
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
            /*public void refresh(){
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
                    thisFrame.setVisible(true);
                }catch (Exception e){
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(thisFrame, "请输入一个26以内的正整数！");
                }
            }*/
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
        MyButton edit,adding;
        ScreenedQuestion(int id,String title,double diff,double score,int type,int y,ActionListener viewListener,ActionListener addListener){
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

            this.edit=new MyButton("预览");
            this.edit.setBounds(WW(5),HH(1),WW(1.5),HH(0.7));
            this.edit.addActionListener(viewListener);
            this.add(this.edit);

            this.adding=new MyButton("添加");
            this.adding.setBounds(WW(6.5),HH(1),WW(1),HH(0.7));
            this.adding.addActionListener(addListener);
            this.add(this.adding);
        }
    }
    class PaperInfoPanel extends MyPanel{
        private JFrame thisFrame;
        private ClientContext context;

        protected MyTextArea title,hour,min,sec;
        private MyTextArea numOfSingle,numOfMulti,numOfFill,numOfShort;
        private MyTextArea scoreOfSingle,scoreOfMulti,scoreOfFill,scoreOfShort;
        private MyTextArea diff;

        private JLabel id;
        private JLabel realNumOfSingle,realNumOfMulti,realNumOfFill,realNumOfShort;
        private JLabel realScoreOfSingle,realScoreOfMulti,realScoreOfFill,realScoreOfShort;
        private JLabel diffOfSingle,diffOfMulti,diffOfFill,diffOfShort;
        private JLabel realNum,realScore,realDiff;

        PaperInfoPanel(JFrame thisFrame,ClientContext context){
            this.context=context; this.thisFrame=thisFrame;

            this.setSize(WW(8),HH(9));
            this.setLocation(0,0);
            this.setLayout(null);
            this.setBorder(BorderFactory.createLineBorder(Color.black));

            //<editor-fold desc="Main Info">
            MyPanel mainInfo=new MyPanel();
            mainInfo.setSize(WW(8),HH(2));
            mainInfo.setLocation(0,0);
            mainInfo.setLayout(null);
            mainInfo.setBorder(BorderFactory.createLineBorder(Color.black));

            id=new JLabel("id：");
            id.setBounds(0,0,WW(1.5),HH(1));
            mainInfo.add(id);

            JLabel titleLabel=new JLabel("考试名：");
            titleLabel.setBounds(WW(2.5),0,WW(3.5),HH(1));
            mainInfo.add(titleLabel);

            title=new MyTextArea();
            title.setBounds(WW(3.5),HH(0.2),WW(3.5),HH(0.6));
            title.setBorder(BorderFactory.createLineBorder(Color.black));
            mainInfo.add(title);

            JLabel timeLabel=new JLabel("时限：");
            timeLabel.setBounds(0,HH(1),WW(1),HH(1));
            mainInfo.add(timeLabel);

            hour=new MyTextArea();
            hour.setBounds(WW(1),HH(1+0.2),WW(1),HH(1-0.4));
            hour.setBorder(BorderFactory.createLineBorder(Color.black));
            mainInfo.add(hour);

            JLabel hourLabel=new JLabel("时");
            hourLabel.setBounds(WW(2),HH(1),WW(0.5),HH(1));
            mainInfo.add(hourLabel);

            min=new MyTextArea();
            min.setBounds(WW(2.5),HH(1+0.2),WW(1),HH(1-0.4));
            min.setBorder(BorderFactory.createLineBorder(Color.black));
            mainInfo.add(min);

            JLabel minLabel=new JLabel("分");
            minLabel.setBounds(WW(3.5),HH(1),WW(0.5),HH(1));
            mainInfo.add(minLabel);

            sec=new MyTextArea();
            sec.setBounds(WW(4),HH(1+0.2),WW(1),HH(1-0.4));
            sec.setBorder(BorderFactory.createLineBorder(Color.black));
            mainInfo.add(sec);

            JLabel secLabel=new JLabel("秒");
            secLabel.setBounds(WW(5),HH(1),WW(0.5),HH(1));
            mainInfo.add(secLabel);

            this.add(mainInfo);
            //</editor-fold>

            //<editor-fold desc="Num Info">
            MyPanel numInfo=new MyPanel();
            numInfo.setLocation(0,HH(2));
            numInfo.setSize(WW(8),HH(7));
            numInfo.setBorder(BorderFactory.createLineBorder(Color.black));
            numInfo.setLayout(null);

            MyPanel paras=new MyPanel();
            paras.setLocation(0,0);
            paras.setSize(WW(8),HH(1));
            paras.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            paras.setLayout(null);

            JLabel numPara=new JLabel("数量");
            numPara.setBounds(WW(2),0,WW(1),HH(1));
            paras.add(numPara);

            JLabel scorePara=new JLabel("分数");
            scorePara.setBounds(WW(4),0,WW(1),HH(1));
            paras.add(scorePara);

            JLabel diffPara=new JLabel("难度系数");
            diffPara.setBounds(WW(6),0,WW(2),HH(1));
            paras.add(diffPara);

            numInfo.add(paras);

            MyPanel types=new MyPanel();
            types.setSize(WW(8),HH(4));
            types.setLocation(WW(0),HH(1));
            types.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            types.setLayout(null);

            //<editor-fold desc="All Type Paras">

            JLabel singleLabel=new JLabel("单选");
            singleLabel.setBounds(0,0,WW(1),HH(1));
            types.add(singleLabel);

            numOfSingle=new MyTextArea();
            numOfSingle.setBounds(WW(1),HH(0.2),WW(1),HH(1-0.4));
            numOfSingle.setBorder(BorderFactory.createLineBorder(Color.black));
            types.add(numOfSingle);

            realNumOfSingle=new JLabel();
            realNumOfSingle.setBounds(WW(2),0,WW(1),HH(1));
            types.add(realNumOfSingle);

            scoreOfSingle=new MyTextArea();
            scoreOfSingle.setBounds(WW(3),HH(0.2),WW(1),HH(1-0.4));
            scoreOfSingle.setBorder(BorderFactory.createLineBorder(Color.black));
            types.add(scoreOfSingle);

            realScoreOfSingle=new JLabel();
            realScoreOfSingle.setBounds(WW(4),0,WW(1),HH(1));
            types.add(realScoreOfSingle);

            diffOfSingle=new JLabel();
            diffOfSingle.setBounds(WW(5),0,WW(3),HH(1));
            types.add(diffOfSingle);

            JLabel multiLabel=new JLabel("多选");
            multiLabel.setBounds(WW(0),HH(1),WW(1),HH(1));
            types.add(multiLabel);

            numOfMulti=new MyTextArea();
            numOfMulti.setBounds(WW(1),HH(1+0.2),WW(1),HH(1-0.4));
            numOfMulti.setBorder(BorderFactory.createLineBorder(Color.black));
            types.add(numOfMulti);

            realNumOfMulti=new JLabel();
            realNumOfMulti.setBounds(WW(2),HH(1),WW(1),HH(1));
            types.add(realNumOfMulti);

            scoreOfMulti=new MyTextArea();
            scoreOfMulti.setBounds(WW(3),HH(1+0.2),WW(1),HH(1-0.4));
            scoreOfMulti.setBorder(BorderFactory.createLineBorder(Color.black));
            types.add(scoreOfMulti);

            realScoreOfMulti=new JLabel();
            realScoreOfMulti.setBounds(WW(4),HH(1),WW(1),HH(1));
            types.add(realScoreOfMulti);

            diffOfMulti=new JLabel();
            diffOfMulti.setBounds(WW(5),HH(1),WW(3),HH(1));
            types.add(diffOfMulti);

            JLabel fillLabel=new JLabel("填空");
            fillLabel.setBounds(0,HH(2),WW(1),HH(1));
            types.add(fillLabel);

            numOfFill=new MyTextArea();
            numOfFill.setBounds(WW(1),HH(2+0.2),WW(1),HH(1-0.4));
            numOfFill.setBorder(BorderFactory.createLineBorder(Color.black));
            types.add(numOfFill);

            realNumOfFill=new JLabel();
            realNumOfFill.setBounds(WW(2),HH(2),WW(1),HH(1));
            types.add(realNumOfFill);

            scoreOfFill=new MyTextArea();
            scoreOfFill.setBounds(WW(3),HH(2+0.2),WW(1),HH(1-0.4));
            scoreOfFill.setBorder(BorderFactory.createLineBorder(Color.black));
            types.add(scoreOfFill);

            realScoreOfFill=new JLabel();
            realScoreOfFill.setBounds(WW(4),HH(2),WW(1),HH(1));
            types.add(realScoreOfFill);

            diffOfFill=new JLabel();
            diffOfFill.setBounds(WW(5),HH(2),WW(3),HH(1));
            types.add(diffOfFill);

            JLabel shortLabel=new JLabel("简答");
            shortLabel.setBounds(0,HH(3),WW(1),HH(1));
            types.add(shortLabel);

            numOfShort=new MyTextArea();
            numOfShort.setBounds(WW(1),HH(3+0.2),WW(1),HH(1-0.4));
            numOfShort.setBorder(BorderFactory.createLineBorder(Color.black));
            types.add(numOfShort);

            realNumOfShort=new JLabel();
            realNumOfShort.setBounds(WW(2),HH(3),WW(1),HH(1));
            types.add(realNumOfShort);

            scoreOfShort=new MyTextArea();
            scoreOfShort.setBounds(WW(3),HH(3+0.2),WW(1),HH(1-0.4));
            scoreOfShort.setBorder(BorderFactory.createLineBorder(Color.black));
            types.add(scoreOfShort);

            realScoreOfShort=new JLabel();
            realScoreOfShort.setBounds(WW(4),HH(3),WW(1),HH(1));
            types.add(realScoreOfShort);

            diffOfShort=new JLabel();
            diffOfShort.setBounds(WW(5),HH(3),WW(3),HH(1));
            types.add(diffOfShort);

            //</editor-fold>

            numInfo.add(types);

            MyPanel general=new MyPanel();
            general.setSize(WW(8),HH(1));
            general.setLocation(0,HH(5));
            general.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            general.setLayout(null);

            JLabel genLabel=new JLabel("总计");
            genLabel.setBounds(0,0,WW(1),HH(1));
            general.add(genLabel);

            realNum=new JLabel();
            realNum.setBounds(WW(2),0,WW(1),HH(1));
            general.add(realNum);

            realScore=new JLabel();
            realScore.setBounds(WW(4),0,WW(1),HH(1));
            general.add(realScore);

            diff=new MyTextArea();
            diff.setBounds(WW(5),HH(0.2),WW(1),HH(1-0.4));
            diff.setBorder(BorderFactory.createLineBorder(Color.black));
            general.add(diff);

            realDiff=new JLabel();
            realDiff.setBounds(WW(6),0,WW(2),HH(1));
            general.add(realDiff);

            numInfo.add(general);


            //</editor-fold>

            MyButton generate=new MyButton("自动生成");
            generate.setBounds(WW(0),HH(6),WW(8),HH(1));
            generate.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {autoGenerate();}});
            numInfo.add(generate);

            this.add(numInfo);

        }
        PaperInfoPanel(JFrame thisFrame,ClientContext context,TestPaper paper){
            this(thisFrame,context);
            updateParas();
        }
        private void updateParas(){
            id.setText("id："+paper.getId());
            title.setText(paper.getTitle());
            hour.setText(""+paper.getTimeLimit()/3600);
            min.setText(""+(paper.getTimeLimit()/60)%60);
            sec.setText(""+paper.getTimeLimit()%60);

            realNumOfSingle.setText(""+context.getNumOfType(paper,Consts.TYPE_SINGLE_CHOICE));
            realScoreOfSingle.setText(""+context.getScoreOfType(paper,Consts.TYPE_SINGLE_CHOICE));
            diffOfSingle.setText(""+context.getDiffOfType(paper,Consts.TYPE_SINGLE_CHOICE));

            realNumOfMulti.setText(""+context.getNumOfType(paper,Consts.TYPE_MULTI_CHOICE));
            realScoreOfMulti.setText(""+context.getScoreOfType(paper,Consts.TYPE_MULTI_CHOICE));
            diffOfMulti.setText(""+context.getDiffOfType(paper,Consts.TYPE_MULTI_CHOICE));

            realNumOfFill.setText(""+context.getNumOfType(paper,Consts.TYPE_FILL_BLANK));
            realScoreOfFill.setText(""+context.getScoreOfType(paper,Consts.TYPE_FILL_BLANK));
            diffOfFill.setText(""+context.getDiffOfType(paper,Consts.TYPE_FILL_BLANK));

            realNumOfShort.setText(""+context.getNumOfType(paper,Consts.TYPE_SHORT_ANS));
            realScoreOfShort.setText(""+context.getScoreOfType(paper,Consts.TYPE_SHORT_ANS));
            diffOfShort.setText(""+context.getDiffOfType(paper,Consts.TYPE_SHORT_ANS));

            realNum.setText(""+paper.getNumOfQuestions());
            realScore.setText(""+context.getScoreOfType(paper,Consts.TYPE_ALL));
            realDiff.setText(""+context.getDiffOfType(paper,Consts.TYPE_ALL));
        }
        private void autoGenerate(){
            try {
                paper = context.generatePaper(
                        numOfSingle.getText().equals("") ? (0) : Integer.parseInt(numOfSingle.getText()),
                        numOfMulti.getText().equals("") ? (0) : Integer.parseInt(numOfMulti.getText()),
                        numOfFill.getText().equals("") ? (0) : Integer.parseInt(numOfFill.getText()),
                        numOfShort.getText().equals("") ? (0) : Integer.parseInt(numOfShort.getText()),
                        scoreOfSingle.getText().equals("") ? (0) : Double.parseDouble(scoreOfSingle.getText()),
                        scoreOfMulti.getText().equals("") ? (0) : Double.parseDouble(scoreOfMulti.getText()),
                        scoreOfFill.getText().equals("") ? (0) : Double.parseDouble(scoreOfFill.getText()),
                        scoreOfShort.getText().equals("") ? (0) : Double.parseDouble(scoreOfShort.getText()),
                        diff.getText().equals("") ? (0.6) : Double.parseDouble(diff.getText()));
                paper=getPaper();
                refreshPaper();
                updateParas();
            }catch (Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(thisFrame, "请输入合法数字！");
            }
        }
        public String getTitle(){return title.getText();}
        public int getTimeLimit(){
            try {
                return Integer.parseInt(hour.getText()) * 3600 +
                        Integer.parseInt(min.getText()) * 60 +
                        Integer.parseInt(sec.getText());
            }catch (Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(thisFrame,"请正确输入时间");
                throw e;
            }
        }
    }
    class PaperQuestionPanel extends MyPanel{

        private JFrame thisFrame;
        private ClientContext context;
        private ArrangedQuestion[] questions;
        private MyPanel type[];
        private MyButton typeButton[];
        private int y[],status[];
        List<Question> list;
        public PaperQuestionPanel(JFrame thisFrame,ClientContext context){
            this.thisFrame=thisFrame; this.context=context;


            this.setLayout(new FlowLayout());
            this.setBorder(BorderFactory.createLineBorder(Color.black));


        }
        public PaperQuestionPanel(JFrame thisFrame,ClientContext context,TestPaper paper){
            this(thisFrame,context);

            this.setPreferredSize(new Dimension(WW(7),HH(2*paper.getNumOfQuestions()+5)));

            questions=new ArrangedQuestion[paper.getNumOfQuestions()];
            type=new MyPanel[4]; y=new int[4]; typeButton=new MyButton[4]; status=new int[4];
            for(int i=0;i<4;i++) {
                type[i] = new MyPanel();
                type[i].setPreferredSize(new Dimension(WW(7.8), HH(2*context.getNumOfType(paper, i+1))));
                type[i].setLayout(null);

                String tpe="";switch (i+1){
                    case Consts.TYPE_FILL_BLANK:
                        tpe="收起填空";
                        break;
                    case Consts.TYPE_MULTI_CHOICE:
                        tpe="收起多选";
                        break;
                    case Consts.TYPE_SHORT_ANS:
                        tpe="收起简答";
                        break;
                    case Consts.TYPE_SINGLE_CHOICE:
                        tpe="收起单选";
                        break;
                }
                typeButton[i]=new MyButton(tpe);
                typeButton[i].setPreferredSize(new Dimension(WW(8),HH(1)));
                int finalI = i;
                typeButton[i].addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
                        if(status[finalI]==0){
                            fold(finalI);
                            typeButton[finalI].setText("展开"+typeButton[finalI].getText().substring(2));
                        }else {
                            extend(finalI);
                            typeButton[finalI].setText("收起"+typeButton[finalI].getText().substring(2));
                        }}});
            }
            this.list=context.getQuestions(paper);
            int i=0;
            for(Question q:list){
                //System.out.println(i);
                questions[i]=new ArrangedQuestion(q,y[q.getQuestionType()-1], new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        showQuestion(q.getId());
                    }}, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        removeQuestion(q.getId());}});
                type[q.getQuestionType()-1].add(questions[i++]);
                y[q.getQuestionType()-1]+=HH(2);
            }
            for(i=0;i<4;i++){
                this.add(typeButton[i]);
                this.add(type[i]);
            }
        }
        private void fold(int type){
            status[type]=1;
            this.removeAll();
            for(int i=0;i<4;i++){
                this.add(typeButton[i]);
                if(status[i]==0)this.add(this.type[i]);
            }
            thisFrame.repaint();
            thisFrame.setVisible(true);
        }
        private void extend(int type){
            status[type]=0;
            this.removeAll();
            for(int i=0;i<4;i++){
                this.add(typeButton[i]);
                if(status[i]==0)this.add(this.type[i]);
            }
            thisFrame.repaint();
            thisFrame.setVisible(true);
        }
        class ArrangedQuestion extends MyPanel{
            private Question q;
            JLabel id,title,diff,type,score;
            MyButton edit,remove;
            public ArrangedQuestion(Question q,int y,ActionListener editListener,ActionListener removeListener){
                this.q=q;

                this.setLayout(null);
                this.setBounds(0,y,WW(7.4),HH(2));
                this.setLocation(WW(0.2),y);
                //this.setPreferredSize(new Dimension(WW(7.6),HH(2)));
                this.setBorder(BorderFactory.createLineBorder(Color.black));



                this.title=new JLabel(q.getTitle());
                this.title.setBounds(WW(2),0,WW(6),HH(1));
                this.add(this.title);

                this.diff=new JLabel("难度："+q.getDifficulty());
                this.diff.setBounds(WW(0.5),HH(1),WW(1.5),HH(1));
                this.add(this.diff);

                this.score=new JLabel("分数："+q.getScore());
                this.score.setBounds(WW(2.25),HH(1),WW(1.5),HH(1));
                this.add(this.score);

                String tpe="";
                switch (q.getQuestionType()){
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
                /*this.type=new JLabel(tpe);
                this.type.setBounds(WW(4),HH(1),WW(2),HH(1));
                this.add(this.type);*/

                this.id=new JLabel(tpe+"第"+(y/HH(2)+1)+"题");
                this.id.setBounds(WW(0.1),0,WW(2),HH(1));
                this.add(this.id);

                this.edit=new MyButton("预览");
                this.edit.setBounds(WW(5),HH(1),WW(1.2),HH(0.7));
                this.edit.addActionListener(editListener);
                this.add(this.edit);

                this.remove=new MyButton("移除");
                this.remove.setBounds(WW(6.2),HH(1),WW(1),HH(0.7));
                this.remove.addActionListener(removeListener);
                this.add(remove);
            }
            public Question getQ(){return q;}
        }
    }
}