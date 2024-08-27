package com.example.demo.entity;


import jakarta.persistence.*;

@Table(name = "Member")
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String Email;
    private String Username;
    private String password;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }



    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}
