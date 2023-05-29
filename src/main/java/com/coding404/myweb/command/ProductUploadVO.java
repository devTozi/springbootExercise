package com.coding404.myweb.command;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductUploadVO {

	private int upload_no;
	private String filename;
	private String filepath;
	private String uuid;
	private LocalDateTime regdate;
	
	private int prod_id;
	private String prod_writer;
	
}
