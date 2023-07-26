package com.estgames.web.service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.extern.java.Log;

@Service
@Log
public class FileService {

	public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
		UUID uuid = UUID.randomUUID();
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		//겹칠 때 추가하기
		//파일저장경로도 img 아래에 -> 올린 날짜 폴더에 들어가게

		//오늘 날짜 폴더 경로 생성
		LocalDate today = LocalDate.now();
		String todayFolder = today.toString();
		File uploadFolder = new File(uploadPath, todayFolder);

		//오늘 날짜 폴더가 없으면 생성
		if (!uploadFolder.exists()) {
			uploadFolder.mkdirs();
		}

		String savedFileName = todayFolder + originalFileName + uuid.toString() + extension;
		String fileUploadFullUrl = uploadFolder.getAbsolutePath() + File.separator + savedFileName;
		FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
		fos.write(fileData);
		fos.close();
		return todayFolder + File.separator + savedFileName;

		// String savedFileName = uuid.toString() + extension;
		// String fileUploadFullUrl = uploadPath + "/" + savedFileName;
		// FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
		// fos.write(fileData);
		// fos.close();
		// return savedFileName;
	}

	public void deleteFile(String filePath) throws Exception {
		File deleteFile = new File(filePath);
		log.info(filePath);
		if (deleteFile.exists()) {
			deleteFile.delete();
			log.info("파일을 삭제하였습니다.");
		} else {
			log.info("파일이 존재하지 않습니다.");
		}
	}
}
