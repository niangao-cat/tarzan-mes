package com.ruike.reports.domain.repository;

import com.ruike.reports.api.dto.HmeEquipmentWorkingDTO;
import com.ruike.reports.domain.vo.HmeEquipmentWorkingVO;
import com.ruike.reports.domain.vo.HmeEquipmentWorkingVO4;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/01/15 10:21
 */

public interface HmeEquipmentWorkingRepository {

    /**
     * 查询页列表
     *
     * @param tenantId    租户
     * @param dto         查询条件
     * @param pageRequest 分页参数
     * @return HmeEquipmentWorkingVO
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-03-17 16:03
     */
    HmeEquipmentWorkingVO4 selectHmeEquipmentWorking(PageRequest pageRequest, String tenantId, HmeEquipmentWorkingDTO dto);

    /**
     * 设备报表导出
     * @param tenantId    租户
     * @param dto         查询条件
     * @return void
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-03-17 16:03
     */
    void export(String tenantId, HmeEquipmentWorkingDTO dto, HttpServletResponse response) throws IOException;
}
