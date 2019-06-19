package testproject.backfront.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import testproject.backfront.entity.User;



public interface UserRepository extends JpaRepository<User, Long> {


    User findUserByFullName(String fullName);
}
