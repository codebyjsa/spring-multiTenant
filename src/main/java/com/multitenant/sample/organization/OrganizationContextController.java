
package com.multitenant.sample.organization;

import com.multitenant.sample.user.AppUser;
import com.multitenant.sample.user.OrganizationRole;
import com.multitenant.sample.user.OrganizationAccessService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/context")
public class OrganizationContextController {

    private final OrganizationAccessService accessService;

    public OrganizationContextController(
            OrganizationAccessService accessService) {
        this.accessService = accessService;
    }

    @GetMapping
    public OrganizationContextResponse getContext(
            HttpServletRequest request,
            @AuthenticationPrincipal AppUser user) {

        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Authentication required");
        }

        String slug = extractSlug(request.getServerName());

        var membership = accessService.requireMembership(
                user.getId(), slug);

        var organization = membership.organization();

        return new OrganizationContextResponse(
                organization.getId(),
                organization.getName(),
                organization.getSlug(),
                membership.role()
        );
    }


    @PostMapping("/owner-action")
    public String ownerAction(
            HttpServletRequest request,
            @AuthenticationPrincipal AppUser user) {

        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Authentication required");
        }

        String slug = extractSlug(request.getServerName());

        accessService.requireRole(
                user.getId(), slug, OrganizationRole.OWNER);

        return "Owner action authorized for " + slug;
    }


    private String extractSlug(String host) {
        if (host == null || !host.endsWith(".localhost")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Expected an organization subdomain");
        }

        String slug = host.substring(
                0, host.length() - ".localhost".length());

        if (slug.isBlank() || slug.contains(".")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid organization subdomain");
        }

        return slug;
    }

    public record OrganizationContextResponse(
            UUID id,
            String name,
            String slug,
            OrganizationRole role) {
    }
}
