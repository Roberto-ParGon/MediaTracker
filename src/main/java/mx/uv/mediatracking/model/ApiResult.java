package mx.uv.mediatracking.model;

import com.google.gson.annotations.SerializedName;

public class ApiResult {
    private int id;

    @SerializedName(value="title", alternate="name")
    private String title;

    private String overview;
    private String poster_path;
    private String media_type;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public String getPosterPath() { return poster_path; }
    public void setPoster_path(String poster_path) { this.poster_path = poster_path; }

    public String getMediaType() { return media_type; }
    public void setMedia_type(String media_type) { this.media_type = media_type; }
}