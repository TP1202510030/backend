package com.tp1202510030.backend.iam.domain.model.commands;

import com.tp1202510030.backend.iam.domain.model.valueobjects.ClientType;

public record SignInCommand(String username, String password, ClientType clientType) {
}