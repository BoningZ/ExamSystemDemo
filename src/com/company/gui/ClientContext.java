package com.company.gui;

import com.company.bean.*;
import com.company.exception.NameOrPasswordException;
import com.company.exception.PasswordNotMatchException;
import com.company.exception.UserExistException;
import com.company.service.ExamService;
import com.company.utils.Consts;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClientContext {
    private ExamService service;
    private LoginFrame loginFrame;
    private StudentRegisterFrame registerFrame;
    private StudentMenuFrame studentMenuFrame;
    private TeacherMenuFrame teacherMenuFrame;
    private StudentRegisterFrame studentRegisterFrame;
    private TeacherRegisterFrame teacherRegisterFrame;
    private QuestionBaseFrame questionBaseFrame;
    private ArrangePaperFrame arrangePaperFrame;
    private CorrectFrame correctFrame;
    private FeedbackFrame feedbackFrame;
    private QueryFrame queryFrame;
    private ExamFrame examFrame;
    private User user;

    public QuestionBaseFrame getQuestionBaseFrame() {
        return questionBaseFrame;
    }

    public void setQuestionBaseFrame(QuestionBaseFrame questionBaseFrame) {
        this.questionBaseFrame = questionBaseFrame;
    }

    public ClientContext(ExamService service){this.service=service;}

    public void showFaultStatistics(){}
    public void showFault(int id){
        feedbackFrame=new FeedbackFrame(this,findPaper(id));
        setFeedbackFrame(feedbackFrame);
        studentMenuFrame.setVisible(false);
        studentMenuFrame.dispose();
        feedbackFrame.showView();
    }
    public void startExam(int paperId){
        examFrame=new ExamFrame(this,actSheet(paperId));
        setExamFrame(examFrame);
        studentMenuFrame.setVisible(false);
        studentMenuFrame.dispose();
        examFrame.showView();
    }
    public AnswerSheet actSheet(int paperId){
        AnswerSheet sheet=findSheet(paperId);
        TestPaper paper=findPaper(paperId);
        for(int i=0;i<paper.getNumOfQuestions();i++)
            sheet.addAnswer(newAnswer(paper.getQuestionIdByRank(i)).getId());
        sheet.setStatus(Consts.STATE_ANSWERING);
        updateSheet(sheet);
        return sheet;
    }
    public void showQuestionBase(){
        teacherMenuFrame.setVisible(false);
        questionBaseFrame.showView();
    }
    public void showQuery(int id){
        queryFrame=new QueryFrame(this,findPaper(id));
        setQueryFrame(queryFrame);
        teacherMenuFrame.setVisible(false);
        teacherMenuFrame.dispose();
        queryFrame.showView();
    }
    public void showCorrect(int id){
        correctFrame=new CorrectFrame(this,findPaper(id));
        setCorrectFrame(correctFrame);
        teacherMenuFrame.setVisible(false);
        teacherMenuFrame.dispose();
        correctFrame.showView();
    }
    public List<TestPaper> getReleased(){return service.getReleased(user.getId());}
    public List<TestPaper> getUnreleased(){return service.getUnreleased(user.getId());}
    public void showEditPaper(int id){
        arrangePaperFrame=new ArrangePaperFrame(this,findPaper(id));
        setArrangePaperFrame(arrangePaperFrame);
        //arrangePaperFrame.setPaper(findPaper(id));
        teacherMenuFrame.setVisible(false);
        arrangePaperFrame.showView();
    }
    public void releasePaper(int id){
        String[] options =new String[Consts.NUM_OF_GRADES];
        for(int i=0;i<Consts.NUM_OF_GRADES;i++)
            options[i]="年级"+(i+1);
        int value=JOptionPane.showOptionDialog(questionBaseFrame, "选择发布给的年级",
                "发布", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                options, "年级"+user.getGrade());
        service.releasePaper(id,value+1);
        teacherMenuFrame.setVisible(false);
        teacherMenuFrame=new TeacherMenuFrame(this);
        teacherMenuFrame.showView();
    }
    public Object[][] getDoneExamInfo(){
        if(user!=null)return service.getDoneInfo(user.getId());
        return null;
    }
    public Object[][] getTodoExamInfo(){
        if(user!=null) {
            List<TestPaper> list = service.getTodoExam(user.getId());
            Object[][] info = new Object[list.size()][3];
            int i = 0;
            for (TestPaper paper : list) {
                info[i][0] = paper.getId();
                info[i][1] = paper.getTitle();
                int t = paper.getTimeLimit();
                info[i][2] = (t / 3600) + "时" + ((t % 3600) / 60) + "分" + (t % 60) + "秒";
                i++;
            }
            return info;
        }
        return null;
    }
    public void createQuestion(){
        String[] options ={"单选","多选","填空","简答"};
        int value=JOptionPane.showOptionDialog(questionBaseFrame, "选择新建题目的类型",
                "题型", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                options, "单选");
        value+=1;//使其与常数表对应
        Question q=service.createQuestion(value);
        questionBaseFrame.showQuestion(q.getId());
    }
    public void createPaper(){
        TestPaper paper=service.createPaper(user);
        showEditPaper(paper.getId());
    }
    public Answer newAnswer(int questionId){return service.createAnswer(questionId);}
    public void backToTeacherMenu(){
        if(arrangePaperFrame!=null)arrangePaperFrame.setVisible(false);
        if(questionBaseFrame!=null)questionBaseFrame.setVisible(false);
        if(correctFrame!=null)correctFrame.setVisible(false);
        if(queryFrame!=null)queryFrame.setVisible(false);
        teacherMenuFrame.setVisible(false);
        teacherMenuFrame=null;
        teacherMenuFrame=new TeacherMenuFrame(this);
        teacherMenuFrame.showView();
    }
    public void backToStudentMenu(){
        if(examFrame!=null)examFrame.setVisible(false);
        if(feedbackFrame!=null)feedbackFrame.setVisible(false);
        studentMenuFrame.setVisible(false);
        studentMenuFrame=null;
        try {
            user = service.login(user.getId(), user.getPassword(), true);
        }catch (Exception e){e.printStackTrace();}
        studentMenuFrame=new StudentMenuFrame(this);
        studentMenuFrame.showView();
    }
    public void deleteQuestion(int id){
        int result = JOptionPane.showConfirmDialog(null, "确认删除？", "删除题目",JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.OK_OPTION) return;
        service.deleteQuestion(id);
        questionBaseFrame.createQuestion();
        questionBaseFrame.repaint();
        questionBaseFrame.setVisible(true);
        JOptionPane.showMessageDialog(questionBaseFrame, "删除成功！");
    }
    public void updateQuestion(int id, Question q){service.updateQuestion(id,q);}
    public void updatePaper(TestPaper paper){service.updatePaper(paper);}
    public void updateSheet(AnswerSheet sheet){service.updateSheet(sheet);}
    public void updateAnswer(Answer answer){service.updateAnswer(answer);}
    public List<Question> screenQuestion(boolean singleChoice,boolean multiChoice,boolean fillBlank,boolean shortAns,double lowDiff,double uppDiff,int key){
        List<Question> temp;
        List<Question> list=new ArrayList<>();
        Comparator<Question> comparator=new Comparator<Question>() {
            public int compare(Question o1, Question o2) {
                switch (key){
                    case Consts.KEY_DIFF:return o1.getDifficulty()<o2.getDifficulty()?-1:1;
                    case Consts.KEY_ID:return o1.getId()-o2.getId();
                    case Consts.KEY_TITLE:return o1.getTitle().compareTo(o2.getTitle());
                    case Consts.KEY_TYPE:return o1.getQuestionType()-o2.getQuestionType();
                }
                return 0;
            }};
        temp=service.screenQuestion(singleChoice,multiChoice,fillBlank,shortAns);
        for(Question q:temp)
            if(q.getDifficulty()>=lowDiff&&q.getDifficulty()<=uppDiff)list.add(q);
        list.sort(comparator);
        return list;
    }
    public Question findQuestion(int id){return service.findQuestion(id);}
    public TestPaper findPaper(int id){return service.findPaper(id);}
    public AnswerSheet findSheet(int paperId){return service.findSheet(user,paperId);}
    public Answer findAnswer(int id){return service.findAnswer(id);}
    public Answer findAnswer(AnswerSheet sheet,Question q){
        for(int i=0;i<sheet.getNumOfAnswers();i++){
            Answer a=service.findAnswer(sheet.getAnswerIdByRank(i));
            if(a.getQuestionId()==q.getId())return a;
        }
        return null;
    }
    public List<Question> getQuestions(TestPaper paper){
        List<Question> list=service.getQuestions(paper);
        Comparator<Question> comparator=new Comparator<Question>() {public int compare(Question o1, Question o2) {return o1.getDifficulty()<o2.getDifficulty()?-1:1;}};
        list.sort(comparator);
        return list;
    }
    public void submit(){
        examFrame.sheet.setStatus(Consts.STATE_WAITING_CORR);
        updateSheet(examFrame.sheet);
        examFrame.timer.cancel();
        examFrame.setVisible(false);
        backToStudentMenu();
    }
    public double autoCorrect(int paperId,int id){
        getToCorrect(findPaper(paperId));
        return service.autoCorrect(id);
    }
    public List<AnswerSheet> getToCorrect(TestPaper paper){return service.getToCorrect(paper);}
    public List<AnswerSheet> getCorrected(TestPaper paper){return service.getCorrected(paper);}
    public double getSheetScore(AnswerSheet sheet){
        double score=0;
        for(int i=0;i<sheet.getNumOfAnswers();i++)score+=findAnswer(sheet.getAnswerIdByRank(i)).getScore();
        return score;
    }
    public Object[][] getTableData(TestPaper paper){return service.getTableData(paper);}


    public int getNumOfType(TestPaper paper,int type){return service.getNumOfType(paper,type);}
    public double getScoreOfType(TestPaper paper,int type){return service.getScoreOfType(paper,type);}
    public double getDiffOfType(TestPaper paper,int type){return service.getDiffOfType(paper,type);}

    public TestPaper generatePaper(int numOfSingle, int numOfMulti, int numOfFill, int numOfShort,double scoreOfSingle, double scoreOfMulti, double scoreOfFill, double scoreOfShort,double diff){
        TestPaper temp=service.generatePaper(numOfSingle,numOfMulti,numOfFill,numOfShort,scoreOfSingle,scoreOfMulti,scoreOfFill,scoreOfShort,diff);
        TestPaper paper=arrangePaperFrame.paper;
        paper.setNumOfQuestions(0);
        for(int i=0;i<temp.getNumOfQuestions();i++)
            paper.addQuestion(temp.getQuestionIdByRank(i));
        return paper;
    }

    public void teacherRegister(){
        String username=teacherRegisterFrame.getUsername();
        String id=teacherRegisterFrame.getId();
        String pwd=teacherRegisterFrame.getPwd();
        String resPwd=teacherRegisterFrame.getResPwd();
        int grade=teacherRegisterFrame.getGrade();
        String adminPwd=teacherRegisterFrame.getAdminPwd();
        if(id.equals("")||username.equals("")||pwd.equals("")||resPwd.equals("")){
            JOptionPane.showMessageDialog(teacherRegisterFrame, "任一项不得为空！");
            return;
        }if(!adminPwd.equals(Consts.ADMINISTRATOR_PASSWORD)){
            JOptionPane.showMessageDialog(teacherRegisterFrame, "管理员密码错误！");
            return;
        }
        try{
            user=service.register(username,id,pwd,resPwd,false,grade);
            teacherRegisterFrame.setVisible(false);
            backToLogin();
        }catch (PasswordNotMatchException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(teacherRegisterFrame, "两次输入不一致！");
        }catch (UserExistException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(teacherRegisterFrame, "用户已存在！");
        }
    }
    public void studentRegister(){
        String username=studentRegisterFrame.getUsername();
        String id=studentRegisterFrame.getId();
        String pwd=studentRegisterFrame.getPwd();
        String resPwd=studentRegisterFrame.getResPwd();
        int grade=studentRegisterFrame.getGrade();
        if(id.equals("")||username.equals("")||pwd.equals("")||resPwd.equals("")){
            JOptionPane.showMessageDialog(studentRegisterFrame, "任一项不得为空！");
            return;
        }
        try{
            user=service.register(username,id,pwd,resPwd,true,grade);
            studentRegisterFrame.setVisible(false);
            backToLogin();
        }catch (PasswordNotMatchException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(studentRegisterFrame, "两次输入不一致！");
        }catch (UserExistException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(studentRegisterFrame, "用户已存在！");
        }
    }

    public void showRegister(boolean isStudent){
        loginFrame.setVisible(false);
        if(isStudent)studentRegisterFrame.showView();
            else teacherRegisterFrame.showView();
    }
    public void backToLogin(){
        studentRegisterFrame.setVisible(false);
        teacherRegisterFrame.setVisible(false);
        loginFrame.showView();
    }
    public void exit() {
        int result = JOptionPane.showConfirmDialog(null, "确定退出？", "退出",
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }

    public void login(boolean isStudent){
        String id = loginFrame.getId();
        String pwd = loginFrame.getPwd();
        if(id.equals("")||pwd.equals("")){
            JOptionPane.showMessageDialog(loginFrame, "任一项不得为空！");
            return;
        }
        try {
            user = service.login(id, pwd, isStudent);
            loginFrame.setVisible(false);
            setUser(user);
            if(isStudent) {
                studentMenuFrame=new StudentMenuFrame(this);
                setStudentMenuFrame(studentMenuFrame);
                studentMenuFrame.showView();
            }else{
                teacherMenuFrame=new TeacherMenuFrame(this);
                questionBaseFrame=new QuestionBaseFrame(this);
                setTeacherMenuFrame(teacherMenuFrame);
                setQuestionBaseFrame(questionBaseFrame);
                questionBaseFrame.setVisible(false);
                teacherMenuFrame.showView();
            }
        } catch (NameOrPasswordException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(loginFrame, "用户名或密码错误！");
        }
    }

    public void start(){loginFrame.showView();}

    public ExamService getService() {
        return service;
    }

    public void setService(ExamService service) {
        this.service = service;
    }

    public LoginFrame getLoginFrame() {
        return loginFrame;
    }

    public void setLoginFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public StudentRegisterFrame getRegisterFrame() {
        return registerFrame;
    }

    public void setRegisterFrame(StudentRegisterFrame registerFrame) {
        this.registerFrame = registerFrame;
    }

    public StudentMenuFrame getStudentMenuFrame() {
        return studentMenuFrame;
    }

    public void setStudentMenuFrame(StudentMenuFrame studentMenuFrame) {
        this.studentMenuFrame = studentMenuFrame;
    }

    public TeacherMenuFrame getTeacherMenuFrame() {
        return teacherMenuFrame;
    }

    public void setTeacherMenuFrame(TeacherMenuFrame teacherMenuFrame) {
        this.teacherMenuFrame = teacherMenuFrame;
    }

    public StudentRegisterFrame getStudentRegisterFrame() {
        return studentRegisterFrame;
    }

    public void setStudentRegisterFrame(StudentRegisterFrame studentRegisterFrame) {
        this.studentRegisterFrame = studentRegisterFrame;
    }

    public TeacherRegisterFrame getTeacherRegisterFrame() {
        return teacherRegisterFrame;
    }

    public void setTeacherRegisterFrame(TeacherRegisterFrame teacherRegisterFrame) {
        this.teacherRegisterFrame = teacherRegisterFrame;
    }

    public ArrangePaperFrame getArrangePaperFrame() {
        return arrangePaperFrame;
    }

    public void setArrangePaperFrame(ArrangePaperFrame arrangePaperFrame) {
        this.arrangePaperFrame = arrangePaperFrame;
    }

    public ExamFrame getExamFrame() {
        return examFrame;
    }

    public void setExamFrame(ExamFrame examFrame) {
        this.examFrame = examFrame;
    }

    public CorrectFrame getCorrectFrame() {
        return correctFrame;
    }

    public void setCorrectFrame(CorrectFrame correctFrame) {
        this.correctFrame = correctFrame;
    }

    public QueryFrame getQueryFrame() {
        return queryFrame;
    }

    public void setQueryFrame(QueryFrame queryFrame) {
        this.queryFrame = queryFrame;
    }

    public FeedbackFrame getFeedbackFrame() {
        return feedbackFrame;
    }

    public void setFeedbackFrame(FeedbackFrame feedbackFrame) {
        this.feedbackFrame = feedbackFrame;
    }
}
