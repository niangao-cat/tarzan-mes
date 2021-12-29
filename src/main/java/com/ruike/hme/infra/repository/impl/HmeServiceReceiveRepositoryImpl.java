package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeServiceReceiveDTO2;
import com.ruike.hme.api.dto.HmeServiceReceiveDTO3;
import com.ruike.hme.domain.entity.HmeLogisticsInfo;
import com.ruike.hme.domain.entity.HmeServiceReceive;
import com.ruike.hme.domain.entity.HmeServiceReceiveHis;
import com.ruike.hme.domain.repository.HmeLogisticsInfoRepository;
import com.ruike.hme.domain.repository.HmeServiceReceiveHisRepository;
import com.ruike.hme.domain.repository.HmeServiceReceiveRepository;
import com.ruike.hme.domain.vo.HmeServiceReceiveVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import com.ruike.itf.app.service.ItfLogisticsServiceReceIfaceService;
import com.ruike.itf.domain.entity.ItfLogisticsServiceReceIface;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 营销服务部接收拆箱登记表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2020-09-01 14:14:21
 */
@Component
@Slf4j
public class HmeServiceReceiveRepositoryImpl extends BaseRepositoryImpl<HmeServiceReceive> implements HmeServiceReceiveRepository {

    @Autowired
    private HmeLogisticsInfoRepository hmeLogisticsInfoRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtUserRepository mtUserRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private HmeServiceReceiveHisRepository hmeServiceReceiveHisRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private ItfLogisticsServiceReceIfaceService itfLogisticsServiceReceIfaceService;
    @Autowired
    private LovAdapter lovAdapter;

    @Override
    @ProcessLovValue
    public HmeServiceReceiveVO scanlogisticsNumber(Long tenantId, String logisticsNumber) {
        //根据物流单号查询物流信息
        HmeLogisticsInfo hmeLogisticsInfo = hmeLogisticsInfoRepository.selectOne(new HmeLogisticsInfo() {{
            setTenantId(tenantId);
            setLogisticsNumber(logisticsNumber);
            setEnableFlag("Y");
        }});
        if (hmeLogisticsInfo == null) {
            //若查询不到，则报错
            throw new MtException("HME_SERVICE_RECEIVE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SERVICE_RECEIVE_0001", "HME"));
        }
        //封装返回结果
        HmeServiceReceiveVO result = new HmeServiceReceiveVO();
        result.setLogisticsInfoId(hmeLogisticsInfo.getLogisticsInfoId());
        result.setLogisticsCompany(hmeLogisticsInfo.getLogisticsCompany());
        result.setCreatedBy(hmeLogisticsInfo.getCreatedBy().toString());
        MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, hmeLogisticsInfo.getCreatedBy());
        result.setCreatedByName(mtUserInfo.getRealName());
        result.setCreationDate(hmeLogisticsInfo.getCreationDate());
        result.setBatchNumber(hmeLogisticsInfo.getBatchNumber());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeServiceReceiveDTO2 confirm(Long tenantId, HmeServiceReceiveDTO2 dto) {
        // 历史记录事件ID
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("SERVICE_RECEIVE_CREATE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        HmeServiceReceive hmeServiceReceive = new HmeServiceReceive();
        hmeServiceReceive.setTenantId(tenantId);
        hmeServiceReceive.setSiteId(dto.getSiteId());
        hmeServiceReceive.setLogisticsInfoId(dto.getLogisticsInfoId());
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? 1L : curUser.getUserId();
        hmeServiceReceive.setReceiveBy(userId);
        hmeServiceReceive.setReceiveDate(new Date());
        hmeServiceReceive.setReceiveStatus("RECEIVE");
        hmeServiceReceive.setEnableFlag("Y");
        //2020-09-23 14:25 add by chaonan.hu for fang.pan 增加调用实时接口逻辑
        List<ItfLogisticsServiceReceIface> receIfaceList = new ArrayList<>();
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Date nowDate = new Date();
        HmeLogisticsInfo hmeLogisticsInfo = hmeLogisticsInfoRepository.selectByPrimaryKey(hmeServiceReceive.getLogisticsInfoId());
        for (HmeServiceReceiveDTO3 hmeServiceReceiveDTO3 : dto.getMaterialDataList()) {
            hmeServiceReceive.setAreaCode(hmeServiceReceiveDTO3.getAreaCode());
            hmeServiceReceive.setMaterialLotId(hmeServiceReceiveDTO3.getMaterialLotId());
            hmeServiceReceive.setSnNum(hmeServiceReceiveDTO3.getSnNum());
            hmeServiceReceive.setMaterialId(hmeServiceReceiveDTO3.getMaterialId());
            this.insertSelective(hmeServiceReceive);
            //记录历史
            HmeServiceReceiveHis hmeServiceReceiveHis = new HmeServiceReceiveHis();
            hmeServiceReceiveHis.setEventId(eventId);
            BeanUtils.copyProperties(hmeServiceReceive, hmeServiceReceiveHis);
            hmeServiceReceiveHisRepository.insertSelective(hmeServiceReceiveHis);
            //封装调用售后接收接口API数据
            ItfLogisticsServiceReceIface receIface = new ItfLogisticsServiceReceIface();
            receIface.setCreatedBy(userDetails != null ? userDetails.getUserId() : -1L);
            receIface.setCreatedByName(userDetails != null ? userDetails.getRealName() : "");
            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(hmeServiceReceive.getMaterialId());
            receIface.setMaterialCode(mtMaterial != null ? mtMaterial.getMaterialCode() : "");
            receIface.setAreaCode(hmeServiceReceive.getAreaCode());
            receIface.setCreationDate(nowDate);
            if (hmeLogisticsInfo != null) {
                //物流公司传入值集对应的含义
                String meaning = lovAdapter.queryLovMeaning("HME.LOGISTICS", tenantId, hmeLogisticsInfo.getLogisticsCompany());
                receIface.setLogisticsCompany(meaning);
                receIface.setLogisticsNumber(hmeLogisticsInfo.getLogisticsNumber());
                receIface.setSnNum(hmeServiceReceive.getSnNum());
                receIface.setLogisticsInfoId(hmeLogisticsInfo.getLogisticsInfoId());
            }
            receIface.setIsFlag(HmeConstants.ConstantValue.NO);
            receIface.setTenantId(tenantId);

            String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
            MtModSite mtModSite = mtModSiteRepository.selectByPrimaryKey(defaultSiteId);
            receIface.setPlantCode(mtModSite != null ? mtModSite.getSiteCode() : "");
            receIfaceList.add(receIface);
        }

        if (CollectionUtils.isNotEmpty(receIfaceList)) {
            itfLogisticsServiceReceIfaceService.sendErpLogisticsMsg(receIfaceList, tenantId);
        }

        return dto;
    }
}
