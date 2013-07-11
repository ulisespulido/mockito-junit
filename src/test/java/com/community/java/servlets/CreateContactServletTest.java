package com.community.java.servlets;

import com.community.java.beans.Contact;
import com.community.java.dao.ContactDao;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateContactServletTest {

    @Test
    public void shouldAskDAOToCreateContact() throws ServletException, IOException {

        CreateContactServlet contactServlet = new CreateContactServlet();
        ContactDao contactDao = mock(ContactDao.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        contactServlet.setContactDAO(contactDao);

        when(request.getParameter("name")).thenReturn("Juan Perez");
        when(request.getParameter("email")).thenReturn("juan@perez.com");

        contactServlet.doPost(request, null);

        ArgumentCaptor<Contact> argumentCaptor = ArgumentCaptor.forClass(Contact.class);

        verify(contactDao).save(argumentCaptor.capture());

        Contact actualContact = argumentCaptor.getValue();
        assertEquals("Juan Perez", actualContact.getName());
        assertEquals("juan@perez.com", actualContact.getEmail());
    }

    @Test
    public void shouldReturnAnInstanceOfDAOAlways() {
        CreateContactServlet createContactServlet = new CreateContactServlet();
        assertNotNull("Expected an instance but was null", createContactServlet.getContactDAO());
    }
}
