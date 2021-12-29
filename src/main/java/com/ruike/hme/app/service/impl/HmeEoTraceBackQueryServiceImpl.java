package com.ruike.hme.app.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeEoTraceBackQueryService;
import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.entity.HmeQuantityAnalyzeLine;
import com.ruike.hme.domain.repository.HmeEoJobDataRecordRepository;
import com.ruike.hme.domain.repository.HmeMaterialTransferRepository;
import com.ruike.hme.domain.vo.HmeDataRecordVO;
import com.ruike.hme.domain.vo.HmeEoTraceBackExportVO;
import com.ruike.hme.domain.vo.HmeEoTraceBackQueryVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEoTraceBackQueryMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import com.ruike.wms.infra.barcode.CommonPdfTemplateUtil;
import com.ruike.wms.infra.barcode.GetFileCharset;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.*;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.BaseConstants;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO2;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 产品追溯应用服务实现
 *
 * @author jiangling.zheng@hand-china.com 2020-04-21 13:18
 */
@Slf4j
@Service
public class HmeEoTraceBackQueryServiceImpl implements HmeEoTraceBackQueryService {

    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;

    @Autowired
    private HmeEoTraceBackQueryMapper hmeEoTraceBackQueryMapper;

    @Autowired
    private MtUserClient userClient;

    @Autowired
    private MtUserRepository mtUserRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeEoJobDataRecordRepository hmeEoJobDataRecordRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtTagRepository mtTagRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private HmeMaterialTransferRepository hmeMaterialTransferRepository;


