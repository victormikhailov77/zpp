package org.vmtest.persistence.entity;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by victor on 24.10.15.
 */
// The same POJO used for persistence and for web model validation
// This is violation of SRP, I know :-)
public class User implements Serializable {

    private static final String NAME_REGEX = "[a-zA-z]+([ '-][a-zA-Z]+)*";

    private static final String INTERNATIONAL_PHONE_REGEX = "^\\+(?:[0-9] ?){6,14}[0-9]$";

    @Id
    private BigInteger id;

    @NotEmpty
    @Size(min = 6, max = 30)
    @Indexed(unique = true)
    private String userName;

    @NotEmpty
    @Size(min = 8, max = 30)
    private String password;

    @NotEmpty
    @Size(min = 8, max = 30)
    @Transient
    private String verifyPassword;

    @NotEmpty
    @Size(min = 1, max = 60)
    @Pattern(regexp = NAME_REGEX, message = "Only letters, space and dash allowed in first name")
    private String firstName;

    @NotEmpty
    @Size(min = 1, max = 60)
    @Pattern(regexp = NAME_REGEX, message = "Only letters, space and dash allowed in last name")
    private String lastName;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min = 2, max = 50)
    private String street;

    @NotEmpty
    @Size(min = 2, max = 50)
    private String city;

    @NotEmpty
    @Size(min = 2, max = 50)
    private String state;

    @NotEmpty
    @Size(min = 2, max = 50)
    private String country;

    @NotEmpty
    @Size(min = 2, max = 12)
    private String postCode;

    @NotEmpty
    @Pattern(regexp = INTERNATIONAL_PHONE_REGEX, message = "Phone number must be in international format +123456789, +12 3456789 or +123 456 789 000")
    private String phone;

    @AssertTrue(message = "password and verify password must be the same")
    private boolean isValid() {
        return this.password.equals(this.verifyPassword);
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

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

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", postCode='" + postCode + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (userName != null ? !userName.equals(user.userName) : user.userName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
