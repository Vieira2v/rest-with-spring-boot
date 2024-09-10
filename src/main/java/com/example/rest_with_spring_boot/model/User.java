package com.example.rest_with_spring_boot.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class User implements UserDetails{

    private static final Long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_name", unique=true)
    private String username;

    @Column(name="full_name", unique=true)
    private String fullName;

    @Column(name="password")
    private String password;

    @Column(name="account_non_expired")
    private boolean accountNonExpired;

    @Column(name="account_non_locked")
    private boolean accountNonLocked;

    @Column(name="credentials_non_expired")
    private boolean credentialsNonExpired;

    @Column(name="enabled")
    private boolean enabled;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="user_permission", joinColumns={@JoinColumn (name="id_user")},
        inverseJoinColumns = {@JoinColumn (name="id_permission")})
    private List<Permission> permissions;
    //Esta linha define uma lista de permissões que um User possui. Cada instância da classe User terá um conjunto de permissões relacionadas.

    public List<String> getRoles() {
        List<String> roles = new ArrayList<>();
        for (Permission permission : permissions) {
            roles.add(permission.getDescription());
        }
        return roles;

        //Esse método transforma a lista de permissões (permissions) em uma lista de "roles" (cargos/funções) que o usuário possui.
    }


    //Esse é um construtor padrão da classe User. Ele não faz nada explicitamente, mas é necessário para que o framework (como Hibernate ou JPA) possa criar instâncias de User.
    public User() {}


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.permissions;

        /*É uma interface do Spring Security que representa uma autoridade 
        concedida a um usuário (como uma permissão ou um papel).
        return this.permissions;: Aqui, ele está retornando a lista de 
        permissões do usuário. Provavelmente a classe Permission implementa
        GrantedAuthority, então as permissões podem ser tratadas 
        diretamente como autoridades (roles) no Spring Securit */
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }


    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }


    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }


    @Override
    public boolean isEnabled() {
        return this.enabled;
    }


    public static Long getSerialversionuid() {
        return serialVersionUID;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getUserName() {
        return username;
    }


    public void setUserName(String userName) {
        this.username = userName;
    }


    public String getFullName() {
        return fullName;
    }


    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }


    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }


    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }


    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public List<Permission> getPermissions() {
        return permissions;
    }


    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + (accountNonExpired ? 1231 : 1237);
        result = prime * result + (accountNonLocked ? 1231 : 1237);
        result = prime * result + (credentialsNonExpired ? 1231 : 1237);
        result = prime * result + (enabled ? 1231 : 1237);
        result = prime * result + ((permissions == null) ? 0 : permissions.hashCode());
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
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        if (fullName == null) {
            if (other.fullName != null)
                return false;
        } else if (!fullName.equals(other.fullName))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (accountNonExpired != other.accountNonExpired)
            return false;
        if (accountNonLocked != other.accountNonLocked)
            return false;
        if (credentialsNonExpired != other.credentialsNonExpired)
            return false;
        if (enabled != other.enabled)
            return false;
        if (permissions == null) {
            if (other.permissions != null)
                return false;
        } else if (!permissions.equals(other.permissions))
            return false;
        return true;
    }

    
    
    
}
