package com.dannyj182.notesmanager.model.entity;

import com.dannyj182.notesmanager.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 200)
    private String password;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 20)
    private String lastname;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private Boolean locked;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private Boolean disabled;
}
