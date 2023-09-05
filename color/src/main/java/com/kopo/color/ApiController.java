package com.kopo.color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

	@GetMapping("create")
	public String createTable() {
		DB db = new DB();
		db.createTable();		
		return "테이블이 생성되었습니다.";
	}
	
	@GetMapping("insert")
	public HashMap<String, String> insertCode(@RequestParam(value="code", defaultValue="")String code) {
		DB db = new DB();
		db.insertTable(code);
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("message", "sucess");
		return result;
	}

	@GetMapping("/select")
	public String selectTable(Model model) {
	    DB db = new DB();
	    List<Color> colorList = db.select(); // 예시로 데이터를 가져오는 메서드
	    
	    // 모델에 데이터를 추가하여 뷰로 전달
	    model.addAttribute("colorList", colorList);
	    
	    return "histories"; // 뷰 이름
	}
	
	@GetMapping("/histories_api")
	public ArrayList<Color> hiApi(){
		DB db = new DB();
		ArrayList<Color> result = db.select();
		return result;
	}

	@PostMapping("/deletePost")
	public ResponseEntity<String> deletePost(@RequestParam("idx") int idx) {
	    try {
	        // 여기에 보안 검사를 추가하고, 사용자 권한을 확인하세요 (예: 로그인된 사용자만 삭제 가능)
	        
	        DB db = new DB();
	        db.deleteData(idx);

	        // 성공적인 응답 반환
	        return ResponseEntity.ok("삭제가 완료되었습니다.");
	    } catch (Exception e) {
	        e.printStackTrace();

	        // 실패한 경우 오류 응답 반환
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 중 오류가 발생했습니다.");
	    }
	}



}
