package com.test.customercrud.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, updatable = false)
    private Long created;
    @Column(nullable = false)
    private Long updated;
    @Column(name = "full_name", nullable = false, length = 50)
    private String fullName;
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    @Column(length = 14)
    private String phone;
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        created = System.currentTimeMillis();
        updated = System.currentTimeMillis();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = System.currentTimeMillis();
    }

    public LocalDateTime getCreatedDateTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(created), ZoneOffset.UTC);
    }

    public void setCreatedDateTime(LocalDateTime dateTime) {
        created = dateTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
    }

    public LocalDateTime getUpdatedDateTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(updated), ZoneOffset.UTC);
    }

    public void setUpdatedDateTime(LocalDateTime dateTime) {
        updated = dateTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
    }
}


