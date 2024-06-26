import 'package:dio/dio.dart' hide Headers;
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:frontend/board/model/msg_board_detail_response_model.dart';
import 'package:frontend/common/provider/dio_provider.dart';
import 'package:retrofit/http.dart';

import '../../common/const/data.dart';
import '../model/msg_board_response_model.dart';

part 'board_add_provider.g.dart';

final boardAddProvider = Provider<BoardAdd>((ref) {
  final dio = ref.watch(dioProvider);

  return BoardAdd(dio, baseUrl: ip);
});

@RestApi()
abstract class BoardAdd {
  factory BoardAdd(Dio dio, {String baseUrl}) = _BoardAdd;

  @POST('/api/post')
  @Headers({
    'accessToken': 'true',
  })
  Future<MsgBoardResponseModel> post(
    @Body() Map<String, dynamic> data,
  );

  @PUT('/api/post/{postId}')
  @Headers({
    'accessToken': 'true',
  })
  Future<void> heart(
    @Path('postId') int postId,
  );

  @DELETE('/api/post/{postId}')
  @Headers({
    'accessToken': 'true',
  })
  Future<void> delete(
    @Path('postId') int postId,
  );

  @PUT('/api/post')
  @Headers({
    'accessToken': 'true',
  })
  Future<MsgBoardResponseModel> modify(
    @Body() Map<String, dynamic> data,
  );

  @GET('/api/post/{postId}')
  @Headers({
    'accessToken': 'true',
  })
  Future<MsgBoardDetailResponseModel> get(
    @Path('postId') int postId,
  );
}
