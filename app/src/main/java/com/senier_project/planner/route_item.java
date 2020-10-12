package com.senier_project.planner;

public class route_item {

    private String total_time ;
    private String startname;
    private String finalname;

    public void setTotal_time(String Total_time) {
        total_time = Total_time ;
    }
    public void setStartname(String start) {
        startname = start ;
    }

    public void setFinalname(String dest) {
        finalname = dest ;
    }

    public String getTotal_time() {
        return this.total_time ;
    }

    public String getStartname() {
        return this.startname;
    }
    public String getFinalname() {
        return this.finalname ;
    }

}
