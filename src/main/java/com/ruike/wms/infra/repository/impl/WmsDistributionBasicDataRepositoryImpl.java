package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.api.dto.WmsDistributionBasicDataDTO;
import com.ruike.wms.api.dto.WmsDistributionBasicDataDTO1;
import com.ruike.wms.api.dto.WmsDistributionBasicDataDTO2;
import com.ruike.wms.domain.entity.WmsDistributionBasicDataProductionLine;
import com.ruike.wms.domain.repository.WmsDistributionBasicDataProductionLineRepository;
import com.ruike.wms.domain.vo.WmsDistributionBasicDataVO;
import com.ruike.wms.domain.vo.WmsDistributionBasicDataVO1;
import com.ruike.wms.infra.mapper.WmsDistributionBasicDataMapper;
import com.ruike.wms.infra.mapper.WmsDistributionBasicDataProductionLineMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.wms.domain.entity.WmsDistributionBasicData;
import com.ruike.wms.domain.repository.WmsDistributionBasicDataRepository;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.ruike.wms.infra.constant.WmsConstant.DistributionType.*;

/**
 * 配送基础数据表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2020-07-21 21:05:25
 */
@Component
public class WmsDistributionBasicDataRepositoryImpl extends BaseRepositoryImpl<WmsDistributionBasicData> implements WmsDistributionBasicDataRepository {

    @Autowired
    private WmsDistributionBasicDataProductionLineRepository wmsDisBasDataProdLineRepository;
    @Autowired
    private WmsDistributionBasicDataMapper wmsDistributionBasicDataMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private WmsDistributionBasicDataProductionLineMapper wmsDisBasDataProdLineMapper;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtUserClient mtUserClient;
    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Override
    public Page<WmsDistributionBasicDataVO> query(Long tenantId, WmsDistributionBasicDataDTO2 dto, PageRequest pageRequest) {
        Page<WmsDistributionBasicDataVO> resultPage = PageHelper.doPage(pageRequest, () -> wmsDistributionBasicDataMapper.query(tenantId, dto));
        long number = 1L;
        for (WmsDistributionBasicDataVO wmsDistributionBasicDataVO : resultPage.getContent()) {
            wmsDistributionBasicDataVO.setNumber(pageRequest.getPage() * pageRequest.getSize() + number);
            number++;

            String distributionTypeMeaning = lovAdapter.queryLovMeaning("WMS.DISTRIBUTION", tenantId, wmsDistributionBasicDataVO.getDistributionType());
            wmsDistributionBasicDataVO.setDistributionTypeMeaning(distributionTypeMeaning);

            //产线去重
            List<String> prodLineCodeList = Arrays.asList(wmsDistributionBasicDataVO.getProdLineCodeList().split(","));
            prodLineCodeList = prodLineCodeList.stream().distinct().collect(Collectors.toList());
            wmsDistributionBasicDataVO.setProdLineCodeList(String.join(",",prodLineCodeList));

            WmsDistributionBasicDataProductionLine prodLineRel = new WmsDistributionBasicDataProductionLine();
            prodLineRel.setTenantId(tenantId);
            prodLineRel.setHeaderId(wmsDistributionBasicDataVO.getHeaderId());
            List<WmsDistributionBasicDataProductionLine> lineList = wmsDisBasDataProdLineRepository.select(prodLineRel);
            for(WmsDistributionBasicDataProductionLine line:lineList) {
                MtModProductionLine mtModProductionLine = new MtModProductionLine();
                mtModProductionLine.setProdLineId(line.getProductionLineId());
                mtModProductionLine = mtModProductionLineRepository.selectByPrimaryKey(mtModProductionLine);
                line.setProductionLineCode(mtModProductionLine.getProdLineCode());
                line.setProductionLineName(mtModProductionLine.getProdLineName());
                if (StringUtils.isNotEmpty(line.getWorkcellId())) {
                    MtModWorkcell mtModWorkcell = new MtModWorkcell();
                    mtModWorkcell.setWorkcellId(line.getWorkcellId());
                    mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(mtModWorkcell);
                    line.setWorkcellCode(mtModWorkcell.getWorkcellCode());
                    line.setWorkcellName(mtModWorkcell.getWorkcellName());
                }
            }
            wmsDistributionBasicDataVO.setWmsDistributionBasicDataProductionLines(lineList);
            MtUserInfo userInfo = mtUserClient.userInfoGet(tenantId,wmsDistributionBasicDataVO.getLastUpdatedBy());
            wmsDistributionBasicDataVO.setLastUpdatedByCode(userInfo.getLoginName()+"+"+userInfo.getRealName());
        }
        return resultPage;
    }

