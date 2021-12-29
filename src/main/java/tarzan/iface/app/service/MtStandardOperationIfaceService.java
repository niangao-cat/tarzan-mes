package tarzan.iface.app.service;

import com.ruike.itf.api.dto.ItfSapIfaceDTO;
import tarzan.iface.domain.entity.MtStandardOperationIface;

import java.util.List;

/**
 * 标准工序接口表应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
public interface MtStandardOperationIfaceService {

    /**
     * 标准工序同步接口
     *
     * @param dto
     * @return
     * @author kejin.liu01@hand-china.com
     */
    List<MtStandardOperationIface> invoke(List<ItfSapIfaceDTO> dto);
}
