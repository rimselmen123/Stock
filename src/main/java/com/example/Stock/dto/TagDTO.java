package com.example.Stock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Data Transfer Object for Tag entity.
 * Used for API requests and responses to avoid exposing internal entity structure.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {

    /**
     * Unique identifier for the tag.
     * Null for create requests, populated for responses.
     */
    private UUID tagId;

    /**
     * Name of the tag.
     * Required field with length constraints.
     */
    @NotBlank(message = "Tag name is required")
    @Size(min = 1, max = 50, message = "Tag name must be between 1 and 50 characters")
    private String name;

    /**
     * Constructor for creating DTO without ID (for create requests).
     * 
     * @param name the tag name
     */
    public TagDTO(String name) {
        this.name = name;
    }
}
