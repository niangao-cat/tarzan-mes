package tarzan.method.app.service;


import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.method.api.dto.MtBomDTO;
import tarzan.method.api.dto.MtBomDTO1;
import tarzan.method.api.dto.MtBomDTO5;
import tarzan.method.api.dto.MtBomDTO6;
import tarzan.method.domain.vo.MtBomVO9;

/**
 * 装配清单头应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomService {

    Page<MtBomDTO> listUi(Long tenantId, PageRequest pageRequest, MtBomDTO1 dto);

    MtBomVO9 bomRecordForUi(Long tenantId, String bomId);

    String saveBomForUi(Long tenantId, MtBomDTO5 dto);
    
    String bomCopyForUi(Long tenantId, MtBomDTO6 dto);

}
