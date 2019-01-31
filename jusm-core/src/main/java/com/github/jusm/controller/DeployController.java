package com.github.jusm.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.github.jusm.context.UsmContext;
import com.github.jusm.model.R;
import com.github.jusm.util.Conts;
import com.github.jusm.util.ImageUtil;
import com.github.jusm.web.WebContextHolder;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
public class DeployController {

	private Logger logger = LoggerFactory.getLogger(getClass());


	@GetMapping("deploy.html")
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("deploy");
		return mav;
	}
	
	@ApiOperation(value = "zip信息上传", tags = Conts.Tags.PC)
	@RequestMapping(value = "deploy/upload", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody R upload(@ApiParam("图片") @RequestParam(value = "file", required = true) MultipartFile file)
			throws IOException {
		List<MultipartFile> fileList = Arrays.asList(file);
		
		String sharePath = UsmContext.getSharePath();
		String url = WebContextHolder.getCompleteURL("/res/");
		return R.success(url);
	}
}
