package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeProcessNcImportDTO;
import com.ruike.hme.domain.entity.HmeFreezePrivilege;
import com.ruike.hme.domain.entity.HmeFreezePrivilegeDetail;
import com.ruike.hme.domain.repository.HmeFreezePrivilegeDetailRepository;
import com.ruike.hme.domain.repository.HmeFreezePrivilegeRepository;
import com.ruike.hme.domain.vo.HmeFreezePrivilegeImportVO;
import com.ruike.hme.infra.mapper.HmeFreezePrivilegeDetailMapper;
import com.ruike.hme.infra.mapper.HmeFreezePrivilegeMapper;
import com.ruike.hme.infra.util.CommonUtils;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 冻结解冻权限导入
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/24 13:51
 */
@ImportService(templateCode = "HME.FREEZE_PRIVILEGE_IMPORT")
@Slf4j
public class HmeFreezePrivilegeImportServiceImpl implements IBatchImportService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeFreezePrivilegeRepository hmeFreezePrivilegeRepository;

    @Autowired
    private HmeFreezePrivilegeMapper hmeFreezePrivilegeMapper;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private HmeFreezePrivilegeDetailRepository hmeFreezePrivilegeDetailRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean doImport(List<String> data) {
        // 获取租户Id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        if (CollectionUtils.isNotEmpty(data)) {
            for (String vo : data) {
                HmeFreezePrivilegeImportVO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeFreezePrivilegeImportVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }

                if (importVO.getTenantId() != null) {
                    tenantId = importVO.getTenantId();
                }

                //邮箱格式检验
                if(!CommonUtils.isEmail(importVO.getEmail())){
                    throw new MtException("邮箱格式错误");
                }

                String 	detailObjectId = null;
                //若输入则校验对象编码存在性
                if(StringUtils.isNotBlank(importVO.getDetailObjectType()) && StringUtils.isNotBlank(importVO.getDetailObjectCode())){
                    if("PROD_LINE".equals(importVO.getDetailObjectType())){
                        List<MtModProductionLine> mtModProductionLines = mtModProductionLineRepository.select(MtModProductionLine.FIELD_PROD_LINE_CODE, importVO.getDetailObjectCode());
                        if(CollectionUtils.isEmpty(mtModProductionLines)){
                            //当前产线编码不存在
                            throw new MtException("当前产线编码不存在");
                        }else {
                            detailObjectId = mtModProductionLines.get(0).getProdLineId();
                        }
                    }else if("WAREHOUSE".equals(importVO.getDetailObjectType())){
                        List<MtModLocator> mtModLocators = mtModLocatorRepository.select(MtModLocator.FIELD_LOCATOR_CODE, importVO.getDetailObjectCode());
                        if(CollectionUtils.isEmpty(mtModLocators)){
                            //当前仓库编码不存在
                            throw new MtException("当前仓库编码不存在");
                        }else {
                            detailObjectId = mtModLocators.get(0).getLocatorId();
                        }
                    }
                }else if((StringUtils.isNotBlank(importVO.getDetailObjectType()) && StringUtils.isBlank(importVO.getDetailObjectCode())) || (StringUtils.isBlank(importVO.getDetailObjectType()) && StringUtils.isNotBlank(importVO.getDetailObjectCode()))){
                    throw new MtException("对象类型和对象编码需同时有值或无值");
                }

                //用户
                Long userId = hmeFreezePrivilegeMapper.getUserId(importVO.getRealName());
                if(Objects.isNull(userId)){
                    //该用户不存在
                    throw new MtException("该用户不存在");
                }

                List<HmeFreezePrivilege> select = hmeFreezePrivilegeRepository.select(HmeFreezePrivilege.FIELD_USER_ID, userId);
                int flag = hmeFreezePrivilegeMapper.selectExist(tenantId,userId,detailObjectId);
                log.info("===================tenantId"+tenantId+"=========flag>"+flag);
                if(flag == 0) {
                    HmeFreezePrivilege hmeFreezePrivilege = new HmeFreezePrivilege();
                    log.info(select.toString());
                    if(CollectionUtils.isEmpty(select)) {
                        //头
                        hmeFreezePrivilege.setUserId(userId);
                        hmeFreezePrivilege.setTenantId(tenantId);
                        hmeFreezePrivilege.setEmail(importVO.getEmail());
                        hmeFreezePrivilege.setPrivilegeType(importVO.getPrivilegeType());
                        hmeFreezePrivilege.setCosPrivilegeType(importVO.getCosPrivilegeType());
                        hmeFreezePrivilege.setEnableFlag(importVO.getEnableFlag());
                        log.info(hmeFreezePrivilege.toString());
                        hmeFreezePrivilegeRepository.insertSelective(hmeFreezePrivilege);
                    }
                    //明细
                    if(Objects.nonNull(detailObjectId)) {
                        HmeFreezePrivilegeDetail hmeFreezePrivilegeDetail = new HmeFreezePrivilegeDetail();
                        if(CollectionUtils.isEmpty(select)){
                            hmeFreezePrivilegeDetail.setPrivilegeId(hmeFreezePrivilege.getPrivilegeId());
                        }else {
                            hmeFreezePrivilegeDetail.setPrivilegeId(select.get(0).getPrivilegeId());
                        }
                        hmeFreezePrivilegeDetail.setDetailObjectType(importVO.getDetailObjectType());
                        hmeFreezePrivilegeDetail.setDetailObjectId(detailObjectId);
                        hmeFreezePrivilegeDetail.setTenantId(tenantId);
                        hmeFreezePrivilegeDetailRepository.insertSelective(hmeFreezePrivilegeDetail);
                    }
                }else {
                    //用户（显示用户账号）+对象类型（显示对象类型）+对象编码（显示对象编码）重复
                    throw new MtException("HME.FREEZE_PRIVILEGE_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME.FREEZE_PRIVILEGE_001", "HME", importVO.getRealName(), importVO.getDetailObjectType(), importVO.getDetailObjectCode()));
                }
            }
        }
        return true;
    }
}
