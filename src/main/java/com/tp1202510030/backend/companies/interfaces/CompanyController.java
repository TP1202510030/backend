package com.tp1202510030.backend.companies.interfaces;

import com.tp1202510030.backend.companies.domain.model.aggregates.Company;
import com.tp1202510030.backend.companies.domain.model.commands.company.DeleteCompanyCommand;
import com.tp1202510030.backend.companies.domain.model.queries.company.GetAllCompaniesQuery;
import com.tp1202510030.backend.companies.domain.model.queries.company.GetCompanyByIdQuery;
import com.tp1202510030.backend.companies.domain.services.company.CompanyCommandService;
import com.tp1202510030.backend.companies.domain.services.company.CompanyQueryService;
import com.tp1202510030.backend.companies.interfaces.rest.resources.company.CompanyResource;
import com.tp1202510030.backend.companies.interfaces.rest.resources.company.CreateCompanyResource;
import com.tp1202510030.backend.companies.interfaces.rest.resources.company.PatchCompanyResource;
import com.tp1202510030.backend.companies.interfaces.rest.transform.company.CompanyResourceFromEntityAssembler;
import com.tp1202510030.backend.companies.interfaces.rest.transform.company.CreateCompanyCommandFromResourceAssembler;
import com.tp1202510030.backend.companies.interfaces.rest.transform.company.PatchCompanyCommandFromResourceAssembler;
import com.tp1202510030.backend.shared.infrastructure.authorization.SecurityConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/companies", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearerAuth")
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
    @PreAuthorize(SecurityConstants.IS_ADMIN)
    public ResponseEntity<CompanyResource> createCompany(@RequestBody CreateCompanyResource createCompanyResource) {
        var createCompanyCommand = CreateCompanyCommandFromResourceAssembler.toCommandFromResource(createCompanyResource);

        var companyOptional = companyCommandService.handle(createCompanyCommand);

        if (companyOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var companyResource = CompanyResourceFromEntityAssembler.toResourceFromEntity(companyOptional.get());
        return ResponseEntity.ok(companyResource);
    }

    /*
     * Update company
     *
     * @param companyId The company id
     * @param resource  The {@link UpdateCompanyResource} instance
     * @return The {@link CompanyResource} resource for the updated company
    @PutMapping("/{companyId}")
    @Operation(summary = "Update company", description = "Update company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company updated"),
            @ApiResponse(responseCode = "404", description = "Company not found")})
    @PreAuthorize(SecurityConstants.COMPANY_ADMIN_OR_HIGHER_AND_OWNER)
    public ResponseEntity<CompanyResource> updateCompany(@PathVariable Long companyId, @RequestBody UpdateCompanyResource resource) {
        var updateCompanyCommand = UpdateCompanyCommandFromResourceAssembler.toCommandFromResource(companyId, resource);
        var updatedCompany = companyCommandService.handle(updateCompanyCommand);

        if (updatedCompany.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var updatedCompanyEntity = updatedCompany.get();
        var updatedCompanyResource = CompanyResourceFromEntityAssembler.toResourceFromEntity(updatedCompanyEntity);
        return ResponseEntity.ok(updatedCompanyResource);
    }
     */

    @PatchMapping("/{companyId}")
    @Operation(
            summary = "Patch a company",
            description = "Updates one or more properties of a company. Only fields provided in the request body will be updated. This action is only available to administrators."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Forbidden. User is not an admin."),
            @ApiResponse(responseCode = "404", description = "Company not found")
    })
    @PreAuthorize(SecurityConstants.IS_ADMIN)
    public ResponseEntity<CompanyResource> patchCompany(
            @PathVariable Long companyId,
            @RequestBody PatchCompanyResource resource
    ) {
        var command = PatchCompanyCommandFromResourceAssembler.toCommandFromResource(companyId, resource);
        var patchedCompany = companyCommandService.handle(command);

        if (patchedCompany.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var companyResource = CompanyResourceFromEntityAssembler.toResourceFromEntity(patchedCompany.get());
        return ResponseEntity.ok(companyResource);
    }

    @GetMapping("/{companyId}")
    @Operation(
            summary = "Get company by ID",
            description = "Retrieves a company by the provided ID.",
            tags = {"Companies"}
    )
    @PreAuthorize(SecurityConstants.ADMIN_OR_COMPANY_OWNER)
    public ResponseEntity<CompanyResource> getCompanyById(@PathVariable Long companyId) {
        var getCompanyByIdQuery = new GetCompanyByIdQuery(companyId);
        var company = companyQueryService.handle(getCompanyByIdQuery);

        if (company.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var companyResource = CompanyResourceFromEntityAssembler.toResourceFromEntity(company.get());
        return ResponseEntity.ok(companyResource);
    }

    /**
     * Get all companies
     *
     * @return List of CompanyResources
     */
    @GetMapping()
    @Operation(
            summary = "Get all companies",
            description = "Retrieves a list of paginated companies.",
            tags = {"Companies"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Companies retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompanyResource.class))),
            @ApiResponse(responseCode = "204", description = "No companies found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized.")
    })
    @PreAuthorize(SecurityConstants.IS_ADMIN)
    public ResponseEntity<Page<CompanyResource>> getAllCompanies(@ParameterObject Pageable pageable) {
        var query = new GetAllCompaniesQuery();
        Page<Company> companies = companyQueryService.handle(query, pageable);

        var resources = companies
                .map(CompanyResourceFromEntityAssembler::toResourceFromEntity);

        return ResponseEntity.ok(resources);
    }

    @DeleteMapping("/{companyId}")
    @Operation(
            summary = "Delete a company by ID",
            description = "Deletes a company identified by the provided ID. This action will remove all associated data.",
            tags = {"Companies"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Company deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompanyResource.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.")
    })
    @PreAuthorize(SecurityConstants.IS_ADMIN)
    public ResponseEntity<Void> deleteCompany(@PathVariable Long companyId) {
        companyCommandService.handle(new DeleteCompanyCommand(companyId));
        return ResponseEntity.noContent().build();
    }


}
