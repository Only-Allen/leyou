package com.leyou.search.repository;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsRepositoryTest {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private SearchService searchService;

    @Autowired
    private GoodsClient goodsClient;

    @Test
    public void testCreateIndex() {
        template.createIndex(Goods.class);
        template.putMapping(Goods.class);
    }

    @Test
    public void loadData() {
        int page = 1, rows = 100;
        int size;
        do {
            PageResult<Spu> result = goodsClient.querySpuByPage(page, rows, 1, null);
            if (result == null) {
                System.out.println("page = " + page);
                break;
            }

            List<Spu> items = result.getItems();
            size = items.size();
            List<Goods> goodsList = new ArrayList<>();
            for (Spu item : items) {
                Goods goods = searchService.buildGoods(item);
                goodsList.add(goods);
                goodsRepository.save(goods);
            }
//            goodsRepository.saveAll(goodsList);
            page++;
        } while (rows == size);
    }
}