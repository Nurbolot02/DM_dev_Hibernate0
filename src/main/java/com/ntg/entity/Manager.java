package com.ntg.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Manager extends User {
    private String projectName;

    @Builder
    public Manager(Long id, String userName, PersonalInfo personalInfo, int age, Role role, Company company, Profile profile, List<UserChat> userChats, String projectName) {
        super(id, userName, personalInfo, age, role, company, profile, userChats);
        this.projectName = projectName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Manager manager = (Manager) o;
        return getId() != null && Objects.equals(getId(), manager.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