    @Override
    public List<WmsDistributionBasicDataVO> dataExport(Long tenantId, WmsDistributionBasicDataDTO2 dto) {
        List<WmsDistributionBasicDataVO> dataVOList = wmsDistributionBasicDataMapper.queryDistributionBasic(tenantId, dto);
        Long number = 1L;
        for (WmsDistributionBasicDataVO wmsDistributionBasicDataVO : dataVOList) {
            wmsDistributionBasicDataVO.setNumber(number);
            number++;
            String distributionTypeMeaning = lovAdapter.queryLovMeaning("WMS.DISTRIBUTION", tenantId, wmsDistributionBasicDataVO.getDistributionType());
            wmsDistributionBasicDataVO.setDistributionTypeMeaning(distributionTypeMeaning);

            List<LovValueDTO> lovValueList = lovAdapter.queryLovValue("WMS.FLAG_YN", tenantId);
            List<LovValueDTO> collect = lovValueList.stream().filter(e -> StringUtils.equals(e.getValue(), wmsDistributionBasicDataVO.getEnableFlag())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                wmsDistributionBasicDataVO.setEnableFlagMeaning(collect.get(0).getMeaning());
            } else {
                wmsDistributionBasicDataVO.setEnableFlagMeaning("");
            }

            List<LovValueDTO> lovValueList1 = lovAdapter.queryLovValue("WMS.FLAG_YN", tenantId);
            List<LovValueDTO> collect1 = lovValueList1.stream().filter(e -> StringUtils.equals(e.getValue(), wmsDistributionBasicDataVO.getBackflushFlag())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect1)) {
                wmsDistributionBasicDataVO.setBackflushFlag(collect1.get(0).getMeaning());
            } else {
                wmsDistributionBasicDataVO.setBackflushFlag("");
            }
        }
        return dataVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Long tenantId, WmsDistributionBasicDataDTO dto) {
        //校验
        if (StringUtils.isNotBlank(dto.getDistributionType())) {
            switch (dto.getDistributionType()) {
                case MIN_MAX:
                    if (dto.getInventoryLevel() == null) {
                        throw new MtException("WMS_DIS_BASIC_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_DIS_BASIC_0002", "WMS"));
                    }
                    break;
                case PROPORTION_DISTRIBUTION:
                    if (dto.getProportion() == null) {
                        throw new MtException("WMS_DIS_BASIC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_DIS_BASIC_0003", "WMS"));
                    }
                    break;
                case PACKAGE_DELIVERY:
                    if (dto.getMinimumPackageQty() == null) {
                        throw new MtException("WMS_DIS_BASIC_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_DIS_BASIC_0004", "WMS"));
                    }
                    break;
                default:
                    break;
            }
        }

        //校验唯一性
        List<WmsDistributionBasicData> basicDataList = wmsDistributionBasicDataMapper.selectByCondition(Condition.builder(WmsDistributionBasicData.class)
                .andWhere(Sqls.custom().andEqualTo(WmsDistributionBasicData.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(WmsDistributionBasicData.FIELD_SITE_ID, dto.getSiteId())
                        .andEqualTo(WmsDistributionBasicData.FIELD_MATERIAL_GROUP_ID, dto.getMaterialGroupId())
                        .andEqualTo(WmsDistributionBasicData.FIELD_MATERIAL_ID, dto.getMaterialId())
                        .andEqualTo(WmsDistributionBasicData.FIELD_MATERIAL_VERSION, dto.getMaterialVersion())
                        .andEqualTo(WmsDistributionBasicData.FIELD_ENABLE_FLAG, dto.getEnableFlag())).build());

        if (basicDataList.size() > 0) {
            throw new MtException("WMS_DIS_BASIC_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_DIS_BASIC_0005", "WMS"));
        }

        WmsDistributionBasicData wmsDistributionBasicData = new WmsDistributionBasicData();
        BeanUtils.copyProperties(dto, wmsDistributionBasicData);
        wmsDistributionBasicData.setTenantId(tenantId);
        this.insertSelective(wmsDistributionBasicData);

        for(WmsDistributionBasicDataDTO1 line:dto.getLineList()){
            //校验唯一性
            List<WmsDistributionBasicDataProductionLine> lineDataList = wmsDisBasDataProdLineMapper.selectByCondition(Condition.builder(WmsDistributionBasicDataProductionLine.class)
                    .andWhere(Sqls.custom().andEqualTo(WmsDistributionBasicDataProductionLine.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(WmsDistributionBasicDataProductionLine.FIELD_HEADER_ID, wmsDistributionBasicData.getHeaderId())
                            .andEqualTo(WmsDistributionBasicDataProductionLine.FIELD_WORKCELL_ID, line.getWorkcellId())
                            .andEqualTo(WmsDistributionBasicDataProductionLine.FIELD_PRODUCTION_LINE_ID, line.getProdLineId())).build());

            if (lineDataList.size()>0) {
                throw new MtException("WMS_DIS_BASIC_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_DIS_BASIC_0005", "WMS"));
            }

            WmsDistributionBasicDataProductionLine prodLineRel = new WmsDistributionBasicDataProductionLine();
            prodLineRel.setTenantId(tenantId);
            prodLineRel.setHeaderId(wmsDistributionBasicData.getHeaderId());
            prodLineRel.setProductionLineId(line.getProdLineId());
            prodLineRel.setWorkcellId(line.getWorkcellId());
            prodLineRel.setBackflushFlag(line.getBackflushFlag());
            prodLineRel.setEnabledFlag(dto.getEnableFlag());
            prodLineRel.setEveryQty(line.getEveryQty());
            wmsDisBasDataProdLineRepository.insertSelective(prodLineRel);
        }
        /*if (StringUtils.isNotBlank(dto.getProdLineIdStr())) {
            List<String> prodList = Arrays.asList(dto.getProdLineIdStr().split(","));

            //去重
            List<String> prodLineIdList = prodList.stream().distinct().collect(Collectors.toList());
            for (String prodLineId : prodLineIdList) {
                WmsDistributionBasicDataProductionLine prodLineRel = new WmsDistributionBasicDataProductionLine();
                prodLineRel.setTenantId(tenantId);
                prodLineRel.setHeaderId(wmsDistributionBasicData.getHeaderId());
                prodLineRel.setProductionLineId(prodLineId);
                prodLineRel.setEnabledFlag(dto.getEnableFlag());
                wmsDisBasDataProdLineRepository.insertSelective(prodLineRel);
            }
        }*/
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long tenantId, WmsDistributionBasicDataVO dto) {
        if (StringUtils.isNotBlank(dto.getDistributionType())) {
            switch (dto.getDistributionType()) {
                case MIN_MAX:
                    if (dto.getInventoryLevel() == null) {
                        throw new MtException("WMS_DIS_BASIC_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_DIS_BASIC_0002", "WMS"));
                    }
                    break;
                case PROPORTION_DISTRIBUTION:
                    if (dto.getProportion() == null) {
                        throw new MtException("WMS_DIS_BASIC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_DIS_BASIC_0003", "WMS"));
                    }
                    break;
                case PACKAGE_DELIVERY:
                    if (dto.getMinimumPackageQty() == null) {
                        throw new MtException("WMS_DIS_BASIC_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_DIS_BASIC_0004", "WMS"));
                    }
                    break;
                default:
                    break;
            }
        }

        WmsDistributionBasicData wmsDistributionBasicData = this.selectByPrimaryKey(dto.getHeaderId());
        //更新头
        BeanUtils.copyProperties(dto, wmsDistributionBasicData);
        //唯一性
        List<WmsDistributionBasicData> basicDataList = wmsDistributionBasicDataMapper.selectByCondition(Condition.builder(WmsDistributionBasicData.class)
                .andWhere(Sqls.custom().andEqualTo(WmsDistributionBasicData.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(WmsDistributionBasicData.FIELD_SITE_ID, dto.getSiteId())
                        .andEqualTo(WmsDistributionBasicData.FIELD_MATERIAL_GROUP_ID, dto.getMaterialGroupId())
                        .andEqualTo(WmsDistributionBasicData.FIELD_MATERIAL_ID, dto.getMaterialId())
                        .andEqualTo(WmsDistributionBasicData.FIELD_MATERIAL_VERSION, dto.getMaterialVersion())
                        .andEqualTo(WmsDistributionBasicData.FIELD_ENABLE_FLAG, dto.getEnableFlag())).build());

        if (basicDataList.size() > 0 && !StringUtils.equals(dto.getHeaderId(), basicDataList.get(0).getHeaderId())) {
            throw new MtException("WMS_DIS_BASIC_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_DIS_BASIC_0005", "WMS"));
        }

        wmsDistributionBasicDataMapper.updateByPrimaryKeySelective(wmsDistributionBasicData);
        //更新行
        /*List<String> prodList = Arrays.asList(dto.getProdLineIdStr().split(","));

        List<String> prodLineIdList = prodList.stream().distinct().collect(Collectors.toList());*/


        List<WmsDistributionBasicDataProductionLine> lines = wmsDisBasDataProdLineRepository.select(new WmsDistributionBasicDataProductionLine() {{
            setTenantId(tenantId);
            setHeaderId(dto.getHeaderId());
        }});

        //更新的行
        List<String> lineList = new ArrayList<>();
        for(WmsDistributionBasicDataProductionLine lineDto:dto.getWmsDistributionBasicDataProductionLines()){
            boolean flag = false;
            for (WmsDistributionBasicDataProductionLine line : lines) {
                if (StringUtils.equals(lineDto.getProductionLineId(), line.getProductionLineId())&&StringUtils.equals(lineDto.getWorkcellId(),line.getWorkcellId())) {
                    flag = true;
                    WmsDistributionBasicDataProductionLine lineObj = wmsDisBasDataProdLineRepository.selectByPrimaryKey(line.getLineId());
                    lineObj.setLineId(line.getLineId());
                    lineObj.setProductionLineId(lineDto.getProductionLineId());
                    lineObj.setWorkcellId(lineDto.getWorkcellId());
                    lineObj.setEveryQty(lineDto.getEveryQty());
                    lineObj.setBackflushFlag(lineDto.getBackflushFlag());
                    lineObj.setEnabledFlag(dto.getEnableFlag());
                    wmsDisBasDataProdLineMapper.updateByPrimaryKeySelective(lineObj);
                    lineList.add(lineObj.getLineId());
                    break;
                }
            }

            if (!flag) {
                WmsDistributionBasicDataProductionLine prodLineRel = new WmsDistributionBasicDataProductionLine();
                prodLineRel.setTenantId(tenantId);
                prodLineRel.setHeaderId(wmsDistributionBasicData.getHeaderId());
                prodLineRel.setProductionLineId(lineDto.getProductionLineId());
                prodLineRel.setWorkcellId(lineDto.getWorkcellId());
                prodLineRel.setEveryQty(lineDto.getEveryQty());
                prodLineRel.setBackflushFlag(lineDto.getBackflushFlag());
                prodLineRel.setEnabledFlag(dto.getEnableFlag());
                wmsDisBasDataProdLineRepository.insertSelective(prodLineRel);
            }
        }
        /*for (String proLine : prodLineIdList) {
            boolean flag = false;
            for (WmsDistributionBasicDataProductionLine line : lines) {
                if (StringUtils.equals(proLine, line.getProductionLineId())) {
                    flag = true;
                    WmsDistributionBasicDataProductionLine lineObj = wmsDisBasDataProdLineRepository.selectByPrimaryKey(line.getLineId());
                    lineObj.setLineId(line.getLineId());
                    lineObj.setProductionLineId(dto.getProdLineId());
                    lineObj.setEnabledFlag(dto.getEnableFlag());
                    msDisBasDataProdLineMapper.updateByPrimaryKeySelective(line);
                    lineList.add(line.getLineId());
                    break;
                }
            }

            if (!flag) {
                WmsDistributionBasicDataProductionLine prodLineRel = new WmsDistributionBasicDataProductionLine();
                prodLineRel.setTenantId(tenantId);
                prodLineRel.setHeaderId(wmsDistributionBasicData.getHeaderId());
                prodLineRel.setProductionLineId(proLine);
                prodLineRel.setEnabledFlag(dto.getEnableFlag());
                wmsDisBasDataProdLineRepository.insertSelective(prodLineRel);
            }
        }*/
        if (CollectionUtils.isEmpty(lineList)) {
            wmsDisBasDataProdLineRepository.batchDelete(lines);
        } else {
            String lineStr = StringUtils.join(lineList);

            List<WmsDistributionBasicDataProductionLine> collect = lines.stream().filter(f -> !lineStr.contains(f.getLineId())).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(collect)) {
                wmsDisBasDataProdLineRepository.batchDelete(collect);
            }
        }
    }
}
