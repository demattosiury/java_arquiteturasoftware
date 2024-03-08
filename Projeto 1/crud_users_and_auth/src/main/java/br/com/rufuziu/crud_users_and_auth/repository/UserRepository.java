package br.com.demattosiury.crud_users_and_auth.repository;

import br.com.demattosiury.crud_users_and_auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail (String email);
}
