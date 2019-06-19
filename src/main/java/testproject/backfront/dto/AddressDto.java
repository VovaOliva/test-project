package testproject.backfront.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

    public class AddressDto {

    private String country;
    private String city;
    private String street;
    private String houseNumber;
    private String apartment;
}
