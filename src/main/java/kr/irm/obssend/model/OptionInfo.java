package kr.irm.obssend.model;

public class OptionInfo {
    private String patient_uuid;
    private String access_token;
    private String fhir_obs_url;

    public String getFhir_obs_url() {
        return fhir_obs_url;
    }

    public void setFhir_obs_url(String fhir_obs_url) {
        this.fhir_obs_url = fhir_obs_url;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getPatient_uuid() {
        return patient_uuid;
    }

    public void setPatient_uuid(String patient_uuid) {
        this.patient_uuid = patient_uuid;
    }
}