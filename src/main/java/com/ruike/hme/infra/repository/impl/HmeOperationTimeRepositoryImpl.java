package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeOperationTimeDto;
import com.ruike.hme.api.dto.HmeOperationTimeDto2;
import com.ruike.hme.api.dto.HmeOperationTimeDto3;
import com.ruike.hme.api.dto.HmeOperationTimeDto4;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.mapper.HmeOperationTimeMapper;
import com.ruike.hme.infra.mapper.HmeOperationTimeMaterialMapper;
import com.ruike.hme.infra.mapper.HmeOperationTimeObjectMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.util.List;

/**
 * 工艺时效要求表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2020-08-11 11:44:06
 */
@Component
public class HmeOperationTimeRepositoryImpl extends BaseRepositoryImpl<HmeOperationTime> implements HmeOperationTimeRepository {

    @Autowired
    private HmeOperationTimeMapper hmeOperationTimeMapper;

    @Autowired
    private MtUserRepository mtUserRepository;

    @Autowired
    private HmeOperationTimeMaterialMapper hmeOperationTimeMaterialMapper;

    @Autowired
    private HmeOperationTimeMaterialRepository hmeOperationTimeMaterialRepository;

    @Autowired
    private HmeOperationTimeObjectRepository hmeOperationTimeObjectRepository;

    @Autowired
    private HmeOperationTimeObjectMapper hmeOperationTimeObjectMapper;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private HmeOperationTimeObjectHisRepository hmeOperationTimeObjectHisRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private HmeOperationTimeHisRepository hmeOperationTimeHisRepository;

    @Autowired
    private HmeOperationTimeMaterialHisRepository hmeOperationTimeMaterialHisRepository;

    @Override
    public Page<HmeOperationTimeVO> query(Long tenantId, HmeOperationTimeDto4 dto, PageRequest pageRequest) {
        Page<HmeOperationTimeVO> result = PageHelper.doPageAndSort(pageRequest, () -> hmeOperationTimeMapper.query(tenantId, dto));
        MtUserInfo mtUserInfo = null;
        for (HmeOperationTimeVO hmeOperationTimeVO:result) {
            mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.valueOf(hmeOperationTimeVO.getCreatedBy()));
            hmeOperationTimeVO.setCreatedByName(mtUserInfo.getRealName());
            mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.valueOf(hmeOperationTimeVO.getLastUpdatedBy()));
            hmeOperationTimeVO.setLastUpdatedByName(mtUserInfo.getRealName());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeOperationTime> createOrUpdate(Long tenantId, List<HmeOperationTime> hmeOperationTimeList) {
        String createEventId = null;
        String updateEventId = null;
        for (HmeOperationTime hmeOperationTime:hmeOperationTimeList) {
            hmeOperationTime.setTenantId(tenantId);
            if(StringUtils.isEmpty(hmeOperationTime.getOperationTimeId())){
                //新增
                this.insertSelective(hmeOperationTime);
                //记录历史
                if(StringUtils.isEmpty(createEventId)){
                    MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                    eventCreateVO.setEventTypeCode("HME_OPERATION_TIME_CREATE");
                    createEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
                }

                HmeOperationTimeHis hmeOperationTimeHis = new HmeOperationTimeHis();
                BeanUtils.copyProperties(hmeOperationTime, hmeOperationTimeHis);
                hmeOperationTimeHis.setEventId(createEventId);
                hmeOperationTimeHisRepository.insertSelective(hmeOperationTimeHis);
            }else{
                //更新
                HmeOperationTime hmeOperationTimeDb = this.selectByPrimaryKey(hmeOperationTime.getOperationTimeId());
                hmeOperationTimeDb.setTimeCode(hmeOperationTime.getTimeCode());
                hmeOperationTimeDb.setTimeName(hmeOperationTime.getTimeName());
                hmeOperationTimeDb.setStandardReqdTimeInProcess(hmeOperationTime.getStandardReqdTimeInProcess());
                hmeOperationTimeDb.setOperationId(hmeOperationTime.getOperationId());
                hmeOperationTimeDb.setWorkcellId(hmeOperationTime.getWorkcellId());
                hmeOperationTimeDb.setEnableFlag(hmeOperationTime.getEnableFlag());
                hmeOperationTimeMapper.updateByPrimaryKey(hmeOperationTimeDb);
                //记录历史
                if(StringUtils.isEmpty(updateEventId)) {
                    MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                    eventCreateVO.setEventTypeCode("HME_OPERATION_TIME_UPDATE");
                    updateEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
                }

                HmeOperationTimeHis hmeOperationTimeHis = new HmeOperationTimeHis();
                BeanUtils.copyProperties(hmeOperationTime, hmeOperationTimeHis);
                hmeOperationTimeHis.setEventId(updateEventId);
                hmeOperationTimeHisRepository.insertSelective(hmeOperationTimeHis);
            }
        }
        return hmeOperationTimeList;
    }

