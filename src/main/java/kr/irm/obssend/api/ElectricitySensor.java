package kr.irm.obssend.api;

import kr.irm.obssend.model.OptionInfo;
import kr.irm.obssend.object.ElectricityCode;
import kr.irm.obssend.utils.OBSSend;
import org.hl7.fhir.r4.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

public class ElectricitySensor {
    private static final Logger LOG = LoggerFactory.getLogger(ElectricitySensor.class);

    public static final String HTTP_GET = "GET";
    private static final String CLASS_CODE = "code^display^schema";
    private static final String SYSTEM = "99IRM";

    // 전역변수
    private OptionInfo optionInfo;

    public ElectricitySensor(OptionInfo optionInfo) {
        this.optionInfo = optionInfo;
    }

    public Observation generateObservation(JSONObject requestData) {

        // Generate Observation
        Observation obs = new Observation();
        try{
            // Status
            obs.setStatus(Observation.ObservationStatus.FINAL);

            // Patient
            Patient patient = new Patient();
            patient.setId(optionInfo.getPatient_uuid());
            obs.setSubject(new Reference(patient));

            // Code
            Coding coding = new Coding();
            coding.setSystem(SYSTEM);
            coding.setCode("I1000");
            coding.setDisplay("ElectricitySensorData");

            obs.getCode().addCoding(coding).setText("Electricity Sensor Data");

            // effective[x]
            // yyyy-MM-dd'T'HH:mm:ss'Z' 형태의 date
            long obsInstant = Math.round((double) requestData.get("t"));
            Date formatDate = new Date(obsInstant);
            SimpleDateFormat instantDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+'00:00");
            instantDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

            obs.setEffective(new InstantType(instantDateFormat.format(formatDate)));

            // device
            Device device = new Device();
            Identifier deviceIdentifier = new Identifier();
            deviceIdentifier.setUse(Identifier.IdentifierUse.USUAL);
            deviceIdentifier.setValue((String)requestData.get("device_id"));

            device.addIdentifier(deviceIdentifier);
            device.setStatus(Device.FHIRDeviceStatus.ACTIVE);
            obs.setDevice(new Reference(device));

            // Component
            // When one observation object get multiple value
            // Use component
            JSONArray obsDataArr = (JSONArray) requestData.get("data");

            for (int j=0; j<obsDataArr.size(); j++) {
                JSONObject tempTest = (JSONObject) obsDataArr.get(j);
                String keyStr = (String)tempTest.get("code");
                String codeKey = keyStr.replace("_","").toUpperCase();
                Object value = tempTest.get("value");
                String dataType = value.getClass().getName();

                Type valueType = null;

                Quantity quantity = new Quantity();
                quantity.setUnit(ElectricityCode.valueOf(codeKey).getUnit());

                // When value type is 'long' or 'Double', Create quantity
                if (dataType.equals("java.lang.Long")) {
                    quantity.setValue((Long) tempTest.get("value"));
                    valueType = quantity;
                } else if (dataType.equals("java.lang.Double")) {
                    quantity.setValue((Double) tempTest.get("value"));
                    valueType = quantity;
                } else if (dataType.equals("java.lang.Boolean")) {
                    valueType = new BooleanType((Boolean) value);
                } else if (dataType.equals("java.lang.String")) {
                    valueType = new StringType((String) value);
                }

                // Input component object to Observation
                obs.addComponent()
                        .setValue(valueType)
                        .getCode()
                        .addCoding()
                        .setSystem(ElectricityCode.valueOf(codeKey).getSystem())
                        .setDisplay(ElectricityCode.valueOf(codeKey).getDisplay())
                        .setCode(ElectricityCode.valueOf(codeKey).getCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return obs;
    }

    public boolean sendCloud(Observation obs) {
        if (obs.isEmpty() || obs == null) {
            return false;
        }

        OBSSend obsSend = new OBSSend(optionInfo);
        return obsSend.send(obs);
    }
}