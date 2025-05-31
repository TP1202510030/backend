package com.tp1202510030.backend.companies.domain.model.commands.company;

public record CreateCompanyCommand(String name, Integer taxIdentificationNumber) {
}
