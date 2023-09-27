package com.kopo.tester;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileUploadController {
	@PostMapping("/upload")
	public ModelAndView handleFileUpload(@RequestParam("file") MultipartFile file) {
		ModelAndView modelAndView = new ModelAndView("result"); // "result"는 뷰의 이름입니다.

		try {
			// 파일을 임시 디렉토리에 저장
			Path tempFile = Files.createTempFile("uploaded-file", ".csv");
			file.transferTo(tempFile.toFile());

			// Flask 서버 URL 설정
			String flaskUrl = "http://localhost:34463"; // Flask 서버 주소

			// Flask로 파일 전송
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", new FileSystemResource(tempFile.toFile()));

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			String response = restTemplate.postForObject(flaskUrl, requestEntity, String.class);

			// 임시 파일 삭제 (선택사항)
			Files.deleteIfExists(tempFile);

			// 받아온 응답을 처리
			String[] responseParts = response.split(",");
			String textResult = responseParts[0];
			String imagePath = responseParts[1];
			
			textResult = textResult.split(":")[1];

			// 뷰에 전달
			modelAndView.addObject("textResult", textResult);
			modelAndView.addObject("imagePath", imagePath);

			return modelAndView;
		} catch (IOException e) {
			e.printStackTrace();
			modelAndView.addObject("error", "파일 업로드 및 전송에 실패했습니다.");
			return modelAndView;
		}
	}

}
