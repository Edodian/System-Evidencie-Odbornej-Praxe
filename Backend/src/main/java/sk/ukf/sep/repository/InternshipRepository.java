package sk.ukf.sep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.ukf.sep.entity.Internship;

import java.util.List;

public interface InternshipRepository extends JpaRepository<Internship, Long> {
    List<Internship> findByUser_Id(int userId);
    List<Internship> findByOrganization_Id(Long organizationId);
}
