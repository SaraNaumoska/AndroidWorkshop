package com.example.zivototekrug;

public class User {
    public String name, email, password, phone, type;
    private int ZbirOceni;
    private int VkupnoOceni;

    public User() { }

    public User(String name, String email, String password, String phone, String type, int zbirOceni, int vkupnoOceni) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.type = type;
        this.ZbirOceni = zbirOceni;
        this.VkupnoOceni = vkupnoOceni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getZbirOceni() {
        return ZbirOceni;
    }

    public void setZbirOceni(int zbirOceni) {
        ZbirOceni = zbirOceni;
    }

    public int getVkupnoOceni() {
        return VkupnoOceni;
    }

    public void setVkupnoOceni(int vkupnoOceni) {
        VkupnoOceni = vkupnoOceni;
    }
}
