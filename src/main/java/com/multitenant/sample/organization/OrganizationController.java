package com.multitenant.sample.organization;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationRepository repository;

    public OrganizationController(
            OrganizationRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<OrganizationResponse> getOrganizations() {
        return repository.findAll()
                .stream()
                .map(org -> new OrganizationResponse(
                        org.getId(),
                        org.getName(),
                        org.getSlug()
                ))
                .toList();
    }

    public record OrganizationResponse(
            UUID id,
            String name,
            String slug
    ) {}
}