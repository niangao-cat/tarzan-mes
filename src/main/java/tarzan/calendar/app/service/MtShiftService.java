package tarzan.calendar.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.calendar.api.dto.MtShiftDTO;
import tarzan.calendar.api.dto.MtShiftDTO1;

import java.util.List;

/**
 * 班次信息应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:30:39
 */
public interface MtShiftService {

    /**
     * UI查询班次模板
     * 
     * @author benjamin
     * @date 2019/9/11 1:42 PM
     * @param tenantId 租户Id
     * @param dto MtShiftDTO
     * @param pageRequest PageRequest
     * @return page
     */
    Page<MtShiftDTO> queryShiftForUi(Long tenantId, MtShiftDTO dto, PageRequest pageRequest);

    /**
     * UI保存班次模板
     * 
     * @author benjamin
     * @date 2019/9/11 1:48 PM
     * @param tenantId 租户Id
     * @param dto MtShiftDTO
     * @return String
     */
    String saveShiftForUi(Long tenantId, MtShiftDTO dto);

    /**
     * @author benjamin
     * @date 2019/9/12 9:17 AM
     * @param tenantId 租户Id
     * @param shiftId 班次Id
     */
    void removeShiftForUi(Long tenantId, String shiftId);

    /**
     * UI查询班次模板集合
     *
     * @author chuang.yang
     * @date 2019/12/4
     * @param tanentId
     * @param dto
     * @return java.util.List<tarzan.calendar.api.dto.MtShiftDTO1>
     */
    List<MtShiftDTO1> queryShiftTypesForUi(Long tanentId, MtShiftDTO1 dto);
}
