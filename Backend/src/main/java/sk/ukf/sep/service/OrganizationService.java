package sk.ukf.sep.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sk.ukf.sep.dto.OrganizationRegistrationDTO;
import sk.ukf.sep.entity.Organization;
import sk.ukf.sep.repository.OrganizationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    public Organization registerOrganization(OrganizationRegistrationDTO dto) {

        if (organizationRepository.findByIco(dto.getIco()).isPresent()) {
            throw new IllegalArgumentException("Organization with this ICO already exists");
        }

        if (organizationRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Organization with this email already exists");
        }

        Organization organization = Organization.builder()
                .title(dto.getTitle())
                .ico(dto.getIco())
                .contactPhone(dto.getContactPhone())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .isVerified(false)
                .build();

        return organizationRepository.save(organization);
    }

    public String login(String email, String password) {

        Organization org = organizationRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, org.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!org.isVerified()) {
            throw new RuntimeException("Organization is not verified");
        }

        return jwtTokenService.generateToken(
                org.getEmail(),
                List.of("ROLE_COMPANY")
        );
    }

    public boolean verifyOrganization(Integer id) {
        return organizationRepository.findById(id)
                .map(org -> {
                    org.setVerified(true);
                    organizationRepository.save(org);
                    return true;
                })
                .orElse(false);
    }

    public boolean unverifyOrganization(Integer id) {
        return organizationRepository.findById(id)
                .map(org -> {
                    org.setVerified(false);
                    organizationRepository.save(org);
                    return true;
                })
                .orElse(false);
    }

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }
}
