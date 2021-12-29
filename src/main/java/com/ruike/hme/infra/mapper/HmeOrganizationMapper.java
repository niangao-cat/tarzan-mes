package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeOrganizationDTO;
import com.ruike.hme.api.dto.HmeOrganizationDTO2;
import com.ruike.hme.api.dto.HmeOrganizationDTO3;
import com.ruike.hme.api.dto.HmeOrganizationDTO4;
import org.apache.ibatis.annotations.Param;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModWorkcell;

import java.util.List;

/**
 * 组织关系查询Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-04-12 18:14:23
 */
public interface HmeOrganizationMapper {

    /**
     * 根据产线查询工段
     *
     * @param tenantId 租户ID
     * @param dto 以逗号分隔的多个产线ID字符串以及模糊查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/13 09:01:14
     * @return java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    List<MtModWorkcell> lineWorkcellByProdlineQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeOrganizationDTO dto);

    /**
     * 根据部门查询所有工段
     *
     * @param tenantId 租户ID
     * @param dto 以逗号分隔的多个部门ID字符串以及模糊查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/14 10:52:52
     * @return java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    List<MtModWorkcell> lineWorkcellByDepartmentQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeOrganizationDTO dto);

    /**
     * 查询所有工段
     *
     * @param tenantId 租户ID
     * @param dto 模糊查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/13 09:15:16
     * @return java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    List<MtModWorkcell> lineWorkcellAllQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeOrganizationDTO dto);

    /**
     * 根据工段查询工序
     *
     * @param tenantId 租户ID
     * @param dto 以逗号分隔的多个工段ID字符串以及模糊查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/13 09:26:02
     * @return java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    List<MtModWorkcell> processByLineWorkcellQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeOrganizationDTO2 dto);

    /**
     * 根据产线查询工序
     *
     * @param tenantId 租户ID
     * @param dto 以逗号分隔的多个产线ID字符串以及模糊查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/13 09:44:40
     * @return java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    List<MtModWorkcell> processByProdLineQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeOrganizationDTO2 dto);

    /**
     * 根据部门查询工序
     *
     * @param tenantId 租户ID
     * @param dto 以逗号分隔的多个部门ID字符串以及模糊查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/13 09:44:40
     * @return java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    List<MtModWorkcell> processByDepartmentQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeOrganizationDTO2 dto);

    /**
     * 查询所有工序
     *
     * @param tenantId 租户ID
     * @param dto 模糊查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/13 09:47:39
     * @return java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    List<MtModWorkcell> processAllQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeOrganizationDTO2 dto);

    /**
     * 根据部门查询产线
     *
     * @param tenantId 租户ID
     * @param dto 以逗号分隔的多个部门ID字符串以及模糊查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/14 10:35:26
     * @return java.util.List<tarzan.modeling.domain.entity.MtModProductionLine>
     */
    List<MtModProductionLine> prodLineByDepartmentQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeOrganizationDTO3 dto);

    /**
     * 查询所有产线
     *
     * @param tenantId 租户ID
     * @param dto 模糊查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/14 10:43:04
     * @return java.util.List<tarzan.modeling.domain.entity.MtModProductionLine>
     */
    List<MtModProductionLine> prodLineAllQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeOrganizationDTO3 dto);

    /**
     * 根据车间查询产线
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/4/14 16:07
     * @return java.util.List<tarzan.modeling.domain.entity.MtModProductionLine>
     */
    List<MtModProductionLine> prodLineByWorkshopQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeOrganizationDTO3 dto);

    /**
     * 根据工序查询工位
     *
     * @param tenantId 租户ID
     * @param dto 以逗号分隔的多个工序ID字符串以及模糊查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/15 09:30:00
     * @return java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    List<MtModWorkcell> workcellByProcessQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeOrganizationDTO4 dto);

    /**
     * 根据工段查询工位
     *
     * @param tenantId 租户ID
     * @param dto 以逗号分隔的多个工段ID字符串以及模糊查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/15 09:30:00
     * @return java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    List<MtModWorkcell> workcellByLineWorkcellQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeOrganizationDTO4 dto);

    /**
     * 根据产线查询工位
     *
     * @param tenantId 租户ID
     * @param dto 以逗号分隔的多个产线ID字符串以及模糊查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/15 09:30:00
     * @return java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    List<MtModWorkcell> workcellByProdLineQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeOrganizationDTO4 dto);

    /**
     * 根据事业部查询工位
     *
     * @param tenantId 租户ID
     * @param dto 以逗号分隔的多个事业部ID字符串以及模糊查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/15 09:30:00
     * @return java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    List<MtModWorkcell> workcellByDepartmentQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeOrganizationDTO4 dto);

    /**
     * 查询所有工位
     *
     * @param tenantId 租户ID
     * @param dto 模糊查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/15 09:30:00
     * @return java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    List<MtModWorkcell> workcellAllQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeOrganizationDTO4 dto);

    /**
     * 查询工厂下产线
     *
     * @param tenantId  租户
     * @param dto       条件
     * @author sanfeng.zhang@hand-china.com 2021/4/15 15:10
     * @return java.util.List<tarzan.modeling.domain.entity.MtModProductionLine>
     */
    List<MtModProductionLine> prodLineBySiteQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeOrganizationDTO3 dto);

}
