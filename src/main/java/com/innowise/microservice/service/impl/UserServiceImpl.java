    package com.innowise.microservice.service.impl;

    import com.innowise.microservice.dto.UserInputDto;
    import com.innowise.microservice.dto.UserOutputDto;
    import com.innowise.microservice.exceptions.AlreadyTakenException;
    import com.innowise.microservice.exceptions.EntityNotFoundException;
    import com.innowise.microservice.exceptions.UserAlreadyRegisteredException;
    import com.innowise.microservice.mapper.UserMapper;
    import com.innowise.microservice.model.User;
    import com.innowise.microservice.repository.UserRepository;
    import com.innowise.microservice.service.UserService;
    import com.innowise.microservice.specifications.UserSpecifications;
    import lombok.RequiredArgsConstructor;
    import org.springframework.cache.annotation.CacheEvict;
    import org.springframework.cache.annotation.CachePut;
    import org.springframework.cache.annotation.Cacheable;
    import org.springframework.cache.annotation.Caching;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.jpa.domain.Specification;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;


    @Service
    @RequiredArgsConstructor
    @Transactional(readOnly = true)
    public class UserServiceImpl implements UserService {

        private final UserRepository userRepository;
        private final UserMapper userMapper;

        @Transactional
        public UserOutputDto createUser(UserInputDto userDto){
            User user = userMapper.toEntity(userDto);
            if(userRepository.findByEmail(user.getEmail()).isPresent()){
                throw new UserAlreadyRegisteredException("User",user.getEmail());
            }
            return userMapper.toDto(userRepository.save(user));
        }

        @Cacheable(value = "users", key = "#id")
        public UserOutputDto findUserById(Long id){
            return userRepository.findById(id)
                    .map(userMapper::toDto)
                    .orElseThrow(() -> new EntityNotFoundException("User", id));
        }


        public Page<UserOutputDto> getAllUsers(String firstName, String surname, Pageable pageable){
            Specification<User> specification = Specification.where(UserSpecifications.hasFirstName(firstName)
                    .and(UserSpecifications.hasSurname(surname)));
            return userRepository.findAll(specification,pageable)
                    .map(userMapper::toDto);
        }

        @Transactional
        @CachePut(value = "users",key = "#id")
        public UserOutputDto updateUserById(Long id, UserInputDto userInputDto){
            User user = userRepository.findById(id).orElseThrow(
                    () -> new EntityNotFoundException("User", id));

            if(!user.getEmail().equals(userInputDto.email()) && userRepository.findByEmail(userInputDto.email()).isPresent()){
                throw new AlreadyTakenException("email",userInputDto.email());
            }

            user.setName(userInputDto.name());
            user.setSurname(userInputDto.surname());
            user.setBirthDate(userInputDto.birthDate());
            user.setEmail(userInputDto.email());
            return userMapper.toDto(user);
        }

        @Transactional
        @CachePut(value = "users",key = "#id")
        public UserOutputDto updateUserStatus(Long id, boolean active){
            User user = userRepository.findById(id).orElseThrow(
                    () -> new EntityNotFoundException("User", id));
            user.setActive(active);
            return userMapper.toDto(user);
        }

        @Transactional
        @Caching(evict = {
                @CacheEvict(value = "users", key = "#id"),
                @CacheEvict(value = "paymentCards",allEntries = true)
        })
        public void deleteUser(Long id){
            if(!userRepository.existsById(id)){
                throw new EntityNotFoundException("User", id);
            }

            userRepository.deleteById(id);
        }

    }
