package com.dclass.backend.application

import com.dclass.backend.application.dto.*
import com.dclass.backend.domain.belong.BelongRepository
import com.dclass.backend.domain.comment.CommentRepository
import com.dclass.backend.domain.community.CommunityRepository
import com.dclass.backend.domain.post.PostRepository
import com.dclass.backend.domain.reply.ReplyRepository
import com.dclass.backend.domain.user.UniversityRepository
import com.dclass.backend.domain.user.UserRepository
import com.dclass.support.fixtures.*
import com.dclass.support.test.IntegrationTest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe

@IntegrationTest
class CommentIntegrationTest(
    private val commentService: CommentService,
    private val commentRepository: CommentRepository,
    private val replyRepository: ReplyRepository,
    private val userRepository: UserRepository,
    private val universityRepository: UniversityRepository,
    private val replyService: ReplyService,
    private val belongRepository: BelongRepository,
    private val postRepository: PostRepository,
    private val communityRepository: CommunityRepository,
) : BehaviorSpec({

    extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

    Given("하나의 게시글에 댓글과 대댓글이 있는 경우") {

        val univ = universityRepository.save(university())

        repeat(10) {
            userRepository.save(user(university = univ))
            belongRepository.save(belong(userId = it + 1L))
            postRepository.save(post())
            communityRepository.save(community())
        }

        val comments = commentRepository.saveAll(
            listOf(
                comment(1, 1, "저녁 메뉴 추천 받습니다"),
                comment(1, 2, "게임 같이 하실 분"),
                comment(1, 3, "내 대댓글은 어디 있을까?"),
            )
        )

        val replies = replyRepository.saveAll(
            listOf(
                reply(4, comments[0].id, "짜장면"),
                reply(5, comments[0].id, "탕후루가 더 맛있어요"),
                reply(4, comments[0].id, "글쎄 그건 간식 아냐?"),
                reply(6, comments[1].id, "저도 같이 하고 싶어요"),
                reply(7, comments[1].id, "무슨 게임 할거야?"),
            )
        )

        When("해당 게시글에 댓글을 작성하면") {
            val comment = commentService.create(1, CreateCommentRequest(1, "저녁 메뉴 추천 받습니다"))

            Then("댓글이 작성된다") {
                val savedComment = commentRepository.findById(comment.id).get()
                savedComment.content shouldBe "저녁 메뉴 추천 받습니다"
            }
        }

        When("해당 게시글에 댓글을 삭제하면") {
            val comment = commentService.create(1, CreateCommentRequest(1, "저녁 메뉴 추천 받습니다"))
            commentService.delete(1, DeleteCommentRequest(comment.id))

            Then("댓글이 삭제된다") {
                val savedComment = commentRepository.findById(comment.id)
                savedComment.isEmpty shouldBe true
            }
        }

        When("해당 게시글의 댓글과 대댓글을 조회하면") {
            val comments = commentService.findAllByPostId(1, CommentScrollPageRequest(1L, null, 10))

            Then("댓글과 대댓글이 조회된다") {

                comments.data.size shouldBe 4
                comments.data[0].replies.size shouldBe 3
                comments.data[1].replies.size shouldBe 2
            }
        }

        When("해당 게시글의 댓글과 대댓글 수를 조회하면") {
            val count = commentRepository.countCommentReplyByPostId(1L)

            Then("댓글과 대댓글 수가 조회된다") {
                count shouldBe 9
            }
        }

        When("해당 게시글의 댓글을 수정하면") {
            commentService.update(1, UpdateCommentRequest(comments[0].id, "저녁 메뉴 추천 받아요"))

            Then("댓글이 수정된다") {
                val comment = commentRepository.findCommentByIdAndUserId(comments[0].id, 1)
                comment!!.content shouldBe "저녁 메뉴 추천 받아요"
            }
        }

        When("댓글에 좋아요를 누르면") {
            commentService.like(2, LikeCommentRequest(comments[0].id))

            Then("해당 댓글의 좋아요 수가 증가한다") {
                val comment = commentRepository.findCommentByIdAndUserId(comments[0].id, 1)
                comment!!.commentLikes.count shouldBe 1
            }
        }

        When("대댓글에 좋아요를 누르면") {
            replyService.like(1, LikeReplyRequest(replies[0].id))

            Then("해당 대댓글의 좋아요 수가 증가한다") {
                val reply = replyRepository.findByIdAndUserId(replies[0].id, 4)
                reply!!.replyLikes.count shouldBe 1
            }
        }

    }
})