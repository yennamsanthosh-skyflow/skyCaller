import express from "express";

import { Skyflow } from "skyflow-node";
import {
  getRedactedNumber,
  getSkyflowIdByPhoneNumber,
  shouldBlock,
} from "./services";
import { skyflowClient } from "./skyflow";
const app = express();
const port = 3000;

const phoneToSkyflowID = {
  "+917093143619": "172a51d9-cfe4-4bf6-97af-8dab24151d9d",
  "+919505493189": "5a2e2d3d-436a-4d98-a64b-4e846adc180d",
  "+918309540773": "ad07fb96-091e-4e75-9012-a402918c98db",
  "+918608200493": "ebb4ea53-9528-4fad-9c67-9d40178a313e",
  "+917032455152": "f6b43cfd-931a-40c1-9c78-537b96299558",
};

const routes = {
  VERIFY: "/verify",
  GET_ID_BY_NUMBER: "/getIdByNumber/:phonenumber",
  GET_TOKEN: "/getToken",
};
app.use(express.json());
app.use(express.urlencoded({ extended: false }));

const tablename = "table1";

app.post(routes.VERIFY, async (req, res) => {
  const { caller_number, receiver_number, isMyContact } = req.body;

  const caller_id = phoneToSkyflowID[caller_number];
  const receiver_id = phoneToSkyflowID[receiver_number];

  if (!(caller_number & receiver_number && caller_id && receiver_id)) {
    res.status(400).send("Invalid input to server");
  }
  try {
    const details = await skyflowClient.getById({
      records: [
        {
          ids: [caller_id, receiver_id],
          table: tablename,
          redaction: Skyflow.RedactionType.PLAIN_TEXT,
        },
      ],
    });
    const caller_details = details["records"][0]["fields"];
    const receiver_details = details["records"][1]["fields"];

    if (
      shouldBlock(receiver_details["config"]["blocked_calls"], caller_details)
    ) {
      res.send({
        allow: false,
      });
    } else {
      let redactedNumber = getRedactedNumber(
        caller_details,
        receiver_details,
        isMyContact
      );
      res.send({
        allow: true,
        caller_number: redactedNumber,
      });
    }
  } catch (err) {
    console.log("rejected");
    console.log(err);
  }
});

app.get(routes.GET_ID_BY_NUMBER, async (req, res) => {
  const response = await getSkyflowIdByPhoneNumber(req.params.phonenumber);
  res.send(response);
});

app.get(routes.GET_TOKEN, async (req, res) => {
  //Generate PAT token from studio and update every 24 hrs
  const patToken = `eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2MiOiJmNzk0ZmY4NmZiYzgxMWVhYmQ2YzNhOTExNDNlM2Q0MiIsImF1ZCI6Imh0dHBzOi8vbWFuYWdlLnNreWZsb3dhcGlzLmRldiIsImV4cCI6MTYzOTIxMTQzNDE3MCwiaWF0IjoxNjM5MTI1MDM0MTcwLCJpc3MiOiJzYS1hdXRoQG1hbmFnZS5za3lmbG93YXBpcy5kZXYiLCJqdGkiOiJkNjBiNDhkMTYxM2Q0ZmY4OTU2YjVlZGYwZTk2OWEwMCIsInN1YiI6ImEyMWE0MDI3MmM5MjQ1YzQ4ODZjNDFlNjU4NDdjMTMxIn0.UfZA_6RgfK_Ce0Fy_8E1VqhSP1jGXtPLHlpiHPp3-wCv6xuRbYCZCkNQLQEUT5S0M15echNkhZScjlNG7Y92Il0BSB3bukH5C5u4cfptcbtdO_n023repREDuRpu2mh8r-IVdjfYBQWOAa8N8B3OCcH7gyrx1-WVG8IaJru4wKei8GtAsC48BtE1a8aZN_nXjLlNYZMt_5i5DwkyqObBwCzMQO_n56425fF6-S90N6EwpOG_9_PVtPaeSXzMJL9HG6RJJ35VSZHZohF9qur5mM-pkoQfFO6ovxN-LlGEKPiEaZspycjJcrl-4EmQNxlycuOfGKx-i8Z7qDE_UA6r9A`;
  if (patToken) {
    const authToken = {
      token: `Bearer ${patToken}`,
    };

    res.send(authToken);
  }
});

app.listen(port, () => {
  console.log(`skyCaller Service listening at http://localhost:${port}`);
});
