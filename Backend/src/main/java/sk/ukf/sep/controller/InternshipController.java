package sk.ukf.sep.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.ukf.sep.dto.InternshipDTO;
import sk.ukf.sep.service.InternshipService;

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

}
