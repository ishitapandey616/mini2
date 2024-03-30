package com.nagarro.mini.service;

import com.jayway.jsonpath.JsonPath;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nagarro.mini.entity.User;
import com.nagarro.mini.exceptionhandling.PageNotFoundException;
import com.nagarro.mini.pagination.PageInfoDTO;
import com.nagarro.mini.repository.UserRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

	private final WebClient api1WebClient;
	private final WebClient api2WebClient;
	private final WebClient api3WebClient;
	private final UserRepository userRepository;

	

	@Autowired
	public UserService(WebClient api1WebClient, WebClient api2WebClient, WebClient api3WebClient,
			UserRepository userRepository) {
		this.api1WebClient = api1WebClient;
		this.api2WebClient = api2WebClient;
		this.api3WebClient = api3WebClient;
		this.userRepository = userRepository;
	}

	public List<User> createUsers(int size) {
		List<User> users = new ArrayList<>();

		for (int i = 0; i < size; i++) {
			User randomUser = getRandomUserFromAPI();
			String username=randomUser.getName();
			List<String> nationality = getNationalityForUser(username.substring(0,username.indexOf(' ')));
			String gender = getGenderForUser(username.substring(0,username.indexOf(' ')));
			

			validateAndMarkUser(randomUser, nationality, gender);
			randomUser.setDateCreated(new Date());
			randomUser.setDateModified(new Date());

			// randomUser.setDateCreated(LocalDateTime.now());
			// randomUser.setDateModified(LocalDateTime.now());

			userRepository.save(randomUser);
			users.add(randomUser);
		}

		return users;
	}

	private String getGenderForUser(String name) {
		String jsonResponse = api3WebClient.get().uri("?name={name}",name).retrieve().bodyToMono(String.class).block();
		String gender = JsonPath.read(jsonResponse, "$.gender");
		return gender;

	}

	private List<String> getNationalityForUser(String name) {
		System.out.println(name);
		String jsonResponse = api2WebClient.get().uri("?name={name}",name).retrieve().bodyToMono(String.class).block();
		List<String> nationalities = new ArrayList<>();

		Object countries = JsonPath.read(jsonResponse, "$.country");
		for (Object country : (Iterable<?>) countries) {
			String id = JsonPath.read(country, "$.country_id");
			nationalities.add(id);

		}
		return nationalities;

	}

	public Map<String, Object> getUsers(String sortType, String sortOrder, int limit, int offset) {
		// Validate parameters
		validateParameters(sortType, sortOrder, limit, offset);

		Sort sort;
		if ("Name".equalsIgnoreCase(sortType)) {
			// Sort by Name and handle odd or even characters count
			sort = Sort.by((sortOrder.equalsIgnoreCase("even1")) ? Sort.Direction.DESC : Sort.Direction.ASC, "name");
		} else if ("Age".equalsIgnoreCase(sortType)) {
			// Sort by Age and handle even/odd age
			sort = Sort.by((sortOrder.equalsIgnoreCase("even1")) ? Sort.Direction.DESC : Sort.Direction.ASC, "age");
		} else {
			// Default sorting behavior
			sort = Sort.by(Sort.Direction.ASC, "userId");
		}
		PageRequest pageRequest = PageRequest.of(offset, limit, sort);
		org.springframework.data.domain.Page<User> userPage = userRepository.findAll(pageRequest);

		long totalCount = userRepository.count(); // Total count of users

		PageInfoDTO pageInfo = constructPageInfo(totalCount, offset, limit);
		// Set the PageInfo in some response object or DTO along with the list of users
		// ...

		Map<String, Object> response = new HashMap<>();
		response.put("users", userPage.getContent());
		response.put("pageInfo", pageInfo);

		// Exception Handling
		if (offset > totalCount) {
			throw new PageNotFoundException("Page not found");
		}
		return response;
	}

	// Fetch users based on the parameters provided
	// PageRequest pageRequest = PageRequest.of(offset, limit, sort);
	// org.springframework.data.domain.Page<User> userPage =
	// userRepository.findAll(pageRequest);
	// return userPage.getContent();

	private void validateParameters(String sortType, String sortOrder, int limit, int offset) {

		if (!("Name".equalsIgnoreCase(sortType) || "Age".equalsIgnoreCase(sortType))) {
			throw new IllegalArgumentException("Invalid sortType provided: " + sortType);
		}
		if (!sortOrder.matches("(?i)^(even1|odd1)$")) {
			throw new IllegalArgumentException("Invalid sortOrder provided: " + sortOrder);
		}
		validateLimitAndOffset(limit, offset);

	}

	private void validateLimitAndOffset(int limit, int offset) {
		if (limit < 1 || limit > 5) {
			throw new IllegalArgumentException("Limit should be between 1 and 5");
		}
		if (offset < 0) {
			throw new IllegalArgumentException("Offset should be a positive value");
		}
	}

	private PageInfoDTO constructPageInfo(long totalCount, int offset, int limit) {
		PageInfoDTO pageInfo = new PageInfoDTO();
		pageInfo.setTotal(totalCount);

		int totalPages = (int) Math.ceil((double) totalCount / limit);
		int currentPage = (offset / limit) + 1;

		pageInfo.setTotalPages(totalPages);
		pageInfo.setHasNextPage((currentPage < totalPages));
		pageInfo.setHasPreviousPage((offset > 0));

		return pageInfo;
	}

	private User getRandomUserFromAPI() {
		String jsonResponse = api1WebClient.get().retrieve().bodyToMono(String.class).block(); // Blocking operation to
																								// get the response
																								// synchronously
		User user = new User();
		String firstName = JsonPath.read(jsonResponse, "$.results[0].name.first");
		String lastName = JsonPath.read(jsonResponse, "$.results[0].name.last");
		String gender = JsonPath.read(jsonResponse, "$.results[0].gender");
		String nationality = JsonPath.read(jsonResponse, "$.results[0].nat");
		String dob = JsonPath.read(jsonResponse, "$.results[0].dob.date");
		int age = JsonPath.read(jsonResponse, "$.results[0].dob.age");

		Instant instant = Instant.parse(dob);
		user.setDob(Date.from(instant));
		user.setName(firstName + " " + lastName);
		user.setGender(gender);
		user.setNationality(nationality);
		user.setAge(age);
		return user;
	}

	private void validateAndMarkUser(User user, List<String> nationality, String gender) {
		// Implement Step 4 logic here: validate and mark user accordingly
		boolean flag = false;
		for (String nation : nationality) {
			if (user.getNationality().equalsIgnoreCase(nation)) {
				flag = true;
				break;

			}

		}
		if (flag) {
			if (user.getGender().equalsIgnoreCase(gender)) {
				user.setVerificationStatus("VERIFIED");
			} else {
				user.setVerificationStatus("TO BE VERIFIED");
			}

		} else {
			user.setVerificationStatus("TO BE VERIFIED");
		}

	}
}
