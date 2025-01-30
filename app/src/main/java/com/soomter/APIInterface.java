package com.soomter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIInterface {

    //   @GET("/api/unknown")
//    Call<MultipleResource> doGetListResources();

    @POST("api/LoginSoomter/")
    Call<UserData> loginUser(@Body UserLogin user);
    @POST("api/RETRIVEACCOUNT/")
    Call<AccountRecoverData> recoverAccount(@Body AccountRecover user);
    @POST("api/RegisterCompany/")
    Call<CompanyRegData> createCompnay(@Body CompanyReg company);
    @POST("api/RegisterProfile/")
    Call<CompanyRegData> createProfile(@Body ProfileReg profileReg);
    @POST("api/GetAllCities/")
    Call<CitiesData> getCitites(@Body CitiesRequest citiesRequest);
    @POST("api/GetPortalBusinessCategories/")
    Call<PortalCatgData> getPortalCatg(@Body PortalCatgRequest portalCatgRequest);
    @FormUrlEncoded
    @POST("api/GetBusinessCategoriesByName/")
    Call<PortalCatgData> getBusinessCategoriesByNam(@Field("Name") String Name , @Field("Lang") String lang);
    @POST("api/GetMostWatchedCompanies/")
    Call<MostWatchedData> getMostWatchedCatgs(@Body MostWatchedCatgRequest portalCatgRequest);
    @POST("api/GetSubBusinessCategories/")
    Call<BussinesCatgData> getBussinesCatg(@Body BussinessCatgsRequest bussinessCatgsRequest);
    @POST("api/GetCompaniesByCategories/")
    Call<CompaniesData> getCompanies(@Body CompaniesRequest companiesRequest);
    @POST("api/Search/")
    Call<CompaniesData> searchCompanies(@Body CompaniesSearchRequest companiesRequest);
    @POST("api/CompanyProfile/")
    Call<CompanyProfileData> getCompanyProfile(@Body CompanyProfileRequest companiesRequest);
    @POST("api/GetSoomterAdsStatistics/")
    Call<AdsStatisticsData> getAdsStatistics(@Body AdsStatisticsRequest adsStatisticsRequest);
    @POST("api/GetSoomterAds/")
    Call<AdsData> getAds(@Body AdsRequest adsData);
    @FormUrlEncoded
    @POST("api/GetTopDownAds/")
    Call<TopDownAdsData> getTopDownAds(@Field("PlaceId") int PlaceId);
    @POST("api/GetAdsDetails/")
    Call<AdsDetailData> getAdDetails(@Body AdsDetailsRequest adsDetailsRequest);
    @POST("api/SendAdReportRequest/")
    Call<Void> reportAd(@Body AdReportRequest adReportRequest);
    @POST("api/GETEVENTS/")
    Call<EventsData> getEvents(@Body EventsRequest eventsRequest);
    @POST("API/GETEVENTSCOUNT/")
    Call<EventsCountsData> getEventsCount(@Body EventsCountsRequest eventsCountsRequest);
    @FormUrlEncoded
    @POST("API/GETEVENTSBYID/")
    Call<EventDetailData> getEventDetails(@Field("Id") int Id , @Field("lang") String lang);
    @POST("api/GetProductCategories/")
    Call<ProductsCatgData> getProductsCatgs(@Body PortalCatgRequest portalCatgRequest);
    @POST("api/GetProductSubCategories/")
    Call<ProductSubCatgsData> getProductsSubCatgs(@Body ProductSubCatgRequest portalCatgRequest);
    @POST("api/GetProductTrademarks/")
    Call<ProductTradeMarksData> getProductTrademarks(@Body PortalCatgRequest portalCatgRequest);
    @POST("api/GetBestSellingProducts/")
    Call<BestSellingData> getBestSellProds(@Body BestSellingProdsRequest bestSellingProdsRequest);
    @POST("api/GetBestTradeMarks/")
    Call<ProductsCatgData> getBestTradeMarks(@Body PortalCatgRequest request);
    @POST("api/GETPRODUCTS/")
    Call<BestSellingData> searchProducts(@Body ProdsSearchRequest request);
    @POST("api/GetBestCategories/")
    Call<ProductsCatgData> getBestCategories(@Body PortalCatgRequest request);
    @POST("api/GetBestCategories/")
    Call<PromotedProductsData> getPromotedProduct(@Body PortalCatgRequest request);
    @FormUrlEncoded
    @POST("api/FlowCompany/")
    Call<Void> followCompany(@Field("CompanyId") int CompanyId , @Field("UserId") String UserId);
    @POST("API/GetProductDetails/")
    Call<ProductDetailData> getProductDetails(@Body ProductDetailsRequest request);
    //@GET("/api/users?")
    //Call<UserList> doGetUserList(@Query("page") String page);

    //@FormUrlEncoded
    //@POST("/api/users?")
    // Call<UserList> doCreateUserWithField(@Field("name") String name, @Field("job") String job);
    @POST("api/ALLNOTIFICATIONS/")
    Call<NotificationsData> getNotifications(@Body NotificationsRequest notif);

    @POST("api/ADDFAVPRODUCT/")
    Call<ProductFavData> addFavProduct(@Body ProductFavRequest request);
    @POST("api/REMOVEFAVPRODUCT/")
    Call<ProductFavData> removeFavProduct(@Body ProductFavRequest request);

    @POST("api/ADDTOCART/")
    Call<Void> addToCart(@Body ProductCartRequest request);
    @FormUrlEncoded
    @POST("api/REMOVEFROMCART/")
    Call<Void> removeFromCart(@Field("CartItemId") int CartItemId , @Field("UserId") String UserId);
    @FormUrlEncoded
    @POST("api/LOADCART/")
    Call<ProductCartData> loadCart(@Field("Lang") String Lang , @Field("UserId") String UserId);
    @FormUrlEncoded
    @POST("api/PROCESSCART/")
    Call<Void> processCart(@Field("Lang") String Lang , @Field("UserId") String UserId);
    @FormUrlEncoded
    @POST("api/GETSHIPPINGADDRESSES/")
    Call<ProductAddreseData> getAddresses(@Field("Lang") String Lang , @Field("UserId") String UserId);
    @POST("api/ADDSHIPPINGADDRESSES/")
    Call<Void> addAddress(@Body AddAddressRequest request);

    @FormUrlEncoded
    @POST("api/GetProductCategoriesTree/")
    Call<ProductCatgsTreeData> getProductCatgsTree(@Field("lang") String lang);
    @FormUrlEncoded
    @POST("api/GetEventTypes/")
    Call<ProductTradeMarksData> getEventTyps(@Field("lang") String lang);
    @POST("api/SendGeneralCompanyRequest/")
    Call<Void> sendGeneralCompanyRequest(@Body GeneralCompRequest generalCompRequest);
    @FormUrlEncoded
    @POST("api/SendEventAttadenceRequest/")
    Call<Void> registerToEvent(@Field("EventId") int EventId , @Field("Name") String Name , @Field("Phone") String Phone);
}