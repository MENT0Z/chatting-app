package com.mruraza.mychat;

import android.net.Uri;

public class User {

    String  profilepic,userName, emailadd, passone ,userId,lastmessage,status;

    public User(){}
    public User(String profilepic, String userName, String re_email, String password1, String userId, String status)
    {
        this.profilepic=profilepic;
        this.userName=userName;
        this.emailadd=re_email;
        this.passone=password1;
        this.userId = userId;
        this.status=status;
    }


    public String getProfilepic() {
        return profilepic;
    }


    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getEmailadd() {
        return emailadd;
    }

    public void setEmailadd(String emailadd) {
        this.emailadd = emailadd;
    }


    public String getPassone() {
        return passone;
    }

    public void setPassone(String passone) {
        this.passone = passone;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}