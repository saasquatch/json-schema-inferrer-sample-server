package controllers;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.inject.Inject;
import org.apache.commons.validator.routines.UrlValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.saasquatch.jsonschemainferrer.AdditionalPropertiesPolicies;
import com.saasquatch.jsonschemainferrer.ArrayLengthFeature;
import com.saasquatch.jsonschemainferrer.DefaultPolicies;
import com.saasquatch.jsonschemainferrer.ExamplesPolicies;
import com.saasquatch.jsonschemainferrer.FormatInferrer;
import com.saasquatch.jsonschemainferrer.FormatInferrers;
import com.saasquatch.jsonschemainferrer.JsonSchemaInferrer;
import com.saasquatch.jsonschemainferrer.JsonSchemaInferrerBuilder;
import com.saasquatch.jsonschemainferrer.ObjectSizeFeature;
import com.saasquatch.jsonschemainferrer.RequiredPolicies;
import com.saasquatch.jsonschemainferrer.StringLengthFeature;
import com.saasquatch.jsonschemainferrer.TitleGenerators;
import models.InferenceRequest;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

public class HomeController extends Controller {

  private static final ObjectMapper mapper = Json.mapper();

  private static final FormatInferrer uriFormatInferrer = input -> {
    if (UrlValidator.getInstance().isValid(input.getSample().textValue())) {
      return "uri";
    }
    return null;
  };

  private final FormFactory formFactory;
  private final MessagesApi messagesApi;

  @Inject
  public HomeController(FormFactory formFactory, MessagesApi messagesApi) {
    this.formFactory = formFactory;
    this.messagesApi = messagesApi;
  }

  public Result index(Http.Request request) {
    final Form<InferenceRequest> myForm = formFactory.form(InferenceRequest.class);
    return ok(views.html.inferenceRequestForm.render(myForm, messagesApi.preferred(request)));
  }

  public Result inferenceRequestFormSubmit(Http.Request request) {
    try {
      final InferenceRequest inferenceRequest =
          formFactory.form(InferenceRequest.class).bindFromRequest(request).get();
      final JsonNode schema = doInfer(inferenceRequest);
      return ok(views.html.inferenceResult.render(mapper.writeValueAsString(schema)));
    } catch (Exception e) {
      e.printStackTrace();
      final String message = e.getMessage();
      if (message == null) {
        return internalServerError();
      }
      return badRequest(views.html.inferenceResult.render(message));
    }
  }

  private JsonNode doInfer(InferenceRequest inferenceRequest) {
    if (inferenceRequest.getSample().trim().isEmpty()) {
      throw new RuntimeException("Blank JSON input");
    }
    final JsonNode sample;
    try {
      sample = mapper.readTree(inferenceRequest.getSample());
    } catch (IOException e) {
      throw new UncheckedIOException("Invalid JSON input", e);
    }
    final JsonSchemaInferrerBuilder b = JsonSchemaInferrer.newBuilder();
    Optional.ofNullable(inferenceRequest.getSpecVersion()).ifPresent(b::setSpecVersion);
    Optional.ofNullable(inferenceRequest.getExamplesLimit()).ifPresent(
        examplesLimit -> b.setExamplesPolicy(ExamplesPolicies.useFirstSamples(examplesLimit)));
    Optional.ofNullable(inferenceRequest.getIntegerTypePreference())
        .ifPresent(b::setIntegerTypePreference);
    if (inferenceRequest.getAdditionalPropertiesPolicy() != null) {
      switch (inferenceRequest.getAdditionalPropertiesPolicy()) {
        case "allowed":
          b.setAdditionalPropertiesPolicy(AdditionalPropertiesPolicies.allowed());
          break;
        case "notAllowed":
          b.setAdditionalPropertiesPolicy(AdditionalPropertiesPolicies.notAllowed());
          break;
        case "existingTypes":
          b.setAdditionalPropertiesPolicy(AdditionalPropertiesPolicies.existingTypes());
          break;
        default:
          break;
      }
    }
    if (inferenceRequest.getRequiredPolicy() != null) {
      switch (inferenceRequest.getRequiredPolicy()) {
        case "commonFields":
          b.setRequiredPolicy(RequiredPolicies.commonFields());
          break;
        case "nonNullCommonFields":
          b.setRequiredPolicy(RequiredPolicies.nonNullCommonFields());
          break;
        default:
          break;
      }
    }
    if (inferenceRequest.getDefaultPolicy() != null) {
      switch (inferenceRequest.getDefaultPolicy()) {
        case "useFirstSamples":
          b.setDefaultPolicy(DefaultPolicies.useFirstSamples());
          break;
        case "useLastSamples":
          b.setDefaultPolicy(DefaultPolicies.useLastSamples());
          break;
        default:
          break;
      }
    }
    if (inferenceRequest.getTitleGenerator() != null) {
      switch (inferenceRequest.getTitleGenerator()) {
        case "useFieldNames":
          b.setTitleGenerator(TitleGenerators.useFieldNames());
          break;
        default:
          break;
      }
    }
    if (inferenceRequest.getInferObjectSizeLimits() != null) {
      b.enable(ObjectSizeFeature.values());
    }
    if (inferenceRequest.getInferArrayLengthLimits() != null) {
      b.enable(ArrayLengthFeature.values());
    }
    if (inferenceRequest.getInferStringLengthLimits() != null) {
      b.enable(StringLengthFeature.values());
    }
    if (inferenceRequest.getFormatInferrers() != null) {
      final FormatInferrer[] formatInferrers = inferenceRequest.getFormatInferrers().stream()
          .distinct()
          .filter(Objects::nonNull)
          .map(formatInferrer -> {
            switch (formatInferrer) {
              case "dateTime":
                return FormatInferrers.dateTime();
              case "email":
                return FormatInferrers.email();
              case "ip":
                return FormatInferrers.ip();
              case "uri":
                return uriFormatInferrer;
              default:
                return null;
            }
          })
          .filter(Objects::nonNull)
          .toArray(FormatInferrer[]::new);
      b.setFormatInferrer(FormatInferrers.chained(formatInferrers));
    }
    final JsonSchemaInferrer inferrer = b.build();
    final ObjectNode schema;
    if (inferenceRequest.getArraySampleAsMulti() != null && sample.isArray()) {
      schema = inferrer.inferForSamples(
          StreamSupport.stream(sample.spliterator(), false).collect(Collectors.toList()));
    } else {
      schema = inferrer.inferForSample(sample);
    }
    return schema;
  }

}
