package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeCosCheckBarcodesDTO;
import com.ruike.hme.api.dto.HmeFunctionReportDTO;
import com.ruike.hme.domain.repository.HmeCosCheckBarcodesRepository;
import com.ruike.hme.domain.vo.HmeCosCheckBarcodesVO;
import com.ruike.hme.infra.mapper.HmeCosCheckBarcodesMapper;
import com.ruike.wms.domain.vo.WmsReceiptDetailVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.sys.MtUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * cos目检条码
 *
 * @author li.zhang 2021/01/19 12:27
 */
@Component
public class HmeCosCheckBarcodesRepositoryImpl implements HmeCosCheckBarcodesRepository {

    @Autowired
    HmeCosCheckBarcodesMapper hmeCosCheckBarcodesMapper;

    @Override
    public Page<HmeCosCheckBarcodesVO> selectCheckBarcodes(String tenantId, HmeCosCheckBarcodesDTO dto, PageRequest pageRequest) {
        Page<HmeCosCheckBarcodesVO> page = PageHelper.doPage(pageRequest, () -> hmeCosCheckBarcodesMapper.selectCheckBarcodes(tenantId, dto));
        for (HmeCosCheckBarcodesVO dto4 : page) {
            //旧位置行号替换为字母
            if (StringUtils.isNotBlank(dto4.getRowCloumn())) {
                String[] split = dto4.getRowCloumn().split(",");
                if (split.length != 2) {
                    continue;
                }
                dto4.setRowCloumn((char) (64 + Integer.parseInt(split[0])) +split[1]);
            }
        }
        //List<HmeCosCheckBarcodesVO> list = hmeCosCheckBarcodesMapper.selectCheckBarcodes(tenantId, dto);
        //List<HmeCosCheckBarcodesVO> list1 = hmeCosCheckBarcodesMapper.selectCheckBarcodes2(tenantId, dto);
        //for (HmeCosCheckBarcodesVO a : list1) {
        //    list.add(a);
        //}
        //Page<HmeCosCheckBarcodesVO> page = new Page<HmeCosCheckBarcodesVO>(list, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), list.size());
        return page;
    }
}
