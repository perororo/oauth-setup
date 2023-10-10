package com.blog.resourceserver

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@ResponseBody
class HelloController(
    val helloService: HelloService,
) {
    @GetMapping("/hello")
    fun hello(): String {
        return helloService.hello()
    }
    @GetMapping("/")
    fun free(): String {
        return "some free content"
    }
}
