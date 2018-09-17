package net.ttk1.mcserverstatus;


import play.http.HttpErrorHandler;
import play.mvc.Http.*;
import play.mvc.*;

import javax.inject.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class ErrorHandler implements HttpErrorHandler {

    @Override
    public CompletionStage<Result> onClientError(RequestHeader request, int statusCode, String message) {
        Result result;

        switch (statusCode) {
            case 400:
                result = Results.badRequest("{\"message\":\"bad_request\"}");
                break;
            case 404:
                result = Results.badRequest("{\"message\":\"not_found\"}");
                break;
            default:
                result = Results.status(statusCode, "{\"message\":\"other_client_error\"}");
        }

        return CompletableFuture.completedFuture(result.as("application/json"));
    }

    @Override
    public CompletionStage<Result> onServerError(RequestHeader request, Throwable exception) {
        exception.printStackTrace();
        return CompletableFuture.completedFuture(
                Results.internalServerError("{\"message\":\"internal_server_error\"}").as("application/json")
        );
    }
}