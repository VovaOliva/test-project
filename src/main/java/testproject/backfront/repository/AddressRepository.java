package testproject.backfront.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import testproject.backfront.entity.Address;
import testproject.backfront.entity.User;

import java.util.List;
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAllByUser(User user);

    void deleteAllByUser(User user);
}
