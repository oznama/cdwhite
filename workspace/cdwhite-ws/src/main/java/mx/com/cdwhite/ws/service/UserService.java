package mx.com.cdwhite.ws.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.cdwhite.ws.constants.GeneralConstants;
import mx.com.cdwhite.ws.dto.UserDto;
import mx.com.cdwhite.ws.exception.ResourceNotFoundException;
import mx.com.cdwhite.ws.model.User;
import mx.com.cdwhite.ws.repository.UserRepository;
import mx.com.cdwhite.ws.utils.GenericUtils;

@Service
public class UserService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	
	private final String MODULE = User.class.getSimpleName();
	
	@Autowired
	private UserRepository userRepository;
	
	public UserDto create(UserDto userDto) {
		LOGGER.debug("Creating user...");
		User user = new User();
		BeanUtils.copyProperties(userDto, user);
		userRepository.saveAndFlush(user);
		userDto.setId(user.getId());
		LOGGER.debug("Transaction creating user finished with id: {}!", userDto.getId());
		return userDto;
	}
	
	public void update(UserDto userDto) {
		User user = new User();
		BeanUtils.copyProperties(userDto, user);
		userRepository.findById(userDto.getId()).map(t -> {
			BeanUtils.copyProperties(userDto, t);
			t.setDateCreated(GenericUtils.currentDateToString(GeneralConstants.FORMAT_DATE_TIME));
			return userRepository.saveAndFlush(t);
		}).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", userDto.getId()));
	}

	public void delete(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MODULE, "id", id));
		userRepository.delete(user);
		userRepository.flush();
	}
	
	public Boolean login(UserDto userDto) throws Exception {
		LOGGER.debug("Logig service for user: {} with pswd: ****{}", userDto.getUsername(), userDto.getPassword().length()>6 ? userDto.getPassword().substring(6) : "");
		User userFinded = userRepository.findByUsername(userDto.getUsername());
		if (userFinded != null) {
			return userDto.getPassword().equals(userFinded.getPassword());
		}
		throw new Exception("El usuario no se encuentra registrado");
	}

}
