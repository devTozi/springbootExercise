package com.coding404.myweb.product.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.coding404.myweb.command.CategoryVO;
import com.coding404.myweb.command.ProductUploadVO;
import com.coding404.myweb.command.ProductVO;
import com.coding404.myweb.util.Criteria;

@Service("productService")
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductMapper productMapper;
	
	@Value("${project.uploadpath}")
	private String uploadpath;

	// 날짜별로 폴더 생성
	public String makeDir() {

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		String now = sdf.format(date);

		String path = uploadpath + "\\" + now;
		File file = new File(path);

		if(file.exists() == false) { // 존재하면 true
			file.mkdir();
		}

		return now; // 년월일 폴더위치
	}
	
	// 글 등록
	// 한 프로세스 안에서 예외가 발생하면 실행됐던 CRUD작업을 롤백시켜줌
	// 조건 - catch를 통해서 예외처리가 진행되면 트랜잭션 처리가 되지 않음
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int regist(ProductVO vo, List<MultipartFile> list) {
		
		// 글 등록 처리
		int result = productMapper.regist(vo);
		
		// 파일 저장
		for(MultipartFile file : list) {

			String origin = file.getOriginalFilename();
			String filename = origin.substring(origin.lastIndexOf("\\")+1);
			String filepath = makeDir(); // 폴더 생성 메서드 호출
			String uuid = UUID.randomUUID().toString(); // 중복 파일 방지용 랜덤값 생성
			String savename = uploadpath +"\\"+ filepath +"\\"+ uuid +"_"+ filename;

			try {
				File save = new File(savename);
				file.transferTo(save);

			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
				return 0; // 실패 시
			}
			
			// 업로드 테이블 인서트 - insert 이전에 prod_id 필요, selectKey 방식으로 처리 
			ProductUploadVO prodVO =  ProductUploadVO.builder()
													 .filename(filename)
													 .filepath(filepath)
													 .uuid(uuid)
													 .prod_writer(vo.getProd_writer())
													 .build();
			productMapper.registFile(prodVO);
			
		}// end for
		
		return result;
	}

	@Override
	public ArrayList<ProductVO> getList(String user_id, Criteria cri) {

		return productMapper.getList(user_id, cri);
	}

	@Override
	public int getTotal(String user_id, Criteria cri) {
		
		return productMapper.getTotal(user_id, cri);
	}

	@Override
	public ProductVO getDetail(int prod_id) {
		
		return productMapper.getDetail(prod_id);
	}

	@Override
	public int modify(ProductVO vo) {
		
		return productMapper.modify(vo);
	}

	@Override
	public int delete(int prod_id) {
		
		return productMapper.delete(prod_id);
	}

	@Override
	public List<CategoryVO> getCategory() {
		
		return productMapper.getCategory();
	}

	@Override
	public List<CategoryVO> getCategoryChild(CategoryVO vo) {
		
		return productMapper.getCategoryChild(vo);
	}

	@Override
	public List<ProductUploadVO> getProductImg(ProductVO vo) {
		
		return productMapper.getProductImg(vo);
	}

	
	
}
