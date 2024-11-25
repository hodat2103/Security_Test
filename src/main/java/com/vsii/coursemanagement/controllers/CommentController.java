package com.vsii.coursemanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}comments")
public class CommentController {

    @Autowired
    private XSSUtils xssSanitizer;

    @PostMapping
    public String saveComment(@RequestBody String comment) {

        return "Comment saved: " + comment;
    }
}