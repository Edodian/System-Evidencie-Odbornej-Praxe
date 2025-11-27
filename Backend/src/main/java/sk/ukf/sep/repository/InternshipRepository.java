package sk.ukf.sep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.ukf.sep.entity.Internship;
import sk.ukf.sep.entity.Organization;

import java.util.Optional;

public interface InternshipRepository extends JpaRepository<Internship, Long> {
    // Optional<Internship> findByTitle(Organization organization);
}
