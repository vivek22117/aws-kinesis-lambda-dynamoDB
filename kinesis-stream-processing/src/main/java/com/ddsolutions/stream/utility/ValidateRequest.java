package com.ddsolutions.stream.utility;

import com.ddsolutions.stream.domain.UserRequest;

import java.time.Instant;
import java.util.Objects;

import static com.amazonaws.util.StringUtils.isNullOrEmpty;


public class ValidateRequest {

    public boolean validateRequest(UserRequest userRequest) {
        if (userRequest.isLastReporting()) {
            return validateLastKnownRequestParameters(userRequest);
        }

        return (validateTimeInterval(userRequest) && validateCount(userRequest));
    }

    private boolean validateLastKnownRequestParameters(UserRequest userRequest) {
        return (isNullOrEmpty(userRequest.getStartTime()) && isNullOrEmpty(userRequest.getEndTime())
                && (Objects.isNull(userRequest.getCount())));
    }

    private boolean validateTimeInterval(UserRequest userRequest) {
        if (!isNullOrEmpty(userRequest.getStartTime()) && !isNullOrEmpty(userRequest.getEndTime())) {
            return !Instant.parse(userRequest.getStartTime()).isAfter(Instant.parse(userRequest.getEndTime()));
        }
        return true;
    }

    private boolean validateCount(UserRequest userRequest) {
        if (userRequest.getCount() <= 0) {
            return false;
        } else return userRequest.getCount() <= 250;
    }
}
