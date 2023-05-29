package com.coding404.myweb.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.coding404.myweb.command.CategoryVO;
import com.coding404.myweb.command.ProductUploadVO;
import com.coding404.myweb.command.ProductVO;
import com.coding404.myweb.product.service.ProductService;

@RestController
public class AjaxController {

	@Autowired
	private ProductService productService;

	@Value("${project.uploadpath}")
	private String uploadpath;

	// 대분류 카테고리 요청
	@GetMapping("/getCategory")
	public List<CategoryVO> getCategory(){

		return productService.getCategory();
	}

	// 중분류, 소분류 카테고리 요청
	@GetMapping("/getCategoryChild/{group_id}/{category_lv}/{category_detail_lv}")
	public List<CategoryVO> getCategoryChild(@PathVariable("group_id") String group_id,
			@PathVariable("category_lv") int category_lv,
			@PathVariable("category_detail_lv") int category_detail_lv) {

		CategoryVO vo = CategoryVO.builder().group_id(group_id)
				.category_lv(category_lv)
				.category_detail_lv(category_detail_lv)
				.build();

		return productService.getCategoryChild(vo);
	}

	// 이미지 정보를 처리
	// 1. ?키=값
	// 2. PathVariable
	// 화면에는 이진데이터 타입이 반환됨 (produces 속성으로 타입 지정하면 이미지 출력 가능)
	//	@GetMapping("/display/{filepath}/{uuid}/{filename}")
	//	public byte[] display(@PathVariable("filepath") String filepath,
	//						  @PathVariable("uuid") String uuid,
	//						  @PathVariable("filename") String filename) {
	//		
	//		// 파일이 저장된 경로
	//		String savename = uploadpath +"\\"+ filepath +"\\"+ uuid +"_"+ filename;
	//		File file = new File(savename);
	//		// 저장된 이미지 파일의 이진데이터 형식을 구함
	//		byte[] result = null;
	//		try {
	//			result = FileCopyUtils.copyToByteArray(file);
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//		
	//		return result;
	//	}

	// ResponseEntity - 응답본문을 직접 작성
	@GetMapping("/display/{filepath}/{uuid}/{filename}")
	public ResponseEntity<byte[]> display(@PathVariable("filepath") String filepath,
										  @PathVariable("uuid") String uuid,
										  @PathVariable("filename") String filename) {

		// 파일이 저장된 경로
		String savename = uploadpath +"\\"+ filepath +"\\"+ uuid +"_"+ filename;
		File file = new File(savename);

		// 저장된 이미지 파일의 이진데이터 형식을 구함
		byte[] data = null;
		ResponseEntity<byte[]> entity = null;

		try {
			// 보낼 데이터 - byte array type
			data = FileCopyUtils.copyToByteArray(file);

			// 헤더 정보 작성
			// Files.probeContentType(path type) - 파일의 컨텐츠 타입을 직접 구함
			HttpHeaders header = new HttpHeaders();
			header.add("Content-type", Files.probeContentType(file.toPath())); 

			// 응답 본문 작성
			// 매개변수 - (데이터, 헤더, 상태값)
			entity = new ResponseEntity<>(data, header, HttpStatus.OK);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return entity;
	}

	// prod_id 값을 받아서 이미지 정보를 반환
	@PostMapping("/getProductImg")
	public ResponseEntity<List<ProductUploadVO>> getProductImg(@RequestBody ProductVO vo){
		System.out.println("왓냐");
		return new ResponseEntity<>(productService.getProductImg(vo), HttpStatus.OK);
	}
	
//	@GetMapping("/getProductImg")
//	public String getProductImg(){
//		System.out.println("왓냐");
//		return "왔냐";
//	}

	// ResponseEntity - 응답본문을 직접 작성
	@GetMapping("/download/{filepath}/{uuid}/{filename}")
	public ResponseEntity<byte[]> download(@PathVariable("filepath") String filepath,
										   @PathVariable("uuid") String uuid,
										   @PathVariable("filename") String filename) {

		// 파일이 저장된 경로
		String savename = uploadpath +"\\"+ filepath +"\\"+ uuid +"_"+ filename;
		File file = new File(savename);

		// 저장된 이미지 파일의 이진데이터 형식을 구함
		byte[] data = null;
		ResponseEntity<byte[]> entity = null;

		try {
			// 보낼 데이터 - byte array type
			data = FileCopyUtils.copyToByteArray(file);

			// 헤더 정보 작성
			HttpHeaders header = new HttpHeaders();
			// 다운로드명을 명시
			header.add("Content-Disposition", "attachment; filename=" + filename); 

			// 응답 본문 작성
			// 매개변수 - (데이터, 헤더, 상태값)
			entity = new ResponseEntity<>(data, header, HttpStatus.OK);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return entity;
	}


}