    @Override
    public Page<HmeOperationTimeVO2> queryMaterial(Long tenantId, HmeOperationTimeDto dto, PageRequest pageRequest) {
        Page<HmeOperationTimeVO2> result = PageHelper.doPage(pageRequest, () -> hmeOperationTimeMapper.queryMaterial(tenantId, dto));
        MtUserInfo mtUserInfo = null;
        for (HmeOperationTimeVO2 hmeOperationTimeVO2:result) {
            mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.valueOf(hmeOperationTimeVO2.getCreatedBy()));
            hmeOperationTimeVO2.setCreatedByName(mtUserInfo.getRealName());
            mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.valueOf(hmeOperationTimeVO2.getLastUpdatedBy()));
            hmeOperationTimeVO2.setLastUpdatedByName(mtUserInfo.getRealName());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeOperationTimeMaterial> createOrUpdateMaterial(Long tenantId, List<HmeOperationTimeMaterial> list) {
        String createEventId = null;
        String updateEventId = null;
        for (HmeOperationTimeMaterial hmeOperationTimeMaterial:list) {
            hmeOperationTimeMaterial.setTenantId(tenantId);
            if(StringUtils.isEmpty(hmeOperationTimeMaterial.getOperationTimeMaterialId())){
                //新增
                hmeOperationTimeMaterialRepository.insertSelective(hmeOperationTimeMaterial);
                //记录历史
                if(StringUtils.isEmpty(createEventId)){
                    MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                    eventCreateVO.setEventTypeCode("HME_OP_TIME_MATERIAL_CREATE");
                    createEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
                }

                HmeOperationTimeMaterialHis hmeOperationTimeMaterialHis = new HmeOperationTimeMaterialHis();
                BeanUtils.copyProperties(hmeOperationTimeMaterial, hmeOperationTimeMaterialHis);
                hmeOperationTimeMaterialHis.setEventId(createEventId);
                hmeOperationTimeMaterialHisRepository.insertSelective(hmeOperationTimeMaterialHis);
            }else{
                //更新
                HmeOperationTimeMaterial hmeOperationTimeMaterialDb = hmeOperationTimeMaterialRepository.selectByPrimaryKey(hmeOperationTimeMaterial.getOperationTimeMaterialId());
                hmeOperationTimeMaterialDb.setProductionVersionId(hmeOperationTimeMaterial.getProductionVersionId());
                hmeOperationTimeMaterialDb.setEnableFlag(hmeOperationTimeMaterial.getEnableFlag());
                hmeOperationTimeMaterialDb.setMaterialId(hmeOperationTimeMaterial.getMaterialId());
                hmeOperationTimeMaterialDb.setStandardReqdTimeInProcess(hmeOperationTimeMaterial.getStandardReqdTimeInProcess());
                hmeOperationTimeMaterialMapper.updateByPrimaryKey(hmeOperationTimeMaterialDb);
                //记录历史
                if(StringUtils.isEmpty(updateEventId)){
                    MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                    eventCreateVO.setEventTypeCode("HME_OP_TIME_MATERIAL_UPDATE");
                    updateEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
                }

                HmeOperationTimeMaterialHis hmeOperationTimeMaterialHis = new HmeOperationTimeMaterialHis();
                BeanUtils.copyProperties(hmeOperationTimeMaterial, hmeOperationTimeMaterialHis);
                hmeOperationTimeMaterialHis.setEventId(updateEventId);
                hmeOperationTimeMaterialHisRepository.insertSelective(hmeOperationTimeMaterialHis);
            }
        }
        return list;
    }

