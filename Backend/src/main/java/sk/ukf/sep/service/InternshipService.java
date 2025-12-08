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

    private final List<String> statuses =
            List.of("Registered", "Accepted", "Confirmed", "Defended", "Rejected");

    public Internship registerInternship(InternshipDTO dto) {

        User u = userRepository.findById(dto.getUserId())  // userId теперь Integer
                .orElseThrow(() ->
                        new RuntimeException("User with ID " + dto.getUserId() + " not found"));

        Organization o = organizationRepository.findById(dto.getOrganizationId())  // organizationId Long
                .orElseThrow(() ->
                        new RuntimeException("Organization with ID " + dto.getOrganizationId() + " not found"));

        Internship internship = Internship.builder()
                .user(u)
                .organization(o)
                .beginDate(dto.getBeginDate())
                .endDate(dto.getEndDate())
                .note(dto.getNote())
                .status("Registered")
                .semester(dto.getSemester())
                .build();

        return internshipRepository.save(internship);
    }


    public void changeStatus(InternshipDTO dto) {
        Internship i = internshipRepository.getReferenceById(dto.getId());
        if (!statuses.contains(dto.getStatus())) {
            throw new IllegalArgumentException("Invalid status");
        }
        i.setStatus(dto.getStatus());
        internshipRepository.save(i);
    }

    public List<InternshipDTO> getAllInternships() {
        return internshipRepository.findAll().stream()
                .map(i -> {
                    InternshipDTO dto = new InternshipDTO();
                    dto.setId(i.getId());
                    dto.setUserId(i.getUser().getId());
                    dto.setOrganizationId(i.getOrganization().getId());
                    dto.setBeginDate(i.getBeginDate());
                    dto.setEndDate(i.getEndDate());
                    dto.setNote(i.getNote());
                    dto.setStatus(i.getStatus());
                    dto.setSemester(i.getSemester());
                    return dto;
                })
                .toList();
    }
}
