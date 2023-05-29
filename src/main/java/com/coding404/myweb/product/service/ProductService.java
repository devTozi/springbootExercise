package com.coding404.myweb.product.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import com.coding404.myweb.command.CategoryVO;
import com.coding404.myweb.command.ProductUploadVO;
import com.coding404.myweb.command.ProductVO;
import com.coding404.myweb.util.Criteria;

public interface ProductService {
	
	// 등록, 수정, 삭제, 페이징
	public int regist(ProductVO vo, List<MultipartFile> list); // 파일 업로드 기능 추가
	public ArrayList<ProductVO> getList(String user_id, Criteria cri);
	public int getTotal(String user_id, Criteria cri);
	public ProductVO getDetail(int prod_id);
	public int modify(ProductVO vo);
	public int delete(int prod_id);
	
	// 카테고리
	public List<CategoryVO> getCategory();
	public List<CategoryVO> getCategoryChild(CategoryVO vo);
	
	// 이미지 데이터 조회
	public List<ProductUploadVO> getProductImg(ProductVO vo);
	
}
