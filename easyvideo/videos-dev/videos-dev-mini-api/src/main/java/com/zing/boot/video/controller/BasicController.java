package com.zing.boot.video.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.zing.boot.video.utils.RedisOperator;

@RestController
public class BasicController {
	
	@Autowired
	public RedisOperator redis;
	
	public static final String USER_REDIS_SESSION = "user-redis-session";
	
	// 文件保存的命名空间
	public static final String FILE_SPACE = "I:/videos_dev";
	
	// ffmpeg所在目录
	public static final String FFMPEG_EXE = "H:\\3rdparty\\ffmpeg-3.4.2-win64-static\\bin\\ffmpeg.exe";
	
	// 每页分页的记录数
	public static final Integer PAGE_SIZE = 5;
	
}
