package com.ruike.hme.api.dto;


import com.ruike.hme.domain.vo.HmeLovTlsVO;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.Map;

/**
 * Lov头信息
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
public class HmeLovHeadersDTO {

   private String lovCode;

   private Long lovId;

   private String lovName;

   private String lovTypeCode;

   private Long mustPageFlag;

   private Long tenantId;

   private Long enabledFlag;

   private String lovTypeMeaning;

}
