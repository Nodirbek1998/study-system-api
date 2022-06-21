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
public class GroupUserListDTO {

    @Id
    private Long id;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

}
