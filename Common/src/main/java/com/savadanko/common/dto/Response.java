package com.savadanko.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class Response implements Serializable {
    private final String message;
}
