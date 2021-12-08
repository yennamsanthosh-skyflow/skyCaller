const express = require("express");
const app = express();
const port = 3000;

const routes = {
  GET_DETAILS: "/details",
  SAVE_CONTACTS: "/contacts"
}

app.get(routes.SAVE_CONTACTS, () => {
  // TODO: implement function to save all the contact numbers of user

  /*
    steps:
    1. Update user row's "contacts" column with array of phone numbers (Use: skyflowClient.updateContacts(device_id, contacts))
  */

    /*
      requestBody: {
        device_id: str,
        contact_numbers: [str]
      }

      response: {
        success: bool
      }
    */
})

app.get(routes.GET_DETAILS, () => {
  // TODO: implement function to get caller details based on caller and receiver configs

  /*
    steps:
    1. Get caller_info (Use: skyflowClient.get('caller_id'))
    2. Get receiver_config (Use: configStore.get('receiver_id'))
    3. Check if receiver_config has any rules that block caller_config [location, business, time] (Use: utils.verifyCaller(caller_info))
      3.1 If yes, return notify: false
    4. Get caller_config (Use: configStore.get('caller_id'))
    5. Check how to send the caller details to receiver based on caller_config (Use: utils.getRedactedInfo(caller_config))
  */

    /*
      requestBody: {
        device_id: str,
        caller_number: str
        device_time: str/time
      }

      response: {
        notify: Bool,
        info: {
          // dict containing caller info
        },
        redactionType: Enum('plaintext', 'masked', 'redacted')
      }
    */
})

app.listen(port, () => {
  console.log(`skyCaller Service listening at http://localhost:${port}`);
});

