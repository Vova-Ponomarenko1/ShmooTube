package org.example.Video;

public class VideoSegment {

    private Long id;
    private Long videoId;
    private int segmentNumber;
    private byte[] segmentData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public int getSegmentNumber() {
        return segmentNumber;
    }

    public void setSegmentNumber(int segmentNumber) {
        this.segmentNumber = segmentNumber;
    }

    public byte[] getSegmentData() {
        return segmentData;
    }

    public void setSegmentData(byte[] segmentData) {
        this.segmentData = segmentData;
    }
}


