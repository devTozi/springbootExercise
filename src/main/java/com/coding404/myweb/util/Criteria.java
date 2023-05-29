package com.coding404.myweb.util;

import lombok.Data;

@Data
public class Criteria {
	
	// sql에 전달할 page, amount를 가지고 다니는 클래스
	private int page; // 조회하는 페이지 번호
	private int amount; // 데이터 개수
	
	// 검색 키워드
	private String searchName; // 상품명
	private String searchContent; // 상품내용
	private String searchPrice; // 정렬방식
	private String startDate; // 판매 시작일
	private String endDate; // 판매 종료일
	
	public Criteria() {
		this.page = 1;
		this.amount = 10;
	}
	
	public Criteria(int page, int amount) {
		super();
		this.page = page;
		this.amount = amount;
	}
	
	public int getPageStart() {
		return (page - 1) * amount;
	}
	
}
