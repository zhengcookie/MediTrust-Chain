package org.example.Form;

import lombok.Data;

@Data
public class AppointmentInfo {
    private String  patientName;
    private String department;
    private String doctorName;
    private String remark;
    private String createTime;
    private String updateTime;
}
