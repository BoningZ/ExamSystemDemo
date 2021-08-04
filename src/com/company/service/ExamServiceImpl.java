package com.company.service;

import com.company.bean.*;
import com.company.dao.*;
import com.company.exception.NameOrPasswordException;
import com.company.exception.PasswordNotMatchException;
import com.company.exception.UserExistException;
import com.company.utils.Consts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ExamServiceImpl implements ExamService{
    private User user;
    @Override
    public User login(String id, String password, boolean isStudent) throws NameOrPasswordException {
        UserDAO userDAO=new UserDAO();
        User u=userDAO.findById(id);
        if(u==null||u.isStudent()!=isStudent)throw new NameOrPasswordException();
        if(u.getPassword().equals(password))return user=u;
        throw new NameOrPasswordException();
    }

    @Override
    public User register(String username, String id, String password, String repeatPassword, boolean isStudent, int grade) throws UserExistException, PasswordNotMatchException {
        UserDAO userDAO=new UserDAO();
        User u=userDAO.findById(id);
        if(!password.equals(repeatPassword))throw new PasswordNotMatchException();
        if(u!=null)throw new UserExistException();
        user=new User(username,password,id,isStudent,grade);
        userDAO.add(user);
        return user;
    }

    @Override
    public List<TestPaper> getDoneExam(String userId){
        UserDAO userDAO=new UserDAO();
        User user=userDAO.findById(userId);
        return user.getDoneExam();
    }

    @Override
    public Object[][] getDoneInfo(String userId){
        UserDAO userDAO=new UserDAO();
        List<TestPaper> examList=getDoneExam(userId);
        List<AnswerSheet> sheetList=userDAO.findById(userId).getDoneSheets();
        Object[][] info=new Object[examList.size()][4];
        int i=0;
        for(TestPaper paper:examList){
            info[i][0]=paper.getId();
            info[i][1]=paper.getTitle();
            i++;
        }
        i=0;
        for(AnswerSheet sheet:sheetList){
            info[i][2]=sheet.getScore();
            info[i][3]=sheet.getRank();
            i++;
        }
        return info;
    }

    @Override
    public Object[][] getTableData(TestPaper paper) {
        UserDAO userDAO=new UserDAO();
        AnswerDAO answerDAO=new AnswerDAO();
        List<AnswerSheet> list=paper.getCorrected();
        list.sort(new Comparator<AnswerSheet>() {public int compare(AnswerSheet o1, AnswerSheet o2){if(o2.getScore()>o1.getScore())return 1;else return -1;}});
        Object[][] res=new Object[list.size()][paper.getNumOfQuestions()+4];
        int i=0;
        for(AnswerSheet sheet:list){
            res[i][0]=sheet.getRank();
            res[i][1]=userDAO.findById(sheet.getStudentId()).getUsername();
            res[i][2]=sheet.getStudentId();
            res[i][3]=sheet.getScore();
            for(int j=4;j<paper.getNumOfQuestions()+4;j++)
                res[i][j]=answerDAO.findById(sheet.getAnswerIdByRank(j-4)).getScore();
            i++;
        }
        return res;
    }

    @Override
    public List<TestPaper> getTodoExam(String userId){
        UserDAO userDAO=new UserDAO();
        User user=userDAO.findById(userId);
        return user.getTodoExam();
    }

    @Override
    public List<TestPaper> getReleased(String userId){
        UserDAO userDAO=new UserDAO();
        User user=userDAO.findById(userId);
        return user.getReleased();
    }

    @Override
    public List<TestPaper> getUnreleased(String userId){
        UserDAO userDAO=new UserDAO();
        User user=userDAO.findById(userId);
        return user.getUnreleased();
    }

    @Override
    public List<AnswerSheet> getToCorrect(TestPaper paper) {
        return paper.getToCorrect();
    }

    @Override
    public List<AnswerSheet> getCorrected(TestPaper paper) {
        return paper.getCorrected();
    }

    @Override
    public Question createQuestion(int type) {
        QuestionDAO questionDAO=new QuestionDAO();
        int id=questionDAO.getValidId();
        Question q;
        switch (type){
            case Consts.TYPE_SINGLE_CHOICE:q= new Question(id,"","未命名题目",0,0,false,0,4);break;
            case Consts.TYPE_MULTI_CHOICE:q=new Question(id,"","未命名题目",0,0,true,0,4);break;
            case Consts.TYPE_FILL_BLANK:q=new Question(id,"","未命名题目",0,0,"",false);break;
            default:q=new Question(id,"","未命名题目",0,0,"",true);break;
        }
        questionDAO.add(q);
        return q;
    }

    @Override
    public TestPaper createPaper(User u){
        PaperDAO paperDAO=new PaperDAO();
        UserDAO userDAO=new UserDAO();
        int id=paperDAO.getValidId();
        TestPaper paper;
        paper=new TestPaper(id,"未命名考试",Consts.STATE_UNRELEASED,3600);
        paperDAO.add(paper);
        u.addDocument(id);
        userDAO.update(u, u.getId());
        return paper;
    }

    @Override
    public Answer createAnswer(int questionId) {
        AnswerDAO answerDAO=new AnswerDAO();
        Answer answer=new Answer(answerDAO.getValidId(),questionId);
        answerDAO.add(answer);
        return answer;
    }

    @Override
    public List<Question> screenQuestion(boolean singleChoice, boolean multiChoice, boolean fillBlank, boolean shortAns) {
        QuestionDAO questionDAO=new QuestionDAO();
        List<Question> list=new ArrayList<>();
        if(shortAns)list.addAll(questionDAO.findByType(Consts.TYPE_SHORT_ANS));
        if(multiChoice)list.addAll(questionDAO.findByType(Consts.TYPE_MULTI_CHOICE));
        if(fillBlank)list.addAll(questionDAO.findByType(Consts.TYPE_FILL_BLANK));
        if(singleChoice)list.addAll(questionDAO.findByType(Consts.TYPE_SINGLE_CHOICE));
        return list;
    }

    @Override
    public void deleteQuestion(int id) {
        QuestionDAO questionDAO=new QuestionDAO();
        //questionDAO.delete(id);
        Question q=questionDAO.findById(id);
        q.setDifficulty(-1);
        q.setTitle("（已删除题目）"+q.getTitle());
        q.setDescription("（该题目已被删除，仅在已被使用时可以查看）\n\n"+q.getDescription());
        questionDAO.update(q,id);
    }

    @Override
    public Question findQuestion(int id) {
        QuestionDAO questionDAO=new QuestionDAO();
        return questionDAO.findById(id);
    }

    @Override
    public TestPaper findPaper(int id){
        PaperDAO paperDAO=new PaperDAO();
        return paperDAO.findById(id);
    }

    @Override
    public Answer findAnswer(int id) {
        AnswerDAO answerDAO=new AnswerDAO();
        return answerDAO.findById(id);
    }

    @Override
    public void updateQuestion(int id, Question q) {
        QuestionDAO questionDAO=new QuestionDAO();
        questionDAO.update(q,id);
    }

    @Override
    public void updatePaper(TestPaper paper){
        PaperDAO paperDAO=new PaperDAO();
        paperDAO.update(paper, paper.getId());
    }

    @Override
    public void updateSheet(AnswerSheet sheet) {
        SheetDAO sheetDAO=new SheetDAO();
        sheetDAO.update(sheet,sheet.getId());
    }

    @Override
    public void updateAnswer(Answer answer) {
        AnswerDAO answerDAO=new AnswerDAO();
        answerDAO.update(answer,answer.getId());
    }

    @Override
    public TestPaper generatePaper(int numOfSingle, int numOfMulti, int numOfFill, int numOfShort, double scoreOfSingle, double scoreOfMulti, double scoreOfFill, double scoreOfShort,double diff) {
        Comparator<Question> comparator=new Comparator<Question>() {
            public int compare(Question o1, Question o2) {
                return Math.abs(o1.getDifficulty()-diff)-Math.abs(o2.getDifficulty()-diff)<0?-1:1;
            }};
        List<Question> list=new ArrayList<>();
        TestPaper paper=new TestPaper(0,"",0,0);
        QuestionDAO questionDAO=new QuestionDAO();

        list=questionDAO.findByType(Consts.TYPE_SINGLE_CHOICE);
        list.sort(comparator);
        for(Question q:list){
            if(numOfSingle<=0||scoreOfSingle<=0)break;
            if(q.getScore()<=scoreOfSingle){
                paper.addQuestion(q.getId());
                numOfSingle--;
                scoreOfSingle-=q.getScore();
            }
        }
        list.clear();

        list=questionDAO.findByType(Consts.TYPE_MULTI_CHOICE);
        list.sort(comparator);
        for(Question q:list){
            if(numOfMulti<=0||scoreOfMulti<=0)break;
            if(q.getScore()<=scoreOfMulti){
                paper.addQuestion(q.getId());
                numOfMulti--;
                scoreOfMulti-=q.getScore();
            }
        }
        list.clear();

        list=questionDAO.findByType(Consts.TYPE_FILL_BLANK);
        list.sort(comparator);
        for(
                Question q:list){
            if(numOfFill<=0||scoreOfFill<=0)break;
            if(q.getScore()<=scoreOfFill){
                paper.addQuestion(q.getId());
                numOfFill--;
                scoreOfFill-=q.getScore();
            }
        }
        list.clear();

        list=questionDAO.findByType(Consts.TYPE_SHORT_ANS);
        list.sort(comparator);
        for(
                Question q:list){
            if(numOfShort<=0||scoreOfShort<=0)break;
            if(q.getScore()<=scoreOfShort){
                paper.addQuestion(q.getId());
                numOfShort--;
                scoreOfShort-=q.getScore();
            }
        }
        list.clear();

        return paper;
    }

    @Override
    public int getNumOfType(TestPaper paper, int type) {
        return paper.getNumOfCertainType(type);
    }

    @Override
    public double getScoreOfType(TestPaper paper, int type) {
        return paper.getScoreOfCertainType(type);
    }

    @Override
    public double getDiffOfType(TestPaper paper, int type) {
        return paper.getDifficultyOfCertainType(type);
    }

    @Override
    public List<Question> getQuestions(TestPaper paper) {
        return paper.getQuestions();
    }

    @Override
    public void releasePaper(int id, int grade) {
        PaperDAO paperDAO=new PaperDAO();
        UserDAO userDAO=new UserDAO();
        SheetDAO sheetDAO=new SheetDAO();
        TestPaper paper=paperDAO.findById(id);
        paper.setStatus(Consts.STATE_WAITING_ANS);
        paperDAO.update(paper,id);
        List<User> list=userDAO.findStudentByGrade(grade);
        for(User u:list){
            int sheetId=sheetDAO.getValidId();
            AnswerSheet sheet=new AnswerSheet(u.getId(),id,sheetId);
            u.addDocument(sheetId);
            sheetDAO.add(sheet);
            userDAO.update(u,u.getId());
            paper.addAnswerSheet(sheetId);
        }
        paperDAO.update(paper,id);
    }

    @Override
    public AnswerSheet findSheet(User u, int paperId) {
        return u.findSheet(paperId);
    }

    @Override
    public double autoCorrect(int id) {
        SheetDAO sheetDAO=new SheetDAO();
        AnswerSheet sheet=sheetDAO.findById(id);
        sheet.autoCorrect();
        return sheet.getScoreOfCertainType(Consts.TYPE_SINGLE_CHOICE)+sheet.getScoreOfCertainType(Consts.TYPE_MULTI_CHOICE);
    }
}
