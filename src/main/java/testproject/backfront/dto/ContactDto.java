package testproject.backfront.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ContactDto {

    private Long userId;
    private String contactName;
    private String contactEmail;
    private List<String> phoneNumbers;
    private List<AddressDto> addresses;
}
