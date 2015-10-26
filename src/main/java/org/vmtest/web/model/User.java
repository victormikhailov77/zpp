package org.vmtest.web.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by victor on 24.10.15.
 */
public class User implements Serializable {

    @NotEmpty
    @Size(min=6, max=30)
    private String userName;

    @NotEmpty
    @Size(min=8, max=30)
    private String password;

    @NotEmpty
    @Size(min=2, max=50)
    private String firstName;

    @NotEmpty
    @Size(min=2, max=50)
    private String lastName;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min=2, max=50)
    private String street;

    @NotEmpty
    @Size(min=2, max=50)
    private String city;

    @NotEmpty
    @Size(min=2, max=50)
    private String state;

    @NotEmpty
    @Size(min=2, max=50)
    private String country;

    @NotEmpty
    @Size(min=2, max=12)
    private String postCode;

    @NotEmpty
    @Size(min=2, max=50)
    private String phone;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
