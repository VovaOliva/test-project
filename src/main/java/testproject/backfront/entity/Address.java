package testproject.backfront.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;
    private String country;
    private String city;
    private String street;
    private String houseNumber;
    private String apartment;

    @ManyToOne
    private User user;

}
