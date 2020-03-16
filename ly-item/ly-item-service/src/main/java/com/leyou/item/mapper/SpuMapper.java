package com.leyou.item.mapper;

import com.leyou.item.pojo.Spu;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface SpuMapper extends Mapper<Spu> {

    @Update("UPDATE tb_spu SET valid = 0 WHERE id = #{id}")
    int deleteSpu(Long id);

    @Update("UPDATE tb_spu SET saleable = #{saleable} WHERE id = #{id}")
    int updateSpuSaleable(Long id, Boolean saleable);
}
