package com.example.integrationtests.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PersonVO implements Serializable{
    private static final long SERIAL_VERSION_ID = 1L;

    private long id;
    private String firstName; 
    private String lastName;
    private String address;
    private String gender;
    private boolean enabled;

    public static long getSerialversionid() {
        return SERIAL_VERSION_ID;
    }
    public long getId() {
        return id;
    }
    @JsonProperty("first_name")
    public String getFirstName() {
        return firstName;
    }
    @JsonProperty("last_name")
    public String getLastName() {
        return lastName;
    }
    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }
    public boolean isEnabled() {
        return enabled;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + (enabled ? 1231 : 1237);
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
        PersonVO other = (PersonVO) obj;
        if (id != other.id)
            return false;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        if (gender == null) {
            if (other.gender != null)
                return false;
        } else if (!gender.equals(other.gender))
            return false;
        return enabled == other.enabled;
    }

   
}
