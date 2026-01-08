package sk.ukf.sep.controller;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.ukf.sep.entity.Document;
import sk.ukf.sep.service.DocumentService;

@RestController
@RequestMapping("/api/document")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Document> upload(
        @RequestPart("file") MultipartFile file,
        @RequestParam("internshipId") Integer internshipId,
        @RequestParam("type") String type
    ) throws IOException {
        Document saved = documentService.upload(file, internshipId, type);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
