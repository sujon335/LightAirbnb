package com.example.sujon335.loginregister;

/**
 * Created by sujon335 on 2/10/18.
 */

public class BookingRequest {
    int RequestID;
    String ReqRoomTitle;
    String ReqRoomLocation;
    String Requestedby;
    String Guestemail;
    String RequestingTime;

    public BookingRequest(int RequestId,String reqRoomTitle, String reqRoomLocation, String requestedby, String guestemail, String requestingTime) {
        RequestID=RequestId;
        ReqRoomTitle = reqRoomTitle;
        ReqRoomLocation = reqRoomLocation;
        Requestedby = requestedby;
        Guestemail = guestemail;
        RequestingTime = requestingTime;
    }

    public int getRequestID() {
        return RequestID;
    }

    public void setRequestID(int requestID) {
        RequestID = requestID;
    }

    public String getReqRoomTitle() {
        return ReqRoomTitle;

    }

    public void setReqRoomTitle(String reqRoomTitle) {
        ReqRoomTitle = reqRoomTitle;
    }

    public String getReqRoomLocation() {
        return ReqRoomLocation;
    }

    public void setReqRoomLocation(String reqRoomLocation) {
        ReqRoomLocation = reqRoomLocation;
    }

    public String getRequestedby() {
        return Requestedby;
    }

    public void setRequestedby(String requestedby) {
        Requestedby = requestedby;
    }

    public String getGuestemail() {
        return Guestemail;
    }

    public void setGuestemail(String guestemail) {
        Guestemail = guestemail;
    }

    public String getRequestingTime() {
        return RequestingTime;
    }

    public void setRequestingTime(String requestingTime) {
        RequestingTime = requestingTime;
    }
}
