package com.eduardoxduardo.vlibrary.client.impl;

import com.eduardoxduardo.vlibrary.client.BooksClient;
import com.eduardoxduardo.vlibrary.dto.google.GoogleApiResponse;
import com.eduardoxduardo.vlibrary.dto.google.GoogleBookItem;
import com.eduardoxduardo.vlibrary.dto.filter.ExternalBookSearchCriteria;
import com.eduardoxduardo.vlibrary.dto.google.VolumeInfo;
import com.eduardoxduardo.vlibrary.dto.response.BookExternalResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GoogleBooksClient implements BooksClient {

    private final RestTemplate restTemplate;

    private final String apiKey;

    private static final String GOOGLE_API_URL = "https://www.googleapis.com/books/v1/volumes";

    public GoogleBooksClient (RestTemplate restTemplate, @Value("${google.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    @Override
    public List<BookExternalResponseDTO> searchBooks(ExternalBookSearchCriteria criteria) {

        String query = buildQueryString(criteria);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(GOOGLE_API_URL)
                .queryParam("q", query)
                .queryParam("key", apiKey)
                .queryParam("maxResults", 10)
                .queryParam("projection", "lite");

        if (criteria.getLanguage() != null && !criteria.getLanguage().isBlank()) {
            uriBuilder.queryParam("langRestrict", criteria.getLanguage());
        }

        String url = uriBuilder.toUriString();

        GoogleApiResponse response = restTemplate.getForObject(url, GoogleApiResponse.class);

        if (response == null || response.getItems() == null) {
            return Collections.emptyList();
        }

        return response.getItems().stream()
                .filter(Objects::nonNull)
                .map(this::convertToExternalDTO)
                .collect(Collectors.toList());

    }

    @Override
    public BookExternalResponseDTO findBookById(String apiId) {
        String url = UriComponentsBuilder.fromUriString(GOOGLE_API_URL + "/" + apiId)
                .queryParam("key", apiKey)
                .toUriString();

        GoogleBookItem item = restTemplate.getForObject(url, GoogleBookItem.class);
        if (item == null) {
            return null;
        }
        return convertToExternalDTO(item);
    }

    private BookExternalResponseDTO convertToExternalDTO(GoogleBookItem item) {
        VolumeInfo info = item.getVolumeInfo();
        if (info == null) {
            return null;
        }

        return new BookExternalResponseDTO(
                item.getId(),
                info.getTitle(),
                info.getAuthors() != null ? info.getAuthors() : Collections.emptyList(),
                info.getDescription(),
                info.getCategories() != null ? info.getCategories() : Collections.emptyList()
        );
    }

    private String buildQueryString(ExternalBookSearchCriteria criteria) {
        List<String> queryParts = new ArrayList<>();

        if (criteria.getTitle() != null && !criteria.getTitle().isBlank()) {
            queryParts.add("intitle:" + criteria.getTitle());
        }
        if (criteria.getAuthor() != null && !criteria.getAuthor().isBlank()) {
            queryParts.add("inauthor:" + criteria.getAuthor());
        }
        if (criteria.getPublisher() != null && !criteria.getPublisher().isBlank()) {
            queryParts.add("inpublisher:" + criteria.getPublisher());
        }
        if (criteria.getCategory() != null && !criteria.getCategory().isBlank()) {
            queryParts.add("subject:" + criteria.getCategory());
        }
        if (criteria.getIsbn() != null && !criteria.getIsbn().isBlank()) {
            queryParts.add("isbn:" + criteria.getIsbn());
        }
        return String.join("+", queryParts);
    }
}
