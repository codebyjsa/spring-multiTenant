
package com.multitenant.sample.user;

import com.multitenant.sample.organization.Organization;
import com.multitenant.sample.organization.OrganizationContextService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class OrganizationAccessService {

    private final OrganizationContextService organizationService;
    private final MembershipRepository membershipRepository;

    public OrganizationAccessService(
            OrganizationContextService organizationService,
            MembershipRepository membershipRepository) {
        this.organizationService = organizationService;
        this.membershipRepository = membershipRepository;
    }

    public OrganizationMembership requireMembership(
            UUID userId,
            String organizationSlug) {

        Organization organization =
                organizationService.resolveBySlug(organizationSlug);

        Membership membership = membershipRepository
                .findByUser_IdAndOrganization_Id(
                        userId, organization.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "You are not a member of this organization"));

        return new OrganizationMembership(
                organization,
                membership.getRole());
    }


    public OrganizationMembership requireRole(
            UUID userId,
            String organizationSlug,
            OrganizationRole requiredRole) {

        OrganizationMembership membership =
                requireMembership(userId, organizationSlug);

        if (membership.role() != requiredRole) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Insufficient organization permissions");
        }

        return membership;
    }


    public record OrganizationMembership(
            Organization organization,
            OrganizationRole role) {
    }
}
