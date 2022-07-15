package kr.irm.obssend;

import kr.irm.obssend.api.*;
import kr.irm.obssend.model.OptionInfo;
import kr.irm.obssend.utils.ServiceContext;
import org.apache.commons.cli.*;
import org.hl7.fhir.r4.model.Observation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceLauncher extends ServiceContext {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceLauncher.class);

    private static OptionInfo optionInfo;

    public static Options options;

    public static void main(String[] args) {
        // Set Options
        setOptions();

        if (checkRequiredOptions(args)) { // Check whether the options are required
            System.exit(1);
        } else {
            doOBSsend(); // Start OBSSend process
        }

    }

    private static void doOBSsend() {

        LOG.info("==================== OBSsend Process Start ====================");

        try {

            // Create ElectricitySensor object
            ElectricitySensor electricitySensor = new ElectricitySensor(optionInfo);

            // Create sample of electricity sensor data
            JSONObject sensorData = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            sensorData.put("date","20220711");

            JSONObject switchObj = new JSONObject();
            switchObj.put("code","switch_1");
            switchObj.put("value",true);
            jsonArray.add(switchObj);

            JSONObject countdownhObj = new JSONObject();
            countdownhObj.put("code","countdown_1");
            countdownhObj.put("value",0.0);
            jsonArray.add(countdownhObj);

            JSONObject addeleObj = new JSONObject();
            addeleObj.put("code","add_ele");
            addeleObj.put("value",12.0);
            jsonArray.add(addeleObj);

            JSONObject curcurrentObj = new JSONObject();
            curcurrentObj.put("code","cur_current");
            curcurrentObj.put("value",291.0);
            jsonArray.add(curcurrentObj);

            JSONObject curcurrenthObj = new JSONObject();
            curcurrenthObj.put("code","cur_power");
            curcurrenthObj.put("value",397.0);
            jsonArray.add(curcurrenthObj);

            JSONObject curvoltObj = new JSONObject();
            curvoltObj.put("code","cur_voltage");
            curvoltObj.put("value",2173.0);
            jsonArray.add(curvoltObj);

            JSONObject relayStatusObj = new JSONObject();
            relayStatusObj.put("code","relay_status");
            relayStatusObj.put("value","last");
            jsonArray.add(relayStatusObj);

            JSONObject cycleTimeObj = new JSONObject();
            cycleTimeObj.put("code","cycle_time");
            cycleTimeObj.put("value","");
            jsonArray.add(cycleTimeObj);

            JSONObject randomTimeObj = new JSONObject();
            randomTimeObj.put("code","random_time");
            randomTimeObj.put("value","");
            jsonArray.add(randomTimeObj);

            JSONObject powerAmountObj = new JSONObject();
            powerAmountObj.put("code","power_amount");
            powerAmountObj.put("value","0.18");
            jsonArray.add(powerAmountObj);

            sensorData.put("data",jsonArray);

            sensorData.put("device_id","eb192b1696ef61a8fbolfp");
            sensorData.put("t",1.657530713881E12);

            /*
            *** For example ***
            {
              "date": "20220711",
              "data": [
                {
                  "code": "switch_1",
                  "value": true
                },
                {
                  "code": "countdown_1",
                  "value": 0.0
                },
                {
                  "code": "add_ele",
                  "value": 12.0
                },
                {
                  "code": "cur_current",
                  "value": 291.0
                },
                {
                  "code": "cur_power",
                  "value": 397.0
                },
                {
                  "code": "cur_voltage",
                  "value": 2173.0
                },
                {
                  "code": "relay_status",
                  "value": "last"
                },
                {
                  "code": "cycle_time",
                  "value": ""
                },
                {
                  "code": "random_time",
                  "value": ""
                },
                {
                  "code": "power_amount",
                  "value": "0.18"
                }
              ],
              "device_id": "eb192b1696ef61a8fbolfp",
              "t": 1.657530713881E12
            }
             */

            Observation obs = electricitySensor.generateObservation(sensorData);
            electricitySensor.sendCloud(obs);

            LOG.info("==================== OBSsend Process Finish ====================");

        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("==================== OBSsend Error Occurred ====================");
        }
    }

    public static void setOptions() {
        options = new Options();

        // Help
        options.addOption("h", "help", false, "help");

        // Options
        options.addOption("pu", OPTION_PATIENT_UUID, true, "Patient UUID");
        options.addOption("t", OPTION_ACCESS_TOKEN,true,"Access Token");
        options.addOption("s", OPTION_SERVER_URL, true, "FHIR OBS Server URL");

    }

    public static boolean checkRequiredOptions(String[] args) {
        boolean error = false;
        CommandLineParser parser = new DefaultParser();
        optionInfo = new OptionInfo();

        try {
            CommandLine cl = parser.parse(options, args);

            // HELP
            if (cl.hasOption("h") || args.length == 0) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp(
                        "OBSsend [options]",
                        "\nMigrate the cohort list from Oracle to Postgresql", options,
                        "Examples: $ ./OBSsend -sh ...");
                System.exit(2);
            }

            // PATIENT_UUID
            if (cl.hasOption(OPTION_PATIENT_UUID)) {
                String patient_uuid = cl.getOptionValue(OPTION_PATIENT_UUID);
                optionInfo.setPatient_uuid(patient_uuid);
                LOG.info("option {}={}", OPTION_PATIENT_UUID, patient_uuid);
            } else {
                error = true;
                LOG.error("option required: {}", OPTION_PATIENT_UUID);
            }

            // ACCESS_TOKEN
            if (cl.hasOption(OPTION_ACCESS_TOKEN)) {
                String access_token = cl.getOptionValue(OPTION_ACCESS_TOKEN);
                optionInfo.setAccess_token(access_token);
                LOG.info("option {}={}", OPTION_ACCESS_TOKEN, access_token);
            } else {
                error = true;
                LOG.error("option required: {}", OPTION_ACCESS_TOKEN);
            }

            // FHIR_OBS_URL
            if (cl.hasOption(OPTION_SERVER_URL)) {
                String fhir_obs_url = cl.getOptionValue(OPTION_SERVER_URL);
                optionInfo.setFhir_obs_url(fhir_obs_url);
                LOG.info("option {}={}", OPTION_SERVER_URL, fhir_obs_url);
            } else {
                LOG.error("option required: {}", OPTION_SERVER_URL);
            }


        } catch (org.apache.commons.cli.ParseException e) {
            LOG.error("{}", e.getMessage());
        }

        return error;
    }
}
