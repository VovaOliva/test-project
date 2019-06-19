package testproject.backfront.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testproject.backfront.entity.PhoneNumber;
import testproject.backfront.entity.User;

import java.util.List;
import java.util.Optional;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {

    List<PhoneNumber> findAllByUser(Optional<User> user);

    void deleteAllByUser(User user);

    PhoneNumber findByPhoneNumber(String number);



}
