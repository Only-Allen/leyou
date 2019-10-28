package com.leyou.item.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_specification")
@Data
public class Specification {
    @Id//主键
    private Long categoryId;
    private String specifications;
}
