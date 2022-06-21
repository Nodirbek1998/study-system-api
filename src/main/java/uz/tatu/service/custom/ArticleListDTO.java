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
public class ArticleListDTO {

    @Id
    private Long id;

    private String name;

    private String text;

    private String body;

    private LocalDate createdAt;

    private String firstName;

    private Long imagesId;

    private String status;

}
