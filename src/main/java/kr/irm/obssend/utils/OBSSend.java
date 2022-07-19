package kr.irm.obssend.utils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import kr.irm.obssend.model.OptionInfo;
import org.hl7.fhir.r4.model.Observation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class OBSSend {
    private static final Logger LOG = LoggerFactory.getLogger(OBSSend.class);

    private static OptionInfo optionInfo;

    public OBSSend(OptionInfo optionInfo) { this.optionInfo = optionInfo; }

    public boolean send(Observation obs) {
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        String inputLine;
        try {
            URL url = new URL(optionInfo.getFhir_obs_url());

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/fhir+json");
            con.setRequestProperty("Accept", "application/fhir+json");
            con.setRequestProperty("Authorization", "Bearer " + optionInfo.getAccess_token());
            con.setDoOutput(true);
            con.getOutputStream().write(parser.encodeResourceToString(obs).getBytes(StandardCharsets.UTF_8));

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

            // Print response in console
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
            in.close();
            return true;
        } catch (IOException e) {
            // e.printStackTrace();
            LOG.error(e.getMessage());
            return false;
        }
    }
}