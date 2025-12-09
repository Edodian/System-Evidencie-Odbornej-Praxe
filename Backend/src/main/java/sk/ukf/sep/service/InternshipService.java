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

@Service
@RequiredArgsConstructor
public class InternshipService {

    private final InternshipRepository internshipRepository;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    private static final List<String> STATUSES =
            List.of("Registered", "Accepted", "Confirmed", "Defended", "Rejected");

    // -------------------- CREATE --------------------

    public Internship registerInternship(InternshipDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("InternshipDTO must not be null");
        }
        if (dto.getUserId() == null) {
            throw new IllegalArgumentException("userId must not be null");
        }
        if (dto.getOrganizationId() == null) {
            throw new IllegalArgumentException("organizationId must not be null");
        }

        // User.id is Integer
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User with ID " + dto.getUserId() + " not found"));

        // Assuming Organization.id is Long – adjust if it's Integer in your entity
        Organization organization = organizationRepository
                .findById(dto.getOrganizationId())
                .orElseThrow(() ->
                        new RuntimeException("Organization with ID " + dto.getOrganizationId() + " not found"));

        Internship internship = Internship.builder()
                .user(user)
                .organization(organization)
                .beginDate(dto.getBeginDate())
                .endDate(dto.getEndDate())
                .note(dto.getNote())
                .status("Registered")
                .semester(dto.getSemester())
                .build();

        return internshipRepository.save(internship);
    }

    // -------------------- UPDATE STATUS --------------------

    public void changeStatus(InternshipDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        if (dto.getStatus() == null) {
            throw new IllegalArgumentException("status must not be null");
        }
        if (!STATUSES.contains(dto.getStatus())) {
            throw new IllegalArgumentException("Invalid status: " + dto.getStatus());
        }

        Internship i = internshipRepository.getReferenceById(dto.getId());
        i.setStatus(dto.getStatus());
        internshipRepository.save(i);
    }

    // -------------------- MAPPING --------------------

    private InternshipDTO toDTO(Internship internship) {
        InternshipDTO dto = new InternshipDTO();
        dto.setId(internship.getId());
        dto.setUserId(internship.getUser().getId());
        // If Organization.id is Integer, simply: dto.setOrganizationId(internship.getOrganization().getId());
        dto.setOrganizationId(internship.getOrganization().getId().intValue());
        dto.setBeginDate(internship.getBeginDate());
        dto.setEndDate(internship.getEndDate());
        dto.setNote(internship.getNote());
        dto.setStatus(internship.getStatus());
        dto.setSemester(internship.getSemester());
        return dto;
    }


    public Optional<InternshipDTO> findById(int id) {
        // Internship.id is Integer, repository is <Internship, Integer>
        return internshipRepository.findById(id)
                .map(this::toDTO);
    }

    public List<InternshipDTO> findByUserId(int userId) {
        return internshipRepository.findByUser_Id(userId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<InternshipDTO> findByOrganizationId(int organizationId) {
        return internshipRepository.findByOrganization_Id(organizationId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<InternshipDTO> findAll() {
        return internshipRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

}
