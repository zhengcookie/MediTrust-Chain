package org.example.entity;

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
 * @since 2024-12-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Doctor implements Serializable {

    private static final long serialVersionUID=1L;

    private String username;

    private String gender;

    private Integer age;

    @TableField("accountAddress")
    private String accountAddress;

    private String password;


}
