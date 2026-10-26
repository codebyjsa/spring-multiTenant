
package com.multitenant.sample.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface MembershipRepository
        extends JpaRepository<Membership, UUID> {

    Optional<Membership> findByUser_IdAndOrganization_Id(
            UUID userId,
            UUID organizationId
    );
}
