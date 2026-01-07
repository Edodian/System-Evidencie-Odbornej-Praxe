package sk.ukf.sep.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sk.ukf.sep.entity.Document;
import sk.ukf.sep.entity.Internship;
import sk.ukf.sep.properties.StorageProperties;
import sk.ukf.sep.repository.DocumentRepository;
import sk.ukf.sep.repository.InternshipRepository;

@Service
@Transactional
public class DocumentService {

    private final Path root;
    private final DocumentRepository documentRepository;
    private final InternshipRepository internshipRepository;

    public DocumentService(
        StorageProperties storageProperties,
        DocumentRepository documentRepository,
        InternshipRepository internshipRepository
    ) {
        this.root = storageProperties.getPath();
        this.documentRepository = documentRepository;
        this.internshipRepository = internshipRepository;
    }

    public Document upload(
        MultipartFile file,
        Integer internshipId,
        String type
    ) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        Internship internship = internshipRepository
            .findById(internshipId)
            .orElseThrow(() ->
                new IllegalArgumentException("Internship not found")
            );

        Files.createDirectories(root);

        String extension = extractExtension(file.getOriginalFilename());
        String filename = buildFilename(internshipId, type, extension);

        Path target = root.resolve(filename);

        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target);
        }

        Document document = Document.builder()
            .internship(internship)
            .link(filename)
            .type(type)
            .status("UPLOADED")
            .build();

        return documentRepository.save(document);
    }

    private String buildFilename(
        Integer internshipId,
        String type,
        String extension
    ) {
        return (
            "internship_" +
            internshipId +
            "_" +
            type +
            "_" +
            System.currentTimeMillis() +
            "." +
            extension
        );
    }

    private String extractExtension(String name) {
        if (name == null || !name.contains(".")) {
            return "bin";
        }
        return name.substring(name.lastIndexOf('.') + 1).toLowerCase();
    }
}
