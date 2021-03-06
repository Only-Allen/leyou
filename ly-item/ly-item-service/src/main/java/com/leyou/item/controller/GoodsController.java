package com.leyou.item.controller;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<Spu>> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", defaultValue = "0") Integer saleable,
            @RequestParam(value = "key", required = false) String key
    ) {
        return ResponseEntity.ok(goodsService.querySpuByPage(page, rows, saleable, key));
    }

    @PostMapping("/goods")
    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu) {
        goodsService.saveGoods(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/goods")
    public ResponseEntity<Void> deleteGoods(Long id) {
        goodsService.deleteGoods(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/goods/saleable")
    public ResponseEntity<Void> updateGoodsSaleable(Long id, Boolean saleable) {
        goodsService.updateGoodsSaleable(id, saleable);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(goodsService.querySpuById(id));
    }

    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<SpuDetail> queryDetailById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(goodsService.queryDetailById(id));
    }

    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> queryAllSkuBySpuId(@RequestParam("id") Long spuId) {
        return ResponseEntity.ok(goodsService.queryAllSkuBySpuId(spuId));
    }

    @GetMapping("/sku/list/ids")
    public ResponseEntity<List<Sku>> queryAllSkuByIds(@RequestParam("ids") List<Long> ids) {
        return ResponseEntity.ok(goodsService.queryAllSkuByIds(ids));
    }
}
