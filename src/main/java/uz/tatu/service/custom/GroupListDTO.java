package uz.tatu.service.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupListDTO {

    @Id
    private Long id;

    private String name;

    private String imageUrl;

    private LocalDate createdAt;

    private LocalDate updatedAt;

}
