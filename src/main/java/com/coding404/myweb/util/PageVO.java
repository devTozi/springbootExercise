package com.coding404.myweb.util;

import java.util.ArrayList;

import lombok.Data;

@Data
public class PageVO {

	// 페이지네이션을 계산하는 클래스
	private int start; // 첫 페이지 번호
	private int end; // 끝 페이지 번호
	private boolean prev; // 이전버튼
	private boolean next; // 다음버튼
	
	private int page; // 조회하는 번호
	private int amount; // 데이터 개수
	private int total; // 전체 게시글 수
	private int realEnd; // 실제 끝번호
	private int pageCount = 5;
	
	private Criteria cri;
	private ArrayList<Integer> pageList; // 타임리프는 화면에 반복시킬 페이지 번호
	
	public PageVO(Criteria cri, int total) {
		
		this.page = cri.getPage();
		this.amount = cri.getAmount();
		this.total = total;
		this.cri = cri;
		
		// 끝페이지 번호 = (int)Math.ceil( 조회하는 페이지번호 / 페이지네이션 개수) * 페이지네이션 개수
		this.end = (int)Math.ceil( this.page / (double)pageCount) * pageCount;
		
		// 시작페이지 번호 = 끝페이지 번호 - 페이지네이션 개수 + 1
		this.start = this.end - pageCount + 1;
		
		// 실제 끝번호
		// 데이터가 141개 라면 -> 마지막 페이지 번호는 15번
		// (int)Math.ceil( 전체 게시글 수 / 데이터 개수 )
		this.realEnd = (int)Math.ceil( this.total / (double)this.amount);
		
		// 끝번호 재계산
		// 데이터가 141개 라면 -> 1~10 페이지는 10번, 11~20 페이지는 15번
		this.end = this.end > this.realEnd ? this.realEnd : this.end;
		
		// 이전버튼 활성화
		this.prev = this.start > 1;
		
		// 다음버튼 활성화
		this.next = this.realEnd > this.end;
		
		// 화면에서 반복시킬 페이지 데이터
		this.pageList = new ArrayList<>();
		for(int i=this.start; i<=this.end; i++) {
			pageList.add(i);
		}
		
	}
	
}
