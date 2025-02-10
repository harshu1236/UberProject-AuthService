package org.example.uberprojectauthservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // IDENTITY means auto increment
    private Long id;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)  // this annotation tells spring about the formats of date object to be stored . i.e, date,time,timestamp
    @CreatedDate     // this annotation tells spring that only handle it for object creation
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate     // this annotation tells spring that only handle it for object update
    private Date updatedAt;
}
