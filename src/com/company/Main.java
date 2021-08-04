package com.company;

import com.company.bean.User;
import com.company.dao.UserDAO;

public class Main {

    public static void main(String[] args) {
        UserDAO userdao=new UserDAO();
        User user=new User("уехЩ","123456","202022300310",true,1);
        userdao.add(user);
    }
}
