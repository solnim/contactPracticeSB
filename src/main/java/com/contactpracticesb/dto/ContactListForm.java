package com.contactpracticesb.dto;

import com.contactpracticesb.entity.ContactList;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ContactListForm {

    private Long id;
    private String name, phoneNum, email;

    public ContactList toEntity() {
        //새롭게 연락처를 만들어서 반환. ContactList는 엔티티 클래스이므로 엔티티 클래스에 객체를 생성해야하니까 생성자를 호출
        return new ContactList(id, name, phoneNum, email);
    }
}
