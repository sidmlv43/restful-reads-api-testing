package com.restfulReads.config;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.MultiPartSpecification;

import java.util.List;

public class SafeLoggingFilter implements Filter {

    @Override
    public Response filter(
            FilterableRequestSpecification requestSpec,
            FilterableResponseSpecification responseSpec,
            FilterContext ctx
    ) {

        List<MultiPartSpecification> multiParts = requestSpec.getMultiPartParams();

        if (multiParts != null && !multiParts.isEmpty()) {
            logMultipartRequest(requestSpec, multiParts);
        } else {
            logStandardRequest(requestSpec);
        }

        // Drive the chain ourselves — this is what actually sends the
        // request and gets the real response back. Must be called
        // exactly once.
        Response response = ctx.next(requestSpec, responseSpec);

        logResponse(response);

        return response;
    }

    private void logStandardRequest(FilterableRequestSpecification requestSpec) {
        StringBuilder sb = new StringBuilder();
        sb.append("Request method:\t").append(requestSpec.getMethod()).append("\n");
        sb.append("Request URI:\t").append(requestSpec.getURI()).append("\n");
        if (requestSpec.getBody() != null) {
            sb.append("Body:\t\t").append(requestSpec.getBody().toString()).append("\n");
        }
        System.out.println(sb);
    }

    private void logResponse(Response response) {
        if (response == null) {
            System.out.println("Response:\t<none — request may have failed before completion>");
            return;
        }
        System.out.println("Response status:\t" + response.getStatusLine());
        System.out.println("Response body:\t\t" + response.getBody().asPrettyString());
    }

    private void logMultipartRequest(
            FilterableRequestSpecification requestSpec,
            List<MultiPartSpecification> multiParts
    ) {
        StringBuilder sb = new StringBuilder();
        sb.append("Request method:\t").append(requestSpec.getMethod()).append("\n");
        sb.append("Request URI:\t").append(requestSpec.getURI()).append("\n");
        sb.append("Multipart parts:\n");

        for (MultiPartSpecification part : multiParts) {
            sb.append("  - controlName=").append(part.getControlName());

            if (part.getFileName() != null) {
                sb.append(", fileName=").append(part.getFileName());
            }
            if (part.getMimeType() != null) {
                sb.append(", contentType=").append(part.getMimeType());
            }

            Object content = part.getContent();
            if (content instanceof byte[]) {
                sb.append(", size=").append(((byte[]) content).length).append(" bytes");
            } else if (content instanceof String) {
                // form field, not a file — safe to show as-is
                sb.append(", value=").append(content);
            }

            sb.append("\n");
        }

        System.out.println(sb);
    }
}