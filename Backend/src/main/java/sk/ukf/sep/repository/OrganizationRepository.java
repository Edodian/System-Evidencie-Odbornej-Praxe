package sk.ukf.sep.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import sk.ukf.sep.entity.Organization;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findByIco(String ico);
}
