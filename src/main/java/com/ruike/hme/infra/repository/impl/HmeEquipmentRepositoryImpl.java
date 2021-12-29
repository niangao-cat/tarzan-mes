package com.ruike.hme.infra.repository.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.entity.HmeEquipmentHis;
import com.ruike.hme.domain.repository.HmeEquipmentHisRepository;
import com.ruike.hme.domain.repository.HmeEquipmentRepository;
import com.ruike.hme.domain.vo.HmeEquipmentHisVO;
import com.ruike.hme.domain.vo.HmeEquipmentHisVO2;
import com.ruike.hme.domain.vo.HmeEquipmentVO;
import com.ruike.hme.domain.vo.HmeEquipmentVO3;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEquipmentMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.html.HTMLMapElement;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;

import java.util.Arrays;
import java.util.List;

/**
 * 设备表 资源库实现
 *
 * @author xu.deng01@hand-china.com 2020-06-03 18:27:09
 */
@Component
public class HmeEquipmentRepositoryImpl extends BaseRepositoryImpl<HmeEquipment> implements HmeEquipmentRepository {

    @Autowired
    private HmeEquipmentMapper mapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeEquipmentRepository hmeEquipmentRepository;

    @Autowired
    private MtUserClient userClient;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private HmeEquipmentHisRepository hmeEquipmentHisRepository;

    /**
     * @param tenantId  租户ID
     * @param condition 查询条件
     * @return : java.util.List<com.ruike.hme.domain.vo.HmeEquipmentVO>
     * @Description: 设备台账管理-获取设备基础信息
     * @author: Deng Xu
     * @date 2020/6/3 20:02
     * @version 1.0
     */
    @Override
    public List<HmeEquipmentVO> queryEquipmentList(Long tenantId, HmeEquipmentVO condition) {
        condition.setTenantId(tenantId);
        condition.setBusinessIdList(StringUtils.isNotBlank(condition.getBusinessId()) ? Arrays.asList(condition.getBusinessId().split(",")) : null);
        condition.setWorkcellCodeIdList(StringUtils.isNotBlank(condition.getWorkcellCodeId()) ? Arrays.asList(condition.getWorkcellCodeId().split(",")) : null);
        condition.setEquipmentCategoryList(StringUtils.isNotBlank(condition.getEquipmentCategory()) ? Arrays.asList(condition.getEquipmentCategory().split(",")) : null);
        return mapper.queryEquipmentList(condition);
    }

    /**
     * @param tenantId 租户ID
     * @param dto      设备台账信息DTO
     * @return : com.ruike.hme.domain.vo.HmeEquipmentVO
     * @Description: 设备台账管理-新增&更新设备基础信息
     * @author: Deng Xu
     * @date 2020/6/3 20:36
     * @version 1.0
     */
    @Override
    @ProcessLovValue
    public HmeEquipment equipmentBasicPropertyUpdate(Long tenantId, HmeEquipment dto) {
        //校验必输
        if (StringUtils.isEmpty(dto.getAssetEncoding()) || StringUtils.isEmpty(dto.getAssetName())) {
            throw new MtException("HME_EQUIPMENT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_001", "HME"));
        }
        //校验当设备状态为报废时，处置原因和处置单号不可为空
//        boolean statusFlag = HmeConstants.EquipmentStatus.BF.equals(dto.getEquipmentStatus()) && (StringUtils.isEmpty(dto.getDealNum()) || StringUtils.isEmpty(dto.getDealReason()));
//        if (statusFlag) {
//            throw new MtException("HME_EQUIPMENT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "HME_EQUIPMENT_004", "HME"));
//        }
        //如果为更新
        if (StringUtils.isNotEmpty(dto.getEquipmentId())) {
            //先查询版本号，更新
            HmeEquipment queryEquipment = new HmeEquipment();
            queryEquipment.setEquipmentId(dto.getEquipmentId());
            queryEquipment = hmeEquipmentRepository.selectOne(queryEquipment);
            dto.setObjectVersionNumber(queryEquipment.getObjectVersionNumber());
            mapper.updateByPrimaryKeySelective(dto);

            //20210323 add by sanfeng.zhang for peng.zhao 增加设备修改历史
            String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                setEventTypeCode("HME_EQUIPMENT_MODIFED");
            }});
            HmeEquipmentHis hmeEquipmentHis = new HmeEquipmentHis();
            BeanUtils.copyProperties(dto, hmeEquipmentHis);
            hmeEquipmentHis.setEventId(eventId);
            hmeEquipmentHisRepository.insertSelective(hmeEquipmentHis);
        } else {
            //先校验编码+租户ID不存在
            HmeEquipment queryEquipment = new HmeEquipment();
            queryEquipment.setTenantId(tenantId);
            queryEquipment.setAssetEncoding(dto.getAssetEncoding());
            queryEquipment = hmeEquipmentRepository.selectOne(queryEquipment);
            //当资产编码已经存在
            if (null != queryEquipment && StringUtils.isNotEmpty(queryEquipment.getEquipmentId())) {
                throw new MtException("HME_EQUIPMENT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_002", "HME"));
            }
            hmeEquipmentRepository.insertSelective(dto);
        }
        return dto;
    }

    @Override
    public Page<HmeEquipmentHisVO2> queryWorkcellHisForUi(Long tenantId, HmeEquipmentHisVO dto, PageRequest pageRequest) {
        //校验参数
        if (StringUtils.isBlank(dto.getEquipmentId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "equipmentId"));
        }
        //校验时间
        if (StringUtils.isNotBlank(dto.getStartTime()) && StringUtils.isNotBlank(dto.getEndTime())) {
            DateTime startTime = DateUtil.parse(dto.getStartTime(), HmeConstants.ConstantValue.DATE_TIME_FORMAT);
            DateTime endTime = DateUtil.parse(dto.getEndTime(), HmeConstants.ConstantValue.DATE_TIME_FORMAT);
            int compare = DateUtil.compare(startTime, endTime);
            if (compare > 0) {
                throw new MtException("HME_EQUIPMENT_HIS_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_HIS_001", "HME"));
            }
        }

        Page<HmeEquipmentHisVO2> hmeEquipmentHisVo2List = PageHelper.doPage(pageRequest, () -> mapper.queryWorkcellHisForUi(tenantId, dto.getEquipmentId(), dto.getStartTime(), dto.getEndTime()));

        hmeEquipmentHisVo2List.forEach(e -> {
            //操作者
            e.setEventByName(StringUtils.isNotBlank(e.getEventBy()) ? userClient.userInfoGet(tenantId, Long.valueOf(e.getEventBy())).getRealName() : "");
        });
        return hmeEquipmentHisVo2List;
    }

    /**
     * 设备信息
     * @param tenantId
     * @param dto
     * @return
     */
    @Override
    public HmeEquipmentVO3 queryOneInfo(Long tenantId, HmeEquipment dto) {
        return mapper.queryOneInfo(tenantId, dto.getAssetEncoding());
    }


}
