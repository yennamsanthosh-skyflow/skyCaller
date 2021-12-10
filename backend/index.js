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
  7093143619: "172a51d9-cfe4-4bf6-97af-8dab24151d9d",
  9505493189: "5a2e2d3d-436a-4d98-a64b-4e846adc180d",
  8309540773: "ad07fb96-091e-4e75-9012-a402918c98db",
  8608200493: "ebb4ea53-9528-4fad-9c67-9d40178a313e",
  7032455152: "f6b43cfd-931a-40c1-9c78-537b96299558",
};

const routes = {
  VERIFY: "/verify",
  GET_ID_BY_NUMBER: "/getIdByNumber/:phonenumber",
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

app.listen(port, () => {
  console.log(`skyCaller Service listening at http://localhost:${port}`);
});
