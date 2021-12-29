package com.ruike.qms.infra.mapper;

import com.ruike.qms.api.dto.QmsTransitionRuleDTO;
import com.ruike.qms.api.dto.QmsTransitionRuleDTO2;
import com.ruike.qms.domain.entity.QmsTransitionRule;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.general.api.dto.MtUserOrganizationDTO6;

import java.util.List;

/**
 * 检验水平转移规则表Mapper
 *
 * @author tong.li05@hand-china.com 2020-05-11 09:54:52
 */
public interface QmsTransitionRuleMapper extends BaseMapper<QmsTransitionRule> {


    /**
    * @param tenantId 1
    * @param siteId 2
    * @param materialId 3
    * @return : java.util.List<com.ruike.qms.api.dto.QmsTransitionRuleDTO>
    * @Description: 检验水平转移基础设置  查询
    * @author: tong.li
    * @date 2020/5/11 15:39
    * @version 1.0
    */
    List<QmsTransitionRuleDTO> query(@Param("tenantId") Long tenantId,@Param("siteId") String siteId ,@Param("materialId") String materialId);

    /**
    * @param tenantId 1
    * @param userId 2
    * @param defaultOrganizationFlag 3
    * @return : java.util.List<com.ruike.qms.api.dto.QmsTransitionRuleDTO2>
    * @Description: 获取默认站点
    * @author: tong.li
    * @date 2020/5/19 10:26
    * @version 1.0
    */
    List<QmsTransitionRuleDTO2> userSiteListForUi(@Param(value = "tenantId") Long tenantId,
                                                  @Param(value = "userId") Long userId,
                                                  @Param(value = "defaultOrganizationFlag") String defaultOrganizationFlag);
}
