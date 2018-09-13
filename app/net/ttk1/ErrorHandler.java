package net.ttk1;

import play.*;
import play.api.OptionalSourceMapper;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.mvc.Http.*;
import play.mvc.*;

import javax.inject.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class ErrorHandler extends DefaultHttpErrorHandler {

    @Inject
    public ErrorHandler(Configuration config, Environment environment,
                        OptionalSourceMapper sourceMapper, Provider<Router> routes) {
        super(config, environment, sourceMapper, routes);
    }

    @Override
    protected CompletionStage<Result> onNotFound(RequestHeader request, String message) {
        Exception e = new Exception();
        e.printStackTrace();
        return CompletableFuture.completedFuture(
                Results.notFound("{not_found}").as("application/json")
        );
    }
    @Override
    protected CompletionStage<Result> onBadRequest(RequestHeader request, String message) {
        return CompletableFuture.completedFuture(
                Results.badRequest("{bad_request}").as("application/json")
        );
    }

    @Override
    public CompletionStage<Result> onServerError(RequestHeader request, Throwable exception) {
        return CompletableFuture.completedFuture(
                Results.badRequest("{error}").as("text/json")
        );
    }
}