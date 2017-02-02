package android.hua.gr.gpstracker;

class User {
    /**
     * The id
     */
    private int id;

    /**
     * The user's id
     */
    private String userId;

    /**
     * The user's longitude
     */
    private float longitude;

    /**
     * The user's latitude
     */
    private float latitude;

    /**
     * The date and time in which the user's location has been register
     */
    private String dt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    String getUserId() {
        return userId;
    }

    void setUserId(String userId) {
        this.userId = userId;
    }

    float getLongitude() {
        return longitude;
    }

    void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }
}
