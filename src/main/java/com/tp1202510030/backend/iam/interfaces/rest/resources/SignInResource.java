package com.tp1202510030.backend.iam.interfaces.rest.resources;

import com.tp1202510030.backend.iam.domain.model.valueobjects.ClientType;

public record SignInResource(String username, String password, ClientType clientType) {

}