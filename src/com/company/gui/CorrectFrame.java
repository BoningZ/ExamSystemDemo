package com.company.gui;

import com.company.bean.Answer;
import com.company.bean.AnswerSheet;
import com.company.bean.Question;
import com.company.bean.TestPaper;
import com.company.gui.my_component.*;
import com.company.utils.Consts;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Comparator;
import java.util.List;

public class CorrectFrame extends BaseFrame{
    private ClientContext context;

    private TestPaper paper;
    private AnswerSheet sheet;
    List<AnswerSheet> list;

    private MyPanel pane;
    private MyPanel questionPane;
    private QuestionPanel questionPanel;
    private MyPanel testPaperPane;
    private PaperInfoPanel paperInfoPanel;
    private PaperQuestionPanel paperQuestionPanel;
    private MyScrollPane scrollPaper;



    public CorrectFrame(){init();}
    public CorrectFrame(ClientContext context,TestPaper paper){
        this();
        this.context=context;
        setPaper(paper);
        fillData();
    }
    private void fillData(){
        list=context.getToCorrect(paper);
        if(list.size()==0){
            JOptionPane.showMessageDialog(this,"没有可批阅的答卷！");
            return;
        }
        sheet=list.get(0);
        refreshPaper();
    }
    protected void init() {

        ImageIcon icon=new ImageIcon("src/com/company/img/feedBack.jpg" );

        JLabel backGround=new JLabel(icon);
        backGround.setBounds(0,0,WIDTH,HEIGHT);
        JPanel myPanel = (JPanel)this.getContentPane();		//把我的面板设置为内容面板
        myPanel.setOpaque(false);					//把我的面板设置为不可视
        myPanel.setLayout(null);		//把我的面板设置为流动布局
        this.getLayeredPane().setLayout(null);
        this.getLayeredPane().add(backGround, new Integer(Integer.MIN_VALUE));
        
        this.setSize(WIDTH,HEIGHT);
        this.setLocation(0,0);
        this.setTitle("阅卷");
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
        createTestPaper();
        createQuestion();

        MyButton back=new MyButton("返回");
        back.setBounds(0,0,WW(1),HH(1));
        back.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {context.getToCorrect(paper);context.backToTeacherMenu();}});
        pane.add(back);

        return pane;
    }
    private void createTestPaper(){
        testPaperPane=new MyPanel();
        testPaperPane.setLocation(WW(21),HH(1));
        testPaperPane.setLayout(null);
        testPaperPane.setSize(WW(8),HH(28));

        paperInfoPanel=new PaperInfoPanel(this,context);
        testPaperPane.add(paperInfoPanel);


        paperQuestionPanel=new PaperQuestionPanel(this,context);
        //testPaperPane.add(paperQuestionPanel);

        scrollPaper=new MyScrollPane(paperQuestionPanel);
        scrollPaper.setBounds(0,HH(2),WW(8),HH(25));
        testPaperPane.add(scrollPaper);

        pane.add(testPaperPane);
    }
    protected void createQuestion(){
        if(questionPane!=null)pane.remove(questionPane);
        questionPane=new MyPanel();
        questionPane.setSize(WW(19),HH(28));
        questionPane.setLocation(WW(1),HH(1));
        questionPane.setOpaque(false);
        questionPane.setLayout(null);

        questionPanel=new QuestionPanel(0, Consts.TYPE_SHORT_ANS,this,context);
        questionPane.add(questionPanel);

        pane.add(questionPane);
        setVisible(true);
    }
    private void showQuestion(int id){
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
    private void refreshPaper(){

        testPaperPane.remove(paperInfoPanel);
        paperInfoPanel=new PaperInfoPanel(this,context,paper);
        testPaperPane.add(paperInfoPanel);

        testPaperPane.remove(scrollPaper);
        paperQuestionPanel=new PaperQuestionPanel(this,context,paper);
        //testPaperPane.add(paperQuestionPanel);

        scrollPaper=new MyScrollPane(paperQuestionPanel);
        scrollPaper.setBounds(0,HH(2),WW(8),HH(25));
        testPaperPane.add(scrollPaper);

        repaint();
        setVisible(true);
    }
    class QuestionPanel extends MyPanel{
        private ActionListener saveListener=new ActionListener() {public void actionPerformed(ActionEvent e) {updateAnswer();}};
        private DocumentListener documentListener=new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {updateAnswer();}
            public void removeUpdate(DocumentEvent e) {updateAnswer();}
            public void changedUpdate(DocumentEvent e) {updateAnswer();}};
        private JLabel title,score;
        private MyTextArea myScore;
        private MyTextArea description;
        private AnswerPanel answerPanel,realAnswerPanel;
        private ClientContext context;
        private JFrame thisFrame;
        MyButton save=new MyButton("自定义");
        MyButton full=new MyButton("满分");
        MyButton zero=new MyButton("零分");
        MyButton prev=new MyButton("上一题");
        MyButton next=new MyButton("下一题");
        int type,id;
        Question q; Answer a;
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

            title=new JLabel();
            title.setBorder(BorderFactory.createLineBorder(Color.black));
            title.setBounds(WW(3),0,WW(16),HH(1));
            title.setBackground(new Color(238,238,238));
            //title.setEditable(false);
            this.add(title);



            JLabel descriptionLabel=new JLabel("描述：");
            descriptionLabel.setBounds(0,HH(1),WW(3),HH(1));
            this.add(descriptionLabel);

            description= new MyTextArea();
            description.setLineWrap(true);
            description.setBackground(new Color(238,238,238));
            //description.setBounds(WW(0),HH(0),WW(16),HH(10));
            description.setPreferredSize(new Dimension(WW(15),HH(100)));
            description.setEditable(false);
            MyScrollPane descriptionPane=new MyScrollPane(description);
            //descriptionPane.setLayout(null);
            descriptionPane.setBounds(WW(3),HH(1),WW(16),HH(10));
            this.add(descriptionPane);

            JLabel realAnswerLabel=new JLabel("标准答案：");
            realAnswerLabel.setBounds(0,HH(11),WW(2),HH(1));
            this.add(realAnswerLabel);

            realAnswerPanel=new AnswerPanel(type,thisFrame);
            realAnswerPanel.setLocation(WW(1.5),HH(11));
            realAnswerPanel.setEditable(false);
            this.add(realAnswerPanel);

            JLabel answerLabel=new JLabel("学生答案：");
            answerLabel.setBounds(WW(9.5),HH(11),WW(2),HH(1));
            this.add(answerLabel);

            answerPanel=new AnswerPanel(type,thisFrame);
            answerPanel.setLocation(WW(11),HH(11));
            answerPanel.setEditable(false);
            //answerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            this.add(answerPanel);



            /*JLabel diffLabel=new JLabel("难度：");
            diffLabel.setBounds(0,HH(22),WW(3),HH(1));
            this.add(diffLabel);

            difficulty=new MyTextArea();
            difficulty.setBorder(BorderFactory.createLineBorder(Color.black));
            difficulty.setBounds(WW(3),HH(22),WW(3),HH(1));
            difficulty.setEditable(false);
            this.add(difficulty);*/

            JLabel scoreLabel=new JLabel("满分：");
            scoreLabel.setBounds(WW(2-0.3),HH(22),WW(2),HH(1));
            this.add(scoreLabel);



            score= new JLabel();
            score.setBorder(BorderFactory.createLineBorder(Color.black));
            score.setBounds(WW(4),HH(22),WW(3),HH(1));
            //score.setEditable(false);
            this.add(score);

            JLabel myScoreLabel=new JLabel("学生得分：");
            myScoreLabel.setBounds(WW(11.2),HH(22),WW(1.3),HH(1));
            this.add(myScoreLabel);

            myScore=new MyTextArea();
            myScore.setBorder(BorderFactory.createLineBorder(Color.black));
            myScore.setEditable(false);
            myScore.setBounds(WW(14.5),HH(22),WW(3),HH(1));
            myScore.setBackground(new Color(238,238,238));
            this.add(myScore);

            full.setBounds(WW(13.5),HH(22),WW(1),HH(1));
            full.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {myScore.setText(""+q.getScore());updateAnswer();}});
            full.setEnabled(false);
            this.add(full);

            zero.setBounds(WW(12.5),HH(22),WW(1),HH(1));
            zero.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {myScore.setText("0");updateAnswer();}});
            zero.setEnabled(false);
            this.add(zero);

            save.setBounds(WW(17.5),HH(22),WW(1.5),HH(1));
            save.addActionListener(saveListener);
            save.setEnabled(false);
            this.add(save);

            prev.setBounds(WW(9),HH(25),WW(5),HH(1.5));
            prev.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
                int target=0; Question nextQ=new Question();
                for(int i=0;i<paperQuestionPanel.questions.length;i++)
                    if(q.getId()==paperQuestionPanel.questions[i].q.getId())
                    {target=i;if(i!=0)nextQ=paperQuestionPanel.questions[i-1].q;break;}
                if(target>0)
                    showQuestion(nextQ.getId());
                else JOptionPane.showMessageDialog(thisFrame,"已经是第一题");}});
            prev.setEnabled(false);
            this.add(prev);


            next.setBounds(WW(14),HH(25),WW(5),HH(1.5));
            next.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
                int target=0; Question nextQ=new Question();
                for(int i=0;i<paperQuestionPanel.questions.length;i++)
                    if(q.getId()==paperQuestionPanel.questions[i].q.getId())
                    {target=i;if(i!=paperQuestionPanel.questions.length-1)nextQ=paperQuestionPanel.questions[i+1].q;break;}
                if(target<paperQuestionPanel.questions.length-1)
                    showQuestion(nextQ.getId());
                else JOptionPane.showMessageDialog(thisFrame,"已经是最后一题");
            }});
            next.setEnabled(false);
            this.add(next);
        }
        QuestionPanel(Question q,JFrame thisFrame,ClientContext context){
            this(q.getId(),q.getQuestionType(),thisFrame,context);
            this.q=q;
            for(int i=0;i<paper.getNumOfQuestions();i++)
                if(q.getId()==paper.getQuestionIdByRank(i))
                    this.a=context.findAnswer(sheet.getAnswerIdByRank(i));
            /*title.setEditable(true);*/ title.setText(q.getTitle());
            /*description.setEditable(true);*/ description.setText(q.getDescription());
            //answerPanel.setEditable(true);
            myScore.setEditable(true);
            myScore.setBackground(Color.WHITE);
            save.setEnabled(true);
            full.setEnabled(true);
            zero.setEnabled(true);

            this.remove(answerPanel);
            if(type== Consts.TYPE_SHORT_ANS||type==Consts.TYPE_FILL_BLANK)
                answerPanel=new AnswerPanel(q.getQuestionType(),thisFrame,a.getSubAnswer(),documentListener);
            else answerPanel = new AnswerPanel(q.getQuestionType(), thisFrame, a.getObjAnswer(), q.getNumOfOptions(), saveListener);

            answerPanel.setLocation(WW(11),HH(11));
            answerPanel.setEditable(false);
            //answerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            this.add(answerPanel);

            this.remove(realAnswerPanel);
            if(type==Consts.TYPE_SHORT_ANS||type==Consts.TYPE_FILL_BLANK)
                realAnswerPanel=new AnswerPanel(q.getQuestionType(),thisFrame,q.getSubAnswer(),documentListener);
            else realAnswerPanel=new AnswerPanel(q.getQuestionType(),thisFrame,q.getObjAnswer(),q.getNumOfOptions(),saveListener);
            realAnswerPanel.setLocation(WW(1.5),HH(11));
            realAnswerPanel.setEditable(false);
            //answerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            this.add(realAnswerPanel);

            //difficulty.setEditable(true); difficulty.setText(String.valueOf(q.getDifficulty()));
            /*score.setEditable(true);*/ score.setText(String.valueOf(q.getScore()));
            prev.setEnabled(true); save.setEnabled(true);
            next.setEnabled(true);
            thisFrame.setVisible(true);
        }
        void updateAnswer() {
            try{
                double score=Double.parseDouble(myScore.getText());
                if(score<0||score>q.getScore())throw new Exception();
                a.setScore(score);
                a.setCorrected(true);
                context.updateAnswer(a);
                for(int i=0;i<paperQuestionPanel.questions.length;i++)
                    if(q.getId()==paperQuestionPanel.questions[i].q.getId())
                        paperQuestionPanel.questions[i].done();
            }catch (Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(thisFrame,"请输入合适的分数！");
            }
        }

        class AnswerPanel extends MyPanel{
            private ActionListener saveListener;
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
                this.setSize(WW(8),HH(11));
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
                        subAnswer.setPreferredSize(new Dimension(WW(7.5),HH(100)));

                        subPane=new MyScrollPane(subAnswer);
                        subPane.setBounds(0,0,WW(8),HH(11));
                        //subPane.setLayout(null);
                        this.add(subPane);
                        break;
                    case Consts.TYPE_SINGLE_CHOICE:
                    case Consts.TYPE_MULTI_CHOICE:
                        /*JLabel numLabel=new JLabel("答案数量：");
                        numLabel.setBounds(0,0,WW(3),HH(1));
                        this.add(numLabel);*/

                        /*numOfOption=new MyTextArea();
                        numOfOption.setBorder(BorderFactory.createLineBorder(Color.black));
                        numOfOption.setBounds(WW(3),0,WW(3),HH(1));
                        this.add(numOfOption);*/

                        /*MyButton refreshNum=new MyButton("确认");
                        refreshNum.setBounds(WW(6),0,WW(2),HH(1));
                        refreshNum.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {refresh();}});
                        this.add(refreshNum);*/

                        objAnswer=new MyPanel();
                        objAnswer.setLayout(new FlowLayout(FlowLayout.LEFT,WW(1),HH(1)));
                        objAnswer.setSize(WW(8),HH(10));
                        objAnswer.setLocation(0,HH(1));
                        this.add(objAnswer);

                        break;
                }
            }
            AnswerPanel(int type,JFrame thisFrame,int ans,int numOfOptions,ActionListener saveListener){
                this(type,thisFrame);
                this.saveListener=saveListener;
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
                            multiOptions[i].addActionListener(saveListener);
                            objAnswer.add(multiOptions[i]);
                            break;
                        case Consts.TYPE_SINGLE_CHOICE:
                            singleOptions[i]=new MyRadioButton(""+(char)(i+'A'),tmp%2==1);
                            singleOptions[i].setSize(WW(2),HH(1));
                            singleOptions[i].addActionListener(saveListener);
                            group.add(singleOptions[i]);
                            objAnswer.add(singleOptions[i]);
                            break;
                    }
                    tmp/=2;
                }
                //this.thisFrame.setVisible(true);
            }
            AnswerPanel(int type,JFrame thisFrame,String ans,DocumentListener documentListener){
                this(type,thisFrame);
                subAnswer.setText(ans);
                subAnswer.getDocument().addDocumentListener(documentListener);
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
    class PaperInfoPanel extends MyPanel{
        private JFrame thisFrame;
        private ClientContext context;

        private JLabel title;
        private JLabel id,sheetId;

        private MyButton next=new MyButton("下一份答卷");
        private MyButton prev=new MyButton("上一份答卷");

        PaperInfoPanel(JFrame thisFrame,ClientContext context){
            this.context=context; this.thisFrame=thisFrame;

            this.setSize(WW(8),HH(2));
            this.setLocation(0,0);
            this.setLayout(null);
            this.setBorder(BorderFactory.createLineBorder(Color.black));

            //<editor-fold desc="Main Info">
            MyPanel mainInfo=new MyPanel();
            mainInfo.setSize(WW(8),HH(3));
            mainInfo.setLocation(0,0);
            mainInfo.setLayout(null);
            mainInfo.setBorder(BorderFactory.createLineBorder(Color.black));

            id=new JLabel("id：");
            id.setBounds(0,0,WW(1.5),HH(1));
            mainInfo.add(id);

            JLabel titleLabel=new JLabel("考试名：");
            titleLabel.setBounds(WW(2.5),0,WW(3.5),HH(1));
            mainInfo.add(titleLabel);

            title=new JLabel();
            title.setBounds(WW(3.5),HH(0.2),WW(3.5),HH(0.6));
            title.setBorder(BorderFactory.createLineBorder(Color.black));
            mainInfo.add(title);

            sheetId=new JLabel("答卷编号：");
            sheetId.setBounds(WW(0),HH(1),WW(3),HH(1));
            mainInfo.add(sheetId);

            prev.setBounds(WW(3),HH(1),WW(2.5),HH(1));
            prev.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {showPrev();}});
            prev.setEnabled(false);
            mainInfo.add(prev);

            next.setBounds(WW(5.5),HH(1),WW(2.5),HH(1));
            next.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {showNext();}});
            next.setEnabled(false);
            mainInfo.add(next);

            this.add(mainInfo);
            //</editor-fold>


        }
        PaperInfoPanel(JFrame thisFrame,ClientContext context,TestPaper paper){
            this(thisFrame,context);
            prev.setEnabled(true);
            next.setEnabled(true);
            updateParas();
        }
        private void updateParas(){
            id.setText("id："+paper.getId());
            title.setText(paper.getTitle());
            sheetId.setText("答卷编号："+sheet.getId());
        }

    }
    class PaperQuestionPanel extends MyPanel{
        private JFrame thisFrame;
        private ClientContext context;
        protected ArrangedQuestion[] questions;
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
            list.sort(new Comparator<Question>() {public int compare(Question o1, Question o2) {
                return o1.getQuestionType()-o2.getQuestionType();}});
            int i=0;
            for(Question q:list){
                //System.out.println(i);
                questions[i]=new ArrangedQuestion(q,context.findAnswer(sheet,q),y[q.getQuestionType()-1], new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        showQuestion(q.getId());}});
                type[q.getQuestionType()-1].add(questions[i++]);
                y[q.getQuestionType()-1]+=HH(2);
            }
            for(i=2;i<4;i++){
                this.add(typeButton[i]);
                this.add(type[i]);
            }
        }
        private void fold(int type){
            status[type]=1;
            this.removeAll();
            for(int i=2;i<4;i++){
                this.add(typeButton[i]);
                if(status[i]==0)this.add(this.type[i]);
            }
            thisFrame.repaint();
            thisFrame.setVisible(true);
        }
        private void extend(int type){
            status[type]=0;
            this.removeAll();
            for(int i=2;i<4;i++){
                this.add(typeButton[i]);
                if(status[i]==0)this.add(this.type[i]);
            }
            thisFrame.repaint();
            thisFrame.setVisible(true);
        }
        class ArrangedQuestion extends MyPanel{
            private Question q;
            private Answer a;
            JLabel id,title,isDone,score;
            MyButton edit;
            public ArrangedQuestion(Question q,Answer a,int y,ActionListener editListener){
                this.q=q; this.a=a;

                this.setLayout(null);
                this.setBounds(0,y,WW(7.4),HH(2));
                this.setLocation(WW(0.2),y);
                //this.setPreferredSize(new Dimension(WW(7.6),HH(2)));
                this.setBorder(BorderFactory.createLineBorder(Color.black));



                this.title=new JLabel(q.getTitle());
                this.title.setBounds(WW(2),0,WW(6),HH(1));
                this.add(this.title);

                if(!a.isCorrected()){this.isDone=new JLabel("未批阅");this.isDone.setForeground(Color.red);}
                    else this.isDone=new JLabel("已批阅");
                this.isDone.setBounds(WW(0.5),HH(1),WW(1.5),HH(1));
                this.add(this.isDone);

                this.score=new JLabel("得分："+a.getScore()+"/"+q.getScore());
                this.score.setBounds(WW(2.25),HH(1),WW(3),HH(1));
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

                this.edit=new MyButton("批阅");
                this.edit.setBounds(WW(5),HH(1),WW(1.2),HH(0.7));
                this.edit.addActionListener(editListener);
                this.add(this.edit);
            }
            public Question getQ(){return q;}
            public Answer getA(){return a;}
            public void done(){refresh(context.findAnswer(sheet,q));}
            private void refresh(Answer a){
                this.a=a;
                isDone.setText("已批阅");isDone.setForeground(Color.BLACK);
                score.setText("得分："+a.getScore()+"/"+q.getScore());
            }
        }
    }
    public void setPaper(TestPaper paper){this.paper=paper;}
    public void showNext(){
        int index=list.indexOf(sheet);
        if(index==list.size()-1) {
            JOptionPane.showMessageDialog(this, "已经是最后一份答卷！");
            return;
        }
        sheet=list.get(index+1);
        refreshPaper();
        createQuestion();
    }
    public void showPrev(){
        int index=list.indexOf(sheet);
        if(index==0) {
            JOptionPane.showMessageDialog(this, "已经是第一份答卷！");
            return;
        }
        sheet=list.get(index-1);
        refreshPaper();
        createQuestion();
    }
}
