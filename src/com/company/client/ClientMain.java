package com.company.client;

import com.company.gui.*;
import com.company.service.ExamServiceAgentImpl;

public class ClientMain {
    public static void main(String[] args) {
        try{
            ExamServiceAgentImpl service=new ExamServiceAgentImpl();
            ClientContext context=new ClientContext(service);

            LoginFrame loginFrame=new LoginFrame(context);
            StudentRegisterFrame studentRegisterFrame=new StudentRegisterFrame(context);
            TeacherRegisterFrame teacherRegisterFrame=new TeacherRegisterFrame(context);

            context.setLoginFrame(loginFrame);
            context.setStudentRegisterFrame(studentRegisterFrame);
            context.setTeacherRegisterFrame(teacherRegisterFrame);

            context.start();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

