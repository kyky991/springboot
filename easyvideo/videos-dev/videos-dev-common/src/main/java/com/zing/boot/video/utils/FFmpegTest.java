package com.zing.boot.video.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FFmpegTest {

	private String ffmpegEXE;
	
	public FFmpegTest(String ffmpegEXE) {
		super();
		this.ffmpegEXE = ffmpegEXE;
	}
	
	public void convertor(String videoInputPath, String videoOutputPath) throws Exception {
//		ffmpeg -i input.mp4 -y output.avi
		List<String> command = new ArrayList<>();
		command.add(ffmpegEXE);
		
		command.add("-i");
		command.add(videoInputPath);
		command.add("-y");
		command.add(videoOutputPath);
		
		for (String c : command) {
			System.out.print(c + " ");
		}
		
		ProcessBuilder builder = new ProcessBuilder(command);
		Process process = builder.start();
		
		InputStream errorStream = process.getErrorStream();
		InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
		BufferedReader br = new BufferedReader(inputStreamReader);
		
		String line = "";
		while ( (line = br.readLine()) != null ) {
		}
		
		if (br != null) {
			br.close();
		}
		if (inputStreamReader != null) {
			inputStreamReader.close();
		}
		if (errorStream != null) {
			errorStream.close();
		}
		
	}

	public static void main(String[] args) {
		FFmpegTest ffmpeg = new FFmpegTest("H:\\3rdparty\\ffmpeg-3.4.2-win64-static\\bin\\ffmpeg");
		try {
			ffmpeg.convertor("xxx.mp4", "xxx.avi");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
