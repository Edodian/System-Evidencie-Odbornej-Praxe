package sk.ukf.sep.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.ukf.sep.dto.InternshipDTO;
import sk.ukf.sep.service.InternshipService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/internship")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InternshipController {

    private final InternshipService internshipService;

    @PostMapping("/register")
    public ResponseEntity<?> registerInternship(@RequestBody InternshipDTO dto) {
        internshipService.registerInternship(dto);
        return ResponseEntity.ok("Internship registered successfully.");
    }

    @PostMapping("/change_status")
    public ResponseEntity<?> changeInternshipStatus(@RequestBody InternshipDTO dto) {
        internshipService.changeStatus(dto);
        return ResponseEntity.ok("Internship status changed successfully.");
    }

    @GetMapping("/show")
    public ResponseEntity<?> showInternships() {
        return ResponseEntity.ok(internshipService.getAllInternships());
    }


    @PostMapping("/show/id/{id}")
    public ResponseEntity<InternshipDTO> showInternshipById(@PathVariable int id) {
        Optional<InternshipDTO> internshipOpt = internshipService.findById(id);
        return internshipOpt
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/show/user/{userId}")
    public ResponseEntity<List<InternshipDTO>> showInternshipsByUserId(@PathVariable int userId) {
        List<InternshipDTO> internships = internshipService.findByUserId(userId);
        return ResponseEntity.ok(internships);
    }

    @PostMapping("/show/organization/{organizationId}")
    public ResponseEntity<List<InternshipDTO>> showInternshipsByOrganizationId(@PathVariable int organizationId) {
        List<InternshipDTO> internships = internshipService.findByOrganizationId(organizationId);
        return ResponseEntity.ok(internships);
    }

    @PostMapping("/show/all")
    public ResponseEntity<List<InternshipDTO>> showAllInternships() {
        List<InternshipDTO> internships = internshipService.findAll();
        return ResponseEntity.ok(internships);
    }
}
