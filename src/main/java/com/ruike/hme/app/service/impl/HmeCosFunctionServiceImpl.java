package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeCosFunctionHeadDTO;
import com.ruike.hme.api.dto.HmeFunctionExportDTO;
import com.ruike.hme.api.dto.HmeFunctionReportDTO;
import com.ruike.hme.app.service.HmeCosFunctionService;
import com.ruike.hme.domain.entity.HmeCosFunction;
import com.ruike.hme.domain.repository.HmeCosFunctionRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtUserInfo;

import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 芯片性能表应用服务默认实现
 *
 * @author wenzhang.yu@hand-china.com 2020-08-07 15:08:16
 */
@Service
public class HmeCosFunctionServiceImpl implements HmeCosFunctionService {

    @Autowired
    private HmeCosFunctionRepository hmeCosFunctionRepository;

    @Autowired
    private MtUserRepository mtUserRepository;

    @Override
    public Page<HmeCosFunctionHeadDTO> cosFunctionHeadQuery(Long tenantId, HmeCosFunctionHeadDTO dto, PageRequest pageRequest) {
        Page<HmeCosFunctionHeadDTO> result = PageHelper.doPage(pageRequest, () -> hmeCosFunctionRepository.cosFunctionHeadQuery(tenantId, dto));
        for (HmeCosFunctionHeadDTO dto4 : result) {
            //旧位置行号替换为字母
            if (StringUtils.isNotBlank(dto4.getRowCloumn())) {
                String[] split = dto4.getRowCloumn().split(",");
                if (split.length != 2) {
                    continue;
                }
                dto4.setRowCloumn((char) (64 + Integer.parseInt(split[0])) +split[1]);
            }
        }
        return result;
    }

    @Override
    public Page<HmeCosFunction> cosFunctionQuery(Long tenantId, String loadSequence, PageRequest pageRequest) {
        HmeCosFunction hmeCosFunction = new HmeCosFunction();
        hmeCosFunction.setLoadSequence(loadSequence);
        return PageHelper.doPage(pageRequest, () -> hmeCosFunctionRepository.select(hmeCosFunction));
    }

    @Override
    @ProcessLovValue
    public Page<HmeFunctionReportDTO> cosFunctionReport(Long tenantId, HmeCosFunctionHeadDTO dto, PageRequest pageRequest) {
        Page<HmeFunctionReportDTO> result = PageHelper.doPage(pageRequest, () -> hmeCosFunctionRepository.cosFunctionReport(tenantId,dto));
        for (HmeFunctionReportDTO dto4 : result) {
            //旧位置行号替换为字母
            if (StringUtils.isNotBlank(dto4.getRowCloumn())) {
                String[] split = dto4.getRowCloumn().split(",");
                if (split.length != 2) {
                    continue;
                }
                dto4.setRowCloumn((char) (64 + Integer.parseInt(split[0])) +split[1]);
            }
            if (StringUtils.isNotBlank(dto4.getA25())) {
                MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.parseLong(dto4.getA25()));
                dto4.setUserName(mtUserInfo.getRealName());
            }
        }
        return result;
    }

    @Override
    @ProcessLovValue
    public List<HmeFunctionExportDTO> exportDetail(Long tenantId, ExportParam exportParam, HmeCosFunctionHeadDTO dto) {
        List<HmeFunctionReportDTO> list = hmeCosFunctionRepository.cosFunctionReport(tenantId,dto);
        List<HmeFunctionExportDTO> result = new ArrayList<>(list.size());
        BeanCopier copier = BeanCopier.create(HmeFunctionReportDTO.class, HmeFunctionExportDTO.class, false);
        list.forEach(src -> {
            HmeFunctionExportDTO temp = new HmeFunctionExportDTO();
            copier.copy(src, temp, null);
            result.add(temp);
        });
        return result;
    }
    
}
