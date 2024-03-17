package br.com.rufuziu.crud_users_and_auth.services;

import br.com.rufuziu.crud_users_and_auth.dto.user.UserDTO;
import br.com.rufuziu.crud_users_and_auth.entity.User;
import br.com.rufuziu.crud_users_and_auth.exceptions.general.InvalidRequest;
import br.com.rufuziu.crud_users_and_auth.exceptions.user.UserAlreadyExists;
import br.com.rufuziu.crud_users_and_auth.exceptions.user.UserNotFound;
import br.com.rufuziu.crud_users_and_auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository repository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public Boolean createUser(UserDTO userDto) {
        if (repository.existsByEmail(userDto.getEmail())) throw new UserAlreadyExists(userDto.getEmail());

        return repository.save(modelMapper.map(userDto, User.class)).getId() > 0 ?
                Boolean.TRUE : Boolean.FALSE;
    }

    public Integer activateUserByEmail(String email) {
        if (repository.existsByEmailAndActiveFalse(email)) {
            return repository.activateUserByEmail(email);
        } else {
            throw new InvalidRequest("Invalid request. IP registered.");
        }
    }

    public UserDTO readUser(String email) {
        Optional<User> user = repository.findByEmail(email);

        if (user.isPresent()) {
            return modelMapper.map(user.get(), UserDTO.class);
        } else throw new UserNotFound("User not found!");
    }

    public UserDTO updateUser(UserDTO userDTO, String email) {
        if (userDTO.getEmail().contains(email)) {
            Optional<User> user = repository.findByEmail(email);
            if (user.isPresent()) {
                user.get().updateUser(userDTO);
                repository.save(user.get());
                return modelMapper.map(user.get(), UserDTO.class);
            } else {
                String message = new StringBuilder()
                        .append("User with email: ")
                        .append(userDTO.getEmail())
                        .append(" not found")
                        .toString();
                throw new UserNotFound(message);
            }
        } else throw new InvalidRequest("The emails don't match!");
    }

    public String deleteUser() {
        return null;
    }
}
