package com.project.Evenemenetyback.service.dto;

import com.project.Evenemenetyback.config.Constants;
import com.project.Evenemenetyback.domain.Role;
import com.project.Evenemenetyback.domain.User;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

    private Long id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String username;

    @Size(max = 50)
    private String name;


    @Size(max = 30)
    private Set<Role> roles;


    @Email
    @Size(min = 5, max = 254)
    private String email;


    @Size(min = 60, max = 60)
    private String password;


    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.name=user.getName();
        this.email = user.getEmail();
        this.roles = user.getRoles();
       this.password=user.getPassword();
    }

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public Set<Role> getRoles() {
        return roles;
    }

    public void setRole(Set<Role> roles) {
        this.roles = roles;
    }




    @Override
    public String toString() {
        return "UserDTO{" +
            "login='" + username + '\'' +
            ", Name='" + name + '\'' +
             ", email='" + email + '\'' +
                ", roles='" + roles + '\'' +
                "}";
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
