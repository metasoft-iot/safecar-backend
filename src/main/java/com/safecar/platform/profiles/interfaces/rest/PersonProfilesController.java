package com.safecar.platform.profiles.interfaces.rest;

import com.safecar.platform.profiles.domain.services.PersonProfileQueryService;
import com.safecar.platform.profiles.domain.services.PersonProfileCommandService;
import com.safecar.platform.profiles.interfaces.rest.resource.PersonProfileResource;
import com.safecar.platform.profiles.interfaces.rest.resource.UpdatePersonProfileResource;
import com.safecar.platform.profiles.domain.model.queries.GetPersonProfileByUserEmailQuery;
import com.safecar.platform.profiles.interfaces.rest.resource.CreatePersonProfileResource;
import com.safecar.platform.profiles.interfaces.rest.transform.PersonProfileResourceFromEntityAssembler;
import com.safecar.platform.profiles.interfaces.rest.transform.UpdatePersonProfileCommandFromResourceAssembler;
import com.safecar.platform.profiles.interfaces.rest.transform.CreatePersonProfileCommandFromResourceAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.MediaType;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * Profile Controller
 * <p>
 * This controller provides REST endpoints for managing user profiles,
 * including creation and retrieval of profiles.
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/person-profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Profiles", description = "Person Profiles Endpoints")
public class PersonProfilesController {

    private final PersonProfileQueryService queryService;
    private final PersonProfileCommandService commandService;

    public PersonProfilesController(
            PersonProfileCommandService personProfileCommandService,
            PersonProfileQueryService personProfileQueryService) {
        this.commandService = personProfileCommandService;
        this.queryService = personProfileQueryService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a person profile by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile found"),
            @ApiResponse(responseCode = "404", description = "Profile not found") })
    public ResponseEntity<PersonProfileResource> getProfileById(@PathVariable Long id) {

        var getProfileByIdQuery = new com.safecar.platform.profiles.domain.model.queries.GetPersonProfileByIdQuery(id);
        var profile = queryService.handle(getProfileByIdQuery);

        if (profile.isEmpty())
            return ResponseEntity.notFound().build();

        var profileEntity = profile.get();

        var profileResource = PersonProfileResourceFromEntityAssembler.toResourceFromEntity(profileEntity);
        return ResponseEntity.ok(profileResource);
    }

    @GetMapping
    @Operation(summary = "Get a person profile by user email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile found"),
            @ApiResponse(responseCode = "404", description = "Profile not found") })
    public ResponseEntity<PersonProfileResource> getProfileByUserEmail(@RequestParam String userEmail) {

        var getUserProfileQuery = new GetPersonProfileByUserEmailQuery(userEmail);
        var profile = queryService.handle(getUserProfileQuery);

        if (profile.isEmpty())
            return ResponseEntity.notFound().build();

        var profileEntity = profile.get();

        var profileResource = PersonProfileResourceFromEntityAssembler.toResourceFromEntity(profileEntity);
        return ResponseEntity.ok(profileResource);
    }

    @PostMapping
    @Operation(summary = "Create a new person profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Person profile created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<PersonProfileResource> createNewPersonProfile(
            @RequestParam String userEmail,
            @RequestBody CreatePersonProfileResource resource) {

        var command = CreatePersonProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command, userEmail);

        if (!result.isPresent())
            return ResponseEntity.badRequest().build();

        var entity = result.get();

        var personProfileResource = PersonProfileResourceFromEntityAssembler
                .toResourceFromEntity(entity);

        return ResponseEntity.status(CREATED).body(personProfileResource);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update a person profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<PersonProfileResource> updatePersonProfile(
            @PathVariable Long id,
            @RequestBody UpdatePersonProfileResource resource) {

        var command = UpdatePersonProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command, id);

        if (!result.isPresent())
            return ResponseEntity.notFound().build();

        var entity = result.get();

        var personProfileResource = PersonProfileResourceFromEntityAssembler.toResourceFromEntity(entity);

        return ResponseEntity.status(CREATED).body(personProfileResource);
    }
}
