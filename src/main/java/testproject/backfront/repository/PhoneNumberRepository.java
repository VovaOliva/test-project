package testproject.backfront.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import testproject.backfront.entity.PhoneNumber;
import testproject.backfront.entity.User;

import java.util.List;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {

    List<PhoneNumber> findAllByUser(User user);

    void deleteAllByUser(User user);

    PhoneNumber findByPhoneNumber(String number);



}
