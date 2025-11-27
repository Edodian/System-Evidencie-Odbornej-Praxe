package sk.ukf.sep.dto;

import java.time.LocalDate;

public class InternshipDTO {
    public int id;
    public int userId;
    public int organizationId;
    public LocalDate beginDate;
    public LocalDate endDate;
    public String note;
    public String status;
    private String semester;
}
