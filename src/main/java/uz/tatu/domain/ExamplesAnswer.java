package uz.tatu.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uz.tatu.domain.audit.DateAudit;

import javax.persistence.*;
import java.io.Serializable;

@Data
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "example_answer")
public class ExamplesAnswer  extends DateAudit implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    private String name;

    @ManyToOne
    private User createdBy;

    @OneToOne
    private Files files;


    @ManyToOne
    private Examples examples;

    private Double ball;
}
