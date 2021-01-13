package com.hiveelpay.common.model.auth;

import java.io.Serializable;

/**
 * {
 * "id": 10,
 * "email": null,
 * "emailVerified": 0,
 * "phoneNumber": "3236562777",
 * "phoneNumberVerified": 0,
 * "email2": null,
 * "phoneNumber2": null,
 * "userName": "10",
 * "role": "merchant",
 * "nickName": "Sunset carwash",
 * "givenName": null,
 * "familyName": null,
 * "imageUrl": "/upload/merchant/8/diy/avatar.jpeg?7177560",
 * "type": null,
 * "permission": "2",
 * "memberId": null,
 * "merchantId": "10",
 * "employeeId": null,
 * "webSite": "sunsetcarwash.com",
 * "showPhone": 0,
 * "created_at": "2018-06-22T00:05:42.000Z",
 * "updated_at": "2018-06-22T00:05:42.000Z",
 * "password_digest": "$2b$12$4KhLHpKpADBpTPSnGarpdOMDYg8Fye4.XIn7Vz/pbio2XCaykUhuC",
 * "origin": "usedcar_merchant"
 * },
 */
public class User implements Serializable {
    private Long id;
    private String userName;
    private String role;
    private String imageUrl;
    private String email;
    private String permission;
    private String nickName;


    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", role='" + role + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
