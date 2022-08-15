package com.stranger.chat.data;

public class Userdata {
    String nameField, emailField, passwordField, confirmPasswordField, phoneNumberField;

    public Userdata() {
    }

    public Userdata(String nameField, String emailField, String passwordField, String confirmPasswordField, String phoneNumberField) {
        this.nameField = nameField;
        this.emailField = emailField;
        this.passwordField = passwordField;
        this.confirmPasswordField = confirmPasswordField;
        this.phoneNumberField = phoneNumberField;
    }

    public String getNameField() {
        return nameField;
    }

    public void setNameField(String nameField) {
        this.nameField = nameField;
    }

    public String getEmailField() {
        return emailField;
    }

    public void setEmailField(String emailField) {
        this.emailField = emailField;
    }

    public String getPasswordField() {
        return passwordField;
    }

    public void setPasswordField(String passwordField) {
        this.passwordField = passwordField;
    }

    public String getConfirmPasswordField() {
        return confirmPasswordField;
    }

    public void setConfirmPasswordField(String confirmPasswordField) {
        this.confirmPasswordField = confirmPasswordField;
    }

    public String getPhoneNumberField() {
        return phoneNumberField;
    }

    public void setPhoneNumberField(String phoneNumberField) {
        this.phoneNumberField = phoneNumberField;
    }
}
