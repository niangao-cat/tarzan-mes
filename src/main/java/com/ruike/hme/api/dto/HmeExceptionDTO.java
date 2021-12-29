package com.ruike.hme.api.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * HmeExceptionDTO
 *
 * @author liyuan.lv@hand-china.com 2020/05/09 11:17
 */
@Data
public class HmeExceptionDTO implements Serializable {
    private static final long serialVersionUID = 968866823829503016L;
    private String exceptionType;
    private String exceptionTypeName;
    private String exceptionId;
    private String exceptionCode;
    private String exceptionName;
    private String enableFlag;
}
