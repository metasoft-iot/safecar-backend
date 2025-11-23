package com.safecar.platform.profiles.application.internal.queryservices;

import com.safecar.platform.profiles.domain.model.aggregates.PersonProfile;
import com.safecar.platform.profiles.domain.model.queries.GetPersonProfileByIdQuery;
import com.safecar.platform.profiles.domain.model.queries.GetPersonProfileByUserEmailQuery;
import com.safecar.platform.profiles.domain.services.PersonProfileQueryService;
import com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories.PersonProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonProfileQueryServiceImpl implements PersonProfileQueryService {
    private final PersonProfileRepository personProfileRepository;

    public PersonProfileQueryServiceImpl(PersonProfileRepository personProfileRepository) {
        this.personProfileRepository = personProfileRepository;
    }

    @Override
    public Optional<PersonProfile> handle(GetPersonProfileByUserEmailQuery query) {
        return personProfileRepository.findByUserEmail(query.userEmail());
    }

    @Override
    public Optional<PersonProfile> handle(GetPersonProfileByIdQuery query) {
        return personProfileRepository.findById(query.id());
    }
}
