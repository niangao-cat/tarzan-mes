package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeSnReplaceDTO;
import com.ruike.hme.domain.entity.HmeNewOldSnRel;
import com.ruike.hme.domain.repository.HmeSnReplaceRepository;
import com.ruike.hme.domain.vo.HmeSnReplaceVO;
import com.ruike.hme.infra.mapper.HmeNewOldSnRelMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.util.MtBaseConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.entity.MtMaterialLotHis;
import tarzan.inventory.domain.repository.MtMaterialLotHisRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.vo.MtEoVO;
import tarzan.order.domain.vo.MtEoVO38;
import tarzan.order.domain.vo.MtEoVO39;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * HmeSnReplaceRepositoryImpl
 *
 * @author: chaonan.hu@hand-china.com 2020-11-03 22:29:34
 **/
@Component
public class HmeSnReplaceRepositoryImpl implements HmeSnReplaceRepository {

    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private MtMaterialLotHisRepository mtMaterialLotHisRepository;
    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;
    @Autowired
    private HmeNewOldSnRelMapper hmeNewOldSnRelMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSnReplaceData(Long tenantId, HmeSnReplaceVO dto) {
        //创建事件
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("EO_SN_UPDATE");
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        String updateEventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        List<MtMaterialLotVO20> mtMaterialLotVO20List = dto.getMtMaterialLotVO20List();
        List<MtEoVO> eoMessageList = dto.getEoMessageList();
        //调用API{materialLotBatchUpdate}更新物料批
        mtMaterialLotRepository.materialLotBatchUpdate(tenantId, mtMaterialLotVO20List, eventId, MtBaseConstants.NO);
        //调用API{eoUpdate}更新eo 因eoBatchUpdateAPI不满足此种需求，故使用单个更新
        if(CollectionUtils.isNotEmpty(eoMessageList)){
            for (MtEoVO mtEoVO:eoMessageList) {
                mtEoVO.setEventId(eventId);
                mtEoRepository.eoUpdate(tenantId, mtEoVO, MtBaseConstants.NO);
            }
        }
        //因materialLotBatchUpdate不会去更新materialLotCode,故最后自己手动更新下
        for (MtMaterialLotVO20 mtMaterialLotVO20:mtMaterialLotVO20List) {
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(mtMaterialLotVO20.getMaterialLotId());
            mtMaterialLot.setMaterialLotCode(mtMaterialLotVO20.getMaterialLotCode());
            mtMaterialLotRepository.updateByPrimaryKeySelective(mtMaterialLot);
            //记录历史
            MtMaterialLotHis mtMaterialLotHis = new MtMaterialLotHis();
            mtMaterialLotHis.setEventId(updateEventId);
            BeanUtils.copyProperties(mtMaterialLot, mtMaterialLotHis);
            mtMaterialLotHisRepository.insertSelective(mtMaterialLotHis);
        }

        //新旧SN存入表hme_new_old_sn_rel，只存code,不按照ID来存
        List<HmeSnReplaceDTO> dtoList = dto.getHmeSnReplaceDTOList();
        if (CollectionUtils.isNotEmpty(dtoList)) {
            List<HmeNewOldSnRel> list = new ArrayList<>();
            Date nowDate = new Date();
            List<String> ifaceIdList = mtCustomDbRepository.getNextKeys("hme_new_old_sn_rel_s", dtoList.size());
            String batchId = mtCustomDbRepository.getNextKey("hme_new_old_sn_rel_cid_s");
            int ifaceIdIndex = 0;
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails() .getUserId() : -1L;
            for (HmeSnReplaceDTO data :dtoList) {
                HmeNewOldSnRel itf = new HmeNewOldSnRel();
                itf.setNewMaterialLotCode(data.getNewMaterialLotCode());
                itf.setOldMaterialLotCode(data.getOldMaterialLotCode());
                itf.setNewOldSnRelId(ifaceIdList.get(ifaceIdIndex++));
                itf.setCid(Long.valueOf(batchId));
                itf.setTenantId(tenantId);
                itf.setObjectVersionNumber(1L);
                itf.setCreatedBy(userId);
                itf.setCreationDate(nowDate);
                itf.setLastUpdatedBy(userId);
                itf.setLastUpdateDate(nowDate);
                list.add(itf);
            }
            if(CollectionUtils.isNotEmpty(list)) {
                this.myBatchInsert(list); // 批量将传入的数据插入关系表
            }
        }
    }

    /**
     *@description 批量新增
     *@author wenzhang.yu@hand-china.com
     *@date 2020/10/7 16:42
     *@param insertList
     *@return void
     **/
    public void myBatchInsert(List<HmeNewOldSnRel> insertList) {
        if (CollectionUtils.isNotEmpty(insertList)) {
            List<List<HmeNewOldSnRel>> splitSqlList = InterfaceUtils.splitSqlList(insertList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeNewOldSnRel> domains : splitSqlList) {
                hmeNewOldSnRelMapper.insertIface(domains);
            }

        }
    }
}
