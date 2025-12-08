package sk.ukf.sep.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class InternshipDTO {
    private Long id;
    private Integer userId;
    private Long organizationId;
    private LocalDate beginDate;
    private LocalDate endDate;
    private String note;
    private String status;
    private String semester;
}

