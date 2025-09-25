package com.tp1202510030.backend.iam.interfaces.rest.resources;

import java.util.List;
import java.util.Optional;

public record PatchUserResource(Optional<String> username, Optional<List<String>> roles) {
}
