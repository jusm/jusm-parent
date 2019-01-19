package com.github.jusm.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.github.jusm.util.Conts;

@RequestMapping(value = Conts.DEFAULT_REST_API_PATH,headers = Conts.TOKENHEADERKEY)
public class ApiController {

}
