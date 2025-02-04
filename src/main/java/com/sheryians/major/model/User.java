package com.sheryians.major.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import java.util.List;

@Entity
@Data
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @NotEmpty
    @Valid
    @Column(nullable = false)
    private String firstName;

    private String lastName;

    @Column(nullable = false,unique = true)
    @NotEmpty
    @Email(message="{errors.invalid_email}")
    private String email;

    private String password;
    @ManyToMany(cascade= CascadeType.MERGE, fetch=FetchType.EAGER)
    @JoinTable(
            name="user_role",
            joinColumns = {@JoinColumn(name="User_ID",referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID",referencedColumnName="ID")}
    )

    private List<Role> roles;

    public User(){

    }

    public User(User user){

        this.firstName= user.getFirstName();
        this.lastName=user.getLastName();
        this.email= user.getEmail();
        this.password= user.getPassword();
        this.roles=user.getRoles();
    }
    //default constructor

}
