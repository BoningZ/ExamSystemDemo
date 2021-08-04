package com.company.test;

import com.company.bean.User;
import com.company.dao.UserDAO;

public class TestAddUser {
    public static void main(String[] args) {
        UserDAO userDAO=new UserDAO();
        userDAO.add(new User("ÕÅ²©Äþ","123456","202022300310",true,1));
    }
}
