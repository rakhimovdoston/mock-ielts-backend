package com.search.teacher.controller.admin;

import com.search.teacher.dto.filter.UserFilter;
import com.search.teacher.dto.request.test.TestDateRequest;
import com.search.teacher.dto.request.user.UserRequest;
import com.search.teacher.dto.request.user.UsernameRequest;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.user.UserService;
import com.search.teacher.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("api/v1/admin/user")
@Tag(name = "Admin User Controller", description = "Management operations for admin users")
public class AdminController {

    private final UserService userService;
    private final SecurityUtils securityUtils;

    public AdminController(UserService userService, SecurityUtils securityUtils) {
        this.userService = userService;
        this.securityUtils = securityUtils;
    }

    @Operation(
            summary = "Save a new user",
            description = "Saves a new user in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
            }
    )
    @PostMapping("save")
    public JResponse saveUser(@RequestBody UserRequest request) {
        return userService.createUsers(securityUtils.getCurrentUser(), request);
    }

    @Operation(
            summary = "Delete a user",
            description = "Deletes a user by their ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
            }
    )
    @DeleteMapping("delete/{byId}")
    public JResponse deleteUser(
            @Parameter(description = "ID of the user to be deleted") @PathVariable(name = "byId") Long id) {
        return userService.deleteUsers(securityUtils.getCurrentUser(), id);
    }

    @Operation(
            summary = "Create a new teacher",
            description = "Creates a new teacher in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Teacher created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
            }
    )
    @PostMapping("create/teacher")
    public JResponse createTeacher(@RequestBody UserRequest request) {
        return userService.createTeacher(securityUtils.getCurrentUser(), request);
    }

    @Operation(
            summary = "Check username availability",
            description = "Checks if a username is already taken",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Username is available"),
                    @ApiResponse(responseCode = "400", description = "Username is already taken")
            }
    )
    @PostMapping("check-username")
    public JResponse checkUsername(@RequestBody UsernameRequest request) {
        return userService.checkUsername(request.username()) ?
                JResponse.error(400, "Username is already taken") :
                JResponse.success();
    }

    @Operation(
            summary = "Set test start date",
            description = "Sets the start date for a test for a specific user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Test date set successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
            }
    )
    @PutMapping("test-date/{byId}")
    public JResponse setStartTestDate(
            @Parameter(description = "ID of the user") @PathVariable(name = "byId") Long id,
            @RequestBody TestDateRequest testDate) {
        return userService.setTestDate(securityUtils.getCurrentUser(), id, testDate);
    }

    @Operation(
            summary = "Update user",
            description = "Updates details of an existing user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
            }
    )
    @PutMapping("update/{byId}")
    public JResponse updateUser(
            @Parameter(description = "ID of the user to update") @PathVariable(name = "byId") Long id,
            @RequestBody UserRequest request) {
        return userService.updateUser(securityUtils.getCurrentUser(), id, request);
    }

    @Operation(
            summary = "Get user by ID",
            description = "Fetches the details of a user by their ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User details retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
            }
    )
    @GetMapping("by/{id}")
    public JResponse getUserById(
            @Parameter(description = "ID of the user to retrieve") @PathVariable(name = "id") Long id) {
        return userService.getUserBydId(id);
    }

    @Operation(
            summary = "Get all users",
            description = "Retrieves a paginated list of all users with optional filters such as date range and search query",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            }
    )
    @GetMapping("all")
    public JResponse getAllUsers(
            @Parameter(description = "Page number for pagination")
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "Page size for pagination")
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @Parameter(description = "Filter by 'to' date (yyyy-MM-dd)")
            @RequestParam(name = "toDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            @Parameter(description = "Filter by 'from' date (yyyy-MM-dd)")
            @RequestParam(name = "fromDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @Parameter(description = "Search query string")
            @RequestParam(name = "search", required = false) String search
    ) {
        UserFilter userFilter = new UserFilter();
        userFilter.setFromDate(fromDate);
        userFilter.setToDate(toDate);
        userFilter.setPage(page);
        userFilter.setSize(size);
        userFilter.setSearch(search);
        return userService.allUsersByFilter(securityUtils.getCurrentUser(), userFilter);
    }
}