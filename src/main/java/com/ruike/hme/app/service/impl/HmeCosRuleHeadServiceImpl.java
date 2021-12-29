package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeCosRuleDTO;
import com.ruike.hme.api.dto.HmeCosRuleHeadDto;
import com.ruike.hme.api.dto.HmeCosRuleLogicDTO;
import com.ruike.hme.api.dto.HmeCosRuleTypeDTO;
import com.ruike.hme.app.service.HmeCosRuleHeadService;
import com.ruike.hme.domain.entity.HmeCosRuleHead;
import com.ruike.hme.domain.entity.HmeCosRuleLogic;
import com.ruike.hme.domain.entity.HmeCosRuleType;
import com.ruike.hme.domain.repository.HmeCosRuleHeadRepository;
import com.ruike.hme.domain.repository.HmeCosRuleLogicRepository;
import com.ruike.hme.domain.repository.HmeCosRuleTypeRepository;
import com.ruike.hme.infra.mapper.HmeCosRuleHeadMapper;
import com.ruike.hme.infra.mapper.HmeCosRuleLogicMapper;
import com.ruike.hme.infra.mapper.HmeCosRuleTypeMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 芯片规则头表应用服务默认实现
 *
 * @author wenzhang.yu@hand-china.com 2020-08-10 16:16:46
 */
@Service
public class HmeCosRuleHeadServiceImpl implements HmeCosRuleHeadService {

    @Autowired
    private HmeCosRuleHeadRepository hmeCosRuleHeadRepository;

    @Autowired
    private HmeCosRuleLogicRepository hmeCosRuleLogicRepository;

    @Autowired
    private HmeCosRuleTypeRepository hmeCosRuleTypeRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageService;

    @Autowired
    private HmeCosRuleHeadMapper hmeCosRuleHeadMapper;

    @Autowired
    private HmeCosRuleLogicMapper hmeCosRuleLogicMapper;

    @Autowired
    private HmeCosRuleTypeMapper hmeCosRuleTypeMapper;

    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;

