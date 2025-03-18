package org.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author admin
 * @since 2024-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MedicalRecord implements Serializable {

    private static final long serialVersionUID=1L;

//    @TableId(value = "id", type = IdType.AUTO)
//    private Integer id;

    @TableField("patientName")
    private String patientName;

    @TableField("hospitalName")
    private String hospitalName;

    private String department;

    @TableField("doctorName")
    private String doctorName;

    @TableField("registrationInfo")
    private String registrationInfo;

    @TableField("pastMedicalHistory")
    private String pastMedicalHistory;

    @TableField("currentMedicalHistory")
    private String currentMedicalHistory;


}