    @Override
    @ProcessLovValue
    public List<HmeEoTraceBackQueryDTO> eoWorkcellQuery(Long tenantId, HmeEoTraceBackQueryDTO4 dto) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        // 获取当前用户站点信息
        String siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        if (StringUtils.isEmpty(siteId)) {
            return null;
        }
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getEoIdentification());
        }});
        String topEoId = "";
        if (mtMaterialLot != null) {
            // 20210322 add by sanfeng.zhang for fang.pan 判断条码是否存在to_top_id
            List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setAttrName("TOP_EO_ID");
                setTableName("mt_material_lot_attr");
                setKeyId(mtMaterialLot.getMaterialLotId());
            }});
            topEoId = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
        }
        List<HmeEoTraceBackQueryDTO> dtoList = new ArrayList<>();
        if (StringUtils.isBlank(topEoId)) {
            // 查询工序流转
            dtoList = hmeEoTraceBackQueryMapper.eoWorkcellQuery(tenantId, siteId, dto);
        } else {
            // 存在topEoId 则根据topEoId找出所有的EoId 查询工序流转
            dtoList = hmeEoTraceBackQueryMapper.eoWorkcellQueryByReWork(tenantId, siteId, dto);
        }
        // 20210915 add by sanfeng.zhang for wenxin.zhang 增加生产数据采集
        List<HmeEoTraceBackQueryDTO> dataCollectList = hmeEoTraceBackQueryMapper.dataCollectQuery(tenantId, dto);
        if (CollectionUtils.isNotEmpty(dataCollectList)) {
            dtoList.addAll(dataCollectList);
            dtoList = dtoList.stream().sorted(Comparator.comparing(HmeEoTraceBackQueryDTO::getSiteInDate)).collect(Collectors.toList());
        }
        LinkedHashMap<String, List<HmeEoTraceBackQueryDTO>> dtoListMap = dtoList.stream().collect(Collectors.groupingBy(HmeEoTraceBackQueryDTO::getEoId, LinkedHashMap::new, Collectors.toList()));
        for (Map.Entry<String, List<HmeEoTraceBackQueryDTO>> dtoListEntry : dtoListMap.entrySet()) {
            List<HmeEoTraceBackQueryDTO> lineList = dtoListEntry.getValue();
            long lineNum = 0L;
            for (HmeEoTraceBackQueryDTO workcell : lineList) {
                lineNum = lineNum + 1L;
                workcell.setLineNum(lineNum);
                workcell.setCreateUserName(userClient.userInfoGet(tenantId, workcell.getCreatedBy()).getRealName());
                workcell.setOperatorUserName(userClient.userInfoGet(tenantId, workcell.getOperatorId()).getRealName());
                //加工时长
                if (workcell.getSiteInDate() != null && workcell.getSiteOutDate() != null) {
                    long time = workcell.getSiteOutDate().getTime() - workcell.getSiteInDate().getTime();
                    long min = 1000 * 60;
                    BigDecimal processTime = BigDecimal.valueOf(time).divide(BigDecimal.valueOf(min), 2, BigDecimal.ROUND_HALF_UP);
                    workcell.setProcessTime(processTime);
                }
                //返修标识
                if (StringUtils.isNotEmpty(workcell.getIsReworkFlag())) {
                    String isRemark = lovAdapter.queryLovMeaning("WMS.FLAG_YN", tenantId, workcell.getIsReworkFlag());
                    workcell.setIsRework(isRemark);
                }
                // 异常出站标识
                if (StringUtils.isNotEmpty(workcell.getExceptionFlag())) {
                    String exceptionFlag = lovAdapter.queryLovMeaning("WMS.FLAG_YN", tenantId, workcell.getExceptionFlag());
                    workcell.setExceptionFlagMeaning(exceptionFlag);
                }
                //查询不良信息点击标识
                Long count = hmeEoTraceBackQueryMapper.ncInfoFlagQuery(tenantId, workcell.getWorkcellId(), workcell.getEoId());
                if (count > 0) {
                    workcell.setNcInfoFlag(true);
                } else {
                    workcell.setNcInfoFlag(false);
                }
            }
        }
        return dtoList;
    }

    @Override
    public HmeEoTraceBackQueryDTO5 eoWorkcellDetailQuery(Long tenantId, String workcellId, String eoId, String jobId, String collectHeaderId) {
        HmeEoTraceBackQueryDTO5 dto = new HmeEoTraceBackQueryDTO5();
        dto.setMaterialList(eoMaterialQuery(tenantId, workcellId, jobId));
        dto.setJobDataList(eoJobDataQuery(tenantId, workcellId, jobId, collectHeaderId));
        return dto;
    }

    @Override
    public List<HmeEoTraceBackQueryDTO2> eoMaterialQuery(Long tenantId, String workcellId, String jobId) {
        // 20210915 add by sanfeng.zhang for wenxin.zhang 作业类型为DATA_COLLECTION 即jobId为空 不返回结果
        if (StringUtils.isBlank(jobId)) {
            return Collections.EMPTY_LIST;
        }
        List<HmeEoTraceBackQueryDTO2> dtoList = hmeEoTraceBackQueryMapper.eoMaterialQuery(tenantId, workcellId, jobId);
        long lineNum = 0L;
        for (HmeEoTraceBackQueryDTO2 materialDto : dtoList) {
            lineNum = lineNum + 1L;
            materialDto.setLineNum(lineNum);
            if (StringUtils.isNotEmpty(materialDto.getMaterialLotCode())) {
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                    setTenantId(tenantId);
                    setMaterialLotCode(materialDto.getMaterialLotCode());
                }});
                if (mtMaterialLot != null) {
                    materialDto.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    //2020-10-10 add by chaonan.hu for can.wang 增加供应商批次返回
                    //供应商批次
                    List<MtExtendAttrVO> supplierLotList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
                        setMaterialLotId(mtMaterialLot.getMaterialLotId());
                        setAttrName("SUPPLIER_LOT");
                    }});
                    if (CollectionUtils.isNotEmpty(supplierLotList) && StringUtils.isNotEmpty(supplierLotList.get(0).getAttrValue())) {
                        materialDto.setSupplierLot(supplierLotList.get(0).getAttrValue());
                    }
                }
            }
        }
        return dtoList;
    }

    @Override
    public List<HmeEoTraceBackQueryDTO3> eoJobDataQuery(Long tenantId, String workcellId, String jobId, String collectHeaderId) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        // 获取当前用户站点信息
        String siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        if (StringUtils.isEmpty(siteId)) {
            return null;
        }
        // 20210915 add by sanfeng.zhang for wenxin.zhang 作业类型为DATA_COLLECTION 即jobId为空 查询行
        List<HmeEoTraceBackQueryDTO3> dtoList = null;
        if (StringUtils.isBlank(jobId)) {
            dtoList = hmeEoTraceBackQueryMapper.dataCollectJobDataQuery(tenantId, collectHeaderId);
        } else {
            // 查询工序流转
            dtoList = hmeEoTraceBackQueryMapper.eoJobDataQuery(tenantId, siteId, workcellId, jobId);
        }
        List<LovValueDTO> lovValueDTOList = lovAdapter.queryLovValue("HME.COUPLING_SINGLE_STATUS", tenantId);
        long lineNum = 0L;
        for (HmeEoTraceBackQueryDTO3 jobDto : dtoList) {
            lineNum = lineNum + 1L;
            List<LovValueDTO> valueDTOList = lovValueDTOList.stream().filter(e -> StringUtils.equals(jobDto.getCosStatus(), e.getValue())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(valueDTOList)) {
                jobDto.setCosStatusMeaning(valueDTOList.get(0).getMeaning());
            }
            jobDto.setLineNum(lineNum);
        }
        return dtoList;
    }

    @Override
    public List<HmeEoTraceBackQueryDTO7> productComponentQuery(Long tenantId, HmeEoTraceBackQueryDTO6 dto) {
        if (StringUtils.isEmpty(dto.getMaterialLotCode())) {
            throw new MtException("HME_EO_TRACE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_TRACE_0001", "HME"));
        }
        List<HmeEoTraceBackQueryDTO7> resultList = new ArrayList<>();
        if (HmeConstants.ConstantValue.YES.equals(dto.getParentType())) {
            //如果标识为Y,则代表查询最顶层
            resultList = hmeEoTraceBackQueryMapper.productComponentTopQuery(tenantId, dto);
        } else {
            //查询顶层时，根据录入的SN条码查询；查询下层数据时，根据当前层的序列号查询
            //判断扫描条码上是否绑定了eo
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                setTenantId(tenantId);
                setMaterialLotCode(dto.getMaterialLotCode());
            }});
            if (mtMaterialLot != null && StringUtils.isNotEmpty(mtMaterialLot.getEoId())) {
                //找到eo的情况
                resultList = hmeEoTraceBackQueryMapper.productComponentQuery2(tenantId, mtMaterialLot.getSiteId(), mtMaterialLot.getEoId());
            } else {
                //找不到eo的情况
                resultList = hmeEoTraceBackQueryMapper.productComponentQuery(tenantId, dto);
            }
        }
        for (HmeEoTraceBackQueryDTO7 hmeEoTraceBackQueryDTO7 : resultList) {
            if (StringUtils.isEmpty(hmeEoTraceBackQueryDTO7.getMaterialLotCode())) {
                hmeEoTraceBackQueryDTO7.setMaterialLotCode(dto.getMaterialLotCode());
            }
            //数量转换
            if (StringUtils.isNotEmpty(hmeEoTraceBackQueryDTO7.getReleaseQtyStr())) {
                double releaseQtyD = Double.parseDouble(hmeEoTraceBackQueryDTO7.getReleaseQtyStr());
                hmeEoTraceBackQueryDTO7.setReleaseQty((int) releaseQtyD);
            } else {
                hmeEoTraceBackQueryDTO7.setReleaseQty(0);
            }
            //根据当前层的materialLotCode获取下层标识
            if (StringUtils.isEmpty(hmeEoTraceBackQueryDTO7.getMaterialLotCode())) {
                hmeEoTraceBackQueryDTO7.setChildren(false);
            } else {
                //当查询下层数据时，如果当前层的materialLotCode与父层的materialLotCode相同，则下层标识为false,以免循环展示数据
                if (!HmeConstants.ConstantValue.YES.equals(dto.getParentType()) &&
                        dto.getMaterialLotCode().equals(hmeEoTraceBackQueryDTO7.getMaterialLotCode())) {
                    hmeEoTraceBackQueryDTO7.setChildren(false);
                } else {
                    //判断当前层的materialLotCode是否绑定了eo
                    MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                        setTenantId(tenantId);
                        setMaterialLotCode(hmeEoTraceBackQueryDTO7.getMaterialLotCode());
                    }});
                    if (mtMaterialLot != null && StringUtils.isNotEmpty(mtMaterialLot.getEoId())) {
                        //找到eo的情况
                        Long count = hmeEoTraceBackQueryMapper.substrateFlagQuery2(tenantId, mtMaterialLot.getEoId());
                        if (count > 0) {
                            hmeEoTraceBackQueryDTO7.setChildren(true);
                        } else {
                            hmeEoTraceBackQueryDTO7.setChildren(false);
                        }
                    } else {
                        //找不到eo的情况
                        Long count = hmeEoTraceBackQueryMapper.substrateFlagQuery(tenantId, hmeEoTraceBackQueryDTO7.getMaterialLotCode());
                        if (count > 0) {
                            hmeEoTraceBackQueryDTO7.setChildren(true);
                        } else {
                            hmeEoTraceBackQueryDTO7.setChildren(false);
                        }
                    }
                }
            }
        }
        return resultList;
    }

    @Override
    @ProcessLovValue
    public List<HmeEoTraceBackQueryDTO8> equipmentQuery(Long tenantId, HmeEoTraceBackQueryDTO dto) {
        if (StringUtils.isBlank(dto.getJobId())) {
            return Collections.EMPTY_LIST;
        }
        List<HmeEoTraceBackQueryDTO8> resultList = hmeEoTraceBackQueryMapper.equipmentQuery(tenantId, dto.getWorkcellId(), dto.getJobId());
        Long number = 1L;
        for (HmeEoTraceBackQueryDTO8 hmeEoTraceBackQueryDTO8 : resultList) {
            hmeEoTraceBackQueryDTO8.setNumber(number);
            number++;
        }
        return resultList;
    }

    @Override
    public List<HmeEoTraceBackQueryDTO9> exceptionInfoQuery(Long tenantId, HmeEoTraceBackQueryDTO dto) {
        List<HmeEoTraceBackQueryDTO9> resultList = hmeEoTraceBackQueryMapper.exceptionInfoQuery(tenantId, dto.getWorkcellId(), dto.getEoId());
        Long number = 1L;
        MtUserInfo mtUserInfo = null;
        for (HmeEoTraceBackQueryDTO9 hmeEoTraceBackQueryDTO9 : resultList) {
            hmeEoTraceBackQueryDTO9.setNumber(number);
            number++;
            mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.parseLong(hmeEoTraceBackQueryDTO9.getCreatedBy()));
            hmeEoTraceBackQueryDTO9.setCreatedByName(mtUserInfo.getRealName());
            mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.parseLong(hmeEoTraceBackQueryDTO9.getRespondedBy()));
            hmeEoTraceBackQueryDTO9.setRespondedByName(mtUserInfo.getRealName());
        }
        return resultList;
    }

    @Override
    @ProcessLovValue
    public List<HmeEoTraceBackQueryDTO10> ncInfoQuery(Long tenantId, HmeEoTraceBackQueryDTO dto) {
        List<HmeEoTraceBackQueryDTO10> resultList = hmeEoTraceBackQueryMapper.ncInfoQuery(tenantId, dto.getWorkcellId(), dto.getEoId());
        List<MtGenType> ncCodeList = mtGenTypeRepository.select(new MtGenType() {{
            setTenantId(tenantId);
            setModule("NC_CODE");
            setTypeGroup("NC");
        }});
        List<MtGenStatus> ncRecordStatusList = mtGenStatusRepository.select(new MtGenStatus() {{
            setTenantId(tenantId);
            setModule("NC_RECORD");
            setStatusGroup("NC_RECORD_STATUS");
        }});
        for (HmeEoTraceBackQueryDTO10 hmeEoTraceBackQueryDTO10 : resultList) {
            //不良分类含义 关联类型组NC
            Optional<MtGenType> ncCodeOpt = ncCodeList.stream().filter(ncCode -> StringUtils.equals(ncCode.getTypeCode(), hmeEoTraceBackQueryDTO10.getNcType())).findFirst();
            if (ncCodeOpt.isPresent()) {
                hmeEoTraceBackQueryDTO10.setNcTypeMeaning(ncCodeOpt.get().getDescription());
            }
            //不良状态含义 关联状态组NC_RECORD_STATUS
            Optional<MtGenStatus> ncRecodeStatusOpt = ncRecordStatusList.stream().filter(nrs -> StringUtils.equals(nrs.getStatusCode(), hmeEoTraceBackQueryDTO10.getNcStatus())).findFirst();
            if (ncRecodeStatusOpt.isPresent()) {
                hmeEoTraceBackQueryDTO10.setNcStatusMeaning(ncRecodeStatusOpt.get().getDescription());
            }
        }
        return resultList;
    }

    @Override
    public Page<HmeEoTraceBackQueryVO> reverseTrace(Long tenantId, String materialLotCode, PageRequest pageRequest) {
        Page<HmeEoTraceBackQueryVO> result = new Page<>();
        List<String> jobIdList = hmeEoTraceBackQueryMapper.getJobId(tenantId, materialLotCode);
        if (CollectionUtils.isNotEmpty(jobIdList)) {
            result = PageHelper.doPageAndSort(pageRequest, () -> hmeEoTraceBackQueryMapper.reverseTrace(tenantId, jobIdList));
            for (HmeEoTraceBackQueryVO hmeEoTraceBackQueryVO : result) {
                hmeEoTraceBackQueryVO.setMaterialLotCode(materialLotCode);
                if (StringUtils.isEmpty(hmeEoTraceBackQueryVO.getCurrentSn())) {
                    hmeEoTraceBackQueryVO.setCurrentSn(hmeEoTraceBackQueryVO.getFeedSn());
                }
            }
        }
        return result;
    }

    @Override
    public void reportPrint(Long tenantId, String eoIdentification, HttpServletResponse response) throws Exception {
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(eoIdentification);
        }});
        MtMaterial mtMaterial = new MtMaterial();
        if (mtMaterialLot != null) {
            mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtMaterialLot.getMaterialId());
        }
        MtEo mtEo = mtEoRepository.selectOne(new MtEo() {{
            setIdentification(eoIdentification);
            setTenantId(tenantId);
        }});
        if (mtEo == null) {
            // 该产品未进行入库检验,请确认!
            throw new MtException("RK_QMS_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "RK_QMS_0020", "QMS"));
        }
        List<HmeEoJobDataRecord> hmeEoJobDataRecordList = hmeEoJobDataRecordRepository.selectByCondition(Condition
                .builder(HmeEoJobDataRecord.class).andWhere(Sqls.custom()
                        .andEqualTo(HmeEoJobDataRecord.FIELD_EO_ID, mtEo.getEoId())).build());
        Map<@NotBlank String, List<HmeEoJobDataRecord>> tagIdMap = hmeEoJobDataRecordList.stream().collect(Collectors.groupingBy(HmeEoJobDataRecord::getTagId));
        // 获取检验结果值集 在值集中存在的则显示
        List<LovValueDTO> tagLovList = lovAdapter.queryLovValue("QMS.OQC_INSPECTION_REPORT", tenantId);
        // 符合条件的检验项
        List<HmeDataRecordVO> printRecordList = new ArrayList<>();
        List<String> tagIdList = hmeEoJobDataRecordList.stream().map(HmeEoJobDataRecord::getTagId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<MtTag> mtTagList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tagIdList)) {
            mtTagList = mtTagRepository.selectByCondition(Condition.builder(MtTag.class)
                    .andWhere(Sqls.custom().andEqualTo(MtTag.FIELD_TENANT_ID, tenantId)
                            .andIn(MtTag.FIELD_TAG_ID, tagIdList)).build());
        }
        // 根据检验项编码进行分组
        Map<String, List<MtTag>> tagObjMap = mtTagList.stream().collect(Collectors.groupingBy(dto -> dto.getTagCode()));

        // 检验项单位
        List<String> uomIdList = mtTagList.stream().map(MtTag::getUnit).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<MtUom> mtUomList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(uomIdList)) {
            mtUomList = mtUomRepository.selectByCondition(Condition.builder(MtUom.class)
                    .andWhere(Sqls.custom().andEqualTo(MtUom.FIELD_TENANT_ID, tenantId)
                            .andIn(MtUom.FIELD_UOM_ID, uomIdList)).build());
        }
        Map<String, List<MtUom>> uomMap = mtUomList.stream().collect(Collectors.groupingBy(dto -> dto.getUomId()));
        // 20210329 add by sanfeng.zhang for hui.ma 检验人修改
        Long userId = null;
        for (LovValueDTO valueDTO : tagLovList) {
            List<MtTag> tagList = tagObjMap.get(valueDTO.getValue());
            if (CollectionUtils.isNotEmpty(tagList)) {
                // 在值集存在 则显示数据
                MtTag mtTag = tagList.get(0);
                List<HmeEoJobDataRecord> recordList = tagIdMap.get(mtTag.getTagId());
                if (CollectionUtils.isNotEmpty(recordList)) {
                    HmeDataRecordVO recordVO = new HmeDataRecordVO();
                    recordVO.setTagCode(valueDTO.getValue());
                    recordVO.setTagName(valueDTO.getMeaning());
                    recordVO.setRemark(recordList.get(0).getRemark());
                    recordVO.setResult(recordList.get(0).getResult());
                    recordVO.setTagAlias(valueDTO.getDescription());
                    recordVO.setTarget(valueDTO.getTag());
                    List<MtUom> uomList = uomMap.get(mtTag.getUnit());
                    recordVO.setUomCode(CollectionUtils.isNotEmpty(uomList) ? uomList.get(0).getUomCode() : "");
                    printRecordList.add(recordVO);
                    if (userId == null) {
                        userId = recordList.get(0).getLastUpdatedBy();
                    }
                }
            }
        }

        //确定根目录
        String systemPath = System.getProperty("user.dir");
        String classUrl = this.getClass().getResource("/").getPath();
        log.info("<==== HmeEoTraceBackQueryServiceImpl.reportPrint System path :: {}", systemPath);
        log.info("<==== HmeEoTraceBackQueryServiceImpl.reportPrint class path :: {}", classUrl);
        String basePath = classUrl + "/templates";
        String uuid = UUID.randomUUID().toString();
        if (!new File(classUrl).exists()) {
            File file = new File(systemPath + "/templates");
            if (!file.exists()) {
                if (!file.mkdir()) {
                    throw new MtException("创建临时文件夹失败!");
                }
            }
            basePath = systemPath + "/templates";
        } else {
            basePath = classUrl + "/templates";
        }
        String ruikePath = basePath + "/" + "img/raycus.png";
        String pdfFileName = uuid + ".pdf";
        String pdfPath = basePath + "/" + pdfFileName;
        //组装参数 ----   业务说按照排序来取值
        Map<String, Object> imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);

        MtUserInfo mtUserInfo = new MtUserInfo();
        if (userId != null) {
            mtUserInfo = userClient.userInfoGet(tenantId, userId);
        }
        imgMap.put("ruikeImage", ruikePath);
        Map<String, Object> formMap = new HashMap<>(24);
        formMap.put("materialName", mtMaterial.getMaterialName());
        formMap.put("eoIdentification", eoIdentification);
        Long index = 0L;
        for (HmeDataRecordVO recordVO : printRecordList) {
            formMap.put("serialNumber" + index, index + 1);
            formMap.put("tag" + index, recordVO.getTagName());
            formMap.put("tagEg" + index, recordVO.getTagAlias());
            formMap.put("target" + index, recordVO.getTarget());
            formMap.put("target" + index, recordVO.getTarget());
            formMap.put("unit" + index, recordVO.getUomCode());
            formMap.put("result" + index, recordVO.getResult());
            index++;
        }
        formMap.put("createdBy", ObjectUtils.isEmpty(mtUserInfo) ? "" : mtUserInfo.getRealName());
        Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        param.put("formMap", formMap);
        param.put("imgMap", imgMap);
        List<String> pdfTempList = new ArrayList<>();
        pdfTempList.add(basePath + "/hme_laser_inspection_report.pdf");
        pdfTempList.add(basePath + "/hme_laser_inspection_report_annex.pdf");
        //生成PDF
        try {
            log.info("<==== 生成PDF准备数据:{}", pdfPath);
            this.multiplePagePdf(pdfTempList, pdfPath, param);
            log.info("<==== 生成PDF完成！{}", pdfPath);
        } catch (Exception e) {
            log.error("<==== <==== HmeEoTraceBackQueryServiceImpl.reportPrint.generatePDFFile Error", e);
            throw new MtException(e.getMessage());
        }


        //将文件转化成流进行输出
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File pdfFile = new File(pdfPath);
        try {
            //设置相应参数
            response.setHeader("Content-Length", String.valueOf(pdfFile.length()));
            response.setHeader("Content-Disposition", "attachment;filename=" + uuid + ".pdf");
            String encoding = new GetFileCharset().guestFileEncoding(pdfFile);
            if (org.apache.commons.lang.StringUtils.isNotEmpty(encoding)) {
                response.setCharacterEncoding(encoding);
            }

            //将文件转化成流进行输出
            bis = new BufferedInputStream(new FileInputStream(pdfPath));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            log.error("<==== HmeEoTraceBackQueryServiceImpl.reportPrint.outputPDFFile Error", e);
            throw new MtException("Exception", e.getMessage());
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                log.error("<==== HmeEoTraceBackQueryServiceImpl.reportPrint.closeIO Error", e);
            }
        }
        //删除临时文件
        if (!pdfFile.delete()) {
            log.info("<==== HmeEoTraceBackQueryServiceImpl.reportPrint.pdfFile Failed: {}", pdfPath);
        }
    }

    /**
     * @description 打印校验
     * @param tenantId
     * @param eoIdentification
     * @param response
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2020/12/7
     * @time 17:36
     * @version 0.0.1
     * @return void
     */
    @Override
    public void reportPrintCheck(Long tenantId, String eoIdentification, HttpServletResponse response) {

        MtEo mtEo = mtEoRepository.selectOne(new MtEo() {{
            setIdentification(eoIdentification);
            setTenantId(tenantId);
        }});
        if (ObjectUtils.isEmpty(mtEo)) {
            // 该产品未进行入库检验,请确认!
            throw new MtException("RK_QMS_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "RK_QMS_0020", "QMS"));
        }
        // 查询数据 ---- 只会存在一条  防止万一用list
        List<String> tagGroupIdList = hmeEoTraceBackQueryMapper.queryTagGroupIdByDesc(mtEo.getEoId());
        if (CollectionUtils.isEmpty(tagGroupIdList)) {
            // 该产品未进行入库检验,请确认!
            throw new MtException("RK_QMS_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "RK_QMS_0020", "QMS"));
        }

    }

    @Override
    public List<HmeQuantityAnalyzeLine> quantityAnalyzeQuery(Long tenantId, String materialLotCode) {
        MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, materialLotCode);
        if (Objects.isNull(materialLot)) {
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        return hmeEoTraceBackQueryMapper.quantityAnalyzeQuery(tenantId, materialLot.getMaterialLotId());
    }

    @Override
    @ProcessLovValue
    public List<HmeEoTraceBackExportVO> eoWorkcellExport(Long tenantId, HmeEoTraceBackQueryDTO4 queryDTO4) {
        // 工序流转信息
        List<HmeEoTraceBackQueryDTO> eoWorkcellQueryList = this.eoWorkcellQuery(tenantId, queryDTO4);
        List<HmeEoTraceBackExportVO> exportVOList = new ArrayList<>();
        // 异常信息
        List<HmeEoTraceBackQueryDTO9> exceptionInfoList = this.batchExceptionInfoQuery(tenantId, eoWorkcellQueryList);
        Map<String, List<HmeEoTraceBackQueryDTO9>> exceptionInfoMap = exceptionInfoList.stream().collect(Collectors.groupingBy(dto -> dto.getWorkcellId() + "_" + dto.getEoId()));

        List<HmeEoTraceBackQueryDTO> filterJobDataList = eoWorkcellQueryList.stream().filter(vo -> StringUtils.isNotBlank(vo.getJobId())).collect(Collectors.toList());
        List<HmeEoTraceBackQueryDTO3> jobDataList = new ArrayList<>();
        List<HmeEoTraceBackQueryDTO8> equipmentList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(filterJobDataList)) {
            jobDataList = this.batchQueryJobDataList(tenantId, eoWorkcellQueryList);
            equipmentList = hmeEoTraceBackQueryMapper.batchEquipmentQuery(tenantId, filterJobDataList);
        }
        Map<String, List<HmeEoTraceBackQueryDTO3>> jobDataMap = jobDataList.stream().collect(Collectors.groupingBy(dto -> dto.getWorkcellId() + "_" + dto.getJobId(), LinkedHashMap::new, Collectors.toList()));
        LinkedHashMap<String, List<HmeEoTraceBackQueryDTO8>> equipmentMap = equipmentList.stream().collect(Collectors.groupingBy(dto -> dto.getWorkcellId() + "_" + dto.getJobId(), LinkedHashMap::new, Collectors.toList()));
        List<String> collectHeaderIdList = eoWorkcellQueryList.stream().map(HmeEoTraceBackQueryDTO::getCollectHeaderId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<HmeEoTraceBackQueryDTO3> dataCollectJobDataList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(collectHeaderIdList)) {
            dataCollectJobDataList = hmeEoTraceBackQueryMapper.batchDataCollectJobDataQuery(tenantId, collectHeaderIdList);
        }
        Map<String, List<HmeEoTraceBackQueryDTO3>> dataCollectJobDataMap = dataCollectJobDataList.stream().collect(Collectors.groupingBy(HmeEoTraceBackQueryDTO3::getCollectHeaderId));
        BeanCopier copier = BeanCopier.create(HmeEoTraceBackQueryDTO.class, HmeEoTraceBackExportVO.class, false);
        Long exceptionIndex = 0L;
        Long jobDataIndex = 0L;
        Long equipmentIndex = 0L;
        Long eoMaterialIndex = 0L;
        List<LovValueDTO> eqStatusList = lovAdapter.queryLovValue("HME.STATION_EQ_STATUS", tenantId);
        List<LovValueDTO> cosStatusList = lovAdapter.queryLovValue("HME.COUPLING_SINGLE_STATUS", tenantId);
        for (HmeEoTraceBackQueryDTO queryDTO : eoWorkcellQueryList) {
            HmeEoTraceBackExportVO exportVO = new HmeEoTraceBackExportVO();
            copier.copy(queryDTO, exportVO, null);
            // 工艺质量
            List<HmeEoTraceBackQueryDTO3> jobDatas = null;
            List<HmeEoTraceBackQueryDTO8> equipments = new ArrayList<>();
            List<HmeEoTraceBackQueryDTO2> eoMaterialList = new ArrayList<>();
            if (StringUtils.isNotBlank(queryDTO.getJobId())) {
                jobDatas = jobDataMap.getOrDefault(queryDTO.getWorkcellId() + "_" + queryDTO.getJobId(), Collections.EMPTY_LIST);
                equipments = equipmentMap.getOrDefault(queryDTO.getWorkcellId() + "_" + queryDTO.getJobId(), Collections.EMPTY_LIST);
                // 物料
                eoMaterialList = this.eoMaterialQuery(tenantId, queryDTO.getWorkcellId(), queryDTO.getJobId());
                if (CollectionUtils.isNotEmpty(eoMaterialList)) {
                    for (HmeEoTraceBackQueryDTO2 eoMaterial : eoMaterialList) {
                        eoMaterial.setLineNum(++eoMaterialIndex);
                        eoMaterial.setHeaderLineNum(queryDTO.getLineNum());
                        eoMaterial.setParentWorkcellName(queryDTO.getParentWorkcellName());
                        eoMaterial.setJobTypeMeaning(queryDTO.getJobTypeMeaning());
                        eoMaterial.setWorkcellName(queryDTO.getWorkcellName());
                        eoMaterial.setSiteInDate(queryDTO.getSiteInDate());
                        eoMaterial.setSiteOutDate(queryDTO.getSiteOutDate());
                        eoMaterial.setProcessTime(queryDTO.getProcessTime());
                        eoMaterial.setCreateUserName(queryDTO.getCreateUserName());
                        eoMaterial.setNcInfoFlagMeaning(queryDTO.getNcInfoFlag() ? "是" : "否");
                        eoMaterial.setIsRework(queryDTO.getIsRework());
                        eoMaterial.setExceptionFlagMeaning(queryDTO.getExceptionFlagMeaning());
                    }
                }
            } else {
                jobDatas = dataCollectJobDataMap.getOrDefault(queryDTO.getCollectHeaderId(), Collections.EMPTY_LIST);
            }
            exportVO.setMaterialList(eoMaterialList);
            if (CollectionUtils.isNotEmpty(jobDatas)) {
                for (HmeEoTraceBackQueryDTO3 jobData : jobDatas) {
                    if (StringUtils.isNotBlank(jobData.getCosStatus())) {
                        Optional<LovValueDTO> firstOpt = cosStatusList.stream().filter(cos -> StringUtils.equals(cos.getValue(), jobData.getCosStatus())).findFirst();
                        if (firstOpt.isPresent()) {
                            jobData.setCosStatusMeaning(firstOpt.get().getMeaning());
                        }
                    }
                    jobData.setLineNum(++jobDataIndex);
                    jobData.setHeaderLineNum(queryDTO.getLineNum());
                    jobData.setHeaderProcessName(queryDTO.getParentWorkcellName());
                    jobData.setJobTypeMeaning(queryDTO.getJobTypeMeaning());
                    jobData.setHeaderWorkcellName(queryDTO.getWorkcellName());
                    jobData.setSiteInDate(queryDTO.getSiteInDate());
                    jobData.setSiteOutDate(queryDTO.getSiteOutDate());
                    jobData.setProcessTime(queryDTO.getProcessTime());
                    jobData.setCreateUserName(queryDTO.getCreateUserName());
                    jobData.setNcInfoFlagMeaning(queryDTO.getNcInfoFlag() ? "是" : "否");
                    jobData.setIsRework(queryDTO.getIsRework());
                    jobData.setExceptionFlagMeaning(queryDTO.getExceptionFlagMeaning());
                }
            }
            exportVO.setJobDataList(jobDatas);
            // 设备
            if (CollectionUtils.isNotEmpty(equipments)) {
                for (HmeEoTraceBackQueryDTO8 equipment : equipments) {
                    Optional<LovValueDTO> statusOpt = eqStatusList.stream().filter(lov -> StringUtils.equals(lov.getValue(), equipment.getEquipmentStatus())).findFirst();
                    equipment.setEquipmentStatusMeaning(statusOpt.isPresent() ? statusOpt.get().getMeaning() : "");
                    equipment.setNumber(++equipmentIndex);
                    equipment.setLineNum(queryDTO.getLineNum());
                    equipment.setParentWorkcellName(queryDTO.getParentWorkcellName());
                    equipment.setJobTypeMeaning(queryDTO.getJobTypeMeaning());
                    equipment.setWorkcellName(queryDTO.getWorkcellName());
                    equipment.setSiteInDate(queryDTO.getSiteInDate());
                    equipment.setSiteOutDate(queryDTO.getSiteOutDate());
                    equipment.setProcessTime(queryDTO.getProcessTime());
                    equipment.setCreateUserName(queryDTO.getCreateUserName());
                    equipment.setNcInfoFlagMeaning(queryDTO.getNcInfoFlag() ? "是" : "否");
                    equipment.setIsRework(queryDTO.getIsRework());
                    equipment.setExceptionFlagMeaning(queryDTO.getExceptionFlagMeaning());
                }
            }
            exportVO.setEquipmentList(equipments);
            // 异常信息
            List<HmeEoTraceBackQueryDTO9> exceptionInfos = exceptionInfoMap.get(queryDTO.getWorkcellId() + "_" + queryDTO.getEoId());
            if (CollectionUtils.isNotEmpty(exceptionInfos)) {
                for (HmeEoTraceBackQueryDTO9 exceptionInfo : exceptionInfos) {
                    exceptionInfo.setNumber(++exceptionIndex);
                    exceptionInfo.setLineNum(queryDTO.getLineNum());
                    exceptionInfo.setParentWorkcellName(queryDTO.getParentWorkcellName());
                    exceptionInfo.setJobTypeMeaning(queryDTO.getJobTypeMeaning());
                    exceptionInfo.setWorkcellName(queryDTO.getWorkcellName());
                    exceptionInfo.setSiteInDate(queryDTO.getSiteInDate());
                    exceptionInfo.setSiteOutDate(queryDTO.getSiteOutDate());
                    exceptionInfo.setProcessTime(queryDTO.getProcessTime());
                    exceptionInfo.setCreateUserName(queryDTO.getCreateUserName());
                    exceptionInfo.setNcInfoFlagMeaning(queryDTO.getNcInfoFlag() ? "是" : "否");
                    exceptionInfo.setIsRework(queryDTO.getIsRework());
                    exceptionInfo.setExceptionFlagMeaning(queryDTO.getExceptionFlagMeaning());
                }
            }
            exportVO.setExceptionInfoList(exceptionInfos);
            exportVOList.add(exportVO);
        }
        return exportVOList;
    }

    private List<HmeEoTraceBackQueryDTO9> batchExceptionInfoQuery(Long tenantId, List<HmeEoTraceBackQueryDTO> eoWorkcellQueryList) {
        if (CollectionUtils.isEmpty(eoWorkcellQueryList)) {
            return Collections.EMPTY_LIST;
        }
        List<HmeEoTraceBackQueryDTO9> resultList = hmeEoTraceBackQueryMapper.batchExceptionInfoQuery(tenantId, eoWorkcellQueryList);
        MtUserInfo mtUserInfo = null;
        for (HmeEoTraceBackQueryDTO9 hmeEoTraceBackQueryDTO9:resultList) {
            mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.parseLong(hmeEoTraceBackQueryDTO9.getCreatedBy()));
            hmeEoTraceBackQueryDTO9.setCreatedByName(mtUserInfo.getRealName());
            mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.parseLong(hmeEoTraceBackQueryDTO9.getRespondedBy()));
            hmeEoTraceBackQueryDTO9.setRespondedByName(mtUserInfo.getRealName());
        }
        return resultList;
    }

    private List<HmeEoTraceBackQueryDTO3> batchQueryJobDataList (Long tenantId, List<HmeEoTraceBackQueryDTO> eoWorkcellQueryList) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        // 获取当前用户站点信息
        String siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        if (StringUtils.isEmpty(siteId)) {
            return Collections.EMPTY_LIST;
        }
        // 查询工序流转
        return hmeEoTraceBackQueryMapper.batchEoJobDataQuery(tenantId, siteId, eoWorkcellQueryList);
    }


    /**
     * 两个pdf合成一个
     *
     * @param templatePathList pdf模板地址
     * @param targetPdfPath    目标地址
     * @param param            数据
     */
    private void multiplePagePdf(List<String> templatePathList, String targetPdfPath, Map<String, Object> param) {
        if (CollectionUtils.isEmpty(templatePathList) || param == null || org.springframework.util.StringUtils.isEmpty(targetPdfPath)) {
            return;
        }
        List<PdfReader> pageList = new ArrayList();
        for (String templatePath : templatePathList) {
            pageList.add(CommonPdfTemplateUtil.generateSinglePdf(templatePath, param));
        }
        Document document = new Document();
        FileOutputStream out = null;
        PdfCopy copy = null;
        try {
            // 输出流
            out = new FileOutputStream(targetPdfPath);
            copy = new PdfCopy(document, out);
            document.open();
            for (int k = 0; k < pageList.size(); k++) {
                document.newPage();
                PdfImportedPage importPage = copy.getImportedPage(pageList.get(k), 1);
                copy.addPage(importPage);
            }

        } catch (Exception e) {
            log.error("<==== PdfTemplateUtil multiplePagePdf exception:{}:{}", e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (copy != null) {
                copy.close();
            }
            if (document != null) {
                document.close();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
