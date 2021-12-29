package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeDataCollectHeader;
import com.ruike.hme.domain.entity.HmeDataCollectLine;
import com.ruike.hme.domain.repository.HmeDataCollectLineRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeDataCollectHeaderMapper;
import com.ruike.hme.infra.mapper.HmeDataCollectLineMapper;
import com.ruike.hme.infra.mapper.HmeNcCheckMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import com.ruike.hme.infra.util.CommonUtils;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.repository.HmeDataCollectHeaderRepository;
import org.hzero.mybatis.domian.Condition;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.domain.vo.MtWkcShiftVO3;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.infra.mapper.MtMaterialMapper;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.vo.MtModWorkcellVO2;
import tarzan.modeling.infra.mapper.MtModWorkcellMapper;

import java.math.BigDecimal;
import java.util.*;

/**
 * 生产数据采集头表 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2020-07-16 19:35:58
 */
@Component
public class HmeDataCollectHeaderRepositoryImpl extends BaseRepositoryImpl<HmeDataCollectHeader> implements HmeDataCollectHeaderRepository {

    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;

    @Autowired
    private HmeDataCollectHeaderMapper hmeDataCollectHeaderMapper;

    @Autowired
    private HmeDataCollectLineMapper hmeDataCollectLineMapper;

    @Autowired
    private MtModWorkcellMapper mtModWorkcellMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeDataCollectLineRepository hmeDataCollectLineRepository;

    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;

    @Autowired
    private MtMaterialMapper mtMaterialMapper;

    @Autowired
    private HmeNcCheckMapper hmeNcCheckMapper;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeDataCollectLineVO3 queryDataCollectLineList(Long tenantId, HmeDataCollectLineVO lineVo) {
        //获取用户及站点信息
        Long userId = DetailsHelper.getUserDetails().getUserId();
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);

