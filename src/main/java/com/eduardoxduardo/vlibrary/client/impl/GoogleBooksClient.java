package com.eduardoxduardo.vlibrary.client.impl;

import com.eduardoxduardo.vlibrary.client.BooksClient;
import com.eduardoxduardo.vlibrary.dto.google.GoogleApiResponse;
import com.eduardoxduardo.vlibrary.dto.google.GoogleBookItem;
import com.eduardoxduardo.vlibrary.dto.google.VolumeInfo;
import com.eduardoxduardo.vlibrary.dto.response.BookExternalResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
    public List<BookExternalResponseDTO> searchBooksByTitle(String title) {
        String url = UriComponentsBuilder.fromUriString(GOOGLE_API_URL)
                .queryParam("q", "intitle:" + title)
                .queryParam("key", apiKey)
                .queryParam("maxResults", 10)
                .queryParam("projection", "lite")
                .toUriString();

        try {
            GoogleApiResponse response = restTemplate.getForObject(url, GoogleApiResponse.class);

            if (response == null || response.getItems() == null) {
                return Collections.emptyList();
            }

            return response.getItems().stream()
                    .filter(Objects::nonNull)
                    .map(this::convertToExternalDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("Error fetching books from Google API: " + e.getMessage());
            return Collections.emptyList();
        }    
    }

    @Override
    public BookExternalResponseDTO findBookById(String apiId) {
        String url = UriComponentsBuilder.fromUriString(GOOGLE_API_URL + "/" + apiId)
                .queryParam("key", apiKey)
                .toUriString();

        try {
            GoogleBookItem item = restTemplate.getForObject(url, GoogleBookItem.class);
            if (item == null) {
                return null;
            }
            return convertToExternalDTO(item);
        } catch (HttpClientErrorException e) {
            System.err.println("HTTP error while fetching book by ID from Google API: " + e.getMessage());
            return null;
        } catch (ResourceAccessException e) {
            System.err.println("Connection error while accessing Google API: " + e.getMessage());
            return null;
        } catch (RestClientException e) {
            System.err.println("Unexpected error while fetching book by ID from Google API: " + e.getMessage());
            return null;
        }
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
}
