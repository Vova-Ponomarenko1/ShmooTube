package org.example.Video;

public class Thumbnail {
    private long id;
    private byte[] thumbnail;

    private String dataUri;

    private String title;

    private String userAvatarBase64;

    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserAvatarBase64() {
        return userAvatarBase64;
    }

    public void setUserAvatarBase64(String userAvatarBase64) {
        this.userAvatarBase64 = userAvatarBase64;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDataUri() {
        return dataUri;
    }

    public void setDataUri(String dataUri) {
        this.dataUri = dataUri;
    }
}
