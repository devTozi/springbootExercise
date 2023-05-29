package com.coding404.myweb.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component("kakao")
public class KakaoAPI {

	// 토큰 발급 기능
	public String getAccessToken(String code) {

		String requestURL = "https://kauth.kakao.com/oauth/token";
		String redirect_url = "http://127.0.0.1:8484/user/kakao";

		String access_token = "";
		String refresh_token = "";

		// post의 폼데이터 형식(키=값&키=값...)
		String data = "grant_type=authorization_code"
				+"&client_id=2cef04fc1faba99845ea134c553a2ba1"
				+"&redirect_uri=" + redirect_url
				+"&code=" + code;

		try {
			URL url = new URL(requestURL);

			HttpURLConnection conn = (HttpURLConnection)url.openConnection();

			conn.setRequestMethod("POST"); // post 형식
			conn.setDoOutput(true); // 카카오서버로부터 데이터 응답을 허용

			// 데이터 전송을 위한 클래스
			//			OutputStream out = conn.getOutputStream();
			//			OutputStreamWriter osw = new OutputStreamWriter(out);
			//			BufferedWriter bw = new BufferedWriter(osw);

			BufferedWriter bw = 
					new BufferedWriter(
							new OutputStreamWriter(conn.getOutputStream())
							);
			bw.write(data);
			bw.flush();

			// 응답 결과를 conn 객체에서 받음
			if(conn.getResponseCode() == 200) {

				BufferedReader br = 
						new BufferedReader(
								new InputStreamReader(conn.getInputStream())
								);
				String result = "";
				String str;
				while( (str = br.readLine()) != null ) { // 읽을 값이 없다면 null
					result += str;
				}

				// 응답받은 json 데이터 파싱, 분해
				// GSON 사용(디펜던시 추가 필요)
				JsonParser json = new JsonParser();
				JsonElement element = json.parse(result);
				JsonObject obj = element.getAsJsonObject();
				access_token = obj.get("access_token").getAsString();
				refresh_token = obj.get("refresh_token").getAsString();

			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		return access_token;
	}

	// 토큰 기반으로 유저 정보 얻기
	public Map<String, Object> getUserInfo(String access_token){

		Map<String, Object> map = new HashMap<>();
		
		String requestURL = "https://kapi.kakao.com/v2/user/me";

		try {

			URL url = new URL(requestURL);

			HttpURLConnection conn = (HttpURLConnection)url.openConnection();

			conn.setRequestMethod("POST"); // post 형식
			conn.setDoOutput(true); // 카카오서버로부터 데이터 응답을 허용

			// 헤더에 토큰 정보 추가
			conn.setRequestProperty("Authorization", "Bearer " + access_token);

			if(conn.getResponseCode() == 200) {

				BufferedReader br = 
						new BufferedReader(
								new InputStreamReader(conn.getInputStream())
								);
				String result = "";
				String str;
				while( (str = br.readLine()) != null ) { // 읽을 값이 없다면 null
					result += str;
				}

		        JsonParser json = new JsonParser();
		        JsonElement element = json.parse(result);
		        
		        //json에서 key를 꺼내고, 다시 key를 꺼낸다.
		        JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
		        JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
		        
		        String nickname = properties.get("nickname").getAsString();
		        String email = kakao_account.get("email").getAsString();
				
		        map.put("nickname", nickname);
		        map.put("email", email);
		        
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}





}
