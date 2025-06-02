package com.tp1202510030.backend.iam.infrastructure.hashing.bcrypt;

import com.tp1202510030.backend.iam.application.internal.outboundservices.hashing.HashingService;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface BCryptHashingService extends HashingService, PasswordEncoder {
}
