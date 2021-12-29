package com.ruike.qms.domain.repository;

import com.ruike.qms.api.dto.QmsIqcExamineBoardDTO;
import org.hzero.core.base.AopProxy;

import java.util.List;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-05-19 11:17
 */
public interface QmsIqcExamineBoardRepository extends AopProxy<QmsIqcExamineBoardRepository> {

    /**
     * 获取ICQ检验看板信息
     * @author jiangling.zheng@hand-china.com
     * @param tenantId 租户ID
     * @return List<QmsIqcExamineBoardDTO>
     */
    List<QmsIqcExamineBoardDTO> selectIqcExamineBoardForUi(Long tenantId);
}
