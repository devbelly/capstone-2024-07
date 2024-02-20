package com.dclass.backend.comment

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class CommentLike(
    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false)
    val commentId: Long,
)