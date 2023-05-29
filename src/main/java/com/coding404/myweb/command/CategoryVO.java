package com.coding404.myweb.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryVO {

	private int category_id;
	private String group_id;
	private int category_lv;
	private String category_nm;
	private int category_detail_lv;
	private String category_detail_nm;
	private int category_parent_lv;
	private int category_detail_parent_lv;
	
	
	
	
}
