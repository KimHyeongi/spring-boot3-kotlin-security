package com.tistory.eclipse4j.auth.demo.contoller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/demo-controller")
class DemoController {

    /**
     * URL | GET : http://localhost:8888/api/v1/demo-controller
     * Header | Authorization : Bearer xxxxxxxxxxxxx
     */
    @GetMapping
    fun sayHello(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello from secured endpoint")
    }
}