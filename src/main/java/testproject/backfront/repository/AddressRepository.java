package testproject.backfront.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testproject.backfront.entity.Address;
import testproject.backfront.entity.User;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAllByUser(Optional<User> user);

    void deleteAllByUser(User user);
}
