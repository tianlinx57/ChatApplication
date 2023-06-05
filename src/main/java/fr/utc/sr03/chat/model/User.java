package fr.utc.sr03.chat.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "sr03_users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) // strategy=GenerationType.IDENTITY => obligatoire pour auto increment mysql
    private long id;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "mail")
    private String mail;

    @Column(name = "isDisabled")
    private Boolean isDisabled = false;


    @JsonBackReference(value="users")
    @ManyToMany(mappedBy = "users")
    private List<Chat> chats_user;

    @JsonBackReference(value="proprietaire")
    @OneToMany(mappedBy = "proprietaire")
    private List<Chat> chats_proprietaire;



    private String password;

    private boolean admin;

    public User(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public List<Chat> getChats_user() {
        return chats_user;
    }

    public void setChats_user(List<Chat> chats_user) {
        this.chats_user = chats_user;
    }

    public List<Chat> getChats_proprietaire() {
        return chats_proprietaire;
    }

    public void setChats_proprietaire(List<Chat> chats_proprietaire) {
        this.chats_proprietaire = chats_proprietaire;
    }
}