        //校验工位Id
        if (StringUtils.isBlank(lineVo.getWorkcellId())) {
            throw new MtException("HME_WO_DISPATCH_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_DISPATCH_0001", "HME", "workcellId", "【API:querySnMaterialQty】"));
        }

        MtModWorkcellVO2 mtModWorkcellVo = mtModWorkcellMapper.selectWorkcellById(tenantId, lineVo.getWorkcellId());

        //校验产品序列号
        if (StringUtils.isBlank(lineVo.getDataRecordCode())) {
            throw new MtException("HME_DATA_COLLECT_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_DATA_COLLECT_003", "HME"));
        }
        //获取工艺Id
        if (StringUtils.isBlank(lineVo.getOperationId())) {
            throw new MtException("HME_DATA_COLLECT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_DATA_COLLECT_002", "HME"));
        }

        HmeDataCollectLineVO3 lineVo3 = new HmeDataCollectLineVO3();

        //校验是否做过数据采集
        Condition condition = new Condition(HmeDataCollectHeader.class);
        condition.and().andEqualTo("dataRecordCode", lineVo.getDataRecordCode())
                .andEqualTo("workcellId", lineVo.getWorkcellId())
                .andEqualTo("tenantId", tenantId);
        List<HmeDataCollectHeader> headerList = hmeDataCollectHeaderMapper.selectByCondition(condition);
        if (CollectionUtils.isNotEmpty(headerList)) {
            //做过 直接进行查询
            List<HmeDataCollectLineVO2> hmeDataCollectLineVo2List = hmeDataCollectHeaderMapper.queryDataCollectLineList(tenantId, headerList.get(0).getCollectHeaderId());
            BeanUtils.copyProperties(headerList.get(0), lineVo3);
            MtMaterial mtMaterial = mtMaterialMapper.selectByPrimaryKey(headerList.get(0).getMaterialId());
            lineVo3.setMaterialCode(mtMaterial != null ? mtMaterial.getMaterialCode() : "");
            lineVo3.setCollectFlag("1");
            lineVo3.setLineContent(hmeDataCollectLineVo2List);
            lineVo3.setWorkCellName(mtModWorkcellVo != null ? mtModWorkcellVo.getWorkcellName() : "");
            return lineVo3;
        }

        //没有 进行采集
        //条码是物料批 查询编码及数量采集
        MtMaterialLot mtMaterialLot = hmeDataCollectHeaderMapper.queryMaterialLotInfoByCode(tenantId, lineVo.getDataRecordCode());

        HmeDataCollectHeader collectHeader = new HmeDataCollectHeader();
        collectHeader.setTenantId(tenantId);
        collectHeader.setDataRecordCode(lineVo.getDataRecordCode());
        collectHeader.setOperationId(lineVo.getOperationId());
        collectHeader.setWorkcellId(lineVo.getWorkcellId());
        //获取当前班次Id
        String lineWorkcellId = hmeDataCollectHeaderMapper.queryLineWorkcellIdByWorkCellId(tenantId, lineVo.getWorkcellId());
        String shiftId = "";
        if (StringUtils.isNotBlank(lineWorkcellId)) {
            MtWkcShiftVO3 mtWkcShiftVo3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, lineWorkcellId);
            shiftId = Optional.ofNullable(mtWkcShiftVo3.getWkcShiftId()).orElse("");
        }
        collectHeader.setShiftId(shiftId);
        collectHeader.setSiteInBy(userId);
        collectHeader.setRemark(lineVo.getRemark());
        collectHeader.setSiteInDate(new Date());
        if(mtMaterialLot != null){
            //头表插入
            collectHeader.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            collectHeader.setQty(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));
            collectHeader.setMaterialId(mtMaterialLot.getMaterialId());
            self().insertSelective(collectHeader);

            //行表插入
            hmeDataCollectLineRepository.insetDataCollectLineMsg(tenantId, lineVo.getWorkcellId(), collectHeader.getCollectHeaderId(), lineVo.getOperationId(), shiftId, mtMaterialLot.getMaterialId(),defaultSiteId);
        }

        if (StringUtils.isNotBlank(lineVo.getMaterialId()) && lineVo.getPrimaryUomQty() != null) {
            //头表插入
            collectHeader.setMaterialId(lineVo.getMaterialId());
            collectHeader.setQty(lineVo.getPrimaryUomQty());
            self().insertSelective(collectHeader);

            //行表插入
            hmeDataCollectLineRepository.insetDataCollectLineMsg(tenantId, lineVo.getWorkcellId(), collectHeader.getCollectHeaderId(), lineVo.getOperationId(), shiftId, lineVo.getMaterialId(),defaultSiteId);
        }
        lineVo3.setLineContent(Collections.emptyList());
        lineVo3.setDataRecordCode(lineVo.getDataRecordCode());
        lineVo3.setQty(lineVo.getPrimaryUomQty());
        lineVo3.setCollectFlag("0");
        lineVo3.setMaterialId(lineVo.getMaterialId());
        if (collectHeader.getCollectHeaderId() != null) {
            lineVo3.setLineContent(hmeDataCollectHeaderMapper.queryDataCollectLineList(tenantId, collectHeader.getCollectHeaderId()));
            BeanUtils.copyProperties(collectHeader, lineVo3);
            MtMaterial mtMaterial = mtMaterialMapper.selectByPrimaryKey(collectHeader.getMaterialId());
            lineVo3.setMaterialCode(mtMaterial != null ? mtMaterial.getMaterialCode() : "");
            lineVo3.setCollectFlag("1");
            lineVo3.setWorkCellName(mtModWorkcellVo != null ? mtModWorkcellVo.getWorkcellName() : "");
        }
        return lineVo3;
    }


    @Override
    public Map<String, Object> querySnMaterialQty(Long tenantId, String materialId) {
        //检验参数
        if (StringUtils.isBlank(materialId)) {
            throw new MtException("HME_WO_DISPATCH_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_DISPATCH_0001", "HME", "materialId", "【API:querySnMaterialQty】"));
        }

        //当前系统登录人默认站点id
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());

        String materialType = hmeDataCollectHeaderMapper.querySnMaterialType(tenantId, defaultSiteId, materialId);

        //SN物料标识 0-否 1-是
        Map<String, Object> result = new HashMap<>(4);
        result.put("snFlag", "0");
        if (StringUtils.equals(materialType, HmeConstants.ConstantValue.SN)) {
            result.put("snFlag", "1");
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDataCollectLineInfo(Long tenantId, HmeDataCollectLineVO2 lineVo) {
        //检验数据
        if (StringUtils.isBlank(lineVo.getCollectLineId())) {
            throw new MtException("HME_WO_DISPATCH_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_DISPATCH_0001", "HME", "collectLineId", "【API:updateDataCollectLineInfo】"));
        }

        HmeDataCollectLine hmeDataCollectLine = hmeDataCollectLineMapper.selectByPrimaryKey(lineVo.getCollectLineId());

        //更新物料编码与结果
        hmeDataCollectLine.setMaterialId(lineVo.getMaterialId());

        //结果 大小值存在 超出范围不允许保存
        if (StringUtils.isNotBlank(lineVo.getResult())) {
            if (hmeDataCollectLine.getMinimumValue() != null && hmeDataCollectLine.getMaximalValue() != null) {
                if (CommonUtils.isNumeric(lineVo.getResult())) {
                    BigDecimal resultData = BigDecimal.valueOf(Double.valueOf(lineVo.getResult()));

                    int minIndex = resultData.compareTo(hmeDataCollectLine.getMinimumValue());

                    int maxIndex = resultData.compareTo(hmeDataCollectLine.getMaximalValue());

                    if (minIndex < 0 || maxIndex > 0) {
                        throw new MtException("HME_DATA_COLLECT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_DATA_COLLECT_001", "HME"));
                    }
                } else {
                    throw new MtException("HME_DATA_COLLECT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_DATA_COLLECT_004", "HME"));
                }
            }
            hmeDataCollectLine.setResult(lineVo.getResult());
        }

        Long userId = DetailsHelper.getUserDetails().getUserId();
        hmeDataCollectLine.setLastUpdateDate(new Date());
        hmeDataCollectLine.setLastUpdatedBy(userId);
        hmeDataCollectLineMapper.updateByPrimaryKeySelective(hmeDataCollectLine);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDataCollectHeaderInfo(Long tenantId, HmeDataCollectLineVO lineVO) {
        //检验数据
        if (StringUtils.isBlank(lineVO.getCollectHeaderId())) {
            throw new MtException("HME_WO_DISPATCH_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_DISPATCH_0001", "HME", "collectHeaderId", "【API:updateDataCollectHeaderInfo】"));
        }

        HmeDataCollectHeader hmeDataCollectHeader = hmeDataCollectHeaderMapper.selectByPrimaryKey(lineVO.getCollectHeaderId());

        hmeDataCollectHeader.setRemark(lineVO.getRemark());

        Long userId = DetailsHelper.getUserDetails().getUserId();
        hmeDataCollectHeader.setSiteOutDate(new Date());
        hmeDataCollectHeader.setSiteOutBy(userId);

        hmeDataCollectHeaderMapper.updateByPrimaryKeySelective(hmeDataCollectHeader);
    }

    @Override
    public HmeDataCollectLineVO4 workcellCodeScan(Long tenantId, String workcellCode) {
        //检验参数
        if (StringUtils.isBlank(workcellCode)) {
            throw new MtException("HME_WO_DISPATCH_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_DISPATCH_0001", "HME", "workcellCode", "【API:updateDataCollectHeaderInfo】"));
        }
        HmeDataCollectLineVO4 hmeDataCollectLineVo4 = hmeDataCollectHeaderMapper.workcellCodeScan(tenantId, workcellCode);
        if (hmeDataCollectLineVo4 == null) {
            throw new MtException("HME_EO_JOB_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_001", "HME"));
        }
        //获取用户站点信息
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());

        //返回工位所对应的工艺列表
        hmeDataCollectLineVo4.setOperationList(hmeDataCollectHeaderMapper.queryOperationIdByWorkcellId(tenantId, defaultSiteId, hmeDataCollectLineVo4.getWorkcellId()));

        //根据工位找产线
        String proLineId = hmeNcCheckMapper.queryProLineByWorkcellId(tenantId, hmeDataCollectLineVo4.getWorkcellId());
        if (StringUtils.isBlank(proLineId)) {
            throw new MtException("HME_EO_JOB_SN_040", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_040", "HME"));
        }else{
            MtModProductionLine mtModProductionLine = mtModProductionLineRepository
                    .prodLineBasicPropertyGet(tenantId, proLineId);
            hmeDataCollectLineVo4.setProdLineId(mtModProductionLine.getProdLineId());
            hmeDataCollectLineVo4.setProdLineName(mtModProductionLine.getProdLineName());
        }
        return hmeDataCollectLineVo4;
    }
}
