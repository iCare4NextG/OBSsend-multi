package kr.irm.obssend.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceContext {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceContext.class);

    public static final String OPTION_PATIENT_UUID = "patient_uuid";
    public static final String OPTION_ACCESS_TOKEN = "access_token";
    public static final String OPTION_SERVER_URL = "fhir_obs_url";

}