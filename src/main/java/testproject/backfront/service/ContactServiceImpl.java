package testproject.backfront.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import testproject.backfront.dto.AddressDto;
import testproject.backfront.dto.ContactDto;
import testproject.backfront.entity.Address;
import testproject.backfront.entity.PhoneNumber;
import testproject.backfront.entity.User;
import testproject.backfront.exeption.UserNotFoundExeption;
import testproject.backfront.repository.AddressRepository;
import testproject.backfront.repository.PhoneNumberRepository;
import testproject.backfront.repository.UserRepository;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PhoneNumberRepository phoneNumberRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<ContactDto> getAllContacts() {
        return null;
    }

    @Override
    public void addContact(ContactDto contactDto) {

       User user = User.builder()
                .userFullName(contactDto.getContactName())
                .userEmail(contactDto.getContactName())
                .createdDate(LocalDateTime.now())
                .build();


        List<PhoneNumber> phoneNumbers = contactDto.getPhoneNumbers().stream()
                                                                     .map(phoneNumber -> new PhoneNumber(0L, LocalDateTime.now(),phoneNumber, user))
                                                                    .collect(Collectors.toList());

        List<Address> addresses = contactDto.getAddresses().stream()
                                                           .map(address -> getAddressFromContactDto(address, user))
                                                            .collect(Collectors.toList());

        userRepository.save(user);
        phoneNumberRepository.saveAll(phoneNumbers);
        addressRepository.saveAll(addresses);
    }

    private Address getAddressFromContactDto(AddressDto addressDto, User user) {
        return new Address( 0L, LocalDateTime.now(), addressDto.getCountry(), addressDto.getCity(), addressDto.getStreet(),
                            addressDto.getHouseNumber(), addressDto.getApartment(), user);
    }

    @Override
    @Transactional
    public void updateContact(Long id ,ContactDto contactDto) {

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundExeption("User not found"));

        User updateUser = User.builder()
                            .id(id)
                            .userFullName(contactDto.getContactName())
                            .userEmail(contactDto.getContactEmail())
                            .createdDate(LocalDateTime.now())
                            .build();

        List<PhoneNumber> updatePhoneNumbers = contactDto.getPhoneNumbers().stream()
                .map(updatePhoneNumber -> new PhoneNumber(1L, LocalDateTime.now(),updatePhoneNumber, user))
                .collect(Collectors.toList());

        List<Address> updateAddresses = contactDto.getAddresses().stream()
                .map(updateAddress -> getAddressFromContactDto(updateAddress, user))
                .collect(Collectors.toList());

        userRepository.save(updateUser);
        phoneNumberRepository.saveAll(updatePhoneNumbers);
        addressRepository.saveAll(updateAddresses);



    }

    @Override
    public void deleteContact(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundExeption("User not found "));
        addressRepository.deleteAllByUser(user);
        phoneNumberRepository.deleteAllByUser(user);
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactDto getByPhoneNumber(String phoneNumber) {

        PhoneNumber phone = phoneNumberRepository.findByPhoneNumber(phoneNumber);
        if (phone == null) {
            throw new UserNotFoundExeption("User not found by this number : " + phoneNumber);
        }
        return getContact(phone.getUser().getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactDto> getByFullName(String fullName) {

        List<User> users = (List<User>) userRepository.findUserByFullName(fullName);

        return users.stream()
                .map(user -> getContact(user.getId()))
                .collect(Collectors.toList());


    }

    @Override
    public ContactDto getContact(Long userId) {

        Optional<User> user = Optional.ofNullable(userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundExeption("User not found by id : " + userId)));;
        List<String> phoneNumbers = getPhoneNumbers(user);
        List<Address> addresses = addressRepository.findAllByUser(user);

        return ContactDto.builder()
                .userId(user.get().getId())
                .contactName(user.get().getUserFullName())
                .contactEmail(user.get().getUserEmail())
                .phoneNumbers(phoneNumbers)
                .addresses(addresses.stream().map(address -> convertAddressToAddressDto(address)).collect(Collectors.toList()))
                .build();

    }

    private AddressDto convertAddressToAddressDto(Address address) {
        return AddressDto.builder()
                .country(address.getCountry())
                .city(address.getCity())
                .street(address.getStreet())
                .apartment(address.getApartment())
                .houseNumber(address.getHouseNumber())
                .build();
    }

    private List<String> getPhoneNumbers(Optional<User> user) {
        List<PhoneNumber> numbers = phoneNumberRepository.findAllByUser(user);
        return numbers.stream()
                .map(phoneNumber -> phoneNumber.getPhoneNumber())
                .collect(Collectors.toList());
    }
}
