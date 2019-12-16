package com.example.kormoran.apihelper;

import com.example.kormoran.data.ResponseComment;
import com.example.kormoran.data.ResponseCommentLike;
import com.example.kormoran.data.ResponseDetailQuestion;
import com.example.kormoran.data.ResponseDetailUser;
import com.example.kormoran.data.ResponseEditProfile;
import com.example.kormoran.data.ResponseKategori;
import com.example.kormoran.data.ResponsePostComment;
import com.example.kormoran.data.ResponsePostQuestion;
import com.example.kormoran.data.ResponseQuestion;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseApiService {

    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> loginRequest(@Field("user_name") String user_name,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Call<ResponseBody> registerRequest(@Field("name") String name,
                                       @Field("user_name") String user_name,
                                       @Field("email") String email,
                                       @Field("password") String password,
                                       @Field("c_password") String c_password);

    @Multipart
    @POST("user/editProfile")
    Call<ResponseEditProfile> editProfileRequest(@Part MultipartBody.Part image,
                                                 @Part("id") RequestBody id,
                                                 @Part("user_name") RequestBody userName,
                                                 @Part("name") RequestBody name);

    @GET("kategori/getAllKategori")
    Call<ResponseKategori> getAllKategori();


    @Multipart
    @POST("pertanyaan/postPertanyaan")
    Call<ResponsePostQuestion> postingQuestionRequest(@Part MultipartBody.Part image,
                                                      @Part("user_id") RequestBody userId,
                                                      @Part("question")  RequestBody question,
                                                      @Part("kategori_id")  RequestBody kategori);

    @POST("pertanyaan/showTrendingPertanyaan")
    Call<ResponseQuestion> getTrendingQuestion();


    @POST("pertanyaan/showLatestPertanyaan")
    Call<ResponseQuestion> getLatestQuestion();


    @GET("pertanyaan/showDetailPertanyaan/{id}")
    Call<ResponseDetailQuestion> getDetailQuestion(@Path("id") int id);

    @POST("pertanyaan/onClickIncrease/{id}")
    Call<ResponseBody> postOnClickIncrease(@Path("id") int id);

    @POST("pertanyaan/postJawaban")
    Call<ResponsePostComment> postingCommentRequest(@Query("pertanyaan_id") int pertanyaan_id,
                                                    @Query("user_id") String userId,
                                                    @Query("jawaban") String jawaban);

    @GET("pertanyaan/showJawaban/{id}")
    Call<ResponseComment> getComment(@Path("id") int id);

    @POST("pertanyaan/showJawaban/like/{id}")
    Call<ResponseCommentLike> postCommentLike(@Path("id") int id,
                                          @Query("user_id") String userId);

    @POST("pertanyaan/showPertanyaanPerKategori/{id}")
    Call<ResponseQuestion> getPerKategoriQuestion(@Path("id") int id);

    @POST("user/historyQuestionActivity/{id}")
    Call<ResponseQuestion> getUserQuestionHistory(@Path("id") String id);

    @POST("user/historyAnswerActivity/{id}")
    Call<ResponseComment> getUserCommentHistory(@Path("id") String id);

    @POST("user/historyAnswerActivity/delete/{id}")
    Call<ResponseComment> deleteComment(@Path("id") int id);

    @POST("user/historyQuestionActivity/delete/{id}")
    Call<ResponseQuestion> deleteQuestion(@Path("id") int id);

    @GET("user/historyQuestionActivity/edit/{id}")
    Call<ResponseDetailQuestion> showEditQuestion(@Path("id") int id);

    @POST("user/historyQuestionActivity/update/{id}")
    Call<ResponseQuestion> editQuestion(@Path("id") int id,
                                        @Query("pertanyaan") String question,
                                        @Query("kategori_id") int kategori);

    @GET("user/countQuestionAndAnswer/{id}")
    Call<ResponseDetailUser> countQuestionAndAnswer(@Path("id") String id);
}
