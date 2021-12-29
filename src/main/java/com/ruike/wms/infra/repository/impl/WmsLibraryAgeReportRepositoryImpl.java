package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.api.dto.WmsLibraryAgeReportDTO;
import com.ruike.wms.api.dto.WmsLibraryAgeReportDTO2;
import com.ruike.wms.api.dto.WmsLibraryAgeReportDTO3;
import com.ruike.wms.domain.repository.WmsLibraryAgeReportRepository;
import com.ruike.wms.domain.vo.WmsLibraryAgeReportVO;
import com.ruike.wms.domain.vo.WmsLibraryAgeReportVO2;
import com.ruike.wms.domain.vo.WmsLibraryAgeReportVO3;
import com.ruike.wms.domain.vo.WmsLibraryAgeReportVO4;
import com.ruike.wms.infra.mapper.WmsLibraryAgeReportMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 库龄报表资源库实现
 *
 * @author: chaonan.hu@hand-china.com 2020-11-18 14:24:34
 **/
@Component
public class WmsLibraryAgeReportRepositoryImpl implements WmsLibraryAgeReportRepository {

    @Autowired
    private WmsLibraryAgeReportMapper wmsLibraryAgeReportMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private LovAdapter lovAdapter;

    @Override
    @ProcessLovValue
    public Page<WmsLibraryAgeReportVO> libraryAgeReportQuery(Long tenantId, WmsLibraryAgeReportDTO dto, PageRequest pageRequest) {
        if(Objects.nonNull(dto.getBeyondDateTo())){
            String beyondDateToStr = dto.getBeyondDateTo().toString();
            beyondDateToStr = beyondDateToStr + " 23:59:59";
            dto.setFinalBeyondDateTo(DateUtil.string2Date(beyondDateToStr, "yyyy-MM-dd HH:mm:ss"));
        }
        Page<WmsLibraryAgeReportVO> pageResult = PageHelper.doPage(pageRequest, () -> wmsLibraryAgeReportMapper.libraryAgeReportQuery(tenantId, dto));
        if(CollectionUtils.isNotEmpty(pageResult.getContent())){
            for (WmsLibraryAgeReportVO wmsLibraryAgeReportVO:pageResult.getContent()) {
                if(Objects.nonNull(wmsLibraryAgeReportVO.getBeyondDay()) && wmsLibraryAgeReportVO.getBeyondDay().compareTo(BigDecimal.ZERO) < 1){
                    wmsLibraryAgeReportVO.setBeyondDay(null);
                }
            }
        }
        return pageResult;
    }

    @Override
    public Page<WmsLibraryAgeReportVO2> libraryAgeGroupQuery(Long tenantId, WmsLibraryAgeReportDTO2 dto, PageRequest pageRequest) {
        Page<WmsLibraryAgeReportVO2> pageResult = PageHelper.doPage(pageRequest, () -> wmsLibraryAgeReportMapper.libraryAgeGroupQuery(tenantId, dto));
        List<String> libraryAgeList = dto.getLibraryAgeList();
        if(CollectionUtils.isNotEmpty(pageResult.getContent())){
            List<WmsLibraryAgeReportDTO3> libraryAgeReportDTO3List = new ArrayList<>();
            for (String libraryAge:libraryAgeList) {
                String[] split = libraryAge.split("-");
                if(split.length != 2){
                    throw new MtException("WMS_LIBRARY_AGE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_LIBRARY_AGE_0001", "WMS"));
                }
                WmsLibraryAgeReportDTO3 wmsLibraryAgeReportDTO3 = new WmsLibraryAgeReportDTO3();
                wmsLibraryAgeReportDTO3.setMinLibraryAge(Long.valueOf(split[0]));
                wmsLibraryAgeReportDTO3.setMaxLibraryAge(Long.valueOf(split[1]));
                libraryAgeReportDTO3List.add(wmsLibraryAgeReportDTO3);
            }
            for (WmsLibraryAgeReportVO2 result:pageResult) {
                List<WmsLibraryAgeReportVO3> libraryAgeResultList = new ArrayList<>();
                for (int i = 0; i < libraryAgeReportDTO3List.size(); i++) {
                    WmsLibraryAgeReportVO3 wmsLibraryAgeReportVO3 = new WmsLibraryAgeReportVO3();
                    BigDecimal qtySum = wmsLibraryAgeReportMapper.qtySumQuery(tenantId, result.getMaterialId(), result.getLot(),
                            result.getLocatorId(), libraryAgeReportDTO3List.get(i));
                    wmsLibraryAgeReportVO3.setTitle(libraryAgeList.get(i));
                    wmsLibraryAgeReportVO3.setValue(qtySum);
                    libraryAgeResultList.add(wmsLibraryAgeReportVO3);
                }
                result.setLibraryAgeList(libraryAgeResultList);
            }
        }
        return pageResult;
    }

    @Override
    public List<WmsLibraryAgeReportVO4> libraryAgeExport(Long tenantId, WmsLibraryAgeReportDTO dto) {
        if(Objects.nonNull(dto.getBeyondDateTo())){
            String beyondDateToStr = dto.getBeyondDateTo().toString();
            beyondDateToStr = beyondDateToStr + " 23:59:59";
            dto.setFinalBeyondDateTo(DateUtil.string2Date(beyondDateToStr, "yyyy-MM-dd HH:mm:ss"));
        }
        List<WmsLibraryAgeReportVO4> wmsLibraryAgeReportVOS = wmsLibraryAgeReportMapper.libraryAgeExportQuery(tenantId, dto);
        if(CollectionUtils.isNotEmpty(wmsLibraryAgeReportVOS)){
            List<LovValueDTO> statusLov = lovAdapter.queryLovValue("Z.MTLOT.STATUS.G", tenantId);
            List<LovValueDTO> qualityStatusLov = lovAdapter.queryLovValue("WMS.MTLOT.QUALITY_STATUS", tenantId);
            for (WmsLibraryAgeReportVO4 wmsLibraryAgeReportVO:wmsLibraryAgeReportVOS) {
                if(Objects.nonNull(wmsLibraryAgeReportVO.getBeyondDay()) && wmsLibraryAgeReportVO.getBeyondDay().compareTo(BigDecimal.ZERO) < 1){
                    wmsLibraryAgeReportVO.setBeyondDay(null);
                }
                if(StringUtils.isNotEmpty(wmsLibraryAgeReportVO.getStatus())){
                    for (LovValueDTO status:statusLov) {
                        if(status.getValue().equals(wmsLibraryAgeReportVO.getStatus())){
                            wmsLibraryAgeReportVO.setStatusMeaning(status.getMeaning());
                            break;
                        }
                    }
                }
                if(StringUtils.isNotEmpty(wmsLibraryAgeReportVO.getQualityStatus())){
                    for (LovValueDTO qualityStatus:qualityStatusLov) {
                        if(qualityStatus.getValue().equals(wmsLibraryAgeReportVO.getQualityStatus())){
                            wmsLibraryAgeReportVO.setQualityStatusMeaning(qualityStatus.getMeaning());
                            break;
                        }
                    }
                }
            }
        }
        return wmsLibraryAgeReportVOS;
    }
}
