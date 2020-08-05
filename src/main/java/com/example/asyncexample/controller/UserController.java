package com.example.asyncexample.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.asyncexample.entity.User;
import com.example.asyncexample.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService service;

	@GetMapping(value = "/hello")
	public String hello() {
		return "Hello World";
	}

	@PostMapping(value = "/users", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = "application/json")
	public ResponseEntity saveUsers(@RequestParam(value = "files") MultipartFile[] files) throws Exception {
		for (MultipartFile file : files) {
			service.saveUsers(file);
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping(value = "/users", produces = "application/json")
	public CompletableFuture<ResponseEntity> findAllUsers() {
		System.out.println("USER GET CALLED");
		return service.findAllUsers().thenApply(ResponseEntity::ok);
	}

	@GetMapping(value = "/getUsersByThread", produces = "application/json")
	public ResponseEntity getUsers() {
		CompletableFuture<List<User>> users1 = service.findAllUsers();
		CompletableFuture<List<User>> users2 = service.findAllUsers();
		CompletableFuture<List<User>> users3 = service.findAllUsers();
		CompletableFuture<List<User>> users6 = service.findAllUsers();
		CompletableFuture<List<User>> users4 = service.findAllUsers();
		CompletableFuture<List<User>> users5 = service.findAllUsers();
		CompletableFuture.allOf(users1, users2, users3, users4, users5, users6).join();
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@RequestMapping(value = "/tryThread", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody CompletableFuture<ResponseEntity> getAllCars() {

		return service.findAllUsers().<ResponseEntity>thenApply(ResponseEntity::ok)

				.exceptionally(handleGetCarFailure);

	}

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	private static Function<Throwable, ResponseEntity<? extends List<User>>> handleGetCarFailure = throwable -> {

		LOGGER.error("Failed to read records: {}", throwable);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

	};

}