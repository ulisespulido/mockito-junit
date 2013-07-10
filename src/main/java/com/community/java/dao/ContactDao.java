package com.community.java.dao;

import com.community.java.beans.Contact;
import com.community.java.beans.Result;

public interface ContactDao {
    Result save(Contact contact);
}
