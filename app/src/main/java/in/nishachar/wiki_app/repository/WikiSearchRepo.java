package in.nishachar.wiki_app.repository;

import com.google.gson.JsonObject;

import in.nishachar.wiki_app.model.SearchResponse;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WikiSearchRepo {
    @GET("w/api.php")
    Observable<Response<SearchResponse>> searchWiki(@Query(value = "action") String action, @Query("format") String format, @Query(value = "prop", encoded = true) String prop, @Query("generator") String generator, @Query("redirects") Integer redirects, @Query("formatversion") Integer formatVersion, @Query("piprop") String piProp, @Query("pithumbsize") Integer piThumbSize, @Query("pilimit") Integer piLimit, @Query("wbptterms") String wbptterms, @Query("gpssearch") String gpsSearch, @Query("gpslimit") Integer gpsLimit);

    @GET("w/api.php")
    Observable<Response<JsonObject>> getPage(@Query(value = "action") String action, @Query("format") String format, @Query(value = "prop") String prop, @Query("pageids") Integer pageId, @Query("inprop") String inProp);
}
