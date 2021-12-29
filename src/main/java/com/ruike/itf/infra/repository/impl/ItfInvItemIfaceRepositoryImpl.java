package com.ruike.itf.infra.repository.impl;

import com.ibm.icu.text.SimpleDateFormat;
import com.ruike.itf.api.dto.RfcParamDTO;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.itf.domain.entity.ItfInvItemIface;
import com.ruike.itf.domain.repository.ItfInvItemIfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 物料接口表 资源库实现
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
@Component
public class ItfInvItemIfaceRepositoryImpl extends BaseRepositoryImpl<ItfInvItemIface> implements ItfInvItemIfaceRepository {

    @Autowired
    private LovAdapter lovAdapter;

    @Override
    public RfcParamDTO getSapParams(Long tenantId, Map<String, String> map) {
        // 获取默认配置时间
        RfcParamDTO dto = new RfcParamDTO();
        String defaultTimeStr = lovAdapter.queryLovMeaning("WMS.INTERFACE_TIME", tenantId, "DEFAULT_TIME");
        int defaultTime = Integer.parseInt(StringUtils.isNotEmpty(defaultTimeStr) ? defaultTimeStr : "0");
        DateTimeFormatter fm = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        LocalDate date = now.plusDays(-1 * defaultTime);
        String dateTo = now.format(fm);
        String dateFrom = date.format(fm);
        String plantCode = null;
        if (map.size() != 0) {
            plantCode = map.get("plantCode");
        }
        dto.setDateFrom(dateFrom);
        dto.setDateTo(dateTo);
        dto.setPlantCode(plantCode);
        return dto;
    }
}
