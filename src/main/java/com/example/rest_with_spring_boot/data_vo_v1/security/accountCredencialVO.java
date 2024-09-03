package com.example.rest_with_spring_boot.data_vo_v1.security;

import java.io.Serializable;

public class accountCredencialVO implements Serializable{

    private static final long SERIAL_VERSION_ID = 1L;

    private String username;
    private String password;

    public accountCredencialVO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static long getSerialVersionId() {
        return SERIAL_VERSION_ID;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        accountCredencialVO other = (accountCredencialVO) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        return true;
    }

    

    
}
