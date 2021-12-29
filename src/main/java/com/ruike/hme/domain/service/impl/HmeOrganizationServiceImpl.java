package com.ruike.hme.domain.service.impl;

import com.ruike.hme.api.dto.HmeOrganizationDTO;
import com.ruike.hme.api.dto.HmeOrganizationDTO2;
import com.ruike.hme.api.dto.HmeOrganizationDTO3;
import com.ruike.hme.api.dto.HmeOrganizationDTO4;
import com.ruike.hme.domain.service.HmeOrganizationService;
import com.ruike.hme.infra.mapper.HmeOrganizationMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModAreaRepository;

import java.util.Objects;

import static io.tarzan.common.domain.util.MtBaseConstants.ORGANIZATION_TYPE.AREA;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/29 18:49
 */
@Service
public class HmeOrganizationServiceImpl implements HmeOrganizationService {
    private final MtUserOrganizationRepository userOrganizationRepository;
    private final MtModAreaRepository areaRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final HmeOrganizationMapper hmeOrganizationMapper;

    public HmeOrganizationServiceImpl(MtUserOrganizationRepository userOrganizationRepository, MtModAreaRepository areaRepository, MtErrorMessageRepository mtErrorMessageRepository, HmeOrganizationMapper hmeOrganizationMapper) {
        this.userOrganizationRepository = userOrganizationRepository;
        this.areaRepository = areaRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.hmeOrganizationMapper = hmeOrganizationMapper;
    }


    @Override
    public MtModArea getUserDefaultArea(Long tenantId) {
        // 获取用户默认部门
        MtUserOrganization userOrganization = new MtUserOrganization();
        userOrganization.setUserId(DetailsHelper.getUserDetails().getUserId());
        userOrganization.setOrganizationType(AREA);
        MtUserOrganization defaultArea =
                userOrganizationRepository.userDefaultOrganizationGet(tenantId, userOrganization);
        if (Objects.isNull(defaultArea)) {
            throw new MtException("HME_SPLIT_RECORD_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_SPLIT_RECORD_0024", "HME"));
        }
        return areaRepository.selectByPrimaryKey(defaultArea.getOrganizationId());
    }

    @Override
    public Page<MtModWorkcell> lineWorkcellLovQuery(Long tenantId, HmeOrganizationDTO dto, PageRequest pageRequest) {
        Page<MtModWorkcell> resultPage = new Page<>();
        if(StringUtils.isNotBlank(dto.getProdLineId())){
            //如果产线不为空,则根据多个产线查询工段
            resultPage = PageHelper.doPage(pageRequest, ()->hmeOrganizationMapper.lineWorkcellByProdlineQuery(tenantId, dto));
        }else if(StringUtils.isNotBlank(dto.getDepartmentId())){
            //如果部门不为空，则根据多个部门查询工段
            resultPage = PageHelper.doPage(pageRequest, ()->hmeOrganizationMapper.lineWorkcellByDepartmentQuery(tenantId, dto));
        } else{
            //如果上层架构的ID均为空，则查询所有工段
            resultPage = PageHelper.doPage(pageRequest, ()->hmeOrganizationMapper.lineWorkcellAllQuery(tenantId, dto));
        }
        return resultPage;
    }

    @Override
    public Page<MtModWorkcell> processLovQuery(Long tenantId, HmeOrganizationDTO2 dto, PageRequest pageRequest) {
        Page<MtModWorkcell> resultPage = new Page<>();
        if(StringUtils.isNotBlank(dto.getLineWorkcellId())){
            //如果工段不为空，则根据多个工段查询工序
            resultPage = PageHelper.doPage(pageRequest, ()->hmeOrganizationMapper.processByLineWorkcellQuery(tenantId, dto));
        }else if(StringUtils.isNotBlank(dto.getProdLineId())){
            //如果产线不为空, 则根据多个产线查询工序
            resultPage = PageHelper.doPage(pageRequest, ()->hmeOrganizationMapper.processByProdLineQuery(tenantId, dto));
        }else if(StringUtils.isNotBlank(dto.getDepartmentId())){
            //如果部门不为空, 则根据多个部门查询工序
            resultPage = PageHelper.doPage(pageRequest, ()->hmeOrganizationMapper.processByDepartmentQuery(tenantId, dto));
        } else{
            //如果上层架构的ID均为空，则查询所有工序
            resultPage = PageHelper.doPage(pageRequest, ()->hmeOrganizationMapper.processAllQuery(tenantId, dto));
        }
        return resultPage;
    }

    @Override
    public Page<MtModProductionLine> prodLineLovQuery(Long tenantId, HmeOrganizationDTO3 dto, PageRequest pageRequest) {
        Page<MtModProductionLine> resultPage = new Page<>();
        if (StringUtils.isNotBlank(dto.getWorkshopId())) {
            //如果车间不为空,则根据车间查询产线
            resultPage = PageHelper.doPage(pageRequest, ()->hmeOrganizationMapper.prodLineByWorkshopQuery(tenantId, dto));
        }else if (StringUtils.isNotBlank(dto.getDepartmentId())){
            //如果部门不为空，则根据部门查询产线
            resultPage = PageHelper.doPage(pageRequest, ()->hmeOrganizationMapper.prodLineByDepartmentQuery(tenantId, dto));
        }else if (StringUtils.isNotBlank(dto.getSiteId())){
            // 如果站点不为空 则根据站点查询产线
            resultPage = PageHelper.doPage(pageRequest, ()->hmeOrganizationMapper.prodLineBySiteQuery(tenantId, dto));
        }else {
            //如果上层架构的ID均为空，则查询所有产线
            resultPage = PageHelper.doPage(pageRequest, ()->hmeOrganizationMapper.prodLineAllQuery(tenantId, dto));
        }
        return resultPage;
    }

    @Override
    public Page<MtModWorkcell> workcellLovQuery(Long tenantId, HmeOrganizationDTO4 dto, PageRequest pageRequest) {
        Page<MtModWorkcell> resultPage = new Page<>();
        if(StringUtils.isNotBlank(dto.getProcessId())){
            //如果工序不为空，则根据工序查询工位
            resultPage = PageHelper.doPage(pageRequest, ()->hmeOrganizationMapper.workcellByProcessQuery(tenantId, dto));
        }else if(StringUtils.isNotBlank(dto.getLineWorkcellId())){
            //如果工段不为空，则根据工段查询工位
            resultPage = PageHelper.doPage(pageRequest, ()->hmeOrganizationMapper.workcellByLineWorkcellQuery(tenantId, dto));
        }else if(StringUtils.isNotBlank(dto.getProdLineId())){
            //如果产线不为空，则根据产线查询工位
            resultPage = PageHelper.doPage(pageRequest, ()->hmeOrganizationMapper.workcellByProdLineQuery(tenantId, dto));
        }else if(StringUtils.isNotBlank(dto.getDepartmentId())){
            //如果事业部不为空，则根据事业部查询工位
            resultPage = PageHelper.doPage(pageRequest, ()->hmeOrganizationMapper.workcellByDepartmentQuery(tenantId, dto));
        }else {
            //如果上层架构的ID均为空，则查询所有工位
            resultPage = PageHelper.doPage(pageRequest, ()->hmeOrganizationMapper.workcellAllQuery(tenantId, dto));
        }
        return resultPage;
    }
}
