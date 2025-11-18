package com.neverov.xlsxnumberssorter.controller;

import com.neverov.xlsxnumberssorter.dto.NumberSearchRequest;
import com.neverov.xlsxnumberssorter.service.NumberSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.FileNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/numbers")
@RequiredArgsConstructor
@Tag(name = "Number Search API", description = "Поиск N-ного минимального числа в XLSX файлах")
public class NumberSearchController {

	private final NumberSearchService numberSearchService;

	@PostMapping("/nth-min")
	@Operation(summary = "Найти N-ное минимальное число")
	public ResponseEntity<?> findNthMin(
			@RequestBody @Valid NumberSearchRequest request) {

		log.info("Получен запрос: filePath={}, position={}",
				request.filePath(), request.position());

		try {
			Double result = numberSearchService.findNthMin(
					request.filePath(),
					request.position()
			);

			log.info("Результат: {} для position={}", result, request.position());
			return ResponseEntity.ok(result);

		} catch (FileNotFoundException e) {
			log.warn("Файл не найден: {}", request.filePath());
			return ResponseEntity.notFound().build();
		} catch (IllegalArgumentException e) {
			log.warn("Ошибка валидации: {}", e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			log.error("Ошибка обработки файла: {}", e.getMessage(), e);
			return ResponseEntity.internalServerError()
					.body("Ошибка обработки файла: " + e.getMessage());
		}
	}
}