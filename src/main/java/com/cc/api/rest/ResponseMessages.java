package com.cc.api.rest;

public interface ResponseMessages {

    String USER_CREATED_SUCCESS = "User created successfully.";

    String USER_UPDATED_SUCCESS = "User updated successfully.";

    String USER_DELETED_SUCCESS = "User deleted successfully.";

    String PWD_REST_SUCCESS = "Password reset successfully.";

    String PWD_EXTEND_SUCCESS = "Password extended successfully.";

    String PWM_PWD_REST_SUCCESS =
            "If you have entered a valid username, an email will be sent to you with password reset instructions. " +
            "If you are still experiencing difficulties, please call technical support at 1-800-423-1150.";

    String PWM_PWD_CHANGE_SUCCESS = "Your password has been changed successfully.";

    String INVALID_RECAPTCHA = "Robot Check Failed!";

    String PWM_INVALID_USER = "Invalid/inactive username!";

    String PWM_INVALID_USER_PWD = "Username and Password does not match!";

    String PWM_NEW_PWD_NOT_MATCH = "New Passwords do not match!";

    String PWM_PWD_RULE_FAIL = "New Password does not meet CoreLogic requirement!";

    String PWM_AUTH_TOKEN_ERROR = "Authentication token error!";

    String PWM_AUTH_TOKEN_TIMEOUT = "Reset password link timeout! \nPlease submit a new password reset request.";

    String SECURITY_PROFILE_CREATED = "Security profile created successfully.";

    String SECURITY_PROFILE_DELETED = "Security profile removed successfully.";

}
