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
    @Transactional(readOnly = true)
    public List <ContactDto> getAllContacts() {
        List <Long> userIds = userRepository.getAllContactsById();

        return userIds.stream()
                .map(userId -> getContact(userId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addContact(ContactDto contactDto) {

       User user = User.builder()
                .fullName(contactDto.getContactName())
                .email(contactDto.getContactEmail())
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
                            .fullName(contactDto.getContactName())
                            .email(contactDto.getContactEmail())
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
    @Transactional
    public void deleteContact(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundExeption("User not found "));
        addressRepository.deleteAllByUser(user);
        phoneNumberRepository.deleteAllByUser(user);
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactDto getByPhoneNumber(String phoneNumber) {

        PhoneNumber phone = phoneNumberRepository.findByPhoneNumber(phoneNumber);
        if (phone == null) {
            throw new UserNotFoundExeption("User not found  : " + phoneNumber);
        }
        return getContact(phone.getUser().getId());
    }

    @Override
    @Transactional(readOnly = true)
    public ContactDto getByFullName(String fullName) {

        User user = userRepository.findUserByFullName(fullName);

        return getContact(user.getId());


    }

    @Override
    @Transactional
    public ContactDto getContact(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundExeption("User not found"));

        List <PhoneNumber> phoneNumbers = phoneNumberRepository.findAllByUser(user);
        List <Address> addresses = addressRepository.findAllByUser(user);

        ContactDto contactDto = ContactDto.builder()
                .contactName(user.getFullName())
                .contactEmail(user.getEmail())
                .phoneNumbers(phoneNumbers.stream().map(PhoneNumber::getPhoneNumber).collect(Collectors.toList()))
                .addresses(addresses.stream().map(address -> AddressToAddressDto(address)).collect(Collectors.toList()))
                .build();

        return contactDto;

    }

    private AddressDto AddressToAddressDto(Address address) {
        return AddressDto.builder()
                .country(address.getCountry())
                .city(address.getCity())
                .street(address.getStreet())
                .apartment(address.getApartment())
                .houseNumber(address.getHouseNumber())
                .build();
    }


}
