package com.bt.form.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bt.form.dao.UserInfo;
import com.bt.form.model.LoginForm;
import com.bt.form.model.RegisterForm;
import com.bt.form.model.UserData;
import com.bt.form.service.UserService;
import com.bt.form.validator.UserFormValidator;

/**
 * The Class UserController is entry for Guest book entry and registration.
 */
@Controller
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(UserController.class);

	private static final String REDIRECT_LOGIN = "redirect:/login";
	private static final String SUCCESS = "success";
	private static final String USERS = "users";
	private static final String USERS_LIST = "users/list";
	private static final String USERS_LOGIN = "users/login";
	private static final String UPLOADED_FOLDER_LOCATION = "src/main/resources/images/";
	private static final String MSG = "msg";
	private static final String CSS = "css";
	private static final String DANGER = "danger";
	private Integer currentUserId = 0;

	@Autowired
	UserFormValidator userFormValidator;

	@Autowired
	ServletContext context; 
	
	@Autowired
	Map<String, String> fileMap; 
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(userFormValidator);
	}

	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Login form.
	 *
	 * @param model the model
	 * @return the string It is guest book login page application
	 */
	@GetMapping({ "/", "/login" })
	public String loginForm(Model model) {
		LoginForm loginForm = new LoginForm();
		logger.debug("loginForm method  GET");
		model.addAttribute("loginNew", loginForm);
		return USERS_LOGIN;
	}

	/**
	 * Login form post.
	 *
	 * @param loginForm          the login form
	 * @param result             the result
	 * @param model              the model
	 * @param redirectAttributes the redirect attributes
	 * @return the string
	 * 
	 *         After the user provides credentials it will validate and respond
	 *         differently on the basis of admin or normal user
	 */
	@PostMapping(value = "/users/login")
	public String loginFormPost(@ModelAttribute("loginNew") @Validated LoginForm loginForm, BindingResult result,
			Model model, final RedirectAttributes redirectAttributes) {
		logger.debug("loginForm method POST");

		if (result.hasErrors()) {
			logger.error("**loggin error**");
			return USERS_LOGIN;
		}

		UserData userData = userService.validateUser(loginForm);
		if (!Boolean.TRUE.equals(userData.getIsUserExists())) {
			redirectAttributes.addFlashAttribute(CSS, DANGER);
			redirectAttributes.addFlashAttribute(MSG, userData.getStatusMessage());
			model.addAttribute("loginNew", loginForm);
			logger.debug("**User Does not exist!**");
			return REDIRECT_LOGIN;
		}

		redirectAttributes.addFlashAttribute(CSS, SUCCESS);
		redirectAttributes.addFlashAttribute(MSG, "login successfully!");

			if (Boolean.TRUE.equals(userData.getUserInfo().getIsAdmin())) {
			
			logger.debug("Admin had logged in!");
			model.addAttribute(USERS, userService.findAll());
			return USERS_LIST;
		} else {
			UserInfo userInfo = userData.getUserInfo();
			currentUserId = userInfo.getId();
			model.addAttribute("userForm", userInfo);
			return "users/userform";
		}
	}

	/**
	 * Save or update user.
	 *
	 * @param userInfo           the user info
	 * @param result             the result
	 * @param model              the model
	 * @param redirectAttributes the redirect attributes
	 * @return the string User can update comments from this service
	 */
	@PostMapping(value = "/users/userform")
	public String saveOrUpdateUser(@ModelAttribute("userForm") @Validated UserInfo userInfo, BindingResult result,
			Model model, final RedirectAttributes redirectAttributes) {

		logger.debug("saveOrUpdateUser: {}", userInfo);
		if (result.hasErrors() || StringUtils.isBlank(userInfo.getComments())) {
			redirectAttributes.addFlashAttribute(CSS, DANGER);
			redirectAttributes.addFlashAttribute(MSG, "comments not updated!");
			return REDIRECT_LOGIN;
		}
		userService.saveOrUpdate(userInfo);
		redirectAttributes.addFlashAttribute(CSS, SUCCESS);
		redirectAttributes.addFlashAttribute(MSG, "comment saved successfully!");
		return REDIRECT_LOGIN;
	}
	
	/**
	 * Upload.
	 *
	 * @param userInfo the user info
	 * @param result the result
	 * @param model the model
	 * @param redirectAttributes the redirect attributes
	 * @return The method helps in uploading an image from user
	 */

	@PostMapping(value = "/users/upload")
	public String upload(@ModelAttribute("userForm") @Validated UserInfo userInfo, BindingResult result, Model model,
			final RedirectAttributes redirectAttributes) {
		logger.debug("upload method call for User ID: {} ", userInfo.getId());
		currentUserId = userInfo.getId();
		model.addAttribute("file", "file");
		return "users/uploadimages";

	}
	
	/**
	 * Uploadimages.
	 *
	 * @param file the file
	 * @param redirectAttributes the redirect attributes
	 * @return the string
	 * This method helps in loading image into System to location src/main/resources/images/
	 */
	@PostMapping("/users/uploadimages")
	public String uploadimages(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		logger.debug("Uploadimage method call");
		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute(CSS, DANGER);
			redirectAttributes.addFlashAttribute(MSG, "Image not uploaded!");
			return REDIRECT_LOGIN;
		}

		try {
			byte[] bytes = file.getBytes();
			
			Path path = Paths.get(UPLOADED_FOLDER_LOCATION + currentUserId + "_" + file.getOriginalFilename());
			logger.debug("Uploaded image at location: {} {}" , UPLOADED_FOLDER_LOCATION , file.getOriginalFilename());
			Files.write(path, bytes);
			redirectAttributes.addFlashAttribute(MSG,"You successfully uploaded '" + file.getOriginalFilename() + "'");

		} catch (IOException e) {
			e.printStackTrace();
		}
		UserInfo userInfo = new UserInfo();
		userInfo.setId(currentUserId);
		userInfo.setComments(null);
		userService.saveOrUpdate(userInfo);
		redirectAttributes.addFlashAttribute(CSS, SUCCESS);
		redirectAttributes.addFlashAttribute(MSG, "Image uploaded successfully!");
		fileMap.put(currentUserId.toString(), currentUserId + "_" + file.getOriginalFilename());
		logger.debug("Update user comments to empty!");
		return REDIRECT_LOGIN;
	}

	/**
	 * Download.
	 *
	 * @param userInfo the user info
	 * @param result the result
	 * @param model the model
	 * @param redirectAttributes the redirect attributes
	 * @return the string
	 * This method helps in downloading the image for user or admin 
	 */
	
	@PostMapping(value = "/users/downloadimage")
	public String download(@ModelAttribute("userForm") @Validated UserInfo userInfo, BindingResult result, Model model,
			final RedirectAttributes redirectAttributes) {
		logger.debug("in download for userID: {} " , userInfo.getId());
		currentUserId = userInfo.getId();
		model.addAttribute("id", userInfo.getId());
		return "users/downloadimages/{id}";

	}

	/**
	 * Download.
	 *
	 * @param model the model
	 * @param redirectAttributes the redirect attributes
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 * 	 * This method helps in downloading the image for user or admin 
	 */
	@ResponseBody
	@GetMapping(value = "/users/downloadimages", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] download(Model model, RedirectAttributes redirectAttributes) throws IOException {
		logger.info("inside download image GET for UserID:  {}", currentUserId);
		String filelocation = fileMap.get(currentUserId.toString());
		if(StringUtils.isBlank(filelocation)) {
			filelocation = "Image_Not_Found.png";
		}
		filelocation = "images/"+filelocation;
		InputStream in =  new ClassPathResource(filelocation).getInputStream();
 	    return IOUtils.toByteArray(in);
	}
	
	/**
	 * Register form.
	 *
	 * @param model the model
	 * @return the string New user can register from this service
	 */
	@GetMapping(value = "/register")
	public String registerForm(Model model) {
		RegisterForm registerForm = new RegisterForm();
		logger.debug("registerForm GET");
		model.addAttribute("registerNew", registerForm);
		return "users/register";

	}

	/**
	 * Register form post.
	 *
	 * @param registerForm       the register form
	 * @param result             the result
	 * @param model              the model
	 * @param redirectAttributes the redirect attributes
	 * @return the string User can register from this service and updated in
	 *         database.
	 */
	// list page
	@PostMapping(value = "/users/register")
	public String registerFormPost(@ModelAttribute("registerNew") @Validated RegisterForm registerForm,
			BindingResult result, Model model, final RedirectAttributes redirectAttributes) {

		logger.debug("registerForm POST");
		if (result.hasErrors()) {
			model.addAttribute("registerNew", registerForm);
			logger.debug("Registration has errors!");
			return "users/register";
		}
		userService.save(registerForm);
		redirectAttributes.addFlashAttribute(CSS, SUCCESS);
		redirectAttributes.addFlashAttribute(MSG, "User register successfully!");
		return REDIRECT_LOGIN;
	}

	/**
	 * Show update user form.
	 *
	 * @param id    the idd
	 * @param model the model
	 * @return the string Admin can approved on user comments from this service
	 */
	@GetMapping(value = "/users/{id}/approve")
	public String userApproveByAdminId(@PathVariable("id") int id, Model model, final RedirectAttributes redirectAttributes) {
		logger.debug("userApproveByAdmin for UserID: {}", id);
		userService.approve(id);
		redirectAttributes.addFlashAttribute(CSS, SUCCESS);
		redirectAttributes.addFlashAttribute(MSG, "User is approved!");
		model.addAttribute(USERS, userService.findAll());
		return USERS_LIST;

	}
	
	/**
	 * Show update user form.
	 *
	 * @param id    the idd
	 * @param model the model
	 * @return the string Admin can approved on user comments from this service
	 */
	@PostMapping(value = "/users/approve")
	public String userApproveByAdmin(@ModelAttribute("userForm") UserInfo userInfo, Model model, final RedirectAttributes redirectAttributes) {
		logger.debug("userApproveByAdmin for UserID: {}", currentUserId);
		userService.saveOrUpdate(userInfo);
		userService.approve(currentUserId);
		redirectAttributes.addFlashAttribute(CSS, SUCCESS);
		redirectAttributes.addFlashAttribute(MSG, "User is approved!");
		model.addAttribute(USERS, userService.findAll());
		return USERS_LIST;
	}
	/**
	 * userEditByAdmin
	 *
	 * @param id                 the id
	 * @param model              the model
	 * 
	 * @return 
	 * this updates the user by Admin
	 */
	@GetMapping(value = "/users/{id}/edit")
	public String userEditByAdmin(@PathVariable("id") Integer id, Model model) {
		logger.debug("userEditbyAdmin for ID: : {}", id);
		currentUserId = id;
		UserInfo userInfo = userService.findById(id);
		model.addAttribute("userForm", userInfo);
		return "users/adminapproveusersform";
	}

	/**
	 * Delete user.
	 *
	 * @param id                 the id
	 * @param model              the model
	 * @param redirectAttributes the redirect attributes
	 * @return the string Admin can delete the user from this service
	 */
	@PostMapping(value = "/users/{id}/delete")
	public String userDeleteByAdminId(@PathVariable("id") int id, Model model, final RedirectAttributes redirectAttributes) {
		logger.debug("UserDeleteByAdmin for ID: {}", id);
		userService.delete(id);
		redirectAttributes.addFlashAttribute(CSS, SUCCESS);
		redirectAttributes.addFlashAttribute(MSG, "User is deleted!");
		model.addAttribute(USERS, userService.findAll());
		return USERS_LIST;
	}
	
	/**
	 * Delete user.
	 *
	 * @param id                 the id
	 * @param model              the model
	 * @param redirectAttributes the redirect attributes
	 * @return the string Admin can delete the user from this service
	 */
	@PostMapping(value = "/users/delete")
	public String userDeleteByAdmin(Model model, final RedirectAttributes redirectAttributes) {
		logger.debug("UserDeleteByAdmin for ID: {}", currentUserId);
		userService.delete(currentUserId);
		redirectAttributes.addFlashAttribute(CSS, SUCCESS);
		redirectAttributes.addFlashAttribute(MSG, "User is deleted!");
		model.addAttribute(USERS, userService.findAll());
		return USERS_LIST;
	}

}