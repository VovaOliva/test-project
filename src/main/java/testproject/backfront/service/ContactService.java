package testproject.backfront.service;

import testproject.backfront.dto.ContactDto;

import java.util.List;

public interface ContactService {

    List<ContactDto> getAllContacts();

    void addContact(ContactDto contactDto);

    void updateContact(Long id, ContactDto contactDto);

    void deleteContact(Long userId);

    ContactDto getByPhoneNumber(String phoneNumber);

    List<ContactDto> getByFullName(String fullName);

    ContactDto getContact(Long userId);
}
