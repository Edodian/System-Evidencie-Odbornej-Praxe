package sk.ukf.sep.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sk.ukf.sep.dto.InternshipDTO;
import sk.ukf.sep.entity.Internship;
import sk.ukf.sep.entity.Organization;
import sk.ukf.sep.entity.User;
import sk.ukf.sep.repository.InternshipRepository;
import sk.ukf.sep.repository.OrganizationRepository;
import sk.ukf.sep.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InternshipService {

    private final InternshipRepository internshipRepository;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    private final List statuses = List.of("Registered", "Accepted", "Confirmed", "Defended", "Rejected");

    public Internship registerInternship(InternshipDTO dto) {

        User u = userRepository.findById((Integer) dto.userId).orElseThrow();
        Organization o = organizationRepository.findById((long) dto.organizationId).orElseThrow();

        Internship internship = Internship.builder()
                .user(u)
                .organization(o)
                .beginDate(dto.beginDate)
                .endDate(dto.endDate)
                .note(dto.note)
                .status("Registered")
                .build();

        return internshipRepository.save(internship);
    }

    public void changeStatus(InternshipDTO dto) {
        Internship i = internshipRepository.getReferenceById((long) dto.id);
        if (statuses.contains(dto.status)) i.setStatus(dto.status);
        else throw new IllegalArgumentException("Invalid status");
        internshipRepository.save(i);
    }
}
