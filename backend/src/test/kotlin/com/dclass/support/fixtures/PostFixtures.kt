package com.dclass.support.fixtures

import com.dclass.backend.domain.post.Post
import com.dclass.backend.domain.post.PostCount
import com.dclass.support.domain.Image

fun post(
    title: String = "title",
    content: String = "content",
    userId: Long = 1L,
    communityId: Long = 1L,
    images: List<Image> = listOf(
        createImage("post/s3-test1"),
        createImage("post/s3-test2"),
        createImage("post/s3-test3"),
    ),
    postCount: PostCount = postCount(),
): Post {
    return Post(
        title = title,
        content = content,
        userId = userId,
        communityId = communityId,
        images = images,
        postCount = postCount
    )
}

fun postCount(
    replyCount: Int = 30,
    likeCount: Int = 15
): PostCount {
    return PostCount(
        commentReplyCount = replyCount,
        likeCount = likeCount
    )
}