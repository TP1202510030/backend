package com.tp1202510030.backend.iam.domain.model.commands;

import java.util.List;
import java.util.Optional;

public record PatchUserCommand(
        Long userId,
        Optional<String> username,
        Optional<List<String>> roles
) {
}
