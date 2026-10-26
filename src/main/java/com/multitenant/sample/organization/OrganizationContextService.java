package com.multitenant.sample.organization;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrganizationContextService {

    private final OrganizationRepository repository;

    public OrganizationContextService(
            OrganizationRepository repository) {
        this.repository = repository;
    }

    public Organization resolveBySlug(String slug) {
        return repository.findBySlug(slug)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Organization not found: " + slug
                        )
                );
    }
}