package tarzan.material.infra.mapper;

import org.apache.ibatis.annotations.Param;
import tarzan.material.domain.vo.MtMaterialVO6;

import java.util.List;

public interface MtMaterialNewMapper {
    void batchInsert(@Param("materials") List<MtMaterialVO6> materials);

    void batchInsertTl(@Param("materials") List<MtMaterialVO6> tempList);
}

