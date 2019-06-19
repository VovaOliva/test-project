package testproject.backfront.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import testproject.backfront.dto.ContactDto;
import testproject.backfront.service.ContactService;


import java.util.List;

@RestController
@RequestMapping("/api")

public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/contacts/all")
    public List<ContactDto> getAllContacts() {
        return contactService.getAllContacts();
    }

    @PostMapping("/contacts")
    public void addContact(@RequestBody ContactDto contactDtO) {
        contactService.addContact(contactDtO);
    }

    @GetMapping("/contacts/{userId}")
    public ContactDto getContact(@PathVariable(value = "userId") Long userId) {
        return contactService.getContact(userId);
    }

    @PutMapping("/contacts/{id}")
    public void updateContact(@PathVariable(value = "id") Long id, @RequestBody ContactDto contactDto) {
        contactService.updateContact(id, contactDto);
    }

    @DeleteMapping("/contacts/{userId}")
    public void deleteContact(@PathVariable(value = "userId") Long userId) {
        contactService.deleteContact(userId);
    }

    @GetMapping("/contacts/byPhone")
    public ContactDto getContactByPhoneNumber(@RequestParam(name = "phoneNumber") String phoneNumber) {
        return contactService.getByPhoneNumber(phoneNumber);
    }

    @GetMapping("/contacts/byName")
    public ContactDto getAllContactsByFullName(@RequestParam(name = "name") String fullName) {
        return contactService.getByFullName(fullName);
    }

}