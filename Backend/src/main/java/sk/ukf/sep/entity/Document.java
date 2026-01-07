package sk.ukf.sep.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {
    @Id
    @GeneratedValue
    @Column(name = "document_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "internship_id")
    private Internship internship;
    @Column(columnDefinition = "TEXT")
    private String link;
    @Column
    private String type;
    @Column
    private String status;

    @org.hibernate.annotations.CreationTimestamp
    private java.time.LocalDateTime createdAt;
    @org.hibernate.annotations.UpdateTimestamp
    private java.time.LocalDateTime updatedAt;
}
