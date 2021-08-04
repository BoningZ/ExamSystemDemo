package com.company.bean;

import com.company.dao.PaperDAO;
import com.company.dao.SheetDAO;
import com.company.utils.Consts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class User implements Serializable {
    private String username;
    private String password;
    private String id;
    private boolean isStudent;
    private int[] ownedDocuments=new int[50];
    private int numOfOwnedDocuments=0;
    private int sizeOfOwnedDocuments=50;
    private int grade;

    @Override
    public String toString(){
        return "User:\nUsername:"+username+"\nid:"+id+"\nis"+((isStudent)?"":" not")+" a student";
    }

    public int getNumOfOwnedDocuments() {
        return numOfOwnedDocuments;
    }

    public void setNumOfOwnedDocuments(int numOfOwnedDocuments) {
        this.numOfOwnedDocuments = numOfOwnedDocuments;
    }

    public int getSizeOfOwnedDocuments() {
        return sizeOfOwnedDocuments;
    }

    public void setSizeOfOwnedDocuments(int sizeOfOwnedDocuments) {
        this.sizeOfOwnedDocuments = sizeOfOwnedDocuments;
    }

    public User(){}
    public User(String username, String password, String id, boolean isStudent, int grade){
        this.username=username;
        this.password=password;
        this.id=id;
        this.isStudent=isStudent;
        this.grade=grade;
        sizeOfOwnedDocuments=4;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {this.password = password;}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isStudent() {
        return isStudent;
    }

    public void setStudent(boolean student) {
        isStudent = student;
    }


    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) { this.grade = grade;}

    public void addDocument(int v){
        if(numOfOwnedDocuments==sizeOfOwnedDocuments)expand();
        ownedDocuments[numOfOwnedDocuments++]=v;
    }

    private void expand(){
        int[] temp=new int[sizeOfOwnedDocuments<<1];
        for(int i=0;i<numOfOwnedDocuments;i++)
            temp[i]=ownedDocuments[i];
        ownedDocuments=temp;
        sizeOfOwnedDocuments<<=1;
    }

    public String ownedDocumentsToString(){
        if(numOfOwnedDocuments==0)return "";
        String st=""+ownedDocuments[0];
        for(int i=1;i<numOfOwnedDocuments;i++)
            st+=","+ownedDocuments[i];
        return st;
    }

    public void stringToOwnedDocuments(String st){
        ownedDocuments=new int[50];
        numOfOwnedDocuments=0;
        sizeOfOwnedDocuments=50;
        Scanner sc=new Scanner(st);
        sc.useDelimiter(",");
        while (sc.hasNext()){
            if(numOfOwnedDocuments==sizeOfOwnedDocuments)expand();
            ownedDocuments[numOfOwnedDocuments++]=Integer.parseInt(sc.next());
        }
    }

    public Object getDocumentByRank(int rank){
        if(isStudent){
            SheetDAO sheetDAO=new SheetDAO();
            return sheetDAO.findById(ownedDocuments[rank]);
        }else{
            PaperDAO paperDAO=new PaperDAO();
            return paperDAO.findById(ownedDocuments[rank]);
        }
    }
    public List<TestPaper> getDoneExam(){
        SheetDAO sheetDAO=new SheetDAO();
        PaperDAO paperDAO=new PaperDAO();
        List<TestPaper> list=new ArrayList<>();
        for(int i=0;i<numOfOwnedDocuments;i++) {
            TestPaper paper=paperDAO.findById(sheetDAO.findById(ownedDocuments[i]).getTestPaperId());
            if(paper.isDoneByStudent(id))list.add(paper);
        }
        return list;
    }
    public List<AnswerSheet> getDoneSheets(){
        SheetDAO sheetDAO=new SheetDAO();
        PaperDAO paperDAO=new PaperDAO();
        List<AnswerSheet> list=new ArrayList<>();
        for(int i=0;i<numOfOwnedDocuments;i++) {
            TestPaper paper=paperDAO.findById(sheetDAO.findById(ownedDocuments[i]).getTestPaperId());
            if(paper.isDoneByStudent(id))list.add(sheetDAO.findById(ownedDocuments[i]));
        }
        return list;
    }
    public List<TestPaper> getTodoExam(){
        PaperDAO paperDAO=new PaperDAO();
        SheetDAO sheetDAO=new SheetDAO();
        List<TestPaper> list=new ArrayList<>();
        for(int i=0;i<numOfOwnedDocuments;i++){
            TestPaper paper=paperDAO.findById(sheetDAO.findById(ownedDocuments[i]).getTestPaperId());
            if(!paper.isDoneByStudent(id))list.add(paper);
        }
        return list;
    }
    public List<TestPaper> getUnreleased(){
        PaperDAO paperDAO=new PaperDAO();
        List<TestPaper> list=new ArrayList<>();
        for(int i=0;i<numOfOwnedDocuments;i++){
            TestPaper paper=paperDAO.findById(ownedDocuments[i]);
            if(paper.getStatus()== Consts.STATE_UNRELEASED)list.add(paper);
        }
        return list;
    }
    public List<TestPaper> getReleased(){
        PaperDAO paperDAO=new PaperDAO();
        List<TestPaper> list=new ArrayList<>();
        for(int i=0;i<numOfOwnedDocuments;i++){
            TestPaper paper=paperDAO.findById(ownedDocuments[i]);
            if(paper.getStatus()!= Consts.STATE_UNRELEASED)list.add(paper);
        }
        return list;
    }
    public AnswerSheet findSheet(int paperId){
        SheetDAO sheetDAO=new SheetDAO();
        for(int i=0;i<numOfOwnedDocuments;i++){
            AnswerSheet sheet=sheetDAO.findById(ownedDocuments[i]);
            if(sheet.getTestPaperId()==paperId)return sheet;
        }
        return null;
    }

}
