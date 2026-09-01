package com.portfolio.web.dto;

import com.portfolio.domain.TestAccount;

public class TestAccountItem {

    private String roleName;
    private String username;
    private String password;
    private String notes;

    public static TestAccountItem from(TestAccount account) {
        TestAccountItem item = new TestAccountItem();
        item.roleName = account.getRoleName();
        item.username = account.getUsername();
        item.password = account.getPassword();
        item.notes = account.getNotes();
        return item;
    }

    public boolean isBlank() {
        return isEmpty(roleName) && isEmpty(username) && isEmpty(password) && isEmpty(notes);
    }

    private static boolean isEmpty(String value) {
        return value == null || value.isBlank();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
