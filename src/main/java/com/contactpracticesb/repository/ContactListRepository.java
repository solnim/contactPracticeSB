package com.contactpracticesb.repository;

import com.contactpracticesb.entity.ContactList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface ContactListRepository extends CrudRepository<ContactList, Long> {

    @Override
    ArrayList<ContactList> findAll();
}
