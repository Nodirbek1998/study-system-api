package uz.tatu.service.custom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.tatu.domain.Subjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnitsListDTO {

    @Id
    private Long id;

    private String nameUz;

    private String nameRu;

    private String nameEn;

    private Long subjectsId;

}
