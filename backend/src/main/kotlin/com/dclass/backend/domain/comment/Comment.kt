package com.dclass.backend.domain.comment

import com.dclass.backend.exception.comment.CommentException
import com.dclass.backend.exception.comment.CommentExceptionType
import com.dclass.support.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDateTime

@SQLDelete(sql = "update comment set deleted = true where id = ?")
@SQLRestriction("deleted = false OR (deleted = true AND reply_count > 0)")
@Entity
class Comment(
    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false)
    val postId: Long,

    content: String = "",

    commentLikes: CommentLikes = CommentLikes(),

    @Column(nullable = false)
    val createdDateTime: LocalDateTime = LocalDateTime.now(),

    modifiedDateTime: LocalDateTime = LocalDateTime.now(),

    id: Long = 0L
) : BaseEntity(id) {

    @Column(nullable = false)
    private var deleted: Boolean = false

    @Column(nullable = false, length = 255)
    var content: String = content
        private set

    @Column(nullable = false)
    var modifiedDateTime: LocalDateTime = modifiedDateTime
        private set

    @Embedded
    var commentLikes: CommentLikes = commentLikes
        private set

    @Column(nullable = false)
    var replyCount: Int = 0
        private set

    val likeCount: Int
        get() = commentLikes.count

    fun likedBy(userId: Long) =
        commentLikes.findUserById(userId)

    fun like(userId: Long) {
        if (this.userId == userId) {
            throw CommentException(CommentExceptionType.SELF_LIKE)
        }
        commentLikes.add(userId)
    }

    fun isDeleted() = deleted

    fun changeContent(content: String) {
        this.content = content
        modifiedDateTime = LocalDateTime.now()
    }

    fun isEligibleForSSE(userId: Long) = this.userId != userId

    fun increaseReplyCount() {
        replyCount++
    }

    fun decreaseReplyCount() {
        replyCount--
    }

}