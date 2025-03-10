package com.spring.user.service;

import java.math.BigDecimal;
import java.util.List;

import com.spring.post.dto.request.RegisterPostRequest;
import com.spring.post.service.PostService;
import com.spring.user.dto.request.UserFilterSearchRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.common.exception.runtime.BaseException;
import com.spring.user.dto.request.RegisterUserRequest;
import com.spring.user.dto.request.UpdateUserRequest;
import com.spring.user.dto.request.SimpleUserRequest;
import com.spring.user.dto.response.DeleteUserResponse;
import com.spring.user.dto.response.RegisterUserResponse;
import com.spring.user.dto.response.SimpleUserResponse;
import com.spring.user.domain.User;
import com.spring.user.dto.UserMapper;
import com.spring.user.exception.UserErrorCode;
import com.spring.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PostService postService;

	@Transactional(readOnly = true)
	public SimpleUserResponse getUser(SimpleUserRequest request) {
		User findUser = findUser(request.userId());

		return UserMapper.toSimpleUserResponse(findUser);
	}

	@Transactional(readOnly = true)
	public List<SimpleUserResponse> getUserAll() {
		List<User> findUserAll = userRepository.findAll();

		return UserMapper.toSimpleUserResponses(findUserAll);
	}

	//필터링 검색
	@Transactional
	public List<User> getUserFilter(UserFilterSearchRequest request){
		return userRepository.searchUsers(request);
	}

	@Transactional
	public RegisterUserResponse joinUser(RegisterUserRequest request) {
		User savedUser = userRepository.save(UserMapper.toUser(request));

		return UserMapper.toRegisterUserResponse(savedUser);
	}

	@Transactional
	public SimpleUserResponse updateUser(Long userId, UpdateUserRequest request) {
		validateEmail(request.email());

		User findUser = findUser(userId);

		findUser.updateInfo(request.email(), request.name(), request.role());

		return UserMapper.toSimpleUserResponse(findUser);
	}

	@Transactional
	public DeleteUserResponse deleteUser(Long userId) {
		User findUser = findUser(userId);

		userRepository.delete(findUser);
		return UserMapper.toDeleteUserResponse();
	}

	private User findUser(Long userId) {
		return userRepository.findById(userId).orElseThrow(
			() -> new BaseException(UserErrorCode.NOT_FOUND_USER)
		);
	}

	private void validateEmail(String changingEmail) {
		if(userRepository.existsByEmail(changingEmail)) {
			throw new BaseException(UserErrorCode.DUPLICATED_EMAIL);
		}
	}


	//Amethod
	@Transactional
	public void savewithRequiredFail(RegisterUserRequest request){
		User savedUser = userRepository.save(UserMapper.toUser(request));

		try {
			postService.savePostFail(new RegisterPostRequest("제목","내용", savedUser.getId()));

		}catch (RuntimeException e){
			System.out.println("Required 트랜잭션 롤백 처리 : " + e.getMessage());
		}

	}


	//A method - RequiredNew
	//이 메소드에서 read uncommitted를 건 이유는 그렇게 안하면 트랜잭션 B가 독립적이더라도 user에 접근하지 못해서 트랜잭션 커밋이 안됨!!!
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void savewithRequiresNewFail(RegisterUserRequest request){
		User savedUser = userRepository.save(UserMapper.toUser(request));
		postService.savePostFailwithRequiredNew(new RegisterPostRequest("Requires new TITLE ","Requires new CONTENT", savedUser.getId()));
		throw new RuntimeException();
	}

	//A method - Mandatory
	@Transactional
	public void savewithMandatory(RegisterUserRequest request){
		User savedUser = userRepository.save(UserMapper.toUser(request));
		postService.savePostFailwithMandatory(new RegisterPostRequest("Mandatory TITLE ","Mandatory CONTENT", savedUser.getId()));
	}

	//Transaction Isolation test (READ_UNCOMMITTED)
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void changePointWithReadUncommitted(Long userId, BigDecimal point){
		User finduser = findUser(userId);
		finduser.updatePoint(point);
		//jpa가 자동으로 영속성 컨텍스트 관리 , 이건 transactional의 생명주기와 거의 같다.
		//변경이 감지되면 자동으로 db에 save를 해주기 떄문에 따로 해줄 필요 없음
	}

	//Transaction Isolation test (READ_COMMITTED)
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void changePointwithReadCommitted(Long userId, BigDecimal point){
		User finduser = findUser(userId);
		finduser.updatePoint(point);
		//jpa가 자동으로 영속성 컨텍스트 관리 , 이건 transactional의 생명주기와 거의 같다.
		//변경이 감지되면 자동으로 db에 save를 해주기 떄문에 따로 해줄 필요 없음
	}

	//Transaction Isolation test (REPEATABLE_READ)
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public void changePointwithRepeatableRead(Long userId, BigDecimal point){
		User finduser = findUser(userId);
		finduser.updatePoint(point);
		//jpa가 자동으로 영속성 컨텍스트 관리 , 이건 transactional의 생명주기와 거의 같다.
		//변경이 감지되면 자동으로 db에 save를 해주기 떄문에 따로 해줄 필요 없음
	}

	//Transaction Isolation test (SERIALIZABLE)
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void changePointwithSerializable(Long userId, BigDecimal point){
		User finduser = findUser(userId);
		finduser.updatePoint(point);
		//jpa가 자동으로 영속성 컨텍스트 관리 , 이건 transactional의 생명주기와 거의 같다.
		//변경이 감지되면 자동으로 db에 save를 해주기 떄문에 따로 해줄 필요 없음
	}









}
