package com.dclass.backend.ui.api

import com.dclass.backend.application.PostService
import com.dclass.backend.application.dto.*
import com.dclass.backend.domain.user.User
import com.dclass.backend.security.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RequestMapping("/api/post")
@RestController
class PostController(
    private val postService: PostService
) {

    @GetMapping("/{postId}")
    fun getPost(
        @LoginUser user: User,
        @PathVariable postId: Long
    ): ResponseEntity<PostResponse> {
        val postResponse = postService.getById(user.id, postId)
        return ResponseEntity.ok(postResponse)
    }

    @GetMapping
    fun getPosts(
        @LoginUser user: User,
        request: PostScrollPageRequest
    ): ResponseEntity<PostsResponse> {
        return ResponseEntity.ok(postService.getAll(user.id, request))
    }

    @PostMapping
    fun createPost(
        @LoginUser user: User,
        @RequestBody request: CreatePostRequest
    ): ResponseEntity<PostResponse> {
        return ResponseEntity.ok(postService.create(user.id, request))
    }

    @PutMapping("/{postId}")
    fun updateLikes(
        @LoginUser user: User,
        @PathVariable postId: Long
    ): ResponseEntity<Int> {
        return ResponseEntity.ok(postService.likes(user.id, postId))
    }

    @PutMapping
    fun updatePost(
        @LoginUser user: User,
        @RequestBody request: UpdatePostRequest
    ): ResponseEntity<Unit> {
        postService.update(user.id, request)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{postId}")
    fun deletePost(
        @LoginUser user: User,
        @PathVariable postId: Long
    ): ResponseEntity<Unit> {
        postService.delete(user.id, DeletePostRequest(postId))
        return ResponseEntity.noContent().build()
    }
}

