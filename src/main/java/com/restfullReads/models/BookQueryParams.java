package com.restfullReads.models;


import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * Utility class for constructing query parameters for the Books API.
 * <p>
 * This class follows the Builder pattern to provide a fluent and type-safe way
 * of defining query parameters for the <b>/api/books</b> endpoint.
 * <p>
 * It supports:
 * <ul>
 *     <li>Standard query parameters such as pagination, sorting, and field selection</li>
 *     <li>Common filters like author and minimum rating</li>
 *     <li>Dynamic/advanced filters using operator-based syntax (e.g., price[gt], price[lte])</li>
 * </ul>
 *
 * <h3>Usage Examples</h3>
 *
 * <b>Basic Query:</b>
 * <pre>
 * BookQueryParams query = BookQueryParams.builder()
 *     .page(1)
 *     .limit(10)
 *     .author("John Doe")
 *     .build();
 * </pre>
 *
 * <b>With Advanced Filters:</b>
 * <pre>
 * BookQueryParams query = BookQueryParams.builder()
 *     .page(1)
 *     .limit(10)
 *     .filters(Map.of("price[lte]", 14))
 *     .build();
 * </pre>
 *
 * <p>
 * <b>Note:</b> For advanced filtering scenarios involving comparison or conditional
 * operators (e.g., greater than, less than), use the {@code filters} field. These
 * are passed directly as query parameters and should follow the API's expected
 * syntax (e.g., {@code price[gt]=10}).
 *
 * <p>
 * All parameters are optional. Only non-null values are included in the final query map.
 */


@Data
@Builder
public class BookQueryParams {
    private Integer page;
    private Integer limit;
    private String sort;
    private String author;
    private Integer minRating;
    private String select;

    private Map<String, Object> filters;


    public Map<String, Object> toMap() {
        Map<String, Object> queryMap = new HashMap<>();

        if (page != null) {
            queryMap.put("page", page);
        }

        if (limit != null) {
            queryMap.put("limit", limit);
        }

        if (sort != null) {
            queryMap.put("sort", sort);
        }

        if (author != null) {
            queryMap.put("author", author);
        }

        if (minRating != null) {
            queryMap.put("minRating", minRating);
        }

        if (select != null) {
            queryMap.put("select", select);
        }


        if (filters != null) {
            queryMap.putAll(filters);
        }

        return queryMap;
    }


}
