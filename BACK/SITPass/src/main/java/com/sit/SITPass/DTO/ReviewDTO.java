package com.sit.SITPass.DTO;


public class ReviewDTO {

    private Long userId;
    private CommentDTO commentDTO;
    private RateDTO rateDTO;
    private Long facilityId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public CommentDTO getCommentDTO() {
        return commentDTO;
    }

    public void setCommentDTO(CommentDTO commentDTO) {
        this.commentDTO = commentDTO;
    }

    public RateDTO getRateDTO() {
        return rateDTO;
    }

    public void setRateDTO(RateDTO rateDTO) {
        this.rateDTO = rateDTO;
    }

    public Long getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Long facilityId) {
        this.facilityId = facilityId;
    }

    public ReviewDTO() {
    }

    public ReviewDTO(Long userId, RateDTO rateDTO, Long facilityId) {
        this.userId = userId;
        this.rateDTO = rateDTO;
        this.facilityId = facilityId;
    }

    public ReviewDTO(Long userId, CommentDTO commentDTO, RateDTO rateDTO, Long facilityId) {
        this.userId = userId;
        this.commentDTO = commentDTO;
        this.rateDTO = rateDTO;
        this.facilityId = facilityId;
    }



}
