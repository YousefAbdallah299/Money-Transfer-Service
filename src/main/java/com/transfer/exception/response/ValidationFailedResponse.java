package com.transfer.exception.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@Builder
public class ValidationFailedResponse {
    private final List<ViolationErrors> violationErrosList = new ArrayList<>();
    private final LocalDateTime localDateTime;
    private final HttpStatus httpStatus;
}
