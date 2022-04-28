package com.easysteps.model.dailystepshistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetMyStepsHistoryRes {

@SerializedName("status")
@Expose
private Integer status;
@SerializedName("message")
@Expose
private String message;
@SerializedName("data")
@Expose
private List<GetMyStepsHistoryData> data = null;

public Integer getStatus() {
return status;
}

public void setStatus(Integer status) {
this.status = status;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public List<GetMyStepsHistoryData> getData() {
return data;
}

public void setData(List<GetMyStepsHistoryData> data) {
this.data = data;
}

}
