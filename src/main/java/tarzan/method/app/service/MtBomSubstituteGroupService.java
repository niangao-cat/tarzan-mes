package tarzan.method.app.service;

import java.util.List;

import tarzan.method.domain.vo.MtBomSubstituteGroupVO5;

/**
 * 装配清单行替代组应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomSubstituteGroupService {

    List<MtBomSubstituteGroupVO5> listbomSubstituteGroupUi(Long tenantId, String bomComponentId);

    String saveBomSubstituteGroupForUi(Long tenantId, MtBomSubstituteGroupVO5 dto);
    
}
