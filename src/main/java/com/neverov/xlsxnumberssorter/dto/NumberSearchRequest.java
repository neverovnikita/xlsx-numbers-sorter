package com.neverov.xlsxnumberssorter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос для поиска N-ного минимального числа")
public record NumberSearchRequest(
		@Schema(description = "Абсолютный путь к XLSX файлу", example = "C:/data/numbers.xlsx")
		@NotBlank(message = "FilePath is required")
		String filePath,

		@Schema(description = "Позиция N (начиная с 1)", example = "3")
		@Min(value = 1, message = "Position must be at least 1")
		int position
) {}