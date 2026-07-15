package com.restfulReads.reporting;

import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class ExtentRestAssuredFilter implements Filter {

    @Override
    public Response filter(
            FilterableRequestSpecification requestSpec,
            FilterableResponseSpecification responseSpec,
            FilterContext context
    ) {

        logRequest(requestSpec);

        Response response =
                context.next(requestSpec, responseSpec);

        logResponse(response);

        return response;
    }

    private void logRequest(
            FilterableRequestSpecification requestSpec
    ) {

        if (ExtentTestManager.getTest() == null) {
            return;
        }

        ExtentTestManager.getTest()
                .info(
                        "Request Method: "
                                + requestSpec.getMethod()
                );

        ExtentTestManager.getTest()
                .info(
                        "Request URI: "
                                + requestSpec.getURI()
                );

        if (requestSpec.getBody() != null) {

            ExtentTestManager.getTest()
                    .info(
                            MarkupHelper.createCodeBlock(
                                    requestSpec.getBody().toString(),
                                    CodeLanguage.JSON
                            )
                    );
        }
    }

    private void logResponse(Response response) {

        if (ExtentTestManager.getTest() == null) {
            return;
        }

        ExtentTestManager.getTest()
                .info(
                        "Response Status Code: "
                                + response.getStatusCode()
                );

        ExtentTestManager.getTest()
                .info(
                        MarkupHelper.createCodeBlock(
                                response.getBody()
                                        .asPrettyString(),
                                CodeLanguage.JSON
                        )
                );
    }
}