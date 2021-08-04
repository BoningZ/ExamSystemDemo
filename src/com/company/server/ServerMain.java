package com.company.server;

public class ServerMain {
    public static void main(String[] args) throws Exception{
        ExamServiceServer server=new ExamServiceServer();
        server.listen();
    }
}
