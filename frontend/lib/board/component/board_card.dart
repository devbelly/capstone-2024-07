import 'package:flutter/material.dart';

import '../../common/const/colors.dart';
import '../const/categorys.dart';
import '../model/msg_board_response_model.dart';
import 'text_with_icon_for_view.dart';

class BoardCard extends StatelessWidget {
  final int id;
  final int userId;
  final String userNickname;
  final String universityName;
  final int communityId;
  final String communityTitle;
  final String postTitle;
  final String postContent;
  final List<String> images;
  final ReactCountModel count;
  final bool isQuestion;
  final int imageCount;
  final String createdDateTime;

  const BoardCard({
    required this.id,
    required this.userId,
    required this.userNickname,
    required this.universityName,
    required this.communityId,
    required this.communityTitle,
    required this.postTitle,
    required this.postContent,
    required this.images,
    required this.count,
    required this.isQuestion,
    required this.imageCount,
    required this.createdDateTime,
    super.key,
  });

  factory BoardCard.fromModel(
      {required MsgBoardResponseModel msgBoardResponseModel}) {
    return BoardCard(
      id: msgBoardResponseModel.id,
      userId: msgBoardResponseModel.userId,
      userNickname: msgBoardResponseModel.userNickname,
      universityName: msgBoardResponseModel.universityName,
      communityId: msgBoardResponseModel.communityId,
      communityTitle: msgBoardResponseModel.communityTitle,
      postTitle: msgBoardResponseModel.postTitle,
      postContent: msgBoardResponseModel.postContent,
      images: msgBoardResponseModel.images,
      count: msgBoardResponseModel.count,
      isQuestion: msgBoardResponseModel.isQuestion,
      imageCount: msgBoardResponseModel.imageCount,
      createdDateTime: msgBoardResponseModel.createdDateTime,
    );
  }

  String changeTime(String time) {
    String dt = DateTime.now().toString(); //2022-12-05 20:09:14.322471
    String nowDate = dt.replaceRange(11, dt.length, ""); //2022-12-05
    String nowTime = dt.replaceRange(0, 11, ""); //20:09:14.322471
    nowTime = nowTime.replaceRange(9, nowTime.length, ""); //20:09:14

    debugPrint("nowDate : $nowDate, nowTime : $nowTime");

    time = time.replaceAll('T', " ");
    String uploadDate = time.replaceRange(11, time.length, "");
    String uploadTime = time.replaceRange(0, 11, "");
    uploadTime = uploadTime.replaceRange(9, uploadTime.length, "");

    debugPrint("uploadDate : $uploadDate, uploadTime : $uploadTime");

    if (nowDate == uploadDate) {
      if (nowTime.replaceRange(2, nowTime.length, "") ==
          uploadTime.replaceRange(2, nowTime.length, "")) {
        // same hour
        String nowTmp = nowTime.replaceRange(0, 3, "");
        nowTmp = nowTmp.replaceRange(2, nowTmp.length, "");
        String uploadTmp = uploadTime.replaceRange(0, 3, "");
        uploadTmp = uploadTmp.replaceRange(2, uploadTmp.length, "");

        debugPrint("nowTmp : $nowTmp, uploadTmp : $uploadTmp");

        if (int.parse(nowTmp) - int.parse(uploadTmp) == 0) {
          return "방금전";
        } else {
          return "${int.parse(nowTmp) - int.parse(uploadTmp)}분전";
        }
      } else if (int.parse(nowTime.replaceRange(2, nowTime.length, "")) -
              int.parse(uploadTime.replaceRange(2, nowTime.length, "")) ==
          1) {
        // different 1 hour
        String nowTmp = nowTime.replaceRange(0, 3, "");
        nowTmp = nowTmp.replaceRange(2, nowTmp.length, "");
        String uploadTmp = uploadTime.replaceRange(0, 3, "");
        uploadTmp = uploadTmp.replaceRange(2, uploadTmp.length, "");

        debugPrint("nowTmp : $nowTmp, uploadTmp : $uploadTmp");

        return "${int.parse(nowTmp) - int.parse(uploadTmp)}분전";
      }
    }

    return time.replaceRange(16, time.length, "");
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        border: Border(
          bottom: BorderSide(
            color: BODY_TEXT_COLOR.withOpacity(0.5),
            width: 1,
          ),
        ),
      ),
      margin: const EdgeInsets.only(
        top: 10,
        left: 10,
        right: 10,
      ),
      child: Padding(
        padding: const EdgeInsets.only(
          bottom: 10,
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                _renderCategoryCircleUi(
                    categoryCodesReverseList[communityTitle] ?? communityTitle),
                Row(
                  children: [
                    TextWithIconForView(
                      icon: Icons.favorite_outline_rounded,
                      iconSize: 15,
                      text: count.likeCount.toString(),
                    ),
                    const SizedBox(
                      width: 13,
                    ),
                    TextWithIconForView(
                      icon: Icons.chat_outlined,
                      iconSize: 15,
                      text: count.commentReplyCount.toString(),
                    ),
                    const SizedBox(
                      width: 13,
                    ),
                    TextWithIconForView(
                      icon: Icons.star_outline_rounded,
                      iconSize: 18,
                      text: count.scrapCount.toString(),
                    ),
                    const SizedBox(
                      width: 13,
                    ),
                    TextWithIconForView(
                      icon: Icons.photo_size_select_actual_outlined,
                      iconSize: 18,
                      text: imageCount.toString(),
                    ),
                    const SizedBox(
                      width: 13,
                    ),
                  ],
                ),
              ],
            ),
            const SizedBox(
              height: 5,
            ),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 10),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    postTitle,
                    style: const TextStyle(
                      fontSize: 12.0,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  Text(
                    postContent,
                    softWrap: false,
                    style: const TextStyle(
                      fontSize: 10,
                    ),
                  ),
                  const SizedBox(
                    height: 5,
                  ),
                  Text(
                    "${changeTime(createdDateTime)} | $userNickname",
                    style: const TextStyle(fontSize: 10),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _renderCategoryCircleUi(String category) {
    return Container(
      // category circle
      decoration: BoxDecoration(
        color: PRIMARY10_COLOR,
        borderRadius: BorderRadius.circular(50),
      ),
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 15, vertical: 3),
        child: Center(
          child: Text(
            category,
            style: const TextStyle(
              fontSize: 10,
            ),
          ),
        ),
      ),
    );
  }
}