    /**
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.entity.HmeCosRuleHead>
     * @description 查询芯片规则头表
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/10 19:26
     **/
    @Override
    public Page<HmeCosRuleHeadDto> cosRuleQuery(Long tenantId, HmeCosRuleDTO dto, PageRequest pageRequest) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        // 获取当前用户站点信息
        String siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        dto.setSiteId(siteId);
        return PageHelper.doPage(pageRequest, () -> hmeCosRuleHeadRepository.cosRuleQuery(tenantId, dto));
    }

    /**
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.domain.entity.HmeCosRuleHead
     * @description 新增或修改芯片规则头表
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/10 19:42
     **/
    @Override
    public HmeCosRuleHead addAndUpdateCosRule(Long tenantId, HmeCosRuleHead dto) {
        dto.setTenantId(tenantId);
        Long userId = DetailsHelper.getUserDetails().getUserId();
        // 获取当前用户站点信息
        String siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        dto.setSiteId(siteId);
        //判断id时是否为空，id为空则新增，不为空则更新
        if (StringUtils.isEmpty(dto.getCosRuleId())) {
            //校验code
            HmeCosRuleHead hmeCosRuleHead = new HmeCosRuleHead();
            hmeCosRuleHead.setCosRuleCode(dto.getCosRuleCode());
            hmeCosRuleHead.setTenantId(tenantId);
            hmeCosRuleHead.setSiteId(siteId);
            List<HmeCosRuleHead> select = hmeCosRuleHeadRepository.select(hmeCosRuleHead);
            if (CollectionUtils.isEmpty(select)) {
                hmeCosRuleHeadRepository.insertSelective(dto);
            } else {
                throw new MtException("HME_COS_002",
                        mtErrorMessageService.getErrorMessageWithModule(tenantId, "HME_COS_002", "HME",
                                dto.getCosRuleCode()));
            }
        } else {
            //校验code
            HmeCosRuleHead hmeCosRuleHead = new HmeCosRuleHead();
            hmeCosRuleHead.setCosRuleCode(dto.getCosRuleCode());
            hmeCosRuleHead.setTenantId(tenantId);
            hmeCosRuleHead.setSiteId(siteId);
            List<HmeCosRuleHead> select = hmeCosRuleHeadRepository.select(hmeCosRuleHead);
            if (CollectionUtils.isEmpty(select)) {
                hmeCosRuleHeadMapper.updateByPrimaryKeySelective(dto);
            } else {
                if (select.get(0).getCosRuleId().equals(dto.getCosRuleId())) {

                    hmeCosRuleHeadMapper.updateByPrimaryKeySelective(dto);

                } else {
                    throw new MtException("HME_COS_002",
                            mtErrorMessageService.getErrorMessageWithModule(tenantId, "HME_COS_002", "HME",
                                    dto.getCosRuleCode()));
                }
            }
        }
        return dto;


    }

    /**
     * @param hmeCosRuleHead
     * @return void
     * @description 删除芯片规则头表
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/10 20:14
     **/
    @Override
    public void deleteCosRule(HmeCosRuleHead hmeCosRuleHead) {
        //删除芯片规则逻辑表
        HmeCosRuleLogic hmeCosRuleLogic = new HmeCosRuleLogic();
        hmeCosRuleLogic.setCosRuleId(hmeCosRuleHead.getCosRuleId());
        List<HmeCosRuleLogic> select = hmeCosRuleLogicRepository.select(hmeCosRuleLogic);
        hmeCosRuleLogicRepository.batchDelete(select);

        //删除芯片规则逻辑表
        HmeCosRuleType hmeCosRuleType = new HmeCosRuleType();
        hmeCosRuleLogic.setCosRuleId(hmeCosRuleHead.getCosRuleId());
        List<HmeCosRuleType> select1 = hmeCosRuleTypeRepository.select(hmeCosRuleType);
        hmeCosRuleTypeRepository.batchDelete(select1);

        //删除芯片容器表数据
        hmeCosRuleHeadRepository.deleteByPrimaryKey(hmeCosRuleHead.getCosRuleId());
    }


    /**
     *@description 获取芯片规则类型
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/17 14:12
     *@param tenantId
     *@param cosRuleId
     *@param pageRequest
     *@return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeCosRuleTypeDTO>
     **/
    @Override
    @ProcessLovValue
    public Page<HmeCosRuleTypeDTO> cosRuleTypeQuery(Long tenantId, String cosRuleId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmeCosRuleTypeRepository.cosRuleTypeQuery(tenantId, cosRuleId));
    }

    /**
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.domain.entity.HmeCosRuleType
     * @description 新增或修改芯片规则类型
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/10 20:34
     **/
    @Override
    public HmeCosRuleType addAndUpdateCosRuleType(Long tenantId, HmeCosRuleType dto) {
        dto.setTenantId(tenantId);
        Long userId = DetailsHelper.getUserDetails().getUserId();
        // 获取当前用户站点信息
        String siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        dto.setSiteId(siteId);
        //判断id时是否为空，id为空则新增，不为空则更新
        if (StringUtils.isEmpty(dto.getCosRuleTypeId())) {
            hmeCosRuleTypeRepository.insertSelective(dto);
        } else {
            hmeCosRuleTypeMapper.updateByPrimaryKeySelective(dto);
        }
        return dto;
    }

    /**
     *@description 获取芯片规则逻辑
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/10 20:38
     *@param tenantId
     *@param cosRuleId
     *@param pageRequest
     *@return io.choerodon.core.domain.Page<com.ruike.hme.domain.entity.HmeCosRuleLogic>
     **/
    @Override
    @ProcessLovValue
    public Page<HmeCosRuleLogicDTO> cosRuleLogicQuery(Long tenantId, String cosRuleId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmeCosRuleLogicRepository.cosRuleLogicQuery(tenantId, cosRuleId));
    }

    /**
     *@description 新增或修改芯片规则类型
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/11 9:00
     *@param tenantId
     *@param dto
     *@return com.ruike.hme.domain.entity.HmeCosRuleLogic
     **/
    @Override
    public HmeCosRuleLogic addAndUpdateCosRuleLogic(Long tenantId, HmeCosRuleLogic dto) {
        dto.setTenantId(tenantId);
        Long userId = DetailsHelper.getUserDetails().getUserId();
        // 获取当前用户站点信息
        String siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        dto.setSiteId(siteId);
        //判断id时是否为空，id为空则新增，不为空则更新
        if (StringUtils.isEmpty(dto.getCosRuleLogicId())) {
            hmeCosRuleLogicRepository.insertSelective(dto);
        } else {
            hmeCosRuleLogicMapper.updateByPrimaryKeySelective(dto);
        }
        return dto;    }

}

