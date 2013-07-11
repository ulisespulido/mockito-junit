package com.community.java.servlets;

import com.community.java.beans.Contact;
import com.community.java.dao.ContactDao;
import com.community.java.dao.impl.ContactDaoJdbc;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="Create Contact Servlet", urlPatterns = "/createContact")
public class CreateContactServlet extends HttpServlet {


    private ContactDao contactDAO = new ContactDaoJdbc();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Contact contact = new Contact();
        contact.setName(req.getParameter("name"));
        contact.setEmail(req.getParameter("email"));
        getContactDAO().save(contact);
    }

    public void setContactDAO(ContactDao contactDAO) {
        this.contactDAO = contactDAO;
    }

    public ContactDao getContactDAO() {
        return contactDAO;
    }
}
