package uz.tatu.service.custom;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamplesAnswerListDTO {

    @Id
    private Long id;

    private String name;

    private Long createdBy;

    private Long ball;

    private Long examplesId;

    private Long filesId;

    private String createdAt;
}
