package com.leyou.item.service;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GoodsService {
    PageResult<Spu> querySpuByPage(Integer page, Integer rows, Integer saleable, String key);

    void saveGoods(Spu spu);

    SpuDetail queryDetailById(Long id);

    List<Sku> queryAllSkuBySpuId(Long spuId);

    Spu querySpuById(Long id);

    List<Sku> queryAllSkuByIds(List<Long> ids);
}
