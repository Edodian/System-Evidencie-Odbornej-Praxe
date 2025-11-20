package sk.ukf.sep.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sk.ukf.sep.dto.OrganizationRegistrationDTO;
import sk.ukf.sep.entity.Organization;
import sk.ukf.sep.repository.OrganizationRepository;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public Organization registerOrganization(OrganizationRegistrationDTO dto) {
        if (organizationRepository.findByIco(dto.getIco()).isPresent()) {
            throw new IllegalArgumentException("Organization with this ICO already exists");
        }

        Organization organization = Organization.builder()
                .title(dto.getTitle())
                .ico(dto.getIco())
                .contactPhone(dto.getContactPhone())
                .isVerified(false)
                .build();

        return organizationRepository.save(organization);
    }

    public boolean verifyOrganization(Long id) {
        return organizationRepository.findById(id)
                .map(org -> {
                    org.setVerified(true);
                    organizationRepository.save(org);
                    return true;
                })
                .orElse(false);
    }
    public boolean unverifyOrganization(Long id) {
        return organizationRepository.findById(id)
                .map(org -> {
                    org.setVerified(false);
                    organizationRepository.save(org);
                    return true;
                })
                .orElse(false);
    }
}
