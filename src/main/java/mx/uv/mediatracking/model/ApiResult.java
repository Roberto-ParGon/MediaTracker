package mx.uv.mediatracking.model;

import com.google.gson.annotations.SerializedName;

public class ApiResult {
    private final int id;

    @SerializedName(value="title", alternate="name")
    private String title;

    private final String overview;
    private final String poster_path;
    private final String media_type;

    public ApiResult(int id, String overview, String posterPath, String mediaType) {
        this.id = id;
        this.overview = overview;
        poster_path = posterPath;
        media_type = mediaType;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getOverview() { return overview; }
    public String getPosterPath() { return poster_path; }
    public String getMediaType() { return media_type; }
}