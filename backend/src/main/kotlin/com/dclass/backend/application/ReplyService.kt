package com.dclass.backend.application

import com.dclass.backend.application.dto.CreateReplyRequest
import com.dclass.backend.application.dto.DeleteReplyRequest
import com.dclass.backend.application.dto.ReplyResponse
import com.dclass.backend.application.dto.UpdateReplyRequest
import com.dclass.backend.domain.reply.Reply
import com.dclass.backend.domain.reply.ReplyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class ReplyService(
    private val replyRepository: ReplyRepository,
) {
    fun create(userId: Long, request: CreateReplyRequest): ReplyResponse {
        return replyRepository.save(Reply(userId, request.commentId, request.content))
            .let(::ReplyResponse)
    }

    fun update(userId: Long, request: UpdateReplyRequest) {
        val reply = find(request.replyId, userId)
        reply.changeContent(request.content)
    }

    fun delete(userId: Long, request: DeleteReplyRequest) {
        val reply = find(request.replyId, userId)
        replyRepository.delete(reply)
    }

    private fun find(replyId: Long, userId: Long): Reply {
        val reply = replyRepository.findReplyByIdAndUserId(replyId, userId)
            ?: throw NoSuchElementException("해당 댓글이 존재하지 않습니다.")
        check(!reply.isDeleted(replyId)) {
            "삭제된 댓글입니다."
        }
        return reply
    }
}