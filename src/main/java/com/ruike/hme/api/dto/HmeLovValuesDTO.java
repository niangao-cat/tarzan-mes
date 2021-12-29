package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeLovTlsVO;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.Date;

/**
 * Lov行信息
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/22 19:02
 */
@ApiModel("Lov头信息")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeLovValuesDTO {

    private Long lovValueId;

    private Long lovId;

    private String lovCode;

    private String value;

    private String meaning;

    private String description;

    private Long tenantId;

    private Long enabledFlag;

    private String tag;

    private Long orderSeq;

    private Date startDateActive;

    private Date endDateActive;

}