    @Override
    @ProcessLovValue
    public Page<HmeOperationTimeVO3> queryObject(Long tenantId, HmeOperationTimeDto2 dto, PageRequest pageRequest) {
        Page<HmeOperationTimeVO3> resultPage = PageHelper.doPage(pageRequest, () -> hmeOperationTimeMapper.queryObject(tenantId, dto));
        MtUserInfo mtUserInfo = null;
        for (HmeOperationTimeVO3 hmeOperationTimeVO3 :resultPage) {
            if("EO".equals(hmeOperationTimeVO3.getObjectType())){
                MtEo mtEo = mtEoRepository.selectByPrimaryKey(hmeOperationTimeVO3.getObjectId());
                hmeOperationTimeVO3.setObjectCode(mtEo.getIdentification());
            }else if("WO".equals(hmeOperationTimeVO3.getObjectType())){
                MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(hmeOperationTimeVO3.getObjectId());
                hmeOperationTimeVO3.setObjectCode(mtWorkOrder.getWorkOrderNum());
            }
            mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.valueOf(hmeOperationTimeVO3.getCreatedBy()));
            hmeOperationTimeVO3.setCreatedByName(mtUserInfo.getRealName());
            mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.valueOf(hmeOperationTimeVO3.getLastUpdatedBy()));
            hmeOperationTimeVO3.setLastUpdatedByName(mtUserInfo.getRealName());
        }
        return resultPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeOperationTimeObject> createOrUpdateObject(Long tenantId, List<HmeOperationTimeObject> list) {
        String createEventId = null;
        String updateEventId = null;
        for (HmeOperationTimeObject hmeOperationTimeObject:list) {
            hmeOperationTimeObject.setTenantId(tenantId);
            if(StringUtils.isEmpty(hmeOperationTimeObject.getOperationTimeObjectId())){
                //新增
                hmeOperationTimeObjectRepository.insertSelective(hmeOperationTimeObject);
                //记录历史
                if(StringUtils.isEmpty(createEventId)){
                    MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                    eventCreateVO.setEventTypeCode("HME_OP_TIME_OBJECT_CREATE");
                    createEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
                }

                HmeOperationTimeObjectHis his = new HmeOperationTimeObjectHis();
                his.setEventId(createEventId);
                BeanUtils.copyProperties(hmeOperationTimeObject, his);
                hmeOperationTimeObjectHisRepository.insertSelective(his);
            }else{
                //更新
                HmeOperationTimeObject hmeOperationTimeObjectDb = hmeOperationTimeObjectRepository.selectByPrimaryKey(hmeOperationTimeObject.getOperationTimeObjectId());
                hmeOperationTimeObjectDb.setObjectType(hmeOperationTimeObject.getObjectType());
                hmeOperationTimeObjectDb.setObjectId(hmeOperationTimeObject.getObjectId());
                hmeOperationTimeObjectDb.setStandardReqdTimeInProcess(hmeOperationTimeObject.getStandardReqdTimeInProcess());
                hmeOperationTimeObjectDb.setEnableFlag(hmeOperationTimeObject.getEnableFlag());
                hmeOperationTimeObjectMapper.updateByPrimaryKey(hmeOperationTimeObjectDb);
                //记录历史
                if(StringUtils.isEmpty(updateEventId)){
                    MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                    eventCreateVO.setEventTypeCode("HME_OP_TIME_OBJECT_UPDATE");
                    updateEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
                }

                HmeOperationTimeObjectHis his = new HmeOperationTimeObjectHis();
                his.setEventId(updateEventId);
                BeanUtils.copyProperties(hmeOperationTimeObjectDb, his);
                hmeOperationTimeObjectHisRepository.insertSelective(his);
            }
        }
        return list;
    }

    @Override
    public Page<HmeOperationTimeVO4> queryHis(Long tenantId, HmeOperationTimeDto3 dto, PageRequest pageRequest) {
        Page<HmeOperationTimeVO4> result = PageHelper.doPage(pageRequest, () -> hmeOperationTimeMapper.queryHis(tenantId, dto));
        MtUserInfo mtUserInfo = null;
        for (HmeOperationTimeVO4 hmeOperationTimeVO4:result) {
            mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.valueOf(hmeOperationTimeVO4.getEventBy()));
            hmeOperationTimeVO4.setEventByName(mtUserInfo.getRealName());
        }
        return result;
    }

    @Override
    public Page<HmeOperationTimeVO5> queryMaterialHis(Long tenantId, HmeOperationTimeDto3 dto, PageRequest pageRequest) {
        Page<HmeOperationTimeVO5> result = PageHelper.doPage(pageRequest, () -> hmeOperationTimeMapper.queryMaterialHis(tenantId, dto));
        MtUserInfo mtUserInfo = null;
        for (HmeOperationTimeVO5 hmeOperationTimeVO5:result) {
            mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.valueOf(hmeOperationTimeVO5.getEventBy()));
            hmeOperationTimeVO5.setEventByName(mtUserInfo.getRealName());
        }
        return result;
    }

    @Override
    @ProcessLovValue
    public Page<HmeOperationTimeVO6> queryObjectHis(Long tenantId, HmeOperationTimeDto3 dto, PageRequest pageRequest) {
        Page<HmeOperationTimeVO6> result = PageHelper.doPage(pageRequest, () -> hmeOperationTimeMapper.queryObjectHis(tenantId, dto));
        MtUserInfo mtUserInfo = null;
        for (HmeOperationTimeVO6 hmeOperationTimeVO6:result) {
            if("EO".equals(hmeOperationTimeVO6.getObjectType())){
                MtEo mtEo = mtEoRepository.selectByPrimaryKey(hmeOperationTimeVO6.getObjectId());
                hmeOperationTimeVO6.setObjectCode(mtEo.getIdentification());
            }else if("WO".equals(hmeOperationTimeVO6.getObjectType())){
                MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(hmeOperationTimeVO6.getObjectId());
                hmeOperationTimeVO6.setObjectCode(mtWorkOrder.getWorkOrderNum());
            }
            mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.valueOf(hmeOperationTimeVO6.getEventBy()));
            hmeOperationTimeVO6.setEventByName(mtUserInfo.getRealName());
        }
        return result;
    }
}
