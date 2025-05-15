package com.galdovich.java.course.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "app_user")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String roles;

    public void addRole(String role) {
        if (this.roles == null || this.roles.isBlank()) {
            this.roles = role;
        } else {
            Set<String> roleSet = new HashSet<>(Arrays.asList(this.roles.split(",")));
            if (!roleSet.contains(role)) {
                roleSet.add(role);
                this.roles = String.join(",", roleSet);
            }
        }
    }

    public List<String> getRoleList() {
        if (this.roles == null || this.roles.isBlank()) {
            return List.of();
        }
        return Arrays.asList(this.roles.split(","));
    }
}
