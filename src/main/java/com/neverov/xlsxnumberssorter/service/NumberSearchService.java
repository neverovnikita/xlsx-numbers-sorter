package com.neverov.xlsxnumberssorter.service;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class NumberSearchService {

	public Double findNthMin(String filePath, int n) throws Exception {
		log.info("Поиск {} минимального числа в файле: {}", n, filePath);

		Set<Double> numbers = readExcelFile(filePath);

		if (numbers.isEmpty()) {
			throw new IllegalArgumentException("Файл не содержит чисел");
		}

		if (n > numbers.size()) {
			throw new IllegalArgumentException(
					"N не может быть больше количества чисел в файле (" + numbers.size() + ")"
			);
		}

		return quickSelect(new ArrayList<>(numbers), n);
	}

	private Set<Double> readExcelFile(String filePath) throws Exception {
		Set<Double> numbers = new HashSet<>();
		if (!filePath.toLowerCase().endsWith(".xlsx")) {
			throw new IllegalArgumentException("Поддерживаются только .xlsx файлы");
		}

		try (Workbook workbook = WorkbookFactory.create(new File(filePath))) {
			Sheet sheet = workbook.getSheetAt(0);

			for (Row row : sheet) {
				if (row == null) continue;

				Cell cell = row.getCell(0);
				if (cell != null) {
					Double value = extractNumericValue(cell);
					if (value != null) {
						numbers.add(value);
					}
				}
			}
		}

		log.info("Прочитано {} чисел из файла", numbers.size());
		return numbers;
	}

	private Double extractNumericValue(Cell cell) {
		switch (cell.getCellType()) {
			case NUMERIC:
				return cell.getNumericCellValue();
			case STRING:
				try {
					return Double.parseDouble(cell.getStringCellValue().trim());
				} catch (NumberFormatException e) {
					return null;
				}
			default:
				return null;
		}
	}
	private Double quickSelect(List<Double> uniqueNumbers, int k) {
		return quickSelect(uniqueNumbers, 0, uniqueNumbers.size() - 1, k - 1);
	}

	private Double quickSelect(List<Double> numbers, int left, int right, int k) {
		if (left == right) {
			return numbers.get(left);
		}

		int pivotIndex = partition(numbers, left, right);

		if (k == pivotIndex) {
			return numbers.get(k);
		} else if (k < pivotIndex) {
			return quickSelect(numbers, left, pivotIndex - 1, k);
		} else {
			return quickSelect(numbers, pivotIndex + 1, right, k);
		}
	}

	private int partition(List<Double> numbers, int left, int right) {
		double pivot = numbers.get(right);
		int i = left;

		for (int j = left; j < right; j++) {
			if (numbers.get(j) <= pivot) {
				swap(numbers, i, j);
				i++;
			}
		}

		swap(numbers, i, right);
		return i;
	}

	private void swap(List<Double> numbers, int i, int j) {
		double temp = numbers.get(i);
		numbers.set(i, numbers.get(j));
		numbers.set(j, temp);
	}

}
