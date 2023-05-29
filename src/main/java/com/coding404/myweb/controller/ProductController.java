package com.coding404.myweb.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.coding404.myweb.command.ProductUploadVO;
import com.coding404.myweb.command.ProductVO;
import com.coding404.myweb.product.service.ProductService;
import com.coding404.myweb.util.Criteria;
import com.coding404.myweb.util.PageVO;

@Controller
@RequestMapping("/product")
public class ProductController {


	@Autowired
	@Qualifier("productService")
	private ProductService productService;

	@GetMapping("/productReg")
	public String reg() {
		return "product/productReg";
	}
	
//	@GetMapping("/getProductImg")
//	public String img() {
//		System.out.println("왓냔");
//		return "product/productReg";
//	}

	@GetMapping("/productList")
	public String list(HttpSession session, 
			Model model, 
			Criteria cri
			) {

		/*
		 	1. 검색폼에서는 키워드, page, amount 데이터를 넘긴다.
		 	2. 목록 조회 and total 동적쿼리로 변경
		 	3. 페이지네이션에 키워드, page, amount 데이터를 넘긴다.
		  	4. 검색 키워드 화면에서 유지시키기
		 */


		// 수업 예시 코드
		// 세션은 얘로 써도됨 > HttpServletRequest request
		// 프로세스 - 세션에 있는 id에 맞는 게시글만 리스트에 출력
		// session.setAttribute("user_id", "admin"); // 임시 데이터
		//		
		// // 로그인한 회원만 조회
		// String user_id = (String)session.getAttribute("user_id");

		// 스프링 시큐리티 처리 코드
		String userId = "";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			userId = userDetails.getUsername();
			// 로그인된 유저의 ID를 사용하여 필요한 로직을 수행
		} else {
			// 인증된 사용자의 Principal이 UserDetails 타입이 아닌 경우에 대한 처리
			System.out.println("인증된 사용자의 Principal이 UserDetails 타입이 아닙니다요");
		}
		ArrayList<ProductVO> list = productService.getList(userId, cri);
		model.addAttribute("list", list);

		// 페이지네이션 처리
		// user의 전체 게시글 수
		int total = productService.getTotal(userId, cri);
		PageVO pageVO = new PageVO(cri, total);
		model.addAttribute("pageVO", pageVO);

		return "product/productList";
	}

	@GetMapping("/productDetail")
	public String detail(@RequestParam("prod_id") int prod_id,
			Criteria cri,
			Model model) {

		ProductVO vo = productService.getDetail(prod_id);
		model.addAttribute("vo", vo);
		model.addAttribute("cri", cri);

		return "product/productDetail";
	}

	// 디테일 화면 수정 요청
	@PostMapping("/modifyForm")
	public String modifyForm(ProductVO vo,
			Criteria cri,
			RedirectAttributes ra) {

		int result = productService.modify(vo);
		String msg = result == 1 ? "수정이 완료되었습니다." : "수정에 실패했습니다.";
		ra.addFlashAttribute("msg", msg);

		return "redirect:/product/productList";
	}

	// 디테일 화면에서 상품 삭제 요청
	@PostMapping("/deleteForm")
	public String deleteForm(ProductVO vo,
			Criteria cri,
			RedirectAttributes ra) {

		int result = productService.delete(vo.getProd_id());
		String msg = result == 1 ? "삭제되었습니다." : "삭제에 실패했습니다.";
		ra.addFlashAttribute("msg", msg);

		return "redirect:/product/productList";
	}

	// 상품 등록
	@PostMapping("/registForm") // Error 객체로 err값 전달해보기
	public String registForm(/*@Valid*/ ProductVO vo,
			RedirectAttributes ra,
			@RequestParam("file") List<MultipartFile> list) {

		// 파일 업로드 작업
		// 빈 값 제거
		list = list.stream()
				   .filter(x -> x.isEmpty() == false)
				   .collect(Collectors.toList());
		// 확장자가 image라면 경고문
		for(MultipartFile file : list) {
			if(file.getContentType().contains("image") == false) {
				ra.addFlashAttribute("msg", "png, jpg, jpeg 형식만 등록 가능합니다.");
				return "redirect:/product/productReg";
			}
		}
		
		// 파일 업로드 작업을 service 영역으로 위임
		// 글 등록
		int result = productService.regist(vo, list);
		
		String msg = result == 1 ? "정상 입력되었습니다." : "등록에 실패했습니다.";
		ra.addFlashAttribute("msg", msg);
		
		return "redirect:/product/productList"; // 목록으로
	}
	
	
}
