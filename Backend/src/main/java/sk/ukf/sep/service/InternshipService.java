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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InternshipService {

    private final InternshipRepository internshipRepository;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    private final List statuses = List.of("Registered", "Accepted", "Confirmed", "Defended", "Rejected");

    public Internship registerInternship(InternshipDTO dto) {

        User u = userRepository.findById(dto.userId)
                .orElseThrow(() -> new RuntimeException("User with ID " + dto.userId + " not found"));
        Organization o = organizationRepository.findById((long) dto.organizationId)
                .orElseThrow(() -> new RuntimeException("Organization with ID " + dto.organizationId + " not found"));

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
        if (statuses.contains(dto.status))
            i.setStatus(dto.status);
        else
            throw new IllegalArgumentException("Invalid status");
        internshipRepository.save(i);
    }

    private InternshipDTO toDTO(Internship internship) {
        InternshipDTO dto = new InternshipDTO();
        dto.id = internship.getId();
        dto.userId = internship.getUser().getId();
        dto.organizationId = internship.getOrganization().getId().intValue();
        dto.beginDate = internship.getBeginDate();
        dto.endDate = internship.getEndDate();
        dto.note = internship.getNote();
        dto.status = internship.getStatus();

        return dto;
    }

    public Optional<InternshipDTO> findById(int id) {
        return internshipRepository.findById((long) id)
                .map(this::toDTO);
    }

    public List<InternshipDTO> findByUserId(int userId) {
        return internshipRepository.findByUser_Id(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<InternshipDTO> findByOrganizationId(int organizationId) {
        return internshipRepository.findByOrganization_Id((long) organizationId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<InternshipDTO> findAll() {
        return internshipRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
