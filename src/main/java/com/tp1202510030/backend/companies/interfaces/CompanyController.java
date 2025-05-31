package com.tp1202510030.backend.companies.interfaces;

import com.tp1202510030.backend.companies.domain.model.queries.company.GetCompanyByIdQuery;
import com.tp1202510030.backend.companies.domain.services.company.CompanyCommandService;
import com.tp1202510030.backend.companies.domain.services.company.CompanyQueryService;
import com.tp1202510030.backend.companies.interfaces.rest.resources.company.CompanyResource;
import com.tp1202510030.backend.companies.interfaces.rest.resources.company.CreateCompanyResource;
import com.tp1202510030.backend.companies.interfaces.rest.resources.company.UpdateCompanyResource;
import com.tp1202510030.backend.companies.interfaces.rest.transform.company.CompanyResourceFromEntityAssembler;
import com.tp1202510030.backend.companies.interfaces.rest.transform.company.CreateCompanyCommandFromResourceAssembler;
import com.tp1202510030.backend.companies.interfaces.rest.transform.company.UpdateCompanyCommandResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/companies", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Companies", description = "Companies Management Endpoints")
public class CompanyController {
    private final CompanyQueryService companyQueryService;
    private final CompanyCommandService companyCommandService;

    public CompanyController(CompanyQueryService companyQueryService, CompanyCommandService companyCommandService) {
        this.companyQueryService = companyQueryService;
        this.companyCommandService = companyCommandService;
    }

    /**
     * Create a company
     *
     * @param createCompanyResource The company to be created
     * @return The { @link CompanyResource} resource for the created company
     */
    @PostMapping
    @Operation(
            summary = "Create a new company",
            description = "Creates a new company with the provided details and returns the created company resource.",
            tags = {"Companies"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Company created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompanyResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or unable to create company",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<CompanyResource> createCompany(@RequestBody CreateCompanyResource createCompanyResource) {
        var createCompanyCommand = CreateCompanyCommandFromResourceAssembler.toCommandFromResource(createCompanyResource);
        var companyId = companyCommandService.handle(createCompanyCommand);

        if (companyId == 0L) {
            return ResponseEntity.badRequest().build();
        }

        var getCompanyByIdQuery = new GetCompanyByIdQuery(companyId);
        var company = companyQueryService.handle(getCompanyByIdQuery);

        if (company.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var companyResource = CompanyResourceFromEntityAssembler.toResourceFromEntity(company.get());
        return ResponseEntity.ok(companyResource);
    }

    /**
     * Update company
     *
     * @param companyId The company id
     * @param resource    The {@link UpdateCompanyResource} instance
     * @return The {@link CompanyResource} resource for the updated company
     */
    @PutMapping("/{companyId}")
    @Operation(summary = "Update company", description = "Update company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company updated"),
            @ApiResponse(responseCode = "404", description = "Company not found")})
    public ResponseEntity<CompanyResource> updateCompany(@PathVariable Long companyId, @RequestBody UpdateCompanyResource resource) {
        var updateCompanyCommand = UpdateCompanyCommandResourceFromEntityAssembler.toCommandFromResource(companyId, resource);
        var updatedCompany = companyCommandService.handle(updateCompanyCommand);

        if (updatedCompany.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var updatedCompanyEntity = updatedCompany.get();
        var updatedCompanyResource = CompanyResourceFromEntityAssembler.toResourceFromEntity(updatedCompanyEntity);
        return ResponseEntity.ok(updatedCompanyResource);
    }

    @GetMapping
    @Operation(
            summary = "Get company by ID",
            description = "Retrieves a company by the provided ID.",
            tags = {"Companies"}
    )
    public ResponseEntity<CompanyResource> getCompanyById(@RequestParam Long id) {
        var getCompanyByIdQuery = new GetCompanyByIdQuery(id);
        var company = companyQueryService.handle(getCompanyByIdQuery);

        if (company.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var companyResource = CompanyResourceFromEntityAssembler.toResourceFromEntity(company.get());
        return ResponseEntity.ok(companyResource);
    }

}
