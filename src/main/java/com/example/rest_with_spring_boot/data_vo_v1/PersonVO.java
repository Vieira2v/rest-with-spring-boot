package com.example.rest_with_spring_boot.data_vo_v1;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;


@JsonPropertyOrder({"id", "first_name", "last_name", "gender", "address"})
public class PersonVO extends RepresentationModel<PersonVO> implements Serializable{
    private static final long SERIAL_VERSION_ID = 1L;

    @JsonProperty("id")
    @Mapping("id")
    private long key;
    private String firstName; 
    private String lastName;
    private String address;
    private String gender;
    private boolean enabled;


    public PersonVO() {}

    public static long getSerialversionid() {
        return SERIAL_VERSION_ID;
    }
    public long getKey() {
        return key;
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
    //@PropertyIgnore
    public String getGender() {
        return gender;
    }
    public boolean isEnabled() {
        return enabled;
    }

    public void setKey(long key) {
        this.key = key;
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
        int result = super.hashCode();
        result = prime * result + (int) (key ^ (key >>> 32));
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
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        PersonVO other = (PersonVO) obj;
        if (key != other.key)
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
        if (enabled != other.enabled)
            return false;
        return true;
    }   

   
}
