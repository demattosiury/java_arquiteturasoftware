package br.com.rufuziu.crud_users_and_auth.services;

import br.com.rufuziu.crud_users_and_auth.dto.UserDTO;
import br.com.rufuziu.crud_users_and_auth.entity.User;
import br.com.rufuziu.crud_users_and_auth.exceptions.user.UserAlreadyExists;
import br.com.rufuziu.crud_users_and_auth.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public Boolean createUser(UserDTO userDto){
        if(repository.existsByEmail(userDto.getEmail())) throw new UserAlreadyExists(userDto.getEmail());

        return repository.save(modelMapper.map(userDto,User.class)).getId() > 0 ?
                Boolean.TRUE : Boolean.FALSE;
    }

    public String readUser(){
        return null;
    }

    public String updateUser(){
        return null;
    }

    public String deleteUser(){
        return null;
    }
}
