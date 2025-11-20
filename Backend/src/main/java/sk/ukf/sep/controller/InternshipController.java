package sk.ukf.sep.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.ukf.sep.entity.Internship;
import sk.ukf.sep.service.InternshipService;

@RestController
@RequestMapping("/api/internship")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")

public class InternshipController {

    private final InternshipService internshipService;
}
