package uz.tatu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Files.
 */
@Data
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "files")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Files implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "file_size")
    private Double fileSize;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JsonIgnoreProperties(value = { "role", "groups", "testAnswers", "taskAnswers" }, allowSetters = true)
    private User createdBy;

}
