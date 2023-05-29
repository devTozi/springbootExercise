package com.coding404.myweb.product.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.coding404.myweb.command.CategoryVO;
import com.coding404.myweb.command.ProductUploadVO;
import com.coding404.myweb.command.ProductVO;
import com.coding404.myweb.util.Criteria;

@Mapper
public interface ProductMapper {

	// 등록, 수정, 삭제, 페이징
	public int regist(ProductVO vo);
	public int registFile(ProductUploadVO vo);
	public ArrayList<ProductVO> getList(@Param("user_id") String user_id, 
										@Param("cri") Criteria cri);
	public int getTotal(@Param("user_id") String user_id, 
					    @Param("cri") Criteria cri);
	public ProductVO getDetail(int prod_id);
	public int modify(ProductVO vo);
	public int delete(int prod_id);
	
	// 카테고리
	public List<CategoryVO> getCategory();
	public List<CategoryVO> getCategoryChild(CategoryVO vo);
	
	// 이미지 데이터 조회
	public List<ProductUploadVO> getProductImg(ProductVO vo);
		
}
