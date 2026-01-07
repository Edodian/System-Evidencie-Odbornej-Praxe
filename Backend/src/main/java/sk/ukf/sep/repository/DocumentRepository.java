package sk.ukf.sep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.ukf.sep.entity.Document;
import sk.ukf.sep.entity.Internship;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Integer> {
    Document findByInternship_idAndType(int userId, String type);
    List<Document> findByInternship_Id(int internshipId);
}
