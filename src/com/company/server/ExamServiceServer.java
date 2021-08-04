package com.company.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.company.service.ExamService;
import com.company.service.ExamServiceImpl;
import com.company.exception.BaseException;
import com.company.utils.Consts;

/** 服务器端 */
public class ExamServiceServer {
    private Map<String, ExamService> serviceMap = new HashMap<String, ExamService>();




    public void listen() throws IOException {
        int port = Consts.SERVER_PORT;
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            System.out.println("服务器登陆");
            Socket socket = serverSocket.accept();
            new Service(socket).start();
        }
    }

    class Service extends Thread {
        Socket socket;

        public Service(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                processRequest(in, out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    public void processRequest(ObjectInputStream in, ObjectOutputStream out)
            throws IOException, ClassNotFoundException, SecurityException,
            NoSuchMethodException {
        Request request = (Request) in.readObject();
        String sessionId = request.getSessionId();
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            ExamService service = new ExamServiceImpl();
            serviceMap.put(sessionId, service);
        }
        ExamService service = serviceMap.get(sessionId);
        Class cls = service.getClass();
        String methodName = request.getMethod();
        Class[] argsTypes = request.getArgsTypes();
        Object[] params = request.getArgs();
        Method method = cls.getDeclaredMethod(methodName, argsTypes);
        // 执行业务方法
        Response res;
        try {
            Object val = method.invoke(service, params);
            res = new Response(sessionId, val);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            BaseException ex = (BaseException) e.getTargetException();
            res = new Response(sessionId, ex);
        } catch (Exception e) {
            e.printStackTrace();
            res = new Response(sessionId, e);
        }
        out.writeObject(res);
        out.flush();
    }
}
