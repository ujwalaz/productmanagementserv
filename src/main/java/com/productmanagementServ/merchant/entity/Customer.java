package com.productmanagementServ.merchant.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.OffsetDateTime;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, columnDefinition = "NVARCHAR(150)")
    private String name;

    @Column(name = "phone", nullable = false, unique = true, columnDefinition = "NVARCHAR(20)")
    private String phone;

    @Column(name = "email", unique = true, columnDefinition = "NVARCHAR(255)")
    private String email;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIMEOFFSET")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIMEOFFSET")
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}
