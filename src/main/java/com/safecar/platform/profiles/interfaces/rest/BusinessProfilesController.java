package com.safecar.platform.profiles.interfaces.rest;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safecar.platform.profiles.domain.model.queries.GetBusinessProfileByUserEmailQuery;
import com.safecar.platform.profiles.domain.services.BusinessProfileCommandService;
import com.safecar.platform.profiles.domain.services.BusinessProfileQueryService;
import com.safecar.platform.profiles.interfaces.rest.resource.BusinessProfileResource;
import com.safecar.platform.profiles.interfaces.rest.resource.CreateBusinessProfileResource;
import com.safecar.platform.profiles.interfaces.rest.resource.UpdateBusinessProfileResource;
import com.safecar.platform.profiles.interfaces.rest.transform.BusinessProfileResourceFromEntityAssembler;
import com.safecar.platform.profiles.interfaces.rest.transform.CreateBusinessProfileCommandFromResourceAssembler;
import com.safecar.platform.profiles.interfaces.rest.transform.UpdateBusinessProfileCommandFromResourceAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Business Profiles Controller
 * <p>
 * This controller handles RESTful API requests related to business profiles.
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/business-profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Profiles", description = "Business Profiles Management")
@RequiredArgsConstructor
public class BusinessProfilesController {

    private final BusinessProfileCommandService commandService;
    private final BusinessProfileQueryService queryService;

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update a business profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Business profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<BusinessProfileResource> updateBusinessProfile(
            @PathVariable Long id,
            @RequestBody UpdateBusinessProfileResource resource) {

        var command = UpdateBusinessProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command, id);

        if (!result.isPresent())
            return ResponseEntity.notFound().build();

        var entity = result.get();

        var businessProfileResource = BusinessProfileResourceFromEntityAssembler.toResourceFromEntity(entity);

        return ResponseEntity.status(CREATED).body(businessProfileResource);
    }

    @PostMapping
    @Operation(summary = "Create a new business profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Business profile created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<BusinessProfileResource> createNewBusinessProfile(
            @RequestParam String userEmail,
            @RequestBody CreateBusinessProfileResource resource) {

        var command = CreateBusinessProfileCommandFromResourceAssembler.toCommandFromResource(resource);

        var result = commandService.handle(command, userEmail);

        if (!result.isPresent())
            return ResponseEntity.badRequest().build();

        var entity = result.get();

        var businessProfileResource = BusinessProfileResourceFromEntityAssembler
                .toResourceFromEntity(entity);

        return ResponseEntity.status(CREATED).body(businessProfileResource);
    }

    @GetMapping
    @Operation(summary = "Get a business profile by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Business profile found"),
            @ApiResponse(responseCode = "404", description = "Business profile not found") })
    public ResponseEntity<BusinessProfileResource> getBusinessProfileByEmail(@RequestParam String email) {

        var query = new GetBusinessProfileByUserEmailQuery(email);
        var result = queryService.handle(query);

        if (!result.isPresent())
            return ResponseEntity.notFound().build();

        var entity = result.get();

        var resource = BusinessProfileResourceFromEntityAssembler
                .toResourceFromEntity(entity);

        return ResponseEntity.ok(resource);
    }
}
