package ru.kubsu.geocoder.dto;

/**
 * @param error Ошибка
 * @param path Путь юрл
 * @param status статус ответа
 */
public record RestApiError(
  Integer status,
  String error,
  String path
) {
  public RestApiError() {
    this(0, "", "");
  }
}
