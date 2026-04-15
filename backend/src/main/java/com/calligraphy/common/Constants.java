package com.calligraphy.common;

public class Constants {
    public static final String JWT_SECRET = "calligraphy2024SecretKey!@#$%";
    public static final long JWT_EXPIRATION = 24 * 60 * 60 * 1000; // 24 hours

    // User roles
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_ACTIVITY_ADMIN = "ACTIVITY_ADMIN";

    // Status
    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_DISABLED = 0;

    // Like types
    public static final int LIKE = 1;
    public static final int DISLIKE = 2;

    // Complaint status
    public static final int COMPLAINT_PENDING = 0;
    public static final int COMPLAINT_PROCESSED = 1;
    public static final int COMPLAINT_REJECTED = 2;

    // Registration status
    public static final int REG_PENDING = 0;
    public static final int REG_APPROVED = 1;
    public static final int REG_REJECTED = 2;
    public static final int REG_CANCELLED = 3;

    // Target types for comments and likes
    public static final String TARGET_WORK = "work";
    public static final String TARGET_ACTIVITY = "activity";
    public static final String TARGET_VIDEO = "video";
}
