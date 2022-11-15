package com.contactpracticesb.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@AllArgsConstructor //생성자 생성
@NoArgsConstructor
@ToString
@Getter
public class ContactList {

    @Id
    @GeneratedValue
    public
    Long id;

    @Column
    public
    String name;

    @Column
    public
    String phoneNum;

    @Column
    public
    String email;



}
