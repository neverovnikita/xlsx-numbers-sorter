package com.neverov.xlsxnumberssorter.service;

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

		List<Double> numbers = readExcelFile(filePath);

		if (numbers.isEmpty()) {
			throw new IllegalArgumentException("Файл не содержит чисел");
		}

		if (n > numbers.size()) {
			throw new IllegalArgumentException(
					"N не может быть больше количества чисел в файле (" + numbers.size() + ")"
			);
		}

		return quickSelect(numbers, n);
	}

	private List<Double> readExcelFile(String filePath) throws Exception {
		List<Double> numbers = new ArrayList<>();

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
	private Double quickSelect(List<Double> numbers, int k) {
		double[] arr = numbers.stream().mapToDouble(i -> i).toArray();
		return quickSelect(arr, 0, arr.length - 1, k - 1);
	}

	private double quickSelect(double[] arr, int left, int right, int k) {
		if (left == right) {
			return arr[left];
		}

		int pivotIndex = partition(arr, left, right);

		if (k == pivotIndex) {
			return arr[k];
		} else if (k < pivotIndex) {
			return quickSelect(arr, left, pivotIndex - 1, k);
		} else {
			return quickSelect(arr, pivotIndex + 1, right, k);
		}
	}

	private int partition(double[] arr, int left, int right) {
		double pivot = arr[right];
		int i = left;

		for (int j = left; j < right; j++) {
			if (arr[j] <= pivot) {
				swap(arr, i, j);
				i++;
			}
		}

		swap(arr, i, right);
		return i;
	}

	private void swap(double[] arr, int i, int j) {
		double temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

}
