package models;

import java.util.List;
import com.saasquatch.jsonschemainferrer.IntegerTypePreference;
import com.saasquatch.jsonschemainferrer.SpecVersion;

public class InferenceRequest {

  private String sample;
  private String arraySampleAsMulti;
  private SpecVersion specVersion;
  private Integer examplesLimit;
  private IntegerTypePreference integerTypePreference;
  private String additionalPropertiesPolicy;
  private String requiredPolicy;
  private String defaultPolicy;
  private String titleGenerator;
  private String inferObjectSizeLimits;
  private String inferArrayLengthLimits;
  private String inferStringLengthLimits;
  private List<String> formatInferrers;

  public InferenceRequest() {}

  public String getSample() {
    return sample;
  }

  public void setSample(String sample) {
    this.sample = sample;
  }

  public String getArraySampleAsMulti() {
    return arraySampleAsMulti;
  }

  public void setArraySampleAsMulti(String arraySampleAsMulti) {
    this.arraySampleAsMulti = arraySampleAsMulti;
  }

  public SpecVersion getSpecVersion() {
    return specVersion;
  }

  public void setSpecVersion(SpecVersion specVersion) {
    this.specVersion = specVersion;
  }

  public Integer getExamplesLimit() {
    return examplesLimit;
  }

  public void setExamplesLimit(Integer examplesLimit) {
    this.examplesLimit = examplesLimit;
  }

  public IntegerTypePreference getIntegerTypePreference() {
    return integerTypePreference;
  }

  public void setIntegerTypePreference(IntegerTypePreference integerTypePreference) {
    this.integerTypePreference = integerTypePreference;
  }

  public String getAdditionalPropertiesPolicy() {
    return additionalPropertiesPolicy;
  }

  public void setAdditionalPropertiesPolicy(String additionalPropertiesPolicy) {
    this.additionalPropertiesPolicy = additionalPropertiesPolicy;
  }

  public String getRequiredPolicy() {
    return requiredPolicy;
  }

  public void setRequiredPolicy(String requiredPolicy) {
    this.requiredPolicy = requiredPolicy;
  }

  public String getDefaultPolicy() {
    return defaultPolicy;
  }

  public void setDefaultPolicy(String defaultPolicy) {
    this.defaultPolicy = defaultPolicy;
  }

  public String getTitleGenerator() {
    return titleGenerator;
  }

  public void setTitleGenerator(String titleGenerator) {
    this.titleGenerator = titleGenerator;
  }

  public String getInferObjectSizeLimits() {
    return inferObjectSizeLimits;
  }

  public void setInferObjectSizeLimits(String inferObjectSizeLimits) {
    this.inferObjectSizeLimits = inferObjectSizeLimits;
  }

  public String getInferArrayLengthLimits() {
    return inferArrayLengthLimits;
  }

  public void setInferArrayLengthLimits(String inferArrayLengthLimits) {
    this.inferArrayLengthLimits = inferArrayLengthLimits;
  }

  public String getInferStringLengthLimits() {
    return inferStringLengthLimits;
  }

  public void setInferStringLengthLimits(String inferStringLengthLimits) {
    this.inferStringLengthLimits = inferStringLengthLimits;
  }

  public List<String> getFormatInferrers() {
    return formatInferrers;
  }

  public void setFormatInferrers(List<String> formatInferrers) {
    this.formatInferrers = formatInferrers;
  }

}
